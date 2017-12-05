package personal.calebcordell.coinnection.domain.interactor.impl;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import personal.calebcordell.coinnection.data.portfolioassetdata.PortfolioAssetRepositoryImpl;
import personal.calebcordell.coinnection.domain.interactor.base.FlowableInteractor;
import personal.calebcordell.coinnection.domain.model.PortfolioAsset;
import personal.calebcordell.coinnection.domain.repository.PortfolioAssetRepository;


public class GetPortfolioAssetByIdInteractor extends FlowableInteractor<String, PortfolioAsset> {
    private static final String TAG = GetPortfolioAssetByIdInteractor.class.getSimpleName();

    private PortfolioAssetRepository mPortfolioAssetRepository;

    public GetPortfolioAssetByIdInteractor() {
        mPortfolioAssetRepository = new PortfolioAssetRepositoryImpl();
    }

    protected Flowable<PortfolioAsset> buildFlowable(String id) {
        return mPortfolioAssetRepository.getPortfolioAsset(id)
                .observeOn(AndroidSchedulers.mainThread());
    }
}