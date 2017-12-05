package personal.calebcordell.coinnection.data.watchlistassetdata;

import android.arch.persistence.room.Transaction;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import personal.calebcordell.coinnection.data.base.AppDatabase;
import personal.calebcordell.coinnection.domain.model.Asset;
import personal.calebcordell.coinnection.domain.model.WatchlistAsset;
import personal.calebcordell.coinnection.presentation.App;


public class WatchlistAssetDiskDataStore {
    private static final String TAG = WatchlistAssetDiskDataStore.class.getSimpleName();

    private static WatchlistAssetDiskDataStore INSTANCE;

    private Flowable<List<WatchlistAsset>> mAllProcessor;

    @NonNull
    private final Map<String, Flowable<WatchlistAsset>> mProcessorMap = new HashMap<>();

    private WatchlistAssetDao mWatchlistAssetDao;
    private final WatchlistAssetMapper mWatchlistAssetMapper;


    public WatchlistAssetDiskDataStore() {
        mWatchlistAssetDao = AppDatabase.getDatabase(App.getAppContext()).watchlistAssetDao();
        mWatchlistAssetMapper = new WatchlistAssetMapper();
    }
    public static WatchlistAssetDiskDataStore getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new WatchlistAssetDiskDataStore();
        }
        return INSTANCE;
    }


    public Completable storeSingular(@NonNull final WatchlistAsset watchlistAsset) {
        return Completable.fromRunnable(() -> {
            watchlistAsset.setPosition(mWatchlistAssetDao.getCount());
            mWatchlistAssetDao.insert(mWatchlistAssetMapper.mapDown(watchlistAsset));
        })
                .subscribeOn(Schedulers.computation());
    }
    public Completable storeAll(@NonNull final List<WatchlistAsset> watchlistAssets) {
        return Completable.fromRunnable(() -> {
            int offset = mWatchlistAssetDao.getCount();
            int size = watchlistAssets.size();
            for(int i=0; i<size; i++) {
                watchlistAssets.get(i).setPosition(offset + i);
            }

            mWatchlistAssetDao.insertAll(mWatchlistAssetMapper.mapDown(watchlistAssets));
        })
                .subscribeOn(Schedulers.computation());
    }


    public Completable removeSingular(@NonNull final String id) {
        removeFlowableForKey(id);
        return Completable.fromRunnable(() -> {
            int position = mWatchlistAssetDao.getPosition(id);
            mWatchlistAssetDao.remove(id);
            mWatchlistAssetDao.updatePositions(position);
        })
                .subscribeOn(Schedulers.computation());
    }


    public void updateSingular(@NonNull final Asset asset) {
        mWatchlistAssetDao.get(asset.getId())
                .subscribeOn(Schedulers.computation())
                .observeOn(Schedulers.computation())
                .firstOrError()
                .subscribe(new DisposableSingleObserver<List<WatchlistAssetEntity>>() {
                    @Override
                    public void onSuccess(List<WatchlistAssetEntity> watchlistAssetEntities) {
                        if(watchlistAssetEntities.size() > 0) {
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
                    @Override public void onError(Throwable e) {e.printStackTrace();}
                });
    }
    public void updateAll(@NonNull final List<Asset> assets) {
        mWatchlistAssetDao.getAll()
                .subscribeOn(Schedulers.computation())
                .observeOn(Schedulers.computation())
                .firstOrError()
                .subscribe(new DisposableSingleObserver<List<WatchlistAssetEntity>>() {
                    @Override
                    public void onSuccess(List<WatchlistAssetEntity> watchlistAssetEntities) {
                        HashMap<String, Asset> assetMap = new HashMap<>(assets.size());
                        for(Asset asset : assets) {
                            assetMap.put(asset.getId(), asset);
                        }

                        for(WatchlistAssetEntity entity : watchlistAssetEntities) {
                            Asset asset = assetMap.get(entity.getId());
                            if(asset != null) {
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
                    @Override public void onError(Throwable e) {e.printStackTrace();}
                });
    }


    public Completable replaceAll(@NonNull final List<WatchlistAsset> watchlistAssets) {
        return Completable.fromRunnable(() -> {
            int size = watchlistAssets.size();
            for(int i=0; i<size; i++) {
                watchlistAssets.get(i).setPosition(i);
            }

            mWatchlistAssetDao.clear();
            mWatchlistAssetDao.insertAll(mWatchlistAssetMapper.mapDown(watchlistAssets));
        }).subscribeOn(Schedulers.computation());
    }


    @NonNull
    public Single<List<String>> getAllIds() {
        return mWatchlistAssetDao.getAllIds()
                .subscribeOn(Schedulers.computation());
    }


    @NonNull
    public Single<Boolean> isAssetOnWatchlist(@NonNull final String id) {
        return mWatchlistAssetDao.get(id)
                .subscribeOn(Schedulers.computation())
                .firstElement()
                .toSingle()
                .map(assets -> assets.size() > 0);
    }


    @NonNull
    public Flowable<WatchlistAsset> getSingular(@NonNull final String id) {
        Flowable<WatchlistAsset> flowable = getFlowableForKey(id);
        if(flowable == null) {
            flowable = mWatchlistAssetDao.get(id)
                    .observeOn(Schedulers.computation())
                    .map(assets -> assets.get(0))
                    .map(mWatchlistAssetMapper::mapUp);

            addFlowableWithKey(id, flowable);
        }

        return flowable;
    }
    @NonNull
    public Flowable<List<WatchlistAsset>> getAll() {
        if(mAllProcessor == null) {
            mAllProcessor = mWatchlistAssetDao.getAll()
                    .observeOn(Schedulers.computation())
                    .map(mWatchlistAssetMapper::mapUp);
        }
        return mAllProcessor;
    }


    private Flowable<WatchlistAsset> getFlowableForKey(String key) {
        synchronized (mProcessorMap) {
            if(mProcessorMap.containsKey(key)) {
                return mProcessorMap.get(key);
            }
            return null;
        }
    }
    private void addFlowableWithKey(String key, Flowable<WatchlistAsset> flowable) {
        synchronized (mProcessorMap) {
            if(!mProcessorMap.containsKey(key)) {
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