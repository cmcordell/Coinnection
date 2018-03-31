package personal.calebcordell.coinnection.domain.interactor.impl.portfolioassetinteractors;

import android.support.annotation.NonNull;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import personal.calebcordell.coinnection.domain.interactor.base.CompletableInteractor;
import personal.calebcordell.coinnection.domain.repository.PortfolioAssetRepository;


public class RemoveAssetFromPortfolioInteractor extends CompletableInteractor<String> {
    private static final String TAG = RemoveAssetFromPortfolioInteractor.class.getSimpleName();

    private final PortfolioAssetRepository mPortfolioAssetRepository;

    @Inject
    public RemoveAssetFromPortfolioInteractor(PortfolioAssetRepository portfolioAssetRepository) {
        mPortfolioAssetRepository = portfolioAssetRepository;
    }

    protected Completable buildCompletable(@NonNull final String assetId) {
        return mPortfolioAssetRepository.removeAssetFromPortfolio(assetId)
                .observeOn(AndroidSchedulers.mainThread());
    }
}