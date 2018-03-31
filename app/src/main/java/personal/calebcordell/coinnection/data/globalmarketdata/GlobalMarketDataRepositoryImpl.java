package personal.calebcordell.coinnection.data.globalmarketdata;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.schedulers.Schedulers;
import personal.calebcordell.coinnection.domain.model.GlobalMarketData;
import personal.calebcordell.coinnection.domain.repository.GlobalMarketDataRepository;


@Singleton
public class GlobalMarketDataRepositoryImpl implements GlobalMarketDataRepository {

    private final GlobalMarketDiskDataStore mGlobalMarketDiskDataStore;
    private final GlobalMarketNetworkDataStore mGlobalMarketNetworkDataStore;
    private final GlobalMarketDataMapperFunction mGlobalMarketDataMapperFunction;


    @Inject
    public GlobalMarketDataRepositoryImpl(GlobalMarketDiskDataStore globalMarketDiskDataStore,
                                          GlobalMarketNetworkDataStore globalMarketNetworkDataStore,
                                          GlobalMarketDataMapperFunction globalMarketDataMapperFunction) {
        mGlobalMarketDiskDataStore = globalMarketDiskDataStore;
        mGlobalMarketNetworkDataStore = globalMarketNetworkDataStore;
        mGlobalMarketDataMapperFunction = globalMarketDataMapperFunction;
    }


    public Flowable<GlobalMarketData> getGlobalMarketData() {
        return mGlobalMarketDiskDataStore.getSingular();
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