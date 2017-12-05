package personal.calebcordell.coinnection.data.assetdata;

import android.support.annotation.NonNull;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import personal.calebcordell.coinnection.data.PreferencesRepositoryImpl;
import personal.calebcordell.coinnection.data.portfolioassetdata.PortfolioAssetDiskDataStore;
import personal.calebcordell.coinnection.data.watchlistassetdata.WatchlistAssetDiskDataStore;
import personal.calebcordell.coinnection.domain.model.Asset;
import personal.calebcordell.coinnection.domain.repository.AssetRepository;

import java.util.List;

import io.reactivex.Flowable;
import personal.calebcordell.coinnection.domain.repository.PreferencesRepository;


public class AssetRepositoryImpl implements AssetRepository {
    private String TAG = AssetRepositoryImpl.class.getSimpleName();

    private final AssetDiskDataStore mAssetDiskDataStore;
    private final PortfolioAssetDiskDataStore mPortfolioAssetDiskDataStore;
    private final WatchlistAssetDiskDataStore mWatchlistAssetDiskDataStore;
    private final AssetNetworkDataStore mAssetNetworkDataStore;
    private final PreferencesRepository mPreferencesRepository;

    private final AssetMapper mAssetMapper;

    public AssetRepositoryImpl() {
        mAssetDiskDataStore = AssetDiskDataStore.getInstance();
        mPortfolioAssetDiskDataStore = PortfolioAssetDiskDataStore.getInstance();
        mWatchlistAssetDiskDataStore = WatchlistAssetDiskDataStore.getInstance();
        mAssetNetworkDataStore = AssetNetworkDataStore.getInstance();
        mPreferencesRepository = PreferencesRepositoryImpl.getInstance();
        mAssetMapper = new AssetMapper();
    }

    public Flowable<Asset> getAsset(@NonNull final String id) {
        return mAssetDiskDataStore.getSingular(id);
    }
    public Flowable<List<Asset>> getAllAssets() {
        return mAssetDiskDataStore.getAll();
    }

    public Completable fetchAsset(@NonNull final String id) {
        return mAssetNetworkDataStore.asset(id)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.computation())
                .map(mAssetMapper::mapUp)
                .doOnSuccess(asset -> {mAssetDiskDataStore.storeSingular(asset);
                    mPortfolioAssetDiskDataStore.updateSingular(asset);
                    mWatchlistAssetDiskDataStore.updateSingular(asset);})
                .toCompletable();
    }
    public Completable fetchAllAssets(boolean forceFetch) {
        if(forceFetch || mPreferencesRepository.needsFullUpdate()) {
            return mAssetNetworkDataStore.assets()
                    .subscribeOn(Schedulers.io())
                    .flatMapObservable(Observable::fromIterable)
                    .map(mAssetMapper::mapUp)
                    .toList()
                    .doOnSuccess(assets -> {mAssetDiskDataStore.replaceAll(assets);
                        mPortfolioAssetDiskDataStore.updateAll(assets);
                        mWatchlistAssetDiskDataStore.updateAll(assets);
                        mPreferencesRepository.setLastFullUpdate(System.currentTimeMillis());})
                    .observeOn(AndroidSchedulers.mainThread())
                    .toCompletable();
        }
        return Completable.complete();
    }
}