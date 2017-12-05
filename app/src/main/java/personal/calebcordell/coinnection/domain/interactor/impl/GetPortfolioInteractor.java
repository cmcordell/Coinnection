package personal.calebcordell.coinnection.domain.interactor.impl;

import io.reactivex.android.schedulers.AndroidSchedulers;
import personal.calebcordell.coinnection.data.portfoliodata.PortfolioRepositoryImpl;
import personal.calebcordell.coinnection.domain.interactor.base.FlowableInteractor1;
import personal.calebcordell.coinnection.domain.model.Portfolio;
import personal.calebcordell.coinnection.domain.repository.PortfolioRepository;

import io.reactivex.Flowable;


public class GetPortfolioInteractor extends FlowableInteractor1<Portfolio> {
    private static final String TAG = GetPortfolioInteractor.class.getSimpleName();

    private final PortfolioRepository mPortfolioRepository;

    public GetPortfolioInteractor() {
        mPortfolioRepository = new PortfolioRepositoryImpl();
    }

    protected Flowable<Portfolio> buildFlowable() {
        return mPortfolioRepository.getPortfolio("")
                .observeOn(AndroidSchedulers.mainThread());
    }
}