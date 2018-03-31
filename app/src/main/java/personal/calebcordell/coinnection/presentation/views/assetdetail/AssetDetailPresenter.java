package personal.calebcordell.coinnection.presentation.views.assetdetail;

import android.support.annotation.NonNull;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableCompletableObserver;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.subscribers.DisposableSubscriber;
import personal.calebcordell.coinnection.domain.interactor.impl.assetinteractors.FetchAssetByIdInteractor;
import personal.calebcordell.coinnection.domain.interactor.impl.assetinteractors.GetAssetByIdInteractor;
import personal.calebcordell.coinnection.domain.interactor.impl.portfolioassetinteractors.AddAssetToPortfolioInteractor;
import personal.calebcordell.coinnection.domain.interactor.impl.portfolioassetinteractors.CheckAssetInPortfolioInteractor;
import personal.calebcordell.coinnection.domain.interactor.impl.portfolioassetinteractors.GetPortfolioAssetByIdInteractor;
import personal.calebcordell.coinnection.domain.interactor.impl.portfolioassetinteractors.RemoveAssetFromPortfolioInteractor;
import personal.calebcordell.coinnection.domain.interactor.impl.watchlistassetinteractors.AddAssetToWatchlistInteractor;
import personal.calebcordell.coinnection.domain.interactor.impl.watchlistassetinteractors.CheckAssetOnWatchlistInteractor;
import personal.calebcordell.coinnection.domain.interactor.impl.watchlistassetinteractors.RemoveAssetFromWatchlistInteractor;
import personal.calebcordell.coinnection.domain.model.Asset;
import personal.calebcordell.coinnection.domain.model.PortfolioAsset;
import personal.calebcordell.coinnection.domain.model.WatchlistAsset;
import personal.calebcordell.coinnection.presentation.Constants;


public class AssetDetailPresenter extends AssetDetailContract.Presenter {
    private static final String TAG = AssetDetailPresenter.class.getSimpleName();

    private GetAssetByIdInteractor mGetAssetByIdInteractor;
    private FetchAssetByIdInteractor mFetchAssetByIdInteractor;

    private CheckAssetInPortfolioInteractor mCheckAssetInPortfolioInteractor;
    private GetPortfolioAssetByIdInteractor mGetPortfolioAssetByIdInteractor;
    private AddAssetToPortfolioInteractor mAddAssetToPortfolioInteractor;
    private RemoveAssetFromPortfolioInteractor mRemoveAssetFromPortfolioInteractor;

    private CheckAssetOnWatchlistInteractor mCheckAssetOnWatchlistInteractor;
    private AddAssetToWatchlistInteractor mAddAssetToWatchlistInteractor;
    private RemoveAssetFromWatchlistInteractor mRemoveAssetFromWatchlistInteractor;

    private CompositeDisposable mCompositeDisposable;
    private Disposable mCurrentAssetDisposable;
    private boolean mDestroyed = false;

    private Asset mAsset;

    @Inject
    AssetDetailPresenter(GetAssetByIdInteractor getAssetByIdInteractor,
                         FetchAssetByIdInteractor fetchAssetByIdInteractor,
                         CheckAssetInPortfolioInteractor checkAssetInPortfolioInteractor,
                         GetPortfolioAssetByIdInteractor getPortfolioAssetByIdInteractor,
                         AddAssetToPortfolioInteractor addAssetToPortfolioInteractor,
                         RemoveAssetFromPortfolioInteractor removeAssetFromPortfolioInteractor,
                         CheckAssetOnWatchlistInteractor checkAssetOnWatchlistInteractor,
                         AddAssetToWatchlistInteractor addAssetToWatchlistInteractor,
                         RemoveAssetFromWatchlistInteractor removeAssetFromWatchlistInteractor) {

        mGetAssetByIdInteractor = getAssetByIdInteractor;
        mFetchAssetByIdInteractor = fetchAssetByIdInteractor;
        mCheckAssetInPortfolioInteractor = checkAssetInPortfolioInteractor;
        mGetPortfolioAssetByIdInteractor = getPortfolioAssetByIdInteractor;
        mAddAssetToPortfolioInteractor = addAssetToPortfolioInteractor;
        mRemoveAssetFromPortfolioInteractor = removeAssetFromPortfolioInteractor;
        mAddAssetToWatchlistInteractor = addAssetToWatchlistInteractor;
        mCheckAssetOnWatchlistInteractor = checkAssetOnWatchlistInteractor;
        mRemoveAssetFromWatchlistInteractor = removeAssetFromWatchlistInteractor;

        mCompositeDisposable = new CompositeDisposable();
    }

    @Override
    public void setAsset(Asset asset) {
        mAsset = asset;
    }

    @Override
    public void initialize() {
        if (mAsset == null) {
            throw new RuntimeException("Asset must be set before calling start()");
        } else {
            mView.showAsset(mAsset);
            mCompositeDisposable.add(mCheckAssetInPortfolioInteractor.execute(mAsset.getId(), new DisposableSingleObserver<Boolean>() {
                @Override
                public void onSuccess(@NonNull Boolean isInPortfolio) {
                    mView.showAssetInPortfolio(isInPortfolio);
                    if (isInPortfolio) {
                        getPortfolioAsset();
                    } else {
                        getAsset();
                    }
                }

                @Override
                public void onError(@NonNull Throwable e) {}
            }));

            mCompositeDisposable.add(mCheckAssetOnWatchlistInteractor.execute(mAsset.getId(), new DisposableSingleObserver<Boolean>() {
                @Override
                public void onSuccess(@NonNull Boolean isOnWatchlist) {
                    mView.showAssetOnWatchlist(isOnWatchlist);
                }

                @Override
                public void onError(@NonNull Throwable e) {}
            }));
        }
    }

    private void getPortfolioAsset() {
        if (mCurrentAssetDisposable != null) {
            mCurrentAssetDisposable.dispose();
        }

        mCurrentAssetDisposable = mGetPortfolioAssetByIdInteractor.execute(mAsset.getId(), new DisposableSubscriber<PortfolioAsset>() {
            @Override
            public void onNext(PortfolioAsset portfolioAsset) {
                mAsset = portfolioAsset;
                mView.showAsset(portfolioAsset);

                if(!portfolioAsset.isUpToDate(Constants.RELOAD_TIME_SINGLE_ASSET)) {
                    fetchAsset();
                }
            }

            @Override public void onError(Throwable t) {}
            @Override public void onComplete() {}
        });
    }
    private void getAsset() {
        if (mCurrentAssetDisposable != null) {
            mCurrentAssetDisposable.dispose();
        }

        mCurrentAssetDisposable = mGetAssetByIdInteractor.execute(mAsset.getId(), new DisposableSubscriber<Asset>() {
            @Override
            public void onNext(Asset asset) {
                mAsset = asset;
                mView.showAsset(asset);

                if(!asset.isUpToDate(Constants.RELOAD_TIME_SINGLE_ASSET)) {
                    fetchAsset();
                }
            }

            @Override public void onError(Throwable t) {}
            @Override public void onComplete() {}
        });
    }
    private void fetchAsset() {
        mFetchAssetByIdInteractor.execute(mAsset.getId(), new DisposableCompletableObserver() {
            @Override public void onComplete() {}
            @Override public void onError(Throwable e) {}
        });
    }

    @Override
    public void onEditPortfolioAssetClicked() {
        mView.openEditPortfolioAssetUI(mAsset);
    }

    @Override
    public void onAddAssetToPortfolioClicked() {
        mView.openAddPortfolioAssetUI(mAsset);
    }

    @Override
    public void onRemoveAssetFromPortfolioClicked() {
        mView.openRemovePortfolioAssetUI();
    }

    @Override
    public void editAssetBalance(final double balance) {
        final PortfolioAsset portfolioAsset = new PortfolioAsset(mAsset, balance);
        mAddAssetToPortfolioInteractor.execute(portfolioAsset, new DisposableCompletableObserver() {
            @Override
            public void onComplete() {
                if(!mDestroyed) {
                    getPortfolioAsset();
                }
            }

            @Override
            public void onError(Throwable e) {}
        });
    }

    @Override
    public void addAssetToPortfolio(final double balance) {
        final PortfolioAsset portfolioAsset = new PortfolioAsset(mAsset, balance);
        mAddAssetToPortfolioInteractor.execute(portfolioAsset, new DisposableCompletableObserver() {
            @Override
            public void onComplete() {
                if(!mDestroyed) {
                    mView.showAssetInPortfolio(true);
                    mView.showAssetOnWatchlist(false);
                    getPortfolioAsset();
                }
            }

            @Override
            public void onError(Throwable e) {}
        });
    }

    @Override
    public void removeAssetFromPortfolio() {
        mRemoveAssetFromPortfolioInteractor.execute(mAsset.getId(), new DisposableCompletableObserver() {
            @Override
            public void onComplete() {
                if(!mDestroyed) {
                    mView.showAssetOnWatchlist(false);
                    mView.showAssetInPortfolio(false);
                    getAsset();
                }
            }

            @Override
            public void onError(Throwable e) {}
        });
    }

    @Override
    public void addAssetToWatchlist() {
        final WatchlistAsset watchlistAsset = new WatchlistAsset(mAsset);
        mAddAssetToWatchlistInteractor.execute(watchlistAsset, new DisposableCompletableObserver() {
            @Override
            public void onComplete() {
                if (!mDestroyed) {
                    mView.showAssetOnWatchlist(true);
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {}
        });
    }

    @Override
    public void removeAssetFromWatchlist() {
        mRemoveAssetFromWatchlistInteractor.execute(mAsset.getId(), new DisposableCompletableObserver() {
            @Override
            public void onComplete() {
                if(!mDestroyed) {
                    mView.showAssetOnWatchlist(false);
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {}
        });
    }

    @Override
    public void destroy() {
        mDestroyed = true;

        mCompositeDisposable.dispose();

        if (mCurrentAssetDisposable != null) {
            mCurrentAssetDisposable.dispose();
        }

        super.destroy();
    }
}