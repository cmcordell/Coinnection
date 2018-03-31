package personal.calebcordell.coinnection.presentation.views.allassets;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableCompletableObserver;
import io.reactivex.subscribers.DisposableSubscriber;
import personal.calebcordell.coinnection.dagger.PerFragment;
import personal.calebcordell.coinnection.domain.interactor.impl.assetinteractors.FetchAllAssetsInteractor;
import personal.calebcordell.coinnection.domain.interactor.impl.assetinteractors.GetAllAssetsInteractor;
import personal.calebcordell.coinnection.domain.interactor.impl.globalmarketdatainteractors.FetchGlobalMarketDataInteractor;
import personal.calebcordell.coinnection.domain.interactor.impl.globalmarketdatainteractors.GetGlobalMarketDataInteractor;
import personal.calebcordell.coinnection.domain.model.Asset;
import personal.calebcordell.coinnection.domain.model.GlobalMarketData;
import personal.calebcordell.coinnection.presentation.Constants;


/**
 * Presenter for AllAssetsFragment.
 */
@PerFragment
public class AllAssetsPresenter extends AllAssetsContract.Presenter {
    private static final String TAG = AllAssetsPresenter.class.getSimpleName();

    private FetchAllAssetsInteractor mFetchAllAssetsInteractor;
    private GetAllAssetsInteractor mGetAllAssetsInteractor;
    private FetchGlobalMarketDataInteractor mFetchGlobalMarketDataInteractor;
    private GetGlobalMarketDataInteractor mGetGlobalMarketDataInteractor;

    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();
    private Disposable mFetchAllAssetsDisposable;
    private Disposable mFetchGlobalMarketDataDisposable;

    private Timer mReloadTimer;
    private long mFetchDelay = Constants.RELOAD_TIME_ALL_ASSETS;
    private long mLastUpdated = 0;
    private boolean mFastReloadingEnabled = false;
    private boolean mPaused = false;

    private boolean mFirstAssetsFetch = true;

    @Inject
    AllAssetsPresenter(FetchAllAssetsInteractor fetchAllAssetsInteractor,
                       GetAllAssetsInteractor getAllAssetsInteractor,
                       FetchGlobalMarketDataInteractor fetchGlobalMarketDataInteractor,
                       GetGlobalMarketDataInteractor getGlobalMarketDataInteractor) {
        mFetchAllAssetsInteractor = fetchAllAssetsInteractor;
        mGetAllAssetsInteractor = getAllAssetsInteractor;
        mFetchGlobalMarketDataInteractor = fetchGlobalMarketDataInteractor;
        mGetGlobalMarketDataInteractor = getGlobalMarketDataInteractor;
    }

    /**
     * Gets all necessary data and supplies it to the view.
     * Should be called as soon as possible from the view, assuming the UI has been loaded.
     */
    @Override
    public void initialize() {
        getAssets();
        getGlobalMarketData();
    }

    private void getAssets() {
        mCompositeDisposable.add(mGetAllAssetsInteractor.execute(new DisposableSubscriber<List<Asset>>() {
            @Override
            public void onNext(List<Asset> assets) {
                mView.showAssets(assets);
                if (mFirstAssetsFetch) {
                    mFirstAssetsFetch = false;
                    updateFetchTimer(assets);
                }
            }

            @Override
            public void onError(Throwable t) {}
            @Override
            public void onComplete() {}
        }));
    }

    private void getGlobalMarketData() {
        mCompositeDisposable.add(mGetGlobalMarketDataInteractor.execute(new DisposableSubscriber<GlobalMarketData>() {
            @Override
            public void onNext(GlobalMarketData globalMarketData) {
                mView.showGlobalMarketData(globalMarketData);
            }

            @Override
            public void onError(Throwable t) {}

            @Override
            public void onComplete() {}
        }));
    }

    /**
     * Fetches all assets, new data will automatically be sent from
     * the backend to mGetAllAssetsInteractor once loaded.
     */
    private void fetchAssets() {
        if (mFetchAllAssetsDisposable != null && !mFetchAllAssetsDisposable.isDisposed()) {
            mFetchAllAssetsDisposable.dispose();
        }

        mFetchAllAssetsDisposable = mFetchAllAssetsInteractor.execute(true, new DisposableCompletableObserver() {
            @Override
            public void onComplete() {
                mFetchDelay = 0;
                mLastUpdated = System.currentTimeMillis();
                if (mFastReloadingEnabled) {
                    stopTimer();
                    mFastReloadingEnabled = false;
                    startReloadTimer(Constants.RELOAD_TIME_ALL_ASSETS);
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

    private void fetchGlobalMarketData() {
        if (mFetchGlobalMarketDataDisposable != null && !mFetchGlobalMarketDataDisposable.isDisposed()) {
            mFetchGlobalMarketDataDisposable.dispose();
        }

        mFetchGlobalMarketDataDisposable = mFetchGlobalMarketDataInteractor.execute(new DisposableCompletableObserver() {
            @Override
            public void onComplete() {}

            @Override
            public void onError(Throwable e) {}
        });
    }

    /**
     * Resumes the reload timer and should be tied to views onResume() method.
     * We don't want to waste data while view is paused.
     */
    @Override
    public void resume() {
        if (mPaused) {
            long fetchDelay = Constants.RELOAD_TIME_ALL_ASSETS - (System.currentTimeMillis() - mLastUpdated);
            if (fetchDelay < 0) {
                fetchDelay = 0;
            }
            startReloadTimer(fetchDelay);
            mPaused = false;
        }
    }

    /**
     * Pauses the reload timer and should be tied to views onPause() method.
     * We don't want to waste data while view is paused.
     */
    @Override
    public void pause() {
        mPaused = true;
        stopTimer();
    }

    /**
     * Stop the reload timer and dispose of all interactors.
     * Should be tied to views onDestroy() method.
     */
    @Override
    public void destroy() {
        stopTimer();
        mCompositeDisposable.clear();
        super.destroy();
    }

    /**
     * Checks if assets are up to date, i.e. lastupdated < 15 min.  If not, start reload right away.
     * Else, calculate reload time based on oldest asset.  This method is only called on the
     * initial load of the assets.
     *
     * @param assets Initial list of all assets.
     */
    private void updateFetchTimer(List<? extends Asset> assets) {
        long currentTime = System.currentTimeMillis();
        long fetchDelay = Constants.RELOAD_TIME_ALL_ASSETS;
        long lastUpdated = currentTime;
        for (Asset asset : assets) {
            if (!asset.isUpToDate(Constants.RELOAD_TIME_ALL_ASSETS)) {
                fetchDelay = 0;
                lastUpdated = asset.getLastUpdated();
                break;
            } else {
                long nextUpdate = Constants.RELOAD_TIME_ALL_ASSETS - (currentTime - asset.getLastUpdated());
                if (nextUpdate < fetchDelay) {
                    fetchDelay = nextUpdate;
                    lastUpdated = asset.getLastUpdated();
                }
            }
        }

        mLastUpdated = lastUpdated;

        if (fetchDelay < mFetchDelay) {
            mFetchDelay = fetchDelay;

            startReloadTimer(fetchDelay);
        }
    }

    /**
     * Starts the all asset reload timer at a fixed rate;
     *
     * @param fetchDelay Initial amount of time in ms to wait before fetching all assets.
     */
    private void startReloadTimer(long fetchDelay) {
        stopTimer();
        mReloadTimer = new Timer();
        mReloadTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                fetchAssets();
                fetchGlobalMarketData();
            }
        }, fetchDelay, Constants.RELOAD_TIME_ALL_ASSETS);
    }

    private void stopTimer() {
        if (mReloadTimer != null) {
            mReloadTimer.cancel();
            mReloadTimer = null;
        }
    }
}
