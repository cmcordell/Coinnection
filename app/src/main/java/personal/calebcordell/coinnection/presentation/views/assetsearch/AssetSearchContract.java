package personal.calebcordell.coinnection.presentation.views.assetsearch;

import personal.calebcordell.coinnection.domain.model.Asset;

import java.util.List;


interface AssetSearchContract {
    interface View {
        void setAssets(List<Asset> assets);

        void setPortfolioAssetIds(List<String> portfolioAssetIds);
        void setWatchlistAssetIds(List<String> watchlistAssetIds);

        void openAssetDetailUI(Asset asset);
    }

    interface Presenter {
        void start();

        void onAssetSelected(Asset asset);

        void onAssetFavoriteClicked(Asset asset, boolean isOnWatchlist);

        void destroy();
    }
}
