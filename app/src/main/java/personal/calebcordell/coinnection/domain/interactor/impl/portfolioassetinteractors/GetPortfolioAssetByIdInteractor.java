package personal.calebcordell.coinnection.domain.interactor.impl.portfolioassetinteractors;

import android.support.annotation.NonNull;

import javax.inject.Inject;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import personal.calebcordell.coinnection.domain.interactor.base.FlowableInteractor;
import personal.calebcordell.coinnection.domain.model.PortfolioAsset;
import personal.calebcordell.coinnection.domain.repository.PortfolioAssetRepository;


public class GetPortfolioAssetByIdInteractor extends FlowableInteractor<String, PortfolioAsset> {
    private static final String TAG = GetPortfolioAssetByIdInteractor.class.getSimpleName();

    private final PortfolioAssetRepository mPortfolioAssetRepository;

    @Inject
    public GetPortfolioAssetByIdInteractor(PortfolioAssetRepository portfolioAssetRepository) {
        mPortfolioAssetRepository = portfolioAssetRepository;
    }

    protected Flowable<PortfolioAsset> buildFlowable(@NonNull final String id) {
        return mPortfolioAssetRepository.getPortfolioAsset(id)
                .observeOn(AndroidSchedulers.mainThread());
    }
}