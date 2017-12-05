package personal.calebcordell.coinnection.presentation.util.assetrecyclerview;

import android.support.design.widget.TabLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import personal.calebcordell.coinnection.R;
import personal.calebcordell.coinnection.domain.model.Asset;
import personal.calebcordell.coinnection.domain.model.PortfolioAsset;
import personal.calebcordell.coinnection.presentation.Constants;
import personal.calebcordell.coinnection.presentation.util.RecyclerViewScrollDisabler;


public class AssetRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_ASSET_HEADER = 0;
    private static final int TYPE_ASSET_BUTTONS = 1;
    private static final int TYPE_ASSET_BALANCE = 2;
    private static final int TYPE_ASSET_STATISTICS = 3;

    private static final int ASSET_HEADER_POSITION = 0;
    private static final int ASSET_BUTTONS_POSITION = 1;

    private Asset mAsset;

    private int mTimeframe;

    private View.OnClickListener mOnClickListener;
    private TabLayout.OnTabSelectedListener mOnTabSelectedListener;

    public AssetRecyclerViewAdapter(Asset asset, View.OnClickListener onClickListener, TabLayout.OnTabSelectedListener onTabSelectedListener) {
        mAsset = asset;
        mOnClickListener = onClickListener;
        mOnTabSelectedListener = onTabSelectedListener;
        mTimeframe = Constants.TIMEFRAME_HOUR;
    }

    @Override
    public int getItemViewType(int position) {
        if(position == ASSET_HEADER_POSITION) {
            return TYPE_ASSET_HEADER;
        } else if(position == ASSET_BUTTONS_POSITION) {
           return TYPE_ASSET_BUTTONS;
        } else if(position == 2) {
            if(mAsset instanceof PortfolioAsset) {
                return TYPE_ASSET_BALANCE;
            } else {
                return TYPE_ASSET_STATISTICS;
            }
        } else {
            return TYPE_ASSET_STATISTICS;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, final int viewType) {
        switch(viewType) {
            case TYPE_ASSET_HEADER:
                return new AssetHeaderViewHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.card_view_asset_header, parent, false));
            case TYPE_ASSET_BUTTONS:
                return new AssetButtonsViewHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.card_view_asset_buttons, parent, false));
            case TYPE_ASSET_BALANCE:
                return new AssetBalanceViewHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.card_view_asset_balance, parent, false));
            case TYPE_ASSET_STATISTICS:
                return new AssetStatisticsViewHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.card_view_asset_statistics, parent, false));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        if(viewHolder instanceof AssetHeaderViewHolder) {
            ((AssetHeaderViewHolder) viewHolder).bind(mAsset, mTimeframe, mOnTabSelectedListener);
        } else if(viewHolder instanceof AssetBalanceViewHolder) {
            ((AssetBalanceViewHolder) viewHolder).bind((PortfolioAsset) mAsset);
        } else if(viewHolder instanceof AssetButtonsViewHolder) {
            ((AssetButtonsViewHolder) viewHolder).bind((mAsset instanceof PortfolioAsset), mOnClickListener);
        } else if(viewHolder instanceof AssetStatisticsViewHolder) {
            ((AssetStatisticsViewHolder) viewHolder).bind(mAsset);
        }
    }

    @Override
    public int getItemCount() {
        if(mAsset instanceof PortfolioAsset) {
            return 4;
        } else {
            return 3;
        }
    }

    public void setAsset(Asset asset) {
        mAsset = asset;
        notifyDataSetChanged();
    }

    public void setTimeframe(final int timeframe) {
        mTimeframe = timeframe;
        notifyDataSetChanged();
//        notifyItemChanged(ASSET_HEADER_POSITION);
    }
}