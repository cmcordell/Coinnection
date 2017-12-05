package personal.calebcordell.coinnection.data.portfoliodata;

import android.support.annotation.NonNull;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.schedulers.Schedulers;
import personal.calebcordell.coinnection.data.base.AppDatabase;
import personal.calebcordell.coinnection.domain.model.Portfolio;
import personal.calebcordell.coinnection.presentation.App;


public class PortfolioDiskDataStore {

    private static PortfolioDiskDataStore INSTANCE;

    private PortfolioDao mPortfolioDao;

    private Flowable<Portfolio> mFlowable;


    public PortfolioDiskDataStore() {
        mPortfolioDao = AppDatabase.getDatabase(App.getAppContext()).portfolioDao();
    }

    public static PortfolioDiskDataStore getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new PortfolioDiskDataStore();
        }
        return INSTANCE;
    }

    public Completable storeSingular(@NonNull final Portfolio portfolio) {
        return Completable.fromRunnable(() -> mPortfolioDao.insert(portfolio))
                .subscribeOn(Schedulers.computation());
    }

    @NonNull
    public Flowable<Portfolio> getSingular(@NonNull final String id) {
        if(mFlowable == null) {
            mFlowable = mPortfolioDao.get()
                    .subscribeOn(Schedulers.computation());
        }
        return mFlowable;
    }
}