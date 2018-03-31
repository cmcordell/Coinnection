package personal.calebcordell.coinnection.data.watchlistassetdata;

import android.support.annotation.NonNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import personal.calebcordell.coinnection.domain.model.Asset;
import personal.calebcordell.coinnection.domain.model.WatchlistAsset;


@Singleton
public class WatchlistAssetDiskDataStore {
    private static final String TAG = WatchlistAssetDiskDataStore.class.getSimpleName();

    private Flowable<List<WatchlistAsset>> mAllProcessor;
    private final Map<String, Flowable<WatchlistAsset>> mProcessorMap = new HashMap<>();

    private final WatchlistAssetDao mWatchlistAssetDao;
    private final WatchlistAssetMapper mWatchlistAssetMapper;

    @Inject
    public WatchlistAssetDiskDataStore(WatchlistAssetDao watchlistAssetDao,
                                       WatchlistAssetMapper watchlistAssetMapper) {
        mWatchlistAssetDao = watchlistAssetDao;
        mWatchlistAssetMapper = watchlistAssetMapper;
    }


    public Completable storeSingular(@NonNull final WatchlistAsset watchlistAsset) {
        return Completable.fromRunnable(() -> {
            watchlistAsset.setPosition(mWatchlistAssetDao.getCount());
            mWatchlistAssetDao.insert(mWatchlistAssetMapper.mapDown(watchlistAsset));
        })
                .subscribeOn(Schedulers.io());
    }

    public Completable storeAll(@NonNull final List<WatchlistAsset> watchlistAssets) {
        return Completable.fromRunnable(() -> {
            int offset = mWatchlistAssetDao.getCount();
            int size = watchlistAssets.size();
            for (int i = 0; i < size; i++) {
                watchlistAssets.get(i).setPosition(offset + i);
            }

            mWatchlistAssetDao.insertAll(mWatchlistAssetMapper.mapDown(watchlistAssets));
        })
                .subscribeOn(Schedulers.io());
    }


    public Completable removeSingular(@NonNull final String id) {
        removeFlowableForKey(id);
        return Completable.fromRunnable(() -> {
            int position = mWatchlistAssetDao.getPosition(id);
            mWatchlistAssetDao.remove(id);
            mWatchlistAssetDao.updatePositions(position);
        })
                .subscribeOn(Schedulers.io());
    }

    public Completable removeMultiple(@NonNull final List<String> ids) {
        return Completable.fromRunnable(() -> {
            for (String id : ids) {
                removeFlowableForKey(id);
                mWatchlistAssetDao.remove(id);
            }

            List<WatchlistAssetEntity> watchlistAssetEntities = mWatchlistAssetDao.getAll().blockingFirst();
            for (int i = 0; i < watchlistAssetEntities.size(); i++) {
                watchlistAssetEntities.get(i).setPosition(i);
            }
            mWatchlistAssetDao.insertAll(watchlistAssetEntities);
        })
                .subscribeOn(Schedulers.io());
    }


    public void updateSingular(@NonNull final Asset asset) {
        mWatchlistAssetDao.get(asset.getId())
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .firstOrError()
                .subscribe(new DisposableSingleObserver<List<WatchlistAssetEntity>>() {
                    @Override
                    public void onSuccess(List<WatchlistAssetEntity> watchlistAssetEntities) {
                        if (watchlistAssetEntities.size() > 0) {
                            WatchlistAssetEntity entity = watchlistAssetEntities.get(0);
                            entity.setName(asset.getName());
                            entity.setSymbol(asset.getSymbol());
                            entity.setRank(asset.getRank());
                            entity.setPrice(asset.getPrice());
                            entity.setVolume24Hour(asset.getVolume24Hour());
                            entity.setMarketCap(asset.getMarketCap());
                            entity.setAvailableSupply(asset.getAvailableSupply());
                            entity.setTotalSupply(asset.getTotalSupply());
                            entity.setPercentChange1h(asset.getPercentChange1Hour());
                            entity.setPercentChange24h(asset.getPercentChange24Hour());
                            entity.setPercentChange7d(asset.getPercentChange7Day());
                            entity.setLastUpdated(asset.getLastUpdated());

                            mWatchlistAssetDao.insert(entity);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {}
                });
    }

    public void updateAll(@NonNull final List<Asset> assets) {
        mWatchlistAssetDao.getAll()
                .subscribeOn(Schedulers.newThread())
                .firstOrError()
                .subscribe(new DisposableSingleObserver<List<WatchlistAssetEntity>>() {
                    @Override
                    public void onSuccess(List<WatchlistAssetEntity> watchlistAssetEntities) {
                        HashMap<String, Asset> assetMap = new HashMap<>(assets.size());
                        for (Asset asset : assets) {
                            assetMap.put(asset.getId(), asset);
                        }

                        for (WatchlistAssetEntity entity : watchlistAssetEntities) {
                            Asset asset = assetMap.get(entity.getId());
                            if (asset != null) {
                                entity.setName(asset.getName());
                                entity.setSymbol(asset.getSymbol());
                                entity.setRank(asset.getRank());
                                entity.setPrice(asset.getPrice());
                                entity.setVolume24Hour(asset.getVolume24Hour());
                                entity.setMarketCap(asset.getMarketCap());
                                entity.setAvailableSupply(asset.getAvailableSupply());
                                entity.setTotalSupply(asset.getTotalSupply());
                                entity.setPercentChange1h(asset.getPercentChange1Hour());
                                entity.setPercentChange24h(asset.getPercentChange24Hour());
                                entity.setPercentChange7d(asset.getPercentChange7Day());
                                entity.setLastUpdated(asset.getLastUpdated());

                                mWatchlistAssetDao.insert(entity);
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {}
                });
    }

    public void updateAll(@NonNull final Map<String, Asset> assetMap) {
        mWatchlistAssetDao.getAll()
                .subscribeOn(Schedulers.io())
                .firstOrError()
                .subscribe(new DisposableSingleObserver<List<WatchlistAssetEntity>>() {
                    @Override
                    public void onSuccess(List<WatchlistAssetEntity> watchlistAssetEntities) {
                        for (WatchlistAssetEntity entity : watchlistAssetEntities) {
                            Asset asset = assetMap.get(entity.getId());
                            if (asset != null) {
                                entity.setName(asset.getName());
                                entity.setSymbol(asset.getSymbol());
                                entity.setRank(asset.getRank());
                                entity.setPrice(asset.getPrice());
                                entity.setVolume24Hour(asset.getVolume24Hour());
                                entity.setMarketCap(asset.getMarketCap());
                                entity.setAvailableSupply(asset.getAvailableSupply());
                                entity.setTotalSupply(asset.getTotalSupply());
                                entity.setPercentChange1h(asset.getPercentChange1Hour());
                                entity.setPercentChange24h(asset.getPercentChange24Hour());
                                entity.setPercentChange7d(asset.getPercentChange7Day());
                                entity.setLastUpdated(asset.getLastUpdated());

                                mWatchlistAssetDao.insert(entity);
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {}
                });
    }


    public Completable replaceAll(@NonNull final List<WatchlistAsset> watchlistAssets) {
        return Completable.fromRunnable(() -> {
            int size = watchlistAssets.size();
            for (int i = 0; i < size; i++) {
                watchlistAssets.get(i).setPosition(i);
            }

            mWatchlistAssetDao.clear();
            mWatchlistAssetDao.insertAll(mWatchlistAssetMapper.mapDown(watchlistAssets));
        }).subscribeOn(Schedulers.io());
    }


    @NonNull
    public Single<List<String>> getAllIds() {
        return mWatchlistAssetDao.getAllIds()
                .subscribeOn(Schedulers.io());
    }


    @NonNull
    public Single<Boolean> isAssetOnWatchlist(@NonNull final String id) {
        return mWatchlistAssetDao.get(id)
                .subscribeOn(Schedulers.io())
                .firstElement()
                .toSingle()
                .map(assets -> assets.size() > 0);
    }


    @NonNull
    public Flowable<WatchlistAsset> getSingular(@NonNull final String id) {
        Flowable<WatchlistAsset> flowable = getFlowableForKey(id);
        if (flowable == null) {
            flowable = mWatchlistAssetDao.get(id)
                    .observeOn(Schedulers.io())
                    .map(assets -> assets.get(0))
                    .map(mWatchlistAssetMapper::mapUp);

            addFlowableWithKey(id, flowable);
        }

        return flowable;
    }

    @NonNull
    public Flowable<List<WatchlistAsset>> getAll() {
        if (mAllProcessor == null) {
            mAllProcessor = mWatchlistAssetDao.getAll()
                    .observeOn(Schedulers.io())
                    .map(mWatchlistAssetMapper::mapUp);
        }
        return mAllProcessor;
    }


    private Flowable<WatchlistAsset> getFlowableForKey(@NonNull final String key) {
        synchronized (mProcessorMap) {
            if (mProcessorMap.containsKey(key)) {
                return mProcessorMap.get(key);
            }
            return null;
        }
    }

    private void addFlowableWithKey(@NonNull final String key, @NonNull final Flowable<WatchlistAsset> flowable) {
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