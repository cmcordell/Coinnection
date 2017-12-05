package personal.calebcordell.coinnection.presentation.views.assetdetail;

import android.support.annotation.NonNull;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableCompletableObserver;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.subscribers.DisposableSubscriber;
import personal.calebcordell.coinnection.domain.interactor.impl.AddAssetToPortfolioInteractor;
import personal.calebcordell.coinnection.domain.interactor.impl.AddAssetToWatchlistInteractor;
import personal.calebcordell.coinnection.domain.interactor.impl.CheckAssetInPortfolioInteractor;
import personal.calebcordell.coinnection.domain.interactor.impl.CheckAssetOnWatchlistInteractor;
import personal.calebcordell.coinnection.domain.interactor.impl.GetAssetByIdInteractor;
import personal.calebcordell.coinnection.domain.interactor.impl.GetPortfolioAssetByIdInteractor;
import personal.calebcordell.coinnection.domain.interactor.impl.RemoveAssetFromPortfolioInteractor;
import personal.calebcordell.coinnection.domain.interactor.impl.RemoveAssetFromWatchlistInteractor;
import personal.calebcordell.coinnection.domain.model.Asset;
import personal.calebcordell.coinnection.domain.model.PortfolioAsset;
import personal.calebcordell.coinnection.domain.model.WatchlistAsset;


public class AssetDetailPresenter  implements AssetDetailContract.Presenter {
    private static final String TAG = AssetDetailPresenter.class.getSimpleName();

    private AssetDetailContract.View mView;

    private GetAssetByIdInteractor mGetAssetByIdInteractor;
    private CheckAssetInPortfolioInteractor mCheckAssetInPortfolioInteractor;
    private GetPortfolioAssetByIdInteractor mGetPortfolioAssetByIdInteractor;
    private AddAssetToPortfolioInteractor mAddAssetToPortfolioInteractor;
    private RemoveAssetFromPortfolioInteractor mRemoveAssetFromPortfolioInteractor;
    private CheckAssetOnWatchlistInteractor mCheckAssetOnWatchlistInteractor;
    private AddAssetToWatchlistInteractor mAddAssetToWatchlistInteractor;
    private RemoveAssetFromWatchlistInteractor mRemoveAssetFromWatchlistInteractor;

    private CompositeDisposable mCompositeDisposable;
    private Disposable mCurrentAssetDisposable;

    AssetDetailPresenter(AssetDetailContract.View view) {
        mView = view;

        mGetAssetByIdInteractor = new GetAssetByIdInteractor();
        mCheckAssetInPortfolioInteractor = new CheckAssetInPortfolioInteractor();
        mGetPortfolioAssetByIdInteractor = new GetPortfolioAssetByIdInteractor();
        mAddAssetToPortfolioInteractor = new AddAssetToPortfolioInteractor();
        mRemoveAssetFromPortfolioInteractor = new RemoveAssetFromPortfolioInteractor();
        mAddAssetToWatchlistInteractor = new AddAssetToWatchlistInteractor();
        mCheckAssetOnWatchlistInteractor = new CheckAssetOnWatchlistInteractor();
        mRemoveAssetFromWatchlistInteractor = new RemoveAssetFromWatchlistInteractor();

        mCompositeDisposable = new CompositeDisposable();
    }

    public void start(final String assetId) {
        mCompositeDisposable.add(mCheckAssetInPortfolioInteractor.execute(assetId, new DisposableSingleObserver<Boolean>() {
            @Override
            public void onSuccess(@NonNull Boolean aBoolean) {
                mView.showAssetInPortfolio(aBoolean);
                if(aBoolean) {
                    getPortfolioAsset(assetId);
                } else {
                    getAsset(assetId);
                }
            }
            @Override public void onError(@NonNull Throwable e) {
                e.printStackTrace();
            }
        }));

        mCompositeDisposable.add(mCheckAssetOnWatchlistInteractor.execute(assetId, new DisposableSingleObserver<Boolean>() {
            @Override
            public void onSuccess(@NonNull Boolean isOnWatchlist) {
                mView.showAssetOnWatchlist(isOnWatchlist);
            }
            @Override public void onError(@NonNull Throwable e) {
                e.printStackTrace();
            }
        }));
    }

    private void getPortfolioAsset(String assetId) {
        if(mCurrentAssetDisposable != null) {
            mCurrentAssetDisposable.dispose();
        }

        mCurrentAssetDisposable = mGetPortfolioAssetByIdInteractor.execute(assetId, new DisposableSubscriber<PortfolioAsset>() {
            @Override
            public void onNext(PortfolioAsset portfolioAsset) {
                mView.showAsset(portfolioAsset);
            }
            @Override public void onError(Throwable t) {
                t.printStackTrace();
            }
            @Override public void onComplete() {}
        });
    }
    private void getAsset(String assetId) {
        if(mCurrentAssetDisposable != null) {
            mCurrentAssetDisposable.dispose();
        }

        mCurrentAssetDisposable = mGetAssetByIdInteractor.execute(assetId, new DisposableSubscriber<Asset>() {
            @Override
            public void onNext(Asset asset) {
                mView.showAsset(asset);
            }
            @Override public void onError(Throwable t) {
                t.printStackTrace();
            }
            @Override public void onComplete() {}
        });
    }

    public void addAssetToPortfolio(final Asset asset, final double balance) {
        final PortfolioAsset portfolioAsset = new PortfolioAsset(asset, balance);
        mCompositeDisposable.add(mAddAssetToPortfolioInteractor.execute(portfolioAsset, new DisposableCompletableObserver() {
            @Override public void onComplete() {
                getPortfolioAsset(portfolioAsset.getId());
            }
            @Override public void onError(Throwable e) {
                e.printStackTrace();
            }
        }));
    }
    public void removeAssetFromPortfolio(final String assetId) {
        mCompositeDisposable.add(mRemoveAssetFromPortfolioInteractor.execute(assetId, new DisposableCompletableObserver() {
            @Override public void onComplete() {
                getAsset(assetId);
            }
            @Override public void onError(Throwable e) {
                e.printStackTrace();
            }
        }));
    }

    public void addAssetToWatchlist(final Asset asset) {
        final WatchlistAsset watchlistAsset = new WatchlistAsset(asset);
        mCompositeDisposable.add(mAddAssetToWatchlistInteractor.execute(watchlistAsset, new DisposableCompletableObserver() {
            @Override public void onComplete() {}
            @Override public void onError(@NonNull Throwable e) {
                e.printStackTrace();
            }
        }));
    }
    public void removeAssetFromWatchlist(final String assetId) {
        mCompositeDisposable.add(mRemoveAssetFromWatchlistInteractor.execute(assetId, new DisposableCompletableObserver() {
            @Override public void onComplete() {}
            @Override public void onError(@NonNull Throwable e) {
                e.printStackTrace();
            }
        }));
    }

    public void destroy() {
        mCompositeDisposable.dispose();

        if(mCurrentAssetDisposable != null) {
            mCurrentAssetDisposable.dispose();
        }
    }
}