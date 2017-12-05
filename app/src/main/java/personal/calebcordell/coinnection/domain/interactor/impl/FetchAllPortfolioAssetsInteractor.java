package personal.calebcordell.coinnection.domain.interactor.impl;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import personal.calebcordell.coinnection.data.portfolioassetdata.PortfolioAssetRepositoryImpl;
import personal.calebcordell.coinnection.domain.interactor.base.CompletableInteractor1;
import personal.calebcordell.coinnection.domain.repository.PortfolioAssetRepository;


public class FetchAllPortfolioAssetsInteractor extends CompletableInteractor1 {
    private static final String TAG = FetchAllPortfolioAssetsInteractor.class.getSimpleName();

    private final PortfolioAssetRepository mPortfolioAssetRepository;

    public FetchAllPortfolioAssetsInteractor() {
        mPortfolioAssetRepository = new PortfolioAssetRepositoryImpl();
    }

    protected Completable buildCompletable() {
        return mPortfolioAssetRepository.fetchAllPortfolioAssets()
                .observeOn(AndroidSchedulers.mainThread());
    }
}