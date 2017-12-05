package personal.calebcordell.coinnection.domain.interactor.impl;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import personal.calebcordell.coinnection.data.portfolioassetdata.PortfolioAssetRepositoryImpl;
import personal.calebcordell.coinnection.domain.interactor.base.CompletableInteractor;
import personal.calebcordell.coinnection.domain.model.PortfolioAsset;
import personal.calebcordell.coinnection.domain.repository.PortfolioAssetRepository;


public class RemoveAssetFromPortfolioInteractor extends CompletableInteractor<String> {
    private static final String TAG = AddAssetToPortfolioInteractor.class.getSimpleName();

    private final PortfolioAssetRepository mPortfolioAssetRepository;

    public RemoveAssetFromPortfolioInteractor() {
        mPortfolioAssetRepository = new PortfolioAssetRepositoryImpl();
    }

    protected Completable buildCompletable(final String assetId) {
        return mPortfolioAssetRepository.removeAssetFromPortfolio(assetId)
                .observeOn(AndroidSchedulers.mainThread());
    }
}