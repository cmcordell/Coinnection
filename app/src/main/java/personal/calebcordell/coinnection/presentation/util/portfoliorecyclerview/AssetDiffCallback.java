package personal.calebcordell.coinnection.presentation.util.portfoliorecyclerview;

import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;

import java.util.List;

import personal.calebcordell.coinnection.domain.model.Asset;


public class AssetDiffCallback extends DiffUtil.Callback {
    private List<? extends Asset> mOldPortfolioAssets;
    private List<? extends Asset> mNewPortfolioAssets;

    public AssetDiffCallback(List<? extends Asset> oldPortfolioAssets, List<? extends Asset> newPortfolioAssets) {
        mOldPortfolioAssets = oldPortfolioAssets;
        mNewPortfolioAssets = newPortfolioAssets;
    }

    @Override
    public int getOldListSize() {
        return mOldPortfolioAssets.size();
    }

    @Override
    public int getNewListSize() {
        return mNewPortfolioAssets.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return mOldPortfolioAssets.get(oldItemPosition).getId().equals(mNewPortfolioAssets.get(newItemPosition).getId());
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return mOldPortfolioAssets.get(oldItemPosition).equals(mNewPortfolioAssets.get(newItemPosition));
    }

    @Nullable
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
        //you can return particular field for changed item.
        return super.getChangePayload(oldItemPosition, newItemPosition);
    }
}