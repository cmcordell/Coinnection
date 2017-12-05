package personal.calebcordell.coinnection.domain.interactor.impl;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import personal.calebcordell.coinnection.data.assetdata.AssetRepositoryImpl;
import personal.calebcordell.coinnection.data.portfolioassetdata.PortfolioAssetRepositoryImpl;
import personal.calebcordell.coinnection.data.watchlistassetdata.WatchlistAssetRepositoryImpl;
import personal.calebcordell.coinnection.domain.interactor.base.CompletableInteractor;
import personal.calebcordell.coinnection.domain.model.PortfolioAsset;
import personal.calebcordell.coinnection.domain.repository.AssetRepository;
import personal.calebcordell.coinnection.domain.repository.PortfolioAssetRepository;
import personal.calebcordell.coinnection.domain.repository.WatchlistAssetRepository;


public class AddAssetToPortfolioInteractor extends CompletableInteractor<PortfolioAsset> {
    private static final String TAG = AddAssetToPortfolioInteractor.class.getSimpleName();

    private final PortfolioAssetRepository mPortfolioAssetRepository;
    private final WatchlistAssetRepository mWatchlistAssetRepository;
    private final AssetRepository mAssetRepository;

    public AddAssetToPortfolioInteractor() {
        mPortfolioAssetRepository = new PortfolioAssetRepositoryImpl();
        mWatchlistAssetRepository = new WatchlistAssetRepositoryImpl();
        mAssetRepository = new AssetRepositoryImpl();
    }

    protected Completable buildCompletable(PortfolioAsset portfolioAsset) {
        return mWatchlistAssetRepository.removeAssetFromWatchlist(portfolioAsset.getId())
                .andThen(mPortfolioAssetRepository.addAssetToPortfolio(portfolioAsset))
                .andThen(mAssetRepository.fetchAsset(portfolioAsset.getId()))
                .observeOn(AndroidSchedulers.mainThread());
    }
}