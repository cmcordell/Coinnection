package personal.calebcordell.coinnection.domain.interactor.impl.portfolioassetinteractors;

import android.support.annotation.NonNull;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import personal.calebcordell.coinnection.domain.interactor.base.CompletableInteractor;
import personal.calebcordell.coinnection.domain.model.PortfolioAsset;
import personal.calebcordell.coinnection.domain.repository.AssetRepository;
import personal.calebcordell.coinnection.domain.repository.PortfolioAssetRepository;


public class AddAssetToPortfolioInteractor extends CompletableInteractor<PortfolioAsset> {
    private static final String TAG = AddAssetToPortfolioInteractor.class.getSimpleName();

    private final PortfolioAssetRepository mPortfolioAssetRepository;
    private final AssetRepository mAssetRepository;

    @Inject
    public AddAssetToPortfolioInteractor(PortfolioAssetRepository portfolioAssetRepository,
                                         AssetRepository assetRepository) {
        mPortfolioAssetRepository = portfolioAssetRepository;
        mAssetRepository = assetRepository;
    }

    protected Completable buildCompletable(@NonNull final PortfolioAsset portfolioAsset) {
        return mPortfolioAssetRepository.addAssetToPortfolio(portfolioAsset)
                .andThen(mAssetRepository.fetchAsset(portfolioAsset.getId()))
                .observeOn(AndroidSchedulers.mainThread());
    }
}