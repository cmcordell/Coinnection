package personal.calebcordell.coinnection.presentation.views.portfolio;

import java.util.List;

import personal.calebcordell.coinnection.domain.model.Asset;
import personal.calebcordell.coinnection.domain.model.PortfolioAsset;
import personal.calebcordell.coinnection.domain.model.WatchlistAsset;
import personal.calebcordell.coinnection.presentation.views.base.BasePresenter;


public interface PortfolioContract {
    interface View {
        void showPortfolioAssets(List<PortfolioAsset> portfolioAssets);

        void showWatchlistAssets(List<WatchlistAsset> watchlistAssets);

        void showLastUpdated(long lastUpdated);

        void openAssetSearchUI();

        void openPortfolioDetailViewUI(Asset asset);
    }

    abstract class Presenter extends BasePresenter<View> {
        abstract void resume();

        abstract void reorderPortfolioAssets(List<PortfolioAsset> portfolioAssets);

        abstract void reorderWatchlistAssets(List<WatchlistAsset> watchlistAssets);

        abstract void pause();
    }
}