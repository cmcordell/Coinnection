package personal.calebcordell.coinnection.presentation.views.portfoliodetail;

import java.util.List;

import personal.calebcordell.coinnection.domain.model.Asset;


public interface PortfolioDetailContract {
    interface View {
        void setWatchlistAssetIds(List<String> watchlistAssetIds);
    }

    interface Presenter {
        void start();

        void removeAsset(String assetId);

        void addAssetToWatchlist(final Asset asset);
        void removeAssetFromWatchlist(final String assetId);

        void destroy();
    }
}