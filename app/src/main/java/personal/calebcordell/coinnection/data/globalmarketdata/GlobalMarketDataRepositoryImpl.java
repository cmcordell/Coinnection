package personal.calebcordell.coinnection.data.globalmarketdata;

import io.reactivex.Completable;
import io.reactivex.schedulers.Schedulers;
import personal.calebcordell.coinnection.domain.model.GlobalMarketData;
import personal.calebcordell.coinnection.domain.repository.GlobalMarketDataRepository;

import io.reactivex.Flowable;


public class GlobalMarketDataRepositoryImpl implements GlobalMarketDataRepository {

    private GlobalMarketDiskDataStore mGlobalMarketDiskDataStore;
    private GlobalMarketNetworkDataStore mGlobalMarketNetworkDataStore;
    private GlobalMarketDataMapperFunction mGlobalMarketDataMapperFunction;

    public GlobalMarketDataRepositoryImpl() {
        mGlobalMarketDiskDataStore = GlobalMarketDiskDataStore.getInstance();
        mGlobalMarketNetworkDataStore = GlobalMarketNetworkDataStore.getInstance();
        mGlobalMarketDataMapperFunction = new GlobalMarketDataMapperFunction();
    }


    public Flowable<GlobalMarketData> getGlobalMarketData() {
        return mGlobalMarketDiskDataStore.getSingular("1");
    }

    public Completable fetchGlobalMarketData() {
        return mGlobalMarketNetworkDataStore.getGlobalMarketData()
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.computation())
                .map(mGlobalMarketDataMapperFunction)
                .doOnSuccess(mGlobalMarketDiskDataStore::storeSingular)
                .toCompletable();
    }
}