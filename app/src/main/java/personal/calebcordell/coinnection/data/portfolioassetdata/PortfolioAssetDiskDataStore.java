package personal.calebcordell.coinnection.data.portfolioassetdata;

import android.arch.persistence.room.Transaction;
import android.support.annotation.NonNull;
import android.util.Log;

import org.reactivestreams.Publisher;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.CompletableSource;
import io.reactivex.Flowable;
import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import personal.calebcordell.coinnection.data.base.AppDatabase;
import personal.calebcordell.coinnection.domain.model.Asset;
import personal.calebcordell.coinnection.domain.model.PortfolioAsset;
import personal.calebcordell.coinnection.presentation.App;


public class PortfolioAssetDiskDataStore {
    private static final String TAG = PortfolioAssetDiskDataStore.class.getSimpleName();

    private static PortfolioAssetDiskDataStore INSTANCE;

    private Flowable<List<PortfolioAsset>> mAllProcessor;

    @NonNull
    private final Map<String, Flowable<PortfolioAsset>> mProcessorMap = new HashMap<>();

    private PortfolioAssetDao mPortfolioAssetDao;
    private final PortfolioAssetMapper mPortfolioAssetMapper;


    public PortfolioAssetDiskDataStore() {
        mPortfolioAssetDao = AppDatabase.getDatabase(App.getAppContext()).portfolioAssetDao();
        mPortfolioAssetMapper = new PortfolioAssetMapper();
    }
    public static PortfolioAssetDiskDataStore getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new PortfolioAssetDiskDataStore();
        }
        return INSTANCE;
    }


    public Completable storeSingular(@NonNull final PortfolioAsset portfolioAsset) {
        return Completable.fromRunnable(() -> {
            portfolioAsset.setPosition(mPortfolioAssetDao.getCount());
            mPortfolioAssetDao.insert(mPortfolioAssetMapper.mapDown(portfolioAsset));
        })
                .subscribeOn(Schedulers.computation());
    }
    public Completable storeAll(@NonNull final List<PortfolioAsset> portfolioAssets) {
        return Completable.fromRunnable(() -> {
            int offset = mPortfolioAssetDao.getCount();
            int size = portfolioAssets.size();
            for(int i=0; i<size; i++) {
                portfolioAssets.get(i).setPosition(offset + i);
            }

            mPortfolioAssetDao.insertAll(mPortfolioAssetMapper.mapDown(portfolioAssets));
        })
                .subscribeOn(Schedulers.computation());
    }

    //TODO See if we can combine sqlite statments to make this faster
    public Completable removeSingular(@NonNull final String id) {
        removeFlowableForKey(id);
        return Completable.fromRunnable(() -> {
            int position = mPortfolioAssetDao.getPosition(id);
            mPortfolioAssetDao.remove(id);
            mPortfolioAssetDao.updatePositions(position);
        })
                .subscribeOn(Schedulers.computation());
    }

    public void updateSingular(@NonNull final Asset asset) {
        mPortfolioAssetDao.get(asset.getId())
                .subscribeOn(Schedulers.computation())
                .observeOn(Schedulers.computation())
                .firstOrError()
                .subscribe(new DisposableSingleObserver<List<PortfolioAssetEntity>>() {
                    @Override
                    public void onSuccess(List<PortfolioAssetEntity> portfolioAssetEntities) {
                        if(portfolioAssetEntities.size() > 0) {
                            PortfolioAssetEntity entity = portfolioAssetEntities.get(0);

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

                            mPortfolioAssetDao.insert(entity);
                        }
                    }
                    @Override public void onError(Throwable e) {e.printStackTrace();}
                });
    }
    public void updateAll(@NonNull final List<Asset> assets) {
        mPortfolioAssetDao.getAll()
                .subscribeOn(Schedulers.computation())
                .observeOn(Schedulers.computation())
                .firstOrError()
                .subscribe(new DisposableSingleObserver<List<PortfolioAssetEntity>>() {
                    @Override
                    public void onSuccess(List<PortfolioAssetEntity> portfolioAssetEntities) {
                        HashMap<String, Asset> assetMap = new HashMap<>(assets.size());
                        for(Asset asset : assets) {
                            assetMap.put(asset.getId(), asset);
                        }

                        for(PortfolioAssetEntity entity : portfolioAssetEntities) {
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

                                mPortfolioAssetDao.insert(entity);
                            }
                        }
                    }
                    @Override public void onError(Throwable e) {e.printStackTrace();}
                });
    }

    public Completable replaceAll(@NonNull final List<PortfolioAsset> portfolioAssets) {
        return Completable.fromRunnable(() -> {
            int size = portfolioAssets.size();
            for(int i=0; i<size; i++) {
                portfolioAssets.get(i).setPosition(i);
            }

            mPortfolioAssetDao.clear();
            mPortfolioAssetDao.insertAll(mPortfolioAssetMapper.mapDown(portfolioAssets));
        }).subscribeOn(Schedulers.computation());
    }

    @NonNull
    public Single<List<String>> getAllIds() {
        return mPortfolioAssetDao.getAllIds()
                .subscribeOn(Schedulers.computation());
    }

    @NonNull
    public Single<Boolean> isAssetInPortfolio(@NonNull final String id) {
        return mPortfolioAssetDao.get(id)
                .subscribeOn(Schedulers.computation())
                .firstElement()
                .toSingle()
                .map(assets -> assets.size() > 0);
    }

    @NonNull
    public Flowable<PortfolioAsset> getSingular(@NonNull final String id) {
        Flowable<PortfolioAsset> flowable = getFlowableForKey(id);
        if(flowable == null) {
            flowable = mPortfolioAssetDao.get(id)
                    .subscribeOn(Schedulers.computation())
                    .map(assets -> assets.get(0))
                    .map(mPortfolioAssetMapper::mapUp);

            addFlowableWithKey(id, flowable);
        }

        return flowable;
    }
    @NonNull
    public Flowable<List<PortfolioAsset>> getAll() {
        if(mAllProcessor == null) {
            mAllProcessor = mPortfolioAssetDao.getAll()
                    .subscribeOn(Schedulers.computation())
                    .map(mPortfolioAssetMapper::mapUp);
        }
        return mAllProcessor;
    }

    private Flowable<PortfolioAsset> getFlowableForKey(String key) {
        synchronized (mProcessorMap) {
            if(mProcessorMap.containsKey(key)) {
                return mProcessorMap.get(key);
            }
            return null;
        }
    }
    private void addFlowableWithKey(String key, Flowable<PortfolioAsset> flowable) {
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