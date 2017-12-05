package personal.calebcordell.coinnection.data.globalmarketdata;

import android.support.annotation.NonNull;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.schedulers.Schedulers;
import personal.calebcordell.coinnection.data.base.AppDatabase;
import personal.calebcordell.coinnection.domain.model.GlobalMarketData;
import personal.calebcordell.coinnection.presentation.App;


public class GlobalMarketDiskDataStore {
    //We need to update info about global market data every so often, 5 minutes
    private static final long EXPIRATION_TIME = 5 * 60 * 1000;

    private static GlobalMarketDiskDataStore INSTANCE;

    private GlobalMarketDataDao mGlobalMarketDataDao;

    private Flowable<GlobalMarketData> mFlowable;

    public GlobalMarketDiskDataStore() {
        mGlobalMarketDataDao = AppDatabase.getDatabase(App.getAppContext()).globalMarketDataDao();
    }

    public static GlobalMarketDiskDataStore getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new GlobalMarketDiskDataStore();
        }
        return INSTANCE;
    }

    public void storeSingular(@NonNull final GlobalMarketData globalMarketData) {
        Completable.fromRunnable(() -> mGlobalMarketDataDao.insert(globalMarketData))
                .subscribeOn(Schedulers.computation())
                .subscribe();
    }

    @NonNull
    public Flowable<GlobalMarketData> getSingular(@NonNull final String id) {
        if(mFlowable == null) {
            mFlowable = mGlobalMarketDataDao.get()
                    .subscribeOn(Schedulers.computation());
        }
        return mFlowable;
    }
}