package personal.calebcordell.coinnection.data.portfolioassetdata;

import android.support.annotation.NonNull;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import personal.calebcordell.coinnection.data.assetdata.AssetMapper;
import personal.calebcordell.coinnection.data.assetdata.AssetNetworkDataStore;
import personal.calebcordell.coinnection.domain.model.PortfolioAsset;
import personal.calebcordell.coinnection.domain.repository.PortfolioAssetRepository;


@Singleton
public class PortfolioAssetRepositoryImpl implements PortfolioAssetRepository {
    private String TAG = PortfolioAssetRepositoryImpl.class.getSimpleName();

    private final PortfolioAssetDiskDataStore mPortfolioAssetDiskDataStore;
    private final AssetNetworkDataStore mAssetNetworkDataStore;
    private final AssetMapper mAssetMapper;

    @Inject
    public PortfolioAssetRepositoryImpl(PortfolioAssetDiskDataStore portfolioAssetDiskDataStore,
                                        AssetNetworkDataStore assetNetworkDataStore,
                                        AssetMapper assetMapper) {
        mPortfolioAssetDiskDataStore = portfolioAssetDiskDataStore;
        mAssetNetworkDataStore = assetNetworkDataStore;
        mAssetMapper = assetMapper;
    }


    public Single<List<String>> getPortfolioAssetIds() {
        return mPortfolioAssetDiskDataStore.getAllIds();
    }


    public Completable reorderPortfolio(@NonNull final List<PortfolioAsset> portfolioAssetsInOrder) {
        return mPortfolioAssetDiskDataStore.replaceAll(portfolioAssetsInOrder);
    }


    public Completable addAssetToPortfolio(@NonNull final PortfolioAsset portfolioAsset) {
        return mPortfolioAssetDiskDataStore.storeSingular(portfolioAsset);
    }

    public Completable removeAssetFromPortfolio(@NonNull final String id) {
        return mPortfolioAssetDiskDataStore.removeSingular(id);
    }


    public Single<Boolean> isAssetInPortfolio(@NonNull final String id) {
        return mPortfolioAssetDiskDataStore.isAssetInPortfolio(id);
    }


    public Flowable<PortfolioAsset> getPortfolioAsset(@NonNull final String id) {
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
//                .flatMapObservable(Observable::fromIterable)
                .map(mAssetMapper::mapUp)
//                .toList()
                .doOnSuccess(mPortfolioAssetDiskDataStore::updateAll)
                .toCompletable();
    }

    public Completable fetchPortfolioAsset(@NonNull final String id) {
        return mAssetNetworkDataStore.asset(id)
                .subscribeOn(Schedulers.io())
                .map(mAssetMapper::mapUp)
                .doOnSuccess(mPortfolioAssetDiskDataStore::updateSingular)
                .toCompletable();
    }
}