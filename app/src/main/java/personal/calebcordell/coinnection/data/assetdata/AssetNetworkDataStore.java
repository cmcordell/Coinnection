package personal.calebcordell.coinnection.data.assetdata;

import android.support.annotation.NonNull;
import android.util.Pair;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Flowable;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import personal.calebcordell.coinnection.data.assetpairdata.AssetPairDiskDataStore;
import personal.calebcordell.coinnection.data.assetpairdata.AssetPairEntity;
import personal.calebcordell.coinnection.data.assetpairdata.AssetPairMapper;
import personal.calebcordell.coinnection.data.portfolioassetdata.PortfolioAssetDiskDataStore;
import personal.calebcordell.coinnection.data.watchlistassetdata.WatchlistAssetDiskDataStore;
import personal.calebcordell.coinnection.presentation.Constants;
import personal.calebcordell.coinnection.presentation.Preferences;


/**
 * Uses Retrofit to get data from CoinMarketCap.com
 */
@Singleton
public class AssetNetworkDataStore {
    private static final String TAG = AssetNetworkDataStore.class.getSimpleName();

    private AssetService mAssetService;
    private Preferences mPreferences;

    private AssetDiskDataStore mAssetDiskDataStore;
    private PortfolioAssetDiskDataStore mPortfolioAssetDiskDataStore;
    private WatchlistAssetDiskDataStore mWatchlistAssetDiskDataStore;
    private AssetPairDiskDataStore mAssetPairDiskDataStore;


    @Inject
    public AssetNetworkDataStore(AssetService assetService,
                                 Preferences preferences,
                                 AssetDiskDataStore assetDiskDataStore,
                                 PortfolioAssetDiskDataStore portfolioAssetDiskDataStore,
                                 WatchlistAssetDiskDataStore watchlistAssetDiskDataStore,
                                 AssetPairDiskDataStore assetPairDiskDataStore) {
        mAssetService = assetService;
        mPreferences = preferences;

        mAssetDiskDataStore = assetDiskDataStore;
        mPortfolioAssetDiskDataStore = portfolioAssetDiskDataStore;
        mWatchlistAssetDiskDataStore = watchlistAssetDiskDataStore;
        mAssetPairDiskDataStore = assetPairDiskDataStore;
    }

    public Single<List<AssetEntity>> assets() {
        return mAssetService
                .assets(mPreferences.getCurrencyCode(), 0)
                .subscribeOn(Schedulers.io());
    }

    public Single<List<AssetEntity>> assets(@NonNull final List<String> ids) {
        return Flowable.fromIterable(ids)
                .subscribeOn(Schedulers.io())
                .flatMapSingle(this::asset)
                .filter(assetEntity -> !assetEntity.getId().equals(""))
                .toList();
    }

    public Single<List<AssetPairEntity>> assetPairs(@NonNull final List<Pair<String, String>> argumentsList) {
        return Flowable.fromIterable(argumentsList)
                .subscribeOn(Schedulers.io())
                .flatMapSingle(this::assetPair)
                .filter(assetPairEntity -> !assetPairEntity.getId().equals(""))
                .toList();
    }

    public Single<AssetEntity> asset(@NonNull final String id) {
        return mAssetService
                .asset(id, mPreferences.getCurrencyCode())
                .subscribeOn(Schedulers.io())
                .flatMap(assetEntities -> Single.just(assetEntities.get(0)))
                .doOnError((error) -> {
                    if (error.getMessage().equals(Constants.COIN_DOES_NOT_EXIST_MESSAGE)) {
                        mAssetDiskDataStore.removeSingular(id).subscribeOn(Schedulers.single()).subscribe();
                        mPortfolioAssetDiskDataStore.removeSingular(id).subscribeOn(Schedulers.single()).subscribe();
                        mWatchlistAssetDiskDataStore.removeSingular(id).subscribeOn(Schedulers.single()).subscribe();
                    }
                });
    }

    public Single<AssetPairEntity> assetPair(@NonNull final Pair<String, String> arguments) {
        return mAssetService
                .asset(arguments.first, arguments.second)
                .subscribeOn(Schedulers.io())
                .flatMap(assetEntities -> Single.just(assetEntities.get(0)).map(assetEntity -> AssetPairMapper.map(assetEntity, arguments.second)))
                .doOnError((error) -> {
                    if (error.getMessage().equals(Constants.COIN_DOES_NOT_EXIST_MESSAGE)) {
                        mAssetPairDiskDataStore.removeSingular(arguments.first, arguments.second).subscribeOn(Schedulers.single()).subscribe();
                    }
                });
    }
}