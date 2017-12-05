package personal.calebcordell.coinnection.data.watchlistassetdata;

import android.support.annotation.NonNull;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import personal.calebcordell.coinnection.data.assetdata.AssetMapper;
import personal.calebcordell.coinnection.data.assetdata.AssetNetworkDataStore;
import personal.calebcordell.coinnection.domain.model.WatchlistAsset;
import personal.calebcordell.coinnection.domain.repository.WatchlistAssetRepository;


public class WatchlistAssetRepositoryImpl implements WatchlistAssetRepository {
    private String TAG = WatchlistAssetRepositoryImpl.class.getSimpleName();

    private final WatchlistAssetDiskDataStore mWatchlistAssetDiskDataStore;
    private final AssetNetworkDataStore mAssetNetworkDataStore;
    private final AssetMapper mAssetMapper;

    public WatchlistAssetRepositoryImpl() {
        mWatchlistAssetDiskDataStore = WatchlistAssetDiskDataStore.getInstance();
        mAssetNetworkDataStore = AssetNetworkDataStore.getInstance();
        mAssetMapper = new AssetMapper();
    }


    public Single<List<String>> getWatchlistAssetIds() {
        return mWatchlistAssetDiskDataStore.getAllIds();
    }


    public Completable reorderWatchlist(@NonNull List<WatchlistAsset> watchlistAssetsInOrder) {
        return mWatchlistAssetDiskDataStore.replaceAll(watchlistAssetsInOrder);
    }


    public Completable addAssetToWatchlist(@NonNull WatchlistAsset watchlistAsset) {
        return mWatchlistAssetDiskDataStore.storeSingular(watchlistAsset);
    }
    public Completable removeAssetFromWatchlist(@NonNull String id) {
        return mWatchlistAssetDiskDataStore.removeSingular(id);
    }


    public Single<Boolean> isAssetOnWatchlist(@NonNull String id) {
        return mWatchlistAssetDiskDataStore.isAssetOnWatchlist(id);
    }


    public Flowable<WatchlistAsset> getWatchlistAsset(@NonNull String id) {
        return mWatchlistAssetDiskDataStore.getSingular(id);
    }
    public Flowable<List<WatchlistAsset>> getAllWatchlistAssets() {
        return mWatchlistAssetDiskDataStore.getAll();
    }


    public Completable fetchAllWatchlistAssets() {
        return mWatchlistAssetDiskDataStore.getAllIds()
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.computation())
                .flatMap(mAssetNetworkDataStore::assets)
                .flatMapObservable(Observable::fromIterable)
                .map(mAssetMapper::mapUp)
                .toList()
                .doOnSuccess(mWatchlistAssetDiskDataStore::updateAll)
                .toCompletable();

//        return mAssetNetworkDataStore.assets(mWatchlistAssetDiskDataStore.getAllIds())
//                .subscribeOn(Schedulers.io())
//                .observeOn(Schedulers.computation())
//                .flatMapObservable(Observable::fromIterable)
//                .map(mAssetMapper::mapUp)
//                .toList()
//                .doOnSuccess(mWatchlistAssetDiskDataStore::updateAll)
//                .toCompletable();
    }
    public Completable fetchWatchlistAsset(@NonNull String id) {
        return mAssetNetworkDataStore.asset(id)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.computation())
                .map(mAssetMapper::mapUp)
                .doOnSuccess(mWatchlistAssetDiskDataStore::updateSingular)
                .toCompletable();
    }
}