package personal.calebcordell.coinnection.presentation.views.assetsearch;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableCompletableObserver;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.subscribers.DisposableSubscriber;
import personal.calebcordell.coinnection.domain.interactor.impl.AddAssetToWatchlistInteractor;
import personal.calebcordell.coinnection.domain.interactor.impl.GetAllAssetsInteractor;
import personal.calebcordell.coinnection.domain.interactor.impl.GetPortfolioAssetIdsInteractor;
import personal.calebcordell.coinnection.domain.interactor.impl.GetWatchlistAssetIdsInteractor;
import personal.calebcordell.coinnection.domain.interactor.impl.RemoveAssetFromWatchlistInteractor;
import personal.calebcordell.coinnection.domain.model.Asset;

import java.util.List;

import io.reactivex.annotations.NonNull;
import personal.calebcordell.coinnection.domain.model.WatchlistAsset;


class AssetSearchPresenter implements AssetSearchContract.Presenter {
    private static final String TAG = AssetSearchPresenter.class.getSimpleName();

    private AssetSearchContract.View mView;

    private GetAllAssetsInteractor mGetAllAssetsInteractor;
    private GetPortfolioAssetIdsInteractor mGetPortfolioAssetIdsInteractor;
    private GetWatchlistAssetIdsInteractor mGetWatchlistAssetIdsInteractor;
    private AddAssetToWatchlistInteractor mAddAssetToWatchlistInteractor;
    private RemoveAssetFromWatchlistInteractor mRemoveAssetFromWatchlistInteractor;

    private CompositeDisposable mCompositeDisposable;
    private Disposable mAllAssetsDisposable;

    AssetSearchPresenter(AssetSearchContract.View view) {
        if(view != null) {
            this.mView = view;
        }

        mGetAllAssetsInteractor = new GetAllAssetsInteractor();
        mGetPortfolioAssetIdsInteractor = new GetPortfolioAssetIdsInteractor();
        mGetWatchlistAssetIdsInteractor = new GetWatchlistAssetIdsInteractor();
        mAddAssetToWatchlistInteractor = new AddAssetToWatchlistInteractor();
        mRemoveAssetFromWatchlistInteractor = new RemoveAssetFromWatchlistInteractor();

        mCompositeDisposable = new CompositeDisposable();
    }

    public void start() {
        mAllAssetsDisposable = mGetAllAssetsInteractor.execute(new DisposableSubscriber<List<Asset>>() {
            @Override
            public void onNext(List<Asset> assets) {
                mView.setAssets(assets);
                mAllAssetsDisposable.dispose();
            }

            @Override
            public void onError(Throwable t) {
                t.printStackTrace();
            }

            @Override
            public void onComplete() {}
        });

        mCompositeDisposable.add(mGetPortfolioAssetIdsInteractor.execute(new DisposableSingleObserver<List<String>>() {
            @Override
            public void onSuccess(List<String> portfolioAssetIds) {
                mView.setPortfolioAssetIds(portfolioAssetIds);
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }
        }));
        mCompositeDisposable.add(mGetWatchlistAssetIdsInteractor.execute(new DisposableSingleObserver<List<String>>() {
            @Override
            public void onSuccess(List<String> watchlistAssetIds) {
                mView.setWatchlistAssetIds(watchlistAssetIds);
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }
        }));
    }

    public void onAssetSelected(Asset asset) {
        mView.openAssetDetailUI(asset);
    }

    public void onAssetFavoriteClicked(Asset asset, boolean isOnWatchlist) {
        if(isOnWatchlist) {
            mCompositeDisposable.add(mAddAssetToWatchlistInteractor.execute(new WatchlistAsset(asset), new DisposableCompletableObserver() {
                @Override
                public void onComplete() {
                }

                @Override
                public void onError(@NonNull Throwable e) {
                    e.printStackTrace();
                }
            }));
        } else {
            mCompositeDisposable.add(mRemoveAssetFromWatchlistInteractor.execute(asset.getId(), new DisposableCompletableObserver() {
                @Override
                public void onComplete() {
                }

                @Override
                public void onError(@NonNull Throwable e) {
                    e.printStackTrace();
                }
            }));
        }
    }

    public void destroy() {
        mCompositeDisposable.dispose();

        if(mAllAssetsDisposable != null) {
            mAllAssetsDisposable.dispose();
        }
    }
}