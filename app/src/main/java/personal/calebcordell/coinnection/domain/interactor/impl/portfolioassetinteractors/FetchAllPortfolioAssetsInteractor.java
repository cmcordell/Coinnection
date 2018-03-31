package personal.calebcordell.coinnection.domain.interactor.impl.portfolioassetinteractors;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import personal.calebcordell.coinnection.domain.interactor.base.CompletableInteractor1;
import personal.calebcordell.coinnection.domain.repository.PortfolioAssetRepository;


public class FetchAllPortfolioAssetsInteractor extends CompletableInteractor1 {
    private static final String TAG = FetchAllPortfolioAssetsInteractor.class.getSimpleName();

    private final PortfolioAssetRepository mPortfolioAssetRepository;

    @Inject
    public FetchAllPortfolioAssetsInteractor(PortfolioAssetRepository portfolioAssetRepository) {
        mPortfolioAssetRepository = portfolioAssetRepository;
    }

    protected Completable buildCompletable() {
        return mPortfolioAssetRepository.fetchAllPortfolioAssets()
                .observeOn(AndroidSchedulers.mainThread());
    }
}