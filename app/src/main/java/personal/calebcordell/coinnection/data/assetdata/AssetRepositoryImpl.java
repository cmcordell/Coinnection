package personal.calebcordell.coinnection.data.assetdata;

import android.support.annotation.NonNull;

import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import personal.calebcordell.coinnection.data.portfolioassetdata.PortfolioAssetDiskDataStore;
import personal.calebcordell.coinnection.data.watchlistassetdata.WatchlistAssetDiskDataStore;
import personal.calebcordell.coinnection.domain.model.Asset;
import personal.calebcordell.coinnection.domain.repository.AssetRepository;
import personal.calebcordell.coinnection.presentation.Preferences;


@Singleton
public class AssetRepositoryImpl implements AssetRepository {
    private String TAG = AssetRepositoryImpl.class.getSimpleName();

    private final AssetNetworkDataStore mAssetNetworkDataStore;
    private final AssetDiskDataStore mAssetDiskDataStore;
    private final PortfolioAssetDiskDataStore mPortfolioAssetDiskDataStore;
    private final WatchlistAssetDiskDataStore mWatchlistAssetDiskDataStore;
    private final Preferences mPreferences;

    private final AssetMapper mAssetMapper;

    @Inject
    public AssetRepositoryImpl(AssetNetworkDataStore assetNetworkDataStore,
                               AssetDiskDataStore assetDiskDataStore,
                               PortfolioAssetDiskDataStore portfolioAssetDiskDataStore,
                               WatchlistAssetDiskDataStore watchlistAssetDiskDataStore,
                               Preferences preferences,
                               AssetMapper assetMapper) {
        mAssetNetworkDataStore = assetNetworkDataStore;
        mAssetDiskDataStore = assetDiskDataStore;
        mPortfolioAssetDiskDataStore = portfolioAssetDiskDataStore;
        mWatchlistAssetDiskDataStore = watchlistAssetDiskDataStore;
        mPreferences = preferences;
        mAssetMapper = assetMapper;
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
                .map(mAssetMapper::mapUp)
                .doOnSuccess(asset -> {
                    mAssetDiskDataStore.storeSingular(asset);
                    mPortfolioAssetDiskDataStore.updateSingular(asset);
                    mWatchlistAssetDiskDataStore.updateSingular(asset);
                })
                .toCompletable();
    }

    public Completable fetchAllAssets(final boolean forceFetch) {
        if (forceFetch || mPreferences.needsFullUpdate()) {
            return mAssetNetworkDataStore.assets()
                    .subscribeOn(Schedulers.io())
                    .map(mAssetMapper::mapUp)
                    .doOnSuccess(assets -> {
                        mAssetDiskDataStore.replaceAll(assets);
                        HashMap<String, Asset> assetMap = new HashMap<>(assets.size());
                        for (Asset asset : assets) {
                            assetMap.put(asset.getId(), asset);
                        }
                        mPortfolioAssetDiskDataStore.updateAll(assetMap);
                        mWatchlistAssetDiskDataStore.updateAll(assetMap);
                        mPreferences.setLastFullUpdateTime(System.currentTimeMillis());
                    })
                    .observeOn(AndroidSchedulers.mainThread())
                    .toCompletable();
        }
        return Completable.complete();
    }
}