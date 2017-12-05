package personal.calebcordell.coinnection.presentation.util.assetsearchrecyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import personal.calebcordell.coinnection.R;
import personal.calebcordell.coinnection.domain.model.Asset;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class AssetSearchRecyclerViewAdapter extends RecyclerView.Adapter<AssetSearchItemViewHolder> {

    private List<Asset> mAssets;
    private List<Asset> mAssetsVisible;
    private Map<String, Integer> mVisibleAssetPositions;

    private List<String> mPortfolioAssetIds;
    private List<String> mWatchlistAssetIds;

    private AssetSearchItemListener mAssetSearchItemListener;

    private String mCurrentFilterText;

    public AssetSearchRecyclerViewAdapter() {
        this(null);
    }
    public AssetSearchRecyclerViewAdapter(AssetSearchItemListener assetSearchItemListener) {
        this(new ArrayList<>(), new ArrayList<>(), assetSearchItemListener);
    }
    public AssetSearchRecyclerViewAdapter(List<String> portfolioAssetIds, List<String> watchlistAssetIds,
                                          AssetSearchItemListener assetSearchItemListener) {
        this(new ArrayList<>(), portfolioAssetIds, watchlistAssetIds, assetSearchItemListener);
    }
    public AssetSearchRecyclerViewAdapter(List<Asset> assets, List<String> portfolioAssetIds,
                                          List<String> watchlistAssetIds,
                                          AssetSearchItemListener assetSearchItemListener) {
        mAssets = assets;
        mPortfolioAssetIds = portfolioAssetIds;
        mWatchlistAssetIds = watchlistAssetIds;
        mAssetSearchItemListener = assetSearchItemListener;

        mAssetsVisible = new ArrayList<>();
        mVisibleAssetPositions = new HashMap<>(mAssets.size());
        mCurrentFilterText = "";
    }

    @Override
    public AssetSearchItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_view_search_asset_item, parent, false);
        return new AssetSearchItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AssetSearchItemViewHolder viewHolder, int position) {
        Asset asset = mAssetsVisible.get(position);
        viewHolder.bind(asset, mPortfolioAssetIds.contains(asset.getId()),
                mWatchlistAssetIds.contains(asset.getId()), mAssetSearchItemListener);
    }

    @Override
    public int getItemCount() {
        return mAssetsVisible.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }


    public void replaceAll(List<Asset> assets) {
        mAssets.clear();
        mAssets.addAll(assets);
        filter(mCurrentFilterText);
        notifyDataSetChanged();
    }

    public void filter(String text) {
        mCurrentFilterText = text;

        mAssetsVisible.clear();
        mVisibleAssetPositions.clear();

        if(!text.isEmpty()) {
            text = text.toLowerCase();
            for(Asset asset : mAssets) {
                String assetId = asset.getId();
                if(assetId.toLowerCase().contains(text) || asset.getName().toLowerCase().contains(text)
                        || asset.getSymbol().toLowerCase().contains(text)) {
                    mVisibleAssetPositions.put(assetId, mAssetsVisible.size());
                    mAssetsVisible.add(asset);
                }
            }
        }
        notifyDataSetChanged();
    }

    public void setPortfolioAssetIds(List<String> portfolioAssetIds) {
        mPortfolioAssetIds.clear();
        mPortfolioAssetIds.addAll(portfolioAssetIds);

        for(String assetId : portfolioAssetIds) {
            Integer position = mVisibleAssetPositions.get(assetId);
            if(position != null) {
                notifyItemChanged(position);
            }
        }
    }

    public void setWatchlistAssetIds(List<String> watchlistAssetIds) {
        mWatchlistAssetIds.clear();
        mWatchlistAssetIds.addAll(watchlistAssetIds);

        for(String assetId : watchlistAssetIds) {
            Integer position = mVisibleAssetPositions.get(assetId);
            if(position != null) {
                notifyItemChanged(position);
            }
        }
    }

    public void setAssetOnWatchlist(String assetId, boolean isOnWatchlist) {
        if(isOnWatchlist) {
            if(!mWatchlistAssetIds.contains(assetId)) {
                mWatchlistAssetIds.add(assetId);
            }
        } else {
            mWatchlistAssetIds.remove(assetId);
        }


        Integer position = mVisibleAssetPositions.get(assetId);
        if(position != null) {
            notifyItemChanged(position);
        }
    }

    public void setAssetSearchItemListener(AssetSearchItemListener assetSearchItemListener) {
        mAssetSearchItemListener = assetSearchItemListener;
    }
}