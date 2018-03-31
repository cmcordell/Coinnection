package personal.calebcordell.coinnection.presentation.views.assetdetailtab;

import android.support.annotation.NonNull;

import personal.calebcordell.coinnection.domain.model.Asset;
import personal.calebcordell.coinnection.presentation.views.base.BasePresenter;


interface AssetDetailTabContract {
    interface View {
        void showAsset(@NonNull Asset asset);

        void openEditPortfolioAssetUI(@NonNull Asset asset);

        void openAddPortfolioAssetUI(@NonNull Asset asset);

        void openRemovePortfolioAssetUI();
    }

    abstract class Presenter extends BasePresenter<AssetDetailTabContract.View> {
        abstract void setAsset(@NonNull Asset asset);

        abstract void onEditPortfolioAssetClicked();

        abstract void onAddAssetToPortfolioClicked();

        abstract void onRemoveAssetFromPortfolioClicked();

        abstract void editAssetBalance(double balance);

        abstract void addAssetToPortfolio(double balance);

        abstract void removeAssetFromPortfolio();
    }
}