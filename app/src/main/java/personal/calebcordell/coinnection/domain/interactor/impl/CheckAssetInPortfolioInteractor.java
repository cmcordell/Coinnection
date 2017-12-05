package personal.calebcordell.coinnection.domain.interactor.impl;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import personal.calebcordell.coinnection.data.portfolioassetdata.PortfolioAssetRepositoryImpl;
import personal.calebcordell.coinnection.domain.interactor.base.SingleInteractor;
import personal.calebcordell.coinnection.domain.repository.PortfolioAssetRepository;


public class CheckAssetInPortfolioInteractor extends SingleInteractor<String, Boolean> {
    private static final String TAG = CheckAssetInPortfolioInteractor.class.getSimpleName();

    private PortfolioAssetRepository mPortfolioAssetRepository;

    public CheckAssetInPortfolioInteractor() {
        mPortfolioAssetRepository = new PortfolioAssetRepositoryImpl();
    }

    protected Single<Boolean> buildSingle(String id) {
        return mPortfolioAssetRepository.isAssetInPortfolio(id)
                .observeOn(AndroidSchedulers.mainThread());
    }
}