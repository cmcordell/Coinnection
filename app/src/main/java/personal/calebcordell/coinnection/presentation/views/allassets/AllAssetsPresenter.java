package personal.calebcordell.coinnection.presentation.views.allassets;

import android.support.annotation.NonNull;
import android.util.Log;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableCompletableObserver;
import io.reactivex.subscribers.DisposableSubscriber;
import personal.calebcordell.coinnection.domain.interactor.impl.FetchAllAssetsInteractor;
import personal.calebcordell.coinnection.domain.interactor.impl.GetAllAssetsInteractor;
import personal.calebcordell.coinnection.domain.model.Asset;
import personal.calebcordell.coinnection.presentation.Constants;


public class AllAssetsPresenter implements AllAssetsContract.Presenter {
    private static final String TAG = AllAssetsPresenter.class.getSimpleName();

    private AllAssetsContract.View mView;

    private FetchAllAssetsInteractor mFetchAllAssetsInteractor;
    private GetAllAssetsInteractor mGetAllAssetsInteractor;

    private CompositeDisposable mCompositeDisposable;

    private Timer mReloadTimer = new Timer();
    private long mFetchDelay = Constants.RELOAD_TIME_ALL_ASSETS;
    private long mLastUpdated = 0;
    private boolean mFastReloadingEnabled = false;
    private boolean mPaused = false;

    private boolean mFirstAssetsFetch = true;

    AllAssetsPresenter(@NonNull AllAssetsContract.View view) {
        mView = view;

        mFetchAllAssetsInteractor = new FetchAllAssetsInteractor();
        mGetAllAssetsInteractor = new GetAllAssetsInteractor();

        mCompositeDisposable = new CompositeDisposable();
    }


    public void start() {
        mCompositeDisposable.add(mGetAllAssetsInteractor.execute(new DisposableSubscriber<List<Asset>>() {
            @Override
            public void onNext(List<Asset> assets) {
                if(mFirstAssetsFetch) {
                    mView.showAssets(assets);
                    mFirstAssetsFetch = false;

                    updateFetchTimer(assets);
                } else {
                    mView.updateAssets(assets);
                }
            }

            @Override
            public void onError(Throwable t) {t.printStackTrace();}
            @Override
            public void onComplete() {}
        }));
    }

    private void fetchAssets() {
        Log.d(TAG, "Fetch Assets started");
        mCompositeDisposable.add(mFetchAllAssetsInteractor.execute(true, new DisposableCompletableObserver() {
            @Override
            public void onComplete() {
                mFetchDelay = 0;
                mLastUpdated = System.currentTimeMillis();
                Log.d(TAG, "Fetch Assets complete.");
                if(mFastReloadingEnabled) {
                    mReloadTimer.cancel();
                    mFastReloadingEnabled = false;
                    startReloadTimer(Constants.RELOAD_TIME_ALL_ASSETS);
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

    public void resume() {
        if(mPaused) {
            long fetchDelay = Constants.RELOAD_TIME_ALL_ASSETS - (System.currentTimeMillis() - mLastUpdated);
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
        long fetchDelay = Constants.RELOAD_TIME_ALL_ASSETS;
        long lastUpdated = currentTime;
        for (Asset asset : assets) {
            if (!asset.isUpToDate()) {
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
                fetchAssets();
            }
        }, fetchDelay, Constants.RELOAD_TIME_ALL_ASSETS);
    }
}
