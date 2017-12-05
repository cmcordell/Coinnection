package personal.calebcordell.coinnection.presentation.views.portfolio;

import android.support.annotation.NonNull;
import android.util.Log;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableCompletableObserver;
import io.reactivex.subscribers.DisposableSubscriber;
import personal.calebcordell.coinnection.domain.interactor.impl.FetchAllPortfolioAssetsInteractor;
import personal.calebcordell.coinnection.domain.interactor.impl.FetchAllWatchlistAssetsInteractor;
import personal.calebcordell.coinnection.domain.interactor.impl.GetAllPortfolioAssetsInteractor;
import personal.calebcordell.coinnection.domain.interactor.impl.GetAllWatchlistAssetsInteractor;
import personal.calebcordell.coinnection.domain.interactor.impl.ReorderPortfolioAssetsInteractor;
import personal.calebcordell.coinnection.domain.interactor.impl.ReorderWatchlistAssetsInteractor;
import personal.calebcordell.coinnection.domain.model.Asset;
import personal.calebcordell.coinnection.domain.model.PortfolioAsset;
import personal.calebcordell.coinnection.domain.model.WatchlistAsset;
import personal.calebcordell.coinnection.presentation.Constants;


public class PortfolioPresenter implements PortfolioContract.Presenter {
    private static final String TAG = PortfolioPresenter.class.getSimpleName();

    private PortfolioContract.View mView;
    
    private FetchAllPortfolioAssetsInteractor mFetchAllPortfolioAssetsInteractor;
    private GetAllPortfolioAssetsInteractor mGetAllPortfolioAssetsInteractor;
    private ReorderPortfolioAssetsInteractor mReorderPortfolioAssetsInteractor;
    
    private FetchAllWatchlistAssetsInteractor mFetchAllWatchlistAssetsInteractor;
    private GetAllWatchlistAssetsInteractor mGetAllWatchlistAssetsInteractor;
    private ReorderWatchlistAssetsInteractor mReorderWatchlistAssetsInteractor;
    
    private CompositeDisposable mCompositeDisposable;

    private Timer mReloadTimer = new Timer();
    private long mFetchDelay = Constants.RELOAD_TIME_SINGLE_ASSET;
    private long mLastUpdated = 0;
    private boolean mFastReloadingEnabled = false;
    private boolean mPaused = false;
    
    private boolean mFirstPortfolioAssetsFetch = true;
    private boolean mFirstWatchlistAssetsFetch = true;

    PortfolioPresenter(@NonNull PortfolioContract.View view) {
        mView = view;
        
        mFetchAllPortfolioAssetsInteractor = new FetchAllPortfolioAssetsInteractor();
        mGetAllPortfolioAssetsInteractor = new GetAllPortfolioAssetsInteractor();
        mReorderPortfolioAssetsInteractor = new ReorderPortfolioAssetsInteractor();

        mFetchAllWatchlistAssetsInteractor = new FetchAllWatchlistAssetsInteractor();
        mGetAllWatchlistAssetsInteractor = new GetAllWatchlistAssetsInteractor();
        mReorderWatchlistAssetsInteractor = new ReorderWatchlistAssetsInteractor();
        
        mCompositeDisposable = new CompositeDisposable();
    }


    public void start() {
        mCompositeDisposable.add(mGetAllPortfolioAssetsInteractor.execute(new DisposableSubscriber<List<PortfolioAsset>>() {
            @Override
            public void onNext(List<PortfolioAsset> portfolioAssets) {
                if(mFirstPortfolioAssetsFetch) {
                    mView.showPortfolioAssets(portfolioAssets);
                    mFirstPortfolioAssetsFetch = false;

                    updateFetchTimer(portfolioAssets);
                } else {
                    mView.updatePortfolioAssets(portfolioAssets);
                }
            }

            @Override
            public void onError(Throwable t) {t.printStackTrace();}
            @Override
            public void onComplete() {}
        }));

        mCompositeDisposable.add(mGetAllWatchlistAssetsInteractor.execute(new DisposableSubscriber<List<WatchlistAsset>>() {
            @Override
            public void onNext(List<WatchlistAsset> watchlistAssets) {
                if(mFirstWatchlistAssetsFetch) {
                    mView.showWatchlistAssets(watchlistAssets);
                    mFirstWatchlistAssetsFetch = false;

                    updateFetchTimer(watchlistAssets);
                } else {
                    mView.updateWatchlistAssets(watchlistAssets);
                }
            }

            @Override
            public void onError(Throwable t) {t.printStackTrace();}
            @Override
            public void onComplete() {}
        }));
    }

    
    private void fetchPortfolioAssets() {
        Log.d(TAG, "Fetch PortfolioAssets started");
        mCompositeDisposable.add(mFetchAllPortfolioAssetsInteractor.execute(new DisposableCompletableObserver() {
            @Override
            public void onComplete() {
                mFetchDelay = 0;
                mLastUpdated = System.currentTimeMillis();
                Log.d(TAG, "Fetch PortfolioAssets complete.");
                if(mFastReloadingEnabled) {
                    mReloadTimer.cancel();
                    mFastReloadingEnabled = false;
                    startReloadTimer(Constants.RELOAD_TIME_SINGLE_ASSET);
                }
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                mReloadTimer.cancel();
                mFastReloadingEnabled = true;
                startReloadTimer(Constants.RELOAD_TIME_NETWORK_CHECK);
            }
        }));
    }
    private void fetchWatchlistAssets() {
        Log.d(TAG, "Fetch WatchlistAssets started.");
        mCompositeDisposable.add(mFetchAllWatchlistAssetsInteractor.execute(new DisposableCompletableObserver() {
            @Override
            public void onComplete() {
                mFetchDelay = 0;
                mLastUpdated = System.currentTimeMillis();
                Log.d(TAG, "Fetch WatchlistAssets complete.");
                if(mFastReloadingEnabled) {
                    mReloadTimer.cancel();
                    mFastReloadingEnabled = false;
                    startReloadTimer(Constants.RELOAD_TIME_SINGLE_ASSET);
                }
            }
            @Override public void onError(Throwable e) {
                e.printStackTrace();
            }
        }));
    }

    public void reorderPortfolioAssets(List<PortfolioAsset> portfolioAssets) {
        Log.d(TAG, "Reorder PortfolioAssets started.");
        mCompositeDisposable.add(mReorderPortfolioAssetsInteractor.execute(portfolioAssets, new DisposableCompletableObserver() {
            @Override public void onComplete() {}
            @Override public void onError(Throwable e) {e.printStackTrace();}
        }));
    }
    public void reorderWatchlistAssets(List<WatchlistAsset> watchlistAssets) {
        mCompositeDisposable.add(mReorderWatchlistAssetsInteractor.execute(watchlistAssets, new DisposableCompletableObserver() {
            @Override public void onComplete() {}
            @Override public void onError(Throwable e) {e.printStackTrace();}
        }));
    }

    public void resume() {
        if(mPaused) {
            long fetchDelay = Constants.RELOAD_TIME_SINGLE_ASSET - (System.currentTimeMillis() - mLastUpdated);
            if (fetchDelay < 0) {
                fetchDelay = 0;
            }
            startReloadTimer(fetchDelay);
            mPaused = false;
        }
    }

    public void pause() {
        mPaused = true;
        mReloadTimer.cancel();
    }

    public void destroy() {
        mReloadTimer.cancel();
        mCompositeDisposable.dispose();
    }

    private void updateFetchTimer(List<? extends Asset> assets) {
        long currentTime = System.currentTimeMillis();
        long fetchDelay = Constants.RELOAD_TIME_SINGLE_ASSET;
        long lastUpdated = currentTime;
        for (Asset asset : assets) {
            if (!asset.isUpToDate()) {
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

        if(fetchDelay < mFetchDelay) {
            mFetchDelay = fetchDelay;

            startReloadTimer(fetchDelay);
        }
    }

    private void startReloadTimer(long fetchDelay) {
        Log.d(TAG, "Asset Fetch Delay: " + String.format("%.2f", fetchDelay / 60000.0));
        mReloadTimer.cancel();
        mReloadTimer = new Timer();
        mReloadTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                fetchPortfolioAssets();
                fetchWatchlistAssets();
            }
        }, fetchDelay, Constants.RELOAD_TIME_SINGLE_ASSET);
    }
}