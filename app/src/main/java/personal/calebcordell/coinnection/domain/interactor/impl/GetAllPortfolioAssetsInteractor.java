package personal.calebcordell.coinnection.domain.interactor.impl;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import personal.calebcordell.coinnection.data.portfolioassetdata.PortfolioAssetRepositoryImpl;
import personal.calebcordell.coinnection.domain.interactor.base.FlowableInteractor1;
import personal.calebcordell.coinnection.domain.model.PortfolioAsset;
import personal.calebcordell.coinnection.domain.repository.PortfolioAssetRepository;


public class GetAllPortfolioAssetsInteractor extends FlowableInteractor1<List<PortfolioAsset>> {
    private static final String TAG = GetAllPortfolioAssetsInteractor.class.getSimpleName();

    private PortfolioAssetRepository mPortfolioAssetRepository;

    public GetAllPortfolioAssetsInteractor() {
        mPortfolioAssetRepository = new PortfolioAssetRepositoryImpl();
    }

    protected Flowable<List<PortfolioAsset>> buildFlowable() {
        return mPortfolioAssetRepository.getAllPortfolioAssets()
                .observeOn(AndroidSchedulers.mainThread());
    }
}