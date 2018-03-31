package personal.calebcordell.coinnection.data.globalmarketdata;

import android.support.annotation.NonNull;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.schedulers.Schedulers;
import personal.calebcordell.coinnection.domain.model.GlobalMarketData;


@Singleton
public class GlobalMarketDiskDataStore {

    private final GlobalMarketDataDao mGlobalMarketDataDao;

    private Flowable<GlobalMarketData> mFlowable;

    @Inject
    public GlobalMarketDiskDataStore(GlobalMarketDataDao globalMarketDataDao) {
        mGlobalMarketDataDao = globalMarketDataDao;
    }

    public void storeSingular(@NonNull final GlobalMarketData globalMarketData) {
        Completable.fromRunnable(() -> mGlobalMarketDataDao.insert(globalMarketData))
                .subscribeOn(Schedulers.computation())
                .subscribe();
    }

    @NonNull
    public Flowable<GlobalMarketData> getSingular() {
        if (mFlowable == null) {
            mFlowable = mGlobalMarketDataDao.get()
                    .subscribeOn(Schedulers.computation());
        }
        return mFlowable;
    }
}