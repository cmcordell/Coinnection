package personal.calebcordell.coinnection.presentation.views.assetdetailtab;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableCompletableObserver;
import personal.calebcordell.coinnection.domain.interactor.impl.AddAssetToPortfolioInteractor;
import personal.calebcordell.coinnection.domain.interactor.impl.RemoveAssetFromPortfolioInteractor;
import personal.calebcordell.coinnection.domain.model.Asset;
import personal.calebcordell.coinnection.domain.model.PortfolioAsset;


class AssetDetailTabPresenter implements AssetDetailTabContract.Presenter {
    private static final String TAG = AssetDetailTabPresenter.class.getSimpleName();

    private AssetDetailTabContract.View mView;

    private AddAssetToPortfolioInteractor mAddAssetToPortfolioInteractor;
    private RemoveAssetFromPortfolioInteractor mRemoveAssetFromPortfolioInteractor;

    private CompositeDisposable mCompositeDisposable;

    AssetDetailTabPresenter(AssetDetailTabContract.View view) {
        mView = view;

        mAddAssetToPortfolioInteractor = new AddAssetToPortfolioInteractor();
        mRemoveAssetFromPortfolioInteractor = new RemoveAssetFromPortfolioInteractor();

        mCompositeDisposable = new CompositeDisposable();
    }

    public void start(final String assetId) {

    }

    public void addAssetToPortfolio(final Asset asset, final double balance) {
        final PortfolioAsset portfolioAsset = new PortfolioAsset(asset, balance);
        mCompositeDisposable.add(mAddAssetToPortfolioInteractor.execute(portfolioAsset, new DisposableCompletableObserver() {
            @Override public void onComplete() {}
            @Override public void onError(Throwable e) {
                e.printStackTrace();
            }
        }));
    }
    public void removeAssetFromPortfolio(final String assetId) {
        mCompositeDisposable.add(mRemoveAssetFromPortfolioInteractor.execute(assetId, new DisposableCompletableObserver() {
            @Override public void onComplete() {}
            @Override public void onError(Throwable e) {
                e.printStackTrace();
            }
        }));
    }

    public void fragmentBecameInvisible() {

    }

    public void destroy() {
        mCompositeDisposable.dispose();
    }
}