package personal.calebcordell.coinnection.presentation.views.assetsearch;

import android.support.annotation.NonNull;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableCompletableObserver;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.subscribers.DisposableSubscriber;
import personal.calebcordell.coinnection.domain.interactor.impl.assetinteractors.GetAllAssetsInteractor;
import personal.calebcordell.coinnection.domain.interactor.impl.portfolioassetinteractors.GetPortfolioAssetIdsInteractor;
import personal.calebcordell.coinnection.domain.interactor.impl.watchlistassetinteractors.AddAssetToWatchlistInteractor;
import personal.calebcordell.coinnection.domain.interactor.impl.watchlistassetinteractors.GetWatchlistAssetIdsInteractor;
import personal.calebcordell.coinnection.domain.interactor.impl.watchlistassetinteractors.RemoveAssetFromWatchlistInteractor;
import personal.calebcordell.coinnection.domain.model.Asset;
import personal.calebcordell.coinnection.domain.model.WatchlistAsset;


class AssetSearchPresenter extends AssetSearchContract.Presenter {
    private static final String TAG = AssetSearchPresenter.class.getSimpleName();

    private GetAllAssetsInteractor mGetAllAssetsInteractor;
    private GetPortfolioAssetIdsInteractor mGetPortfolioAssetIdsInteractor;
    private GetWatchlistAssetIdsInteractor mGetWatchlistAssetIdsInteractor;
    private AddAssetToWatchlistInteractor mAddAssetToWatchlistInteractor;
    private RemoveAssetFromWatchlistInteractor mRemoveAssetFromWatchlistInteractor;

    private CompositeDisposable mCompositeDisposable;
    private Disposable mAllAssetsDisposable;

    @Inject
    AssetSearchPresenter(GetAllAssetsInteractor getAllAssetsInteractor,
                         GetPortfolioAssetIdsInteractor getPortfolioAssetIdsInteractor,
                         GetWatchlistAssetIdsInteractor getWatchlistAssetIdsInteractor,
                         AddAssetToWatchlistInteractor addAssetToWatchlistInteractor,
                         RemoveAssetFromWatchlistInteractor removeAssetFromWatchlistInteractor) {
        mGetAllAssetsInteractor = getAllAssetsInteractor;
        mGetPortfolioAssetIdsInteractor = getPortfolioAssetIdsInteractor;
        mGetWatchlistAssetIdsInteractor = getWatchlistAssetIdsInteractor;
        mAddAssetToWatchlistInteractor = addAssetToWatchlistInteractor;
        mRemoveAssetFromWatchlistInteractor = removeAssetFromWatchlistInteractor;

        mCompositeDisposable = new CompositeDisposable();
    }

    @Override
    public void initialize() {
        getAllAssets();
        getPortfolioAssets();
        getWatchlistAssets();
    }

    private void getAllAssets() {
        mAllAssetsDisposable = mGetAllAssetsInteractor.execute(new DisposableSubscriber<List<Asset>>() {
            @Override
            public void onNext(List<Asset> assets) {
                mView.setAssets(assets);
                mAllAssetsDisposable.dispose();
            }

            @Override
            public void onError(Throwable t) {
                mAllAssetsDisposable.dispose();
            }

            @Override
            public void onComplete() {
                mAllAssetsDisposable.dispose();
            }
        });
    }

    private void getPortfolioAssets() {
        mCompositeDisposable.add(mGetPortfolioAssetIdsInteractor.execute(new DisposableSingleObserver<List<String>>() {
            @Override
            public void onSuccess(List<String> portfolioAssetIds) {
                mView.setPortfolioAssetIds(portfolioAssetIds);
            }

            @Override
            public void onError(Throwable e) {}
        }));
    }

    private void getWatchlistAssets() {
        mCompositeDisposable.add(mGetWatchlistAssetIdsInteractor.execute(new DisposableSingleObserver<List<String>>() {
            @Override
            public void onSuccess(List<String> watchlistAssetIds) {
                mView.setWatchlistAssetIds(watchlistAssetIds);
            }

            @Override
            public void onError(Throwable e) {}
        }));
    }

    @Override
    public void onAssetSelected(@NonNull Asset asset) {
        mView.openAssetDetailUI(asset);
    }

    @Override
    public void onAssetFavoriteClicked(@NonNull Asset asset, boolean isOnWatchlist) {
        if (isOnWatchlist) {
            mAddAssetToWatchlistInteractor.execute(new WatchlistAsset(asset), new DisposableCompletableObserver() {
                @Override public void onComplete() {}
                @Override public void onError(@NonNull Throwable e) {}
            });
        } else {
            mRemoveAssetFromWatchlistInteractor.execute(asset.getId(), new DisposableCompletableObserver() {
                @Override public void onComplete() {}
                @Override public void onError(@NonNull Throwable e) {}
            });
        }
    }

    @Override
    public void destroy() {
        mCompositeDisposable.dispose();

        if (mAllAssetsDisposable != null && !mAllAssetsDisposable.isDisposed()) {
            mAllAssetsDisposable.dispose();
        }

        super.destroy();
    }
}