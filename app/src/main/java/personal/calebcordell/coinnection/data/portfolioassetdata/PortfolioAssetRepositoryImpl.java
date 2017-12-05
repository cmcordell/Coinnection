package personal.calebcordell.coinnection.data.portfolioassetdata;

import android.support.annotation.NonNull;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;
import personal.calebcordell.coinnection.data.assetdata.AssetMapper;
import personal.calebcordell.coinnection.data.assetdata.AssetNetworkDataStore;
import personal.calebcordell.coinnection.domain.model.PortfolioAsset;
import personal.calebcordell.coinnection.domain.repository.PortfolioAssetRepository;


public class PortfolioAssetRepositoryImpl implements PortfolioAssetRepository {
    private String TAG = PortfolioAssetRepositoryImpl.class.getSimpleName();

    private final PortfolioAssetDiskDataStore mPortfolioAssetDiskDataStore;
    private final AssetNetworkDataStore mAssetNetworkDataStore;
    private final AssetMapper mAssetMapper;

    public PortfolioAssetRepositoryImpl() {
        mPortfolioAssetDiskDataStore = PortfolioAssetDiskDataStore.getInstance();
        mAssetNetworkDataStore = AssetNetworkDataStore.getInstance();
        mAssetMapper = new AssetMapper();
    }


    public Single<List<String>> getPortfolioAssetIds() {
        return mPortfolioAssetDiskDataStore.getAllIds();
    }


    public Completable reorderPortfolio(@NonNull List<PortfolioAsset> portfolioAssetsInOrder) {
        return mPortfolioAssetDiskDataStore.replaceAll(portfolioAssetsInOrder);
    }


    public Completable addAssetToPortfolio(@NonNull PortfolioAsset portfolioAsset) {
        return mPortfolioAssetDiskDataStore.storeSingular(portfolioAsset);
    }
    public Completable removeAssetFromPortfolio(@NonNull String id) {
        return mPortfolioAssetDiskDataStore.removeSingular(id);
    }


    public Single<Boolean> isAssetInPortfolio(@NonNull String id) {
        return mPortfolioAssetDiskDataStore.isAssetInPortfolio(id);
    }


    public Flowable<PortfolioAsset> getPortfolioAsset(@NonNull String id) {
        return mPortfolioAssetDiskDataStore.getSingular(id);
    }
    public Flowable<List<PortfolioAsset>> getAllPortfolioAssets() {
        return mPortfolioAssetDiskDataStore.getAll();
    }


    public Completable fetchAllPortfolioAssets() {
        return mPortfolioAssetDiskDataStore.getAllIds()
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.computation())
                .flatMap(mAssetNetworkDataStore::assets)
                .flatMapObservable(Observable::fromIterable)
                .map(mAssetMapper::mapUp)
                .toList()
                .doOnSuccess(mPortfolioAssetDiskDataStore::updateAll)
                .toCompletable();

//        return mAssetNetworkDataStore.assets()
//                .subscribeOn(Schedulers.io())
//                .flatMapObservable(Observable::fromIterable)
//                .map(mAssetMapper::mapUp)
//                .toList()
//                .doOnSuccess(mPortfolioAssetDiskDataStore::updateAll)
//                .toCompletable();
    }
    public Completable fetchPortfolioAsset(@NonNull String id) {
        return mAssetNetworkDataStore.asset(id)
                .subscribeOn(Schedulers.io())
                .map(mAssetMapper::mapUp)
                .doOnSuccess(mPortfolioAssetDiskDataStore::updateSingular)
                .toCompletable();
    }
}