package personal.calebcordell.coinnection.presentation.views.portfoliodetail;

import java.util.List;

import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableCompletableObserver;
import io.reactivex.observers.DisposableSingleObserver;
import personal.calebcordell.coinnection.domain.interactor.impl.AddAssetToPortfolioInteractor;
import personal.calebcordell.coinnection.domain.interactor.impl.AddAssetToWatchlistInteractor;
import personal.calebcordell.coinnection.domain.interactor.impl.GetWatchlistAssetIdsInteractor;
import personal.calebcordell.coinnection.domain.interactor.impl.RemoveAssetFromPortfolioInteractor;
import personal.calebcordell.coinnection.domain.interactor.impl.RemoveAssetFromWatchlistInteractor;
import personal.calebcordell.coinnection.domain.model.Asset;
import personal.calebcordell.coinnection.domain.model.WatchlistAsset;


public class PortfolioDetailPresenter implements PortfolioDetailContract.Presenter {
    private static final String TAG = PortfolioDetailPresenter.class.getSimpleName();

    private PortfolioDetailContract.View mView;

    private GetWatchlistAssetIdsInteractor mGetWatchlistAssetIdsInteractor;
    private RemoveAssetFromPortfolioInteractor mRemoveAssetFromPortfolioInteractor;
    private AddAssetToWatchlistInteractor mAddAssetToWatchlistInteractor;
    private RemoveAssetFromWatchlistInteractor mRemoveAssetFromWatchlistInteractor;

    private CompositeDisposable mCompositeDisposable;

    public PortfolioDetailPresenter(PortfolioDetailContract.View view) {
        mView = view;

        mGetWatchlistAssetIdsInteractor = new GetWatchlistAssetIdsInteractor();
        mRemoveAssetFromPortfolioInteractor = new RemoveAssetFromPortfolioInteractor();
        mAddAssetToWatchlistInteractor = new AddAssetToWatchlistInteractor();
        mRemoveAssetFromWatchlistInteractor = new RemoveAssetFromWatchlistInteractor();

        mCompositeDisposable = new CompositeDisposable();
    }

    public void start() {
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

    public void removeAsset(String assetId) {
        mCompositeDisposable.add(mRemoveAssetFromPortfolioInteractor.execute(assetId, new DisposableCompletableObserver() {
            @Override
            public void onComplete() {

            }

            @Override
            public void onError(Throwable e) {
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
    }
}