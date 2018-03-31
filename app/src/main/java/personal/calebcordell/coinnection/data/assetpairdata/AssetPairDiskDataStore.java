package personal.calebcordell.coinnection.data.assetpairdata;

import android.support.annotation.NonNull;
import android.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import personal.calebcordell.coinnection.domain.model.AssetPair;


@Singleton
public class AssetPairDiskDataStore {
    private static final String TAG = AssetPairDiskDataStore.class.getSimpleName();

    private Flowable<List<AssetPair>> mAllProcessor;
    private final Map<String, Flowable<AssetPair>> mProcessorMap = new HashMap<>();

    private final AssetPairDao mAssetPairDao;
    private final AssetPairMapper mAssetPairMapper;


    @Inject
    public AssetPairDiskDataStore(AssetPairDao assetPairDao,
                                  AssetPairMapper assetPairMapper) {
        mAssetPairDao = assetPairDao;
        mAssetPairMapper = assetPairMapper;
    }

    public Completable storeSingular(@NonNull final AssetPair assetPair) {
        return Completable.fromRunnable(() -> {
            assetPair.setPosition(mAssetPairDao.getCount());
            mAssetPairDao.insert(mAssetPairMapper.mapDown(assetPair));
        })
                .subscribeOn(Schedulers.io());
    }
    public Completable storeAll(@NonNull final List<AssetPair> assetPairs) {
        return Completable.fromRunnable(() -> {
            for(int i=0; i<assetPairs.size(); i++) {
                assetPairs.get(i).setPosition(mAssetPairDao.getCount() + i);
            }
            mAssetPairDao.insertAll(mAssetPairMapper.mapDown(assetPairs));
        })
                .subscribeOn(Schedulers.io());
    }

    public Completable removeSingular(@NonNull final String baseAssetId, @NonNull final String quoteCurrencySymbol) {
        return Completable.fromRunnable(() -> {
            removeFlowableForKey(baseAssetId + quoteCurrencySymbol);
            mAssetPairDao.remove(baseAssetId, quoteCurrencySymbol);

            List<AssetPairEntity> assetPairEntities = mAssetPairDao.getAll().blockingFirst();
            for (int i = 0; i < assetPairEntities.size(); i++) {
                assetPairEntities.get(i).setPosition(i);
            }
        })
                .subscribeOn(Schedulers.io());
    }
    public Completable removeMultiple(@NonNull final List<Pair<String, String>> argumentsList) {
        return Completable.fromRunnable(() -> {
            for (Pair<String, String> arguments : argumentsList) {
                removeFlowableForKey(arguments.first + arguments.second);
                mAssetPairDao.remove(arguments.first, arguments.second);
            }

            List<AssetPairEntity> assetPairEntities = mAssetPairDao.getAll().blockingFirst();
            for (int i = 0; i < assetPairEntities.size(); i++) {
                assetPairEntities.get(i).setPosition(i);
            }
            mAssetPairDao.updateAll(assetPairEntities);
        })
                .subscribeOn(Schedulers.io());
    }

    public Completable updateSingular(@NonNull final AssetPair assetPair) {
        return mAssetPairDao.get(assetPair.getId(), assetPair.getQuoteCurrencySymbol())
                .subscribeOn(Schedulers.io())
                .firstOrError()
                .doOnSuccess((entity) -> {
                    if (entity != null) {
                        entity.setName(assetPair.getName());
                        entity.setRank(assetPair.getRank());
                        entity.setPrice(assetPair.getPrice());
                        entity.setVolume24Hour(assetPair.getVolume24Hour());
                        entity.setMarketCap(assetPair.getMarketCap());
                        entity.setAvailableSupply(assetPair.getAvailableSupply());
                        entity.setTotalSupply(assetPair.getTotalSupply());
                        entity.setPercentChange1h(assetPair.getPercentChange1Hour());
                        entity.setPercentChange24h(assetPair.getPercentChange24Hour());
                        entity.setPercentChange7d(assetPair.getPercentChange7Day());
                        entity.setLastUpdated(assetPair.getLastUpdated());

                        mAssetPairDao.update(entity);
                    }
                })
                .toCompletable();
    }
    public Completable updateAll(@NonNull final List<AssetPair> assetPairs) {
        return mAssetPairDao.getAll()
                .subscribeOn(Schedulers.io())
                .firstOrError()
                .doOnSuccess((assetPairEntities) -> {
                    HashMap<String, AssetPair> assetPairMap = new HashMap<>(assetPairs.size());
                    for (AssetPair assetPair : assetPairs) {
                        assetPairMap.put(assetPair.getId() + assetPair.getQuoteCurrencySymbol(), assetPair);
                    }

                    for (AssetPairEntity entity : assetPairEntities) {
                        AssetPair assetPair = assetPairMap.get(entity.getId() + entity.getQuoteCurrencySymbol());
                        if (assetPair != null) {
                            entity.setName(assetPair.getName());
                            entity.setRank(assetPair.getRank());
                            entity.setPrice(assetPair.getPrice());
                            entity.setVolume24Hour(assetPair.getVolume24Hour());
                            entity.setMarketCap(assetPair.getMarketCap());
                            entity.setAvailableSupply(assetPair.getAvailableSupply());
                            entity.setTotalSupply(assetPair.getTotalSupply());
                            entity.setPercentChange1h(assetPair.getPercentChange1Hour());
                            entity.setPercentChange24h(assetPair.getPercentChange24Hour());
                            entity.setPercentChange7d(assetPair.getPercentChange7Day());
                            entity.setLastUpdated(assetPair.getLastUpdated());

                            mAssetPairDao.update(entity);
                        }
                    }
                })
                .toCompletable();
    }

    public Completable replaceAll(@NonNull final List<AssetPair> assetPairs) {
        return Completable.fromRunnable(() -> {
            for (int i = 0; i < assetPairs.size(); i++) {
                assetPairs.get(i).setPosition(i);
            }
            mAssetPairDao.saveAll(mAssetPairMapper.mapDown(assetPairs));
        }).subscribeOn(Schedulers.io());
    }

    public Single<List<Pair<String, String>>> getAllIds() {
        return mAssetPairDao.getAllSingle()
                .subscribeOn(Schedulers.io())
                .map(assetPairEntities -> {
                    List<Pair<String, String>> ids = new ArrayList<>(assetPairEntities.size());
                    for (AssetPairEntity entity : assetPairEntities) {
                        ids.add(new Pair<>(entity.getId(), entity.getQuoteCurrencySymbol()));
                    }
                    return ids;
                });
    }

    @NonNull
    public Flowable<AssetPair> getSingular(@NonNull final String baseAssetId, @NonNull final String quoteAssetSymbol) {
        Flowable<AssetPair> flowable = getFlowableForKey(baseAssetId + quoteAssetSymbol);
        if (flowable == null) {
            flowable = mAssetPairDao.get(baseAssetId, quoteAssetSymbol)
                    .subscribeOn(Schedulers.io())
                    .map(mAssetPairMapper::mapUp);

            addFlowableWithKey(baseAssetId + quoteAssetSymbol, flowable);
        }

        return flowable;
    }
    @NonNull
    public Flowable<List<AssetPair>> getAll() {
        if (mAllProcessor == null) {
            mAllProcessor = mAssetPairDao.getAll()
                    .subscribeOn(Schedulers.io())
                    .map(mAssetPairMapper::mapUp);
        }
        return mAllProcessor;
    }



    private Flowable<AssetPair> getFlowableForKey(@NonNull final String key) {
        synchronized (mProcessorMap) {
            if (mProcessorMap.containsKey(key)) {
                return mProcessorMap.get(key);
            }
            return null;
        }
    }
    private void addFlowableWithKey(@NonNull final String key, @NonNull final Flowable<AssetPair> flowable) {
        synchronized (mProcessorMap) {
            if (!mProcessorMap.containsKey(key)) {
                mProcessorMap.put(key, flowable);
            }
        }
    }
    private void removeFlowableForKey(@NonNull final String key) {
        synchronized (mProcessorMap) {
            mProcessorMap.remove(key);
        }
    }
}