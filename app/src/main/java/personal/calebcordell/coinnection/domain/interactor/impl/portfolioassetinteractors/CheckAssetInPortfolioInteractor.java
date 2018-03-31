package personal.calebcordell.coinnection.domain.interactor.impl.portfolioassetinteractors;

import android.support.annotation.NonNull;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import personal.calebcordell.coinnection.domain.interactor.base.SingleInteractor;
import personal.calebcordell.coinnection.domain.repository.PortfolioAssetRepository;


public class CheckAssetInPortfolioInteractor extends SingleInteractor<String, Boolean> {
    private static final String TAG = CheckAssetInPortfolioInteractor.class.getSimpleName();

    private final PortfolioAssetRepository mPortfolioAssetRepository;

    @Inject
    public CheckAssetInPortfolioInteractor(PortfolioAssetRepository portfolioAssetRepository) {
        mPortfolioAssetRepository = portfolioAssetRepository;
    }

    protected Single<Boolean> buildSingle(@NonNull final String id) {
        return mPortfolioAssetRepository.isAssetInPortfolio(id)
                .observeOn(AndroidSchedulers.mainThread());
    }
}