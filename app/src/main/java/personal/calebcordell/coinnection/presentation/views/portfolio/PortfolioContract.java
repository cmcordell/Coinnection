package personal.calebcordell.coinnection.presentation.views.portfolio;

import java.util.List;

import personal.calebcordell.coinnection.domain.model.Asset;
import personal.calebcordell.coinnection.domain.model.PortfolioAsset;
import personal.calebcordell.coinnection.domain.model.WatchlistAsset;


public interface PortfolioContract {
    interface View {
        void showPortfolioAssets(List<PortfolioAsset> portfolioAssets);
        void updatePortfolioAssets(List<PortfolioAsset> portfolioAssets);

        void showWatchlistAssets(List<WatchlistAsset> watchlistAssets);
        void updateWatchlistAssets(List<WatchlistAsset> watchlistAssets);

        void showLastUpdated(long lastUpdated);

        void openAssetSearchUI();
        void openPortfolioDetailViewUI(Asset asset);
    }

    interface Presenter {
        void start();

        void resume();

        void reorderPortfolioAssets(List<PortfolioAsset> portfolioAssets);
        void reorderWatchlistAssets(List<WatchlistAsset> watchlistAssets);

        void pause();

        void destroy();
    }
}