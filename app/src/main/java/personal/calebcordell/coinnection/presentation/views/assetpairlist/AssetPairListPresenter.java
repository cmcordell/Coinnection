package personal.calebcordell.coinnection.presentation.views.assetpairlist;

import android.support.annotation.NonNull;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableCompletableObserver;
import io.reactivex.subscribers.DisposableSubscriber;
import personal.calebcordell.coinnection.domain.interactor.impl.assetpairinteractors.FetchAllAssetPairsInteractor;
import personal.calebcordell.coinnection.domain.interactor.impl.assetpairinteractors.GetAllAssetPairsInteractor;
import personal.calebcordell.coinnection.domain.interactor.impl.assetpairinteractors.UpdateAssetPairsInteractor;
import personal.calebcordell.coinnection.domain.model.Asset;
import personal.calebcordell.coinnection.domain.model.AssetPair;
import personal.calebcordell.coinnection.presentation.Constants;


public class AssetPairListPresenter extends AssetPairListContract.Presenter {
    private static final String TAG = AssetPairListPresenter.class.getSimpleName();

    private GetAllAssetPairsInteractor mGetAllAssetPairsInteractor;
    private FetchAllAssetPairsInteractor mFetchAllAssetPairsInteractor;
    private UpdateAssetPairsInteractor mUpdateAssetPairsInteractor;

    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    private Timer mReloadTimer;
    private long mFetchDelay = Constants.RELOAD_TIME_SINGLE_ASSET;
    private long mLastUpdated = 0;
    private boolean mFastReloadingEnabled = false;
    private boolean mPaused = false;

    @Inject
    public AssetPairListPresenter(GetAllAssetPairsInteractor getAllAssetPairsInteractor,
                                  FetchAllAssetPairsInteractor fetchAllAssetPairsInteractor,
                                  UpdateAssetPairsInteractor updateAssetPairsInteractor) {
        mGetAllAssetPairsInteractor = getAllAssetPairsInteractor;
        mFetchAllAssetPairsInteractor = fetchAllAssetPairsInteractor;
        mUpdateAssetPairsInteractor = updateAssetPairsInteractor;
    }

    @Override
    public void initialize() {
        getAssetPairs();
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
        super.destroy();
    }

    @Override
    public void getAssetPairs() {
        mCompositeDisposable.add(mGetAllAssetPairsInteractor.execute(new DisposableSubscriber<List<AssetPair>>() {
            @Override
            public void onNext(List<AssetPair> assetPairs) {
                if (assetPairs != null && assetPairs.size() > 0) {
                    mView.hideEmptyText();
                    mView.showAssetPairs(assetPairs);
                    updateFetchTimer(assetPairs);
                } else {
                    mView.showEmptyText();
                }
            }

            @Override
            public void onError(Throwable t) {}

            @Override
            public void onComplete() {}
        }));
    }

    @Override
    public void onAssetPairClicked(@NonNull AssetPair assetPair) {
        mView.openAssetPairDetailViewUI(assetPair);
    }

    @Override
    public void onAddAssetPairClicked() {
        mView.openAssetPairSetupUI();
    }

    @Override
    public void onAssetPairMoved(@NonNull List<AssetPair> assetPairs) {
        mUpdateAssetPairsInteractor.execute(assetPairs, new DisposableCompletableObserver() {
            @Override public void onComplete() {}
            @Override public void onError(Throwable e) {}
        });
    }

    private void fetchAssetPairs() {
        mFetchAllAssetPairsInteractor.execute(new DisposableCompletableObserver() {
            @Override
            public void onComplete() {
                mFetchDelay = 0;
                mLastUpdated = System.currentTimeMillis();
                if(mFastReloadingEnabled) {
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

    private void updateFetchTimer(List<? extends Asset> assets) {
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
                fetchAssetPairs();
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
