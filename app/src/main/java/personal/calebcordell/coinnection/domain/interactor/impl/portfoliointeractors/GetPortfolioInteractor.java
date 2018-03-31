package personal.calebcordell.coinnection.domain.interactor.impl.portfoliointeractors;

import android.support.annotation.NonNull;

import javax.inject.Inject;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import personal.calebcordell.coinnection.domain.interactor.base.FlowableInteractor;
import personal.calebcordell.coinnection.domain.model.Portfolio;
import personal.calebcordell.coinnection.domain.repository.PortfolioRepository;


public class GetPortfolioInteractor extends FlowableInteractor<String, Portfolio> {
    private static final String TAG = GetPortfolioInteractor.class.getSimpleName();

    private final PortfolioRepository mPortfolioRepository;

    @Inject
    public GetPortfolioInteractor(PortfolioRepository portfolioRepository) {
        mPortfolioRepository = portfolioRepository;
    }

    protected Flowable<Portfolio> buildFlowable(@NonNull final String id) {
        return mPortfolioRepository.getPortfolio(id)
                .observeOn(AndroidSchedulers.mainThread());
    }
}