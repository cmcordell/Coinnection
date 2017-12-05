package personal.calebcordell.coinnection.presentation.util.portfoliorecyclerview;

import java.util.List;

import personal.calebcordell.coinnection.domain.model.PortfolioAsset;
import personal.calebcordell.coinnection.domain.model.WatchlistAsset;


public interface OnPortfolioItemDragFinishedListener {
    void onDragFinished(List<PortfolioAsset> portfolioAssets, List<WatchlistAsset> watchlistAssets);
}