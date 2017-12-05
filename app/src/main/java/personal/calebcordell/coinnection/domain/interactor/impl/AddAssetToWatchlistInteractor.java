package personal.calebcordell.coinnection.domain.interactor.impl;

import io.reactivex.Completable;

import io.reactivex.CompletableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import personal.calebcordell.coinnection.data.assetdata.AssetRepositoryImpl;
import personal.calebcordell.coinnection.data.portfolioassetdata.PortfolioAssetRepositoryImpl;
import personal.calebcordell.coinnection.data.watchlistassetdata.WatchlistAssetRepositoryImpl;
import personal.calebcordell.coinnection.domain.interactor.base.CompletableInteractor;
import personal.calebcordell.coinnection.domain.model.WatchlistAsset;
import personal.calebcordell.coinnection.domain.repository.AssetRepository;
import personal.calebcordell.coinnection.domain.repository.PortfolioAssetRepository;
import personal.calebcordell.coinnection.domain.repository.WatchlistAssetRepository;


public class AddAssetToWatchlistInteractor extends CompletableInteractor<WatchlistAsset> {
    private static final String TAG = AddAssetToWatchlistInteractor.class.getSimpleName();

    private final WatchlistAssetRepository mWatchlistAssetRepository;
    private final PortfolioAssetRepository mPortfolioAssetRepository;
    private final AssetRepository mAssetRepository;

    public AddAssetToWatchlistInteractor() {
        mWatchlistAssetRepository = new WatchlistAssetRepositoryImpl();
        mPortfolioAssetRepository = new PortfolioAssetRepositoryImpl();
        mAssetRepository = new AssetRepositoryImpl();
    }

    protected Completable buildCompletable(WatchlistAsset watchlistAsset) {
        return mPortfolioAssetRepository.isAssetInPortfolio(watchlistAsset.getId())
                .flatMapCompletable(new Function<Boolean, CompletableSource>() {
                    @Override
                    public CompletableSource apply(Boolean isInPortfolio) throws Exception {
                        if(isInPortfolio) {
                            return Completable.complete();
                        } else {
                            return mWatchlistAssetRepository.addAssetToWatchlist(watchlistAsset)
                                    .andThen(mAssetRepository.fetchAsset(watchlistAsset.getId()));
                        }
                    }
                })
                .observeOn(AndroidSchedulers.mainThread());
    }
}