package personal.calebcordell.coinnection.presentation.views.portfoliodetail;

import java.util.List;

import personal.calebcordell.coinnection.domain.model.Asset;
import personal.calebcordell.coinnection.domain.model.PortfolioAsset;
import personal.calebcordell.coinnection.domain.model.WatchlistAsset;
import personal.calebcordell.coinnection.presentation.views.base.BasePresenter;


public interface PortfolioDetailContract {
    interface View {
        void setPortfolioAssets(List<PortfolioAsset> portfolioAssets);

        void setWatchlistAssets(List<WatchlistAsset> watchlistAssets);

        void setInitialPosition(int initialPosition);

        void setCurrentAsset(Asset asset, boolean assetOnWatchlist);

        void goBack();
    }

    abstract class Presenter extends BasePresenter<View> {
        abstract void setPortfolioAssets(final List<PortfolioAsset> portfolioAssets);

        abstract void setWatchlistAssets(final List<WatchlistAsset> watchlistAssets);

        abstract void setInitialPosition(final int position);

        abstract void setCurrentAssetPosition(int position);

        abstract void addAssetToWatchlist();

        abstract void removeAssetFromWatchlist();
    }
}