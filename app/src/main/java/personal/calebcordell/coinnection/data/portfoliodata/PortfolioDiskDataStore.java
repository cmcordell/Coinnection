package personal.calebcordell.coinnection.data.portfoliodata;

import android.support.annotation.NonNull;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.schedulers.Schedulers;
import personal.calebcordell.coinnection.domain.model.Portfolio;


@Singleton
public class PortfolioDiskDataStore {

    private final PortfolioDao mPortfolioDao;

    private Flowable<Portfolio> mFlowable;

    @Inject
    public PortfolioDiskDataStore(PortfolioDao portfolioDao) {
        mPortfolioDao = portfolioDao;
    }

    public Completable storeSingular(@NonNull final Portfolio portfolio) {
        return Completable.fromRunnable(() -> mPortfolioDao.insert(portfolio))
                .subscribeOn(Schedulers.computation());
    }

    @NonNull
    public Flowable<Portfolio> getSingular(@NonNull final String id) {
        if (mFlowable == null) {
            mFlowable = mPortfolioDao.get()
                    .subscribeOn(Schedulers.computation());
        }
        return mFlowable;
    }
}