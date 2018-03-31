package personal.calebcordell.coinnection.domain.interactor.impl.portfoliointeractors;

import android.support.annotation.NonNull;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import personal.calebcordell.coinnection.domain.interactor.base.CompletableInteractor;
import personal.calebcordell.coinnection.domain.model.Portfolio;
import personal.calebcordell.coinnection.domain.repository.PortfolioRepository;


public class UpdatePortfolioInteractor extends CompletableInteractor<Portfolio> {
    private static final String TAG = UpdatePortfolioInteractor.class.getSimpleName();

    private final PortfolioRepository mPortfolioRepository;

    @Inject
    public UpdatePortfolioInteractor(PortfolioRepository portfolioRepository) {
        mPortfolioRepository = portfolioRepository;
    }

    protected Completable buildCompletable(@NonNull final Portfolio portfolio) {
        return mPortfolioRepository.createOrUpdatePortfolio(portfolio)
                .observeOn(AndroidSchedulers.mainThread());
    }
}