package personal.calebcordell.coinnection.domain.interactor.impl.portfolioassetinteractors;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import personal.calebcordell.coinnection.domain.interactor.base.SingleInteractor1;
import personal.calebcordell.coinnection.domain.repository.PortfolioAssetRepository;


public class GetPortfolioAssetIdsInteractor extends SingleInteractor1<List<String>> {
    private static final String TAG = GetPortfolioAssetIdsInteractor.class.getSimpleName();

    private final PortfolioAssetRepository mPortfolioAssetRepository;

    @Inject
    public GetPortfolioAssetIdsInteractor(PortfolioAssetRepository portfolioAssetRepository) {
        mPortfolioAssetRepository = portfolioAssetRepository;
    }

    public Single<List<String>> buildSingle() {
        return mPortfolioAssetRepository.getPortfolioAssetIds()
                .observeOn(AndroidSchedulers.mainThread());
    }
}