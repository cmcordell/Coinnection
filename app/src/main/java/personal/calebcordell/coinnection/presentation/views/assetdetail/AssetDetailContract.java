package personal.calebcordell.coinnection.presentation.views.assetdetail;

import android.support.annotation.NonNull;

import personal.calebcordell.coinnection.domain.model.Asset;
import personal.calebcordell.coinnection.presentation.views.base.BasePresenter;


public interface AssetDetailContract {
    interface View {
        void showAsset(Asset asset);

        void showAssetInPortfolio(boolean isInPortfolio);

        void showAssetOnWatchlist(boolean isOnWatchlist);

        void openEditPortfolioAssetUI(@NonNull Asset asset);

        void openAddPortfolioAssetUI(@NonNull Asset asset);

        void openRemovePortfolioAssetUI();
    }

    abstract class Presenter extends BasePresenter<View> {
        abstract void setAsset(Asset asset);

        abstract void onEditPortfolioAssetClicked();

        abstract void onAddAssetToPortfolioClicked();

        abstract void onRemoveAssetFromPortfolioClicked();

        abstract void editAssetBalance(double balance);

        abstract void addAssetToPortfolio(double balance);

        abstract void removeAssetFromPortfolio();

        abstract void addAssetToWatchlist();

        abstract void removeAssetFromWatchlist();
    }
}