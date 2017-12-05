package personal.calebcordell.coinnection.data.portfoliodata;

import android.support.annotation.NonNull;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import personal.calebcordell.coinnection.domain.model.Portfolio;
import personal.calebcordell.coinnection.domain.repository.PortfolioRepository;


public class PortfolioRepositoryImpl implements PortfolioRepository {
    private static final String TAG = PortfolioRepositoryImpl.class.getSimpleName();

    private PortfolioDiskDataStore mPortfolioDiskDataStore;

    public PortfolioRepositoryImpl() {
        mPortfolioDiskDataStore = PortfolioDiskDataStore.getInstance();
    }

    public Flowable<Portfolio> getPortfolio(String id) {
        return mPortfolioDiskDataStore.getSingular(id);
    }

    public Completable createOrUpdatePortfolio(@NonNull final Portfolio portfolio) {
        return mPortfolioDiskDataStore.storeSingular(portfolio);
    }
}