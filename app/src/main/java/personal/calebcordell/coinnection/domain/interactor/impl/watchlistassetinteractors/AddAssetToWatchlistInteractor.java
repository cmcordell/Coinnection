package personal.calebcordell.coinnection.domain.interactor.impl.watchlistassetinteractors;

import android.support.annotation.NonNull;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
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

    @Inject
    public AddAssetToWatchlistInteractor(WatchlistAssetRepository watchlistAssetRepository,
                                         PortfolioAssetRepository portfolioAssetRepository,
                                         AssetRepository assetRepository) {
        mWatchlistAssetRepository = watchlistAssetRepository;
        mPortfolioAssetRepository = portfolioAssetRepository;
        mAssetRepository = assetRepository;
    }

    protected Completable buildCompletable(@NonNull final WatchlistAsset watchlistAsset) {
        return mPortfolioAssetRepository.isAssetInPortfolio(watchlistAsset.getId())
                .flatMapCompletable((isInPortfolio) -> {
                    if (isInPortfolio) {
                        return Completable.complete();
                    } else {
                        return mWatchlistAssetRepository.addAssetToWatchlist(watchlistAsset)
                                .andThen(mAssetRepository.fetchAsset(watchlistAsset.getId()));
                    }
                })
                .observeOn(AndroidSchedulers.mainThread());
    }
}