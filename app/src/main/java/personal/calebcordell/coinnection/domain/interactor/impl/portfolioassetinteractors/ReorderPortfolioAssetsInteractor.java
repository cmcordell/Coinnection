package personal.calebcordell.coinnection.domain.interactor.impl.portfolioassetinteractors;

import android.support.annotation.NonNull;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import personal.calebcordell.coinnection.domain.interactor.base.CompletableInteractor;
import personal.calebcordell.coinnection.domain.model.PortfolioAsset;
import personal.calebcordell.coinnection.domain.repository.PortfolioAssetRepository;


public class ReorderPortfolioAssetsInteractor extends CompletableInteractor<List<PortfolioAsset>> {
    private static final String TAG = ReorderPortfolioAssetsInteractor.class.getSimpleName();

    private final PortfolioAssetRepository mPortfolioAssetRepository;

    @Inject
    public ReorderPortfolioAssetsInteractor(PortfolioAssetRepository portfolioAssetRepository) {
        mPortfolioAssetRepository = portfolioAssetRepository;
    }

    protected Completable buildCompletable(@NonNull final List<PortfolioAsset> portfolioAssets) {
        return mPortfolioAssetRepository.reorderPortfolio(portfolioAssets)
                .observeOn(AndroidSchedulers.mainThread());
    }
}