package personal.calebcordell.coinnection.presentation.views.assetdetailtab;

import android.support.annotation.NonNull;

import javax.inject.Inject;

import io.reactivex.observers.DisposableCompletableObserver;
import personal.calebcordell.coinnection.domain.interactor.impl.portfolioassetinteractors.AddAssetToPortfolioInteractor;
import personal.calebcordell.coinnection.domain.interactor.impl.portfolioassetinteractors.RemoveAssetFromPortfolioInteractor;
import personal.calebcordell.coinnection.domain.model.Asset;
import personal.calebcordell.coinnection.domain.model.PortfolioAsset;


class AssetDetailTabPresenter extends AssetDetailTabContract.Presenter {
    private static final String TAG = AssetDetailTabPresenter.class.getSimpleName();

    private AddAssetToPortfolioInteractor mAddAssetToPortfolioInteractor;
    private RemoveAssetFromPortfolioInteractor mRemoveAssetFromPortfolioInteractor;

    private Asset mAsset;

    @Inject
    AssetDetailTabPresenter(AddAssetToPortfolioInteractor addAssetToPortfolioInteractor,
                            RemoveAssetFromPortfolioInteractor removeAssetFromPortfolioInteractor) {
        mAddAssetToPortfolioInteractor = addAssetToPortfolioInteractor;
        mRemoveAssetFromPortfolioInteractor = removeAssetFromPortfolioInteractor;
    }

    @Override
    public void setAsset(@NonNull Asset asset) {
        mAsset = asset;
    }

    @Override
    public void initialize() {
        if (mAsset == null) {
            throw new RuntimeException("Asset must be set before calling start()");
        } else {
            mView.showAsset(mAsset);
        }
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
    public void editAssetBalance(double balance) {
        final PortfolioAsset portfolioAsset = new PortfolioAsset(mAsset, balance);
        mAddAssetToPortfolioInteractor.execute(portfolioAsset, new DisposableCompletableObserver() {
            @Override
            public void onComplete() {}

            @Override
            public void onError(Throwable e) {}
        });
    }

    @Override
    public void addAssetToPortfolio(final double balance) {
        final PortfolioAsset portfolioAsset = new PortfolioAsset(mAsset, balance);
        mAddAssetToPortfolioInteractor.execute(portfolioAsset, new DisposableCompletableObserver() {
            @Override
            public void onComplete() {}

            @Override
            public void onError(Throwable e) {}
        });
    }

    @Override
    public void removeAssetFromPortfolio() {
        mRemoveAssetFromPortfolioInteractor.execute(mAsset.getId(), new DisposableCompletableObserver() {
            @Override
            public void onComplete() {}

            @Override
            public void onError(Throwable e) {}
        });
    }
}