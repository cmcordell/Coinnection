package personal.calebcordell.coinnection.presentation.views.assetsearch;

import android.support.annotation.NonNull;

import java.util.List;

import personal.calebcordell.coinnection.domain.model.Asset;
import personal.calebcordell.coinnection.presentation.views.base.BasePresenter;


interface AssetSearchContract {
    interface View {
        void setAssets(@NonNull List<Asset> assets);

        void setPortfolioAssetIds(@NonNull List<String> portfolioAssetIds);

        void setWatchlistAssetIds(@NonNull List<String> watchlistAssetIds);

        void openAssetDetailUI(@NonNull Asset asset);
    }

    abstract class Presenter extends BasePresenter<View> {
        abstract void onAssetSelected(@NonNull Asset asset);

        abstract void onAssetFavoriteClicked(@NonNull Asset asset, boolean isOnWatchlist);
    }
}
