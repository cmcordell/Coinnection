package personal.calebcordell.coinnection.domain.interactor.impl.portfolioassetinteractors;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import personal.calebcordell.coinnection.domain.interactor.base.FlowableInteractor1;
import personal.calebcordell.coinnection.domain.model.PortfolioAsset;
import personal.calebcordell.coinnection.domain.repository.PortfolioAssetRepository;


public class GetAllPortfolioAssetsInteractor extends FlowableInteractor1<List<PortfolioAsset>> {
    private static final String TAG = GetAllPortfolioAssetsInteractor.class.getSimpleName();

    private final PortfolioAssetRepository mPortfolioAssetRepository;

    @Inject
    public GetAllPortfolioAssetsInteractor(PortfolioAssetRepository portfolioAssetRepository) {
        mPortfolioAssetRepository = portfolioAssetRepository;
    }

    protected Flowable<List<PortfolioAsset>> buildFlowable() {
        return mPortfolioAssetRepository.getAllPortfolioAssets()
                .observeOn(AndroidSchedulers.mainThread());
    }
}