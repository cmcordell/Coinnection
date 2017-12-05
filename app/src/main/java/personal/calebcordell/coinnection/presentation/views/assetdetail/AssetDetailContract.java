package personal.calebcordell.coinnection.presentation.views.assetdetail;

import personal.calebcordell.coinnection.domain.model.Asset;


public interface AssetDetailContract {
    interface View {
        void showAsset(Asset asset);

        void showAssetInPortfolio(boolean isInPortfolio);

        void showAssetOnWatchlist(boolean isOnWatchlist);
    }

    interface Presenter {
        void start(String assetId);

        void addAssetToPortfolio(Asset asset, double balance);
        void removeAssetFromPortfolio(String assetId);

        void addAssetToWatchlist(Asset asset);
        void removeAssetFromWatchlist(String assetId);

        void destroy();
    }
}