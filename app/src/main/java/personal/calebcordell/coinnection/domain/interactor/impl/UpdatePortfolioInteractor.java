package personal.calebcordell.coinnection.domain.interactor.impl;

import io.reactivex.android.schedulers.AndroidSchedulers;
import personal.calebcordell.coinnection.data.portfoliodata.PortfolioRepositoryImpl;
import personal.calebcordell.coinnection.domain.interactor.base.CompletableInteractor;
import personal.calebcordell.coinnection.domain.model.Portfolio;
import personal.calebcordell.coinnection.domain.repository.PortfolioRepository;

import io.reactivex.Completable;


public class UpdatePortfolioInteractor extends CompletableInteractor<Portfolio> {
    private static final String TAG = UpdatePortfolioInteractor.class.getSimpleName();

    private final PortfolioRepository mPortfolioRepository;

    public UpdatePortfolioInteractor() {
        mPortfolioRepository = new PortfolioRepositoryImpl();
    }

    protected Completable buildCompletable(final Portfolio portfolio) {
        return mPortfolioRepository.createOrUpdatePortfolio(portfolio)
                .observeOn(AndroidSchedulers.mainThread());
    }
}