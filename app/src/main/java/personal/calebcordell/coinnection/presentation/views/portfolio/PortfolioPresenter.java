package personal.calebcordell.coinnection.presentation.views.portfolio;

import android.support.annotation.NonNull;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.inject.Inject;


import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableCompletableObserver;
import io.reactivex.subscribers.DisposableSubscriber;
import personal.calebcordell.coinnection.domain.interactor.impl.portfolioassetinteractors.FetchAllPortfolioAssetsInteractor;
import personal.calebcordell.coinnection.domain.interactor.impl.portfolioassetinteractors.GetAllPortfolioAssetsInteractor;
import personal.calebcordell.coinnection.domain.interactor.impl.portfolioassetinteractors.ReorderPortfolioAssetsInteractor;
import personal.calebcordell.coinnection.domain.interactor.impl.watchlistassetinteractors.FetchAllWatchlistAssetsInteractor;
import personal.calebcordell.coinnection.domain.interactor.impl.watchlistassetinteractors.GetAllWatchlistAssetsInteractor;
import personal.calebcordell.coinnection.domain.interactor.impl.watchlistassetinteractors.ReorderWatchlistAssetsInteractor;
import personal.calebcordell.coinnection.domain.model.Asset;
import personal.calebcordell.coinnection.domain.model.PortfolioAsset;
import personal.calebcordell.coinnection.domain.model.WatchlistAsset;
import personal.calebcordell.coinnection.presentation.Constants;


public class PortfolioPresenter extends PortfolioContract.Presenter {
    private static final String TAG = PortfolioPresenter.class.getSimpleName();

    private FetchAllPortfolioAssetsInteractor mFetchAllPortfolioAssetsInteractor;
    private GetAllPortfolioAssetsInteractor mGetAllPortfolioAssetsInteractor;
    private ReorderPortfolioAssetsInteractor mReorderPortfolioAssetsInteractor;
    private FetchAllWatchlistAssetsInteractor mFetchAllWatchlistAssetsInteractor;
    private GetAllWatchlistAssetsInteractor mGetAllWatchlistAssetsInteractor;
    private ReorderWatchlistAssetsInteractor mReorderWatchlistAssetsInteractor;

    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    private Timer mReloadTimer;
    private long mFetchDelay = Constants.RELOAD_TIME_SINGLE_ASSET;
    private long mLastUpdated = 0;
    private boolean mFastReloadingEnabled = false;
    private boolean mPaused = false;

    private boolean mFirstPortfolioAssetsFetch = true;
    private boolean mFirstWatchlistAssetsFetch = true;

    @Inject
    public PortfolioPresenter(FetchAllPortfolioAssetsInteractor fetchAllPortfolioAssetsInteractor,
                       GetAllPortfolioAssetsInteractor getAllPortfolioAssetsInteractor,
                       ReorderPortfolioAssetsInteractor reorderPortfolioAssetsInteractor,
                       FetchAllWatchlistAssetsInteractor fetchAllWatchlistAssetsInteractor,
                       GetAllWatchlistAssetsInteractor getAllWatchlistAssetsInteractor,
                       ReorderWatchlistAssetsInteractor reorderWatchlistAssetsInteractor) {
        mFetchAllPortfolioAssetsInteractor = fetchAllPortfolioAssetsInteractor;
        mGetAllPortfolioAssetsInteractor = getAllPortfolioAssetsInteractor;
        mReorderPortfolioAssetsInteractor = reorderPortfolioAssetsInteractor;
        mFetchAllWatchlistAssetsInteractor = fetchAllWatchlistAssetsInteractor;
        mGetAllWatchlistAssetsInteractor = getAllWatchlistAssetsInteractor;
        mReorderWatchlistAssetsInteractor = reorderWatchlistAssetsInteractor;
    }

    @Override
    public void initialize() {
        getPortfolioAssets();
        getWatchlistAssets();
    }

    private void getPortfolioAssets() {
        mCompositeDisposable.add(mGetAllPortfolioAssetsInteractor.execute(new DisposableSubscriber<List<PortfolioAsset>>() {
            @Override
            public void onNext(List<PortfolioAsset> portfolioAssets) {
                mView.showPortfolioAssets(portfolioAssets);

                if (mFirstPortfolioAssetsFetch) {
                    mFirstPortfolioAssetsFetch = false;
                    updateFetchTimer(portfolioAssets);
                }
            }
            @Override public void onError(Throwable t) {}
            @Override public void onComplete() {}
        }));
    }

    private void getWatchlistAssets() {
        mCompositeDisposable.add(mGetAllWatchlistAssetsInteractor.execute(new DisposableSubscriber<List<WatchlistAsset>>() {
            @Override
            public void onNext(List<WatchlistAsset> watchlistAssets) {
                mView.showWatchlistAssets(watchlistAssets);
                if (mFirstWatchlistAssetsFetch) {
                    mFirstWatchlistAssetsFetch = false;
                    updateFetchTimer(watchlistAssets);
                }
            }

            @Override
            public void onError(Throwable t) {}
            @Override
            public void onComplete() {}
        }));
    }

    private void fetchPortfolioAssets() {
        mFetchAllPortfolioAssetsInteractor.execute(new DisposableCompletableObserver() {
            @Override
            public void onComplete() {
                mFetchDelay = 0;
                mLastUpdated = System.currentTimeMillis();
                if (mFastReloadingEnabled) {
                    stopTimer();
                    mFastReloadingEnabled = false;
                    startReloadTimer(Constants.RELOAD_TIME_SINGLE_ASSET);
                }
            }

            @Override
            public void onError(Throwable e) {
                stopTimer();
                mFastReloadingEnabled = true;
                startReloadTimer(Constants.RELOAD_TIME_NETWORK_CHECK);
            }
        });
    }

    private void fetchWatchlistAssets() {
        mFetchAllWatchlistAssetsInteractor.execute(new DisposableCompletableObserver() {
            @Override
            public void onComplete() {
                mFetchDelay = 0;
                mLastUpdated = System.currentTimeMillis();
                if (mFastReloadingEnabled) {
                    stopTimer();
                    mFastReloadingEnabled = false;
                    startReloadTimer(Constants.RELOAD_TIME_SINGLE_ASSET);
                }
            }

            @Override
            public void onError(Throwable e) {}
        });
    }

    @Override
    public void reorderPortfolioAssets(@NonNull List<PortfolioAsset> portfolioAssets) {
        mReorderPortfolioAssetsInteractor.execute(portfolioAssets, new DisposableCompletableObserver() {
            @Override public void onComplete() {}
            @Override public void onError(Throwable e) {}
        });
    }

    @Override
    public void reorderWatchlistAssets(@NonNull List<WatchlistAsset> watchlistAssets) {
        mReorderWatchlistAssetsInteractor.execute(watchlistAssets, new DisposableCompletableObserver() {
            @Override public void onComplete() {}
            @Override public void onError(Throwable e) {}
        });
    }

    @Override
    public void resume() {
        if (mPaused) {
            long fetchDelay = Constants.RELOAD_TIME_SINGLE_ASSET - (System.currentTimeMillis() - mLastUpdated);
            if (fetchDelay < 0) {
                fetchDelay = 0;
            }
            startReloadTimer(fetchDelay);
            mPaused = false;
        }
    }

    @Override
    public void pause() {
        mPaused = true;
        stopTimer();
    }

    @Override
    public void destroy() {
        stopTimer();
        mCompositeDisposable.clear();
        mFirstPortfolioAssetsFetch = true;
        mFirstWatchlistAssetsFetch = true;
        super.destroy();
    }

    private void updateFetchTimer(@NonNull List<? extends Asset> assets) {
        long currentTime = System.currentTimeMillis();
        long fetchDelay = Constants.RELOAD_TIME_SINGLE_ASSET;
        long lastUpdated = currentTime;
        for (Asset asset : assets) {
            if (!asset.isUpToDate(Constants.RELOAD_TIME_SINGLE_ASSET)) {
                fetchDelay = 0;
                lastUpdated = asset.getLastUpdated();
                break;
            } else {
                long nextUpdate = Constants.RELOAD_TIME_SINGLE_ASSET - (currentTime - asset.getLastUpdated());
                if (nextUpdate < fetchDelay) {
                    fetchDelay = nextUpdate;
                    lastUpdated = asset.getLastUpdated();
                }
            }
        }

        mLastUpdated = lastUpdated;
        mView.showLastUpdated(lastUpdated);

        if (fetchDelay < mFetchDelay) {
            mFetchDelay = fetchDelay;

            startReloadTimer(fetchDelay);
        }
    }

    private void startReloadTimer(long fetchDelay) {
        stopTimer();
        mReloadTimer = new Timer();
        mReloadTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                fetchPortfolioAssets();
                fetchWatchlistAssets();
            }
        }, fetchDelay, Constants.RELOAD_TIME_SINGLE_ASSET);
    }

    private void stopTimer() {
        if (mReloadTimer != null) {
            mReloadTimer.cancel();
            mReloadTimer = null;
        }
    }
}