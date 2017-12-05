package personal.calebcordell.coinnection.presentation.util.assetsearchrecyclerview;

import personal.calebcordell.coinnection.domain.model.Asset;

public interface AssetSearchItemListener {
    void onAssetItemClick(Asset asset);
    void onFavoriteClick(Asset asset, boolean addToWatchlist);
}
