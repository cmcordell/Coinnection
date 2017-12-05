package personal.calebcordell.coinnection.domain.interactor.impl;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import personal.calebcordell.coinnection.data.portfolioassetdata.PortfolioAssetRepositoryImpl;
import personal.calebcordell.coinnection.domain.interactor.base.CompletableInteractor;
import personal.calebcordell.coinnection.domain.model.PortfolioAsset;
import personal.calebcordell.coinnection.domain.repository.PortfolioAssetRepository;


public class ReorderPortfolioAssetsInteractor extends CompletableInteractor<List<PortfolioAsset>> {
    private static final String TAG = ReorderPortfolioAssetsInteractor.class.getSimpleName();

    private final PortfolioAssetRepository mPortfolioAssetRepository;

    public ReorderPortfolioAssetsInteractor() {
        mPortfolioAssetRepository = new PortfolioAssetRepositoryImpl();
    }

    protected Completable buildCompletable(final List<PortfolioAsset> portfolioAssets) {
        return mPortfolioAssetRepository.reorderPortfolio(portfolioAssets)
                .observeOn(AndroidSchedulers.mainThread());
    }
}