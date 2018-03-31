package personal.calebcordell.coinnection.data.portfoliodata;

import android.support.annotation.NonNull;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import personal.calebcordell.coinnection.domain.model.Portfolio;
import personal.calebcordell.coinnection.domain.repository.PortfolioRepository;


@Singleton
public class PortfolioRepositoryImpl implements PortfolioRepository {
    private static final String TAG = PortfolioRepositoryImpl.class.getSimpleName();

    private final PortfolioDiskDataStore mPortfolioDiskDataStore;

    @Inject
    public PortfolioRepositoryImpl(PortfolioDiskDataStore portfolioDiskDataStore) {
        mPortfolioDiskDataStore = portfolioDiskDataStore;
    }

    public Flowable<Portfolio> getPortfolio(@NonNull final String id) {
        return mPortfolioDiskDataStore.getSingular(id);
    }

    public Completable createOrUpdatePortfolio(@NonNull final Portfolio portfolio) {
        return mPortfolioDiskDataStore.storeSingular(portfolio);
    }
}