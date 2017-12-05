package personal.calebcordell.coinnection.presentation.util.portfoliorecyclerview;

import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.AdapterView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import personal.calebcordell.coinnection.R;
import personal.calebcordell.coinnection.data.PreferencesRepositoryImpl;
import personal.calebcordell.coinnection.domain.model.Asset;
import personal.calebcordell.coinnection.domain.model.PortfolioAsset;
import personal.calebcordell.coinnection.domain.model.WatchlistAsset;
import personal.calebcordell.coinnection.presentation.Constants;
import personal.calebcordell.coinnection.presentation.util.ItemTouchHelperAdapter;
import personal.calebcordell.coinnection.presentation.util.OnObjectItemClickListener;


public class PortfolioRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements ItemTouchHelperAdapter {
    private static final String TAG = PortfolioRecyclerViewAdapter.class.getSimpleName();

    private static final int TYPE_PORTFOLIO_HEADER = 0;
    private static final int TYPE_ASSETLIST_HEADER = 1;
    private static final int TYPE_ASSETLIST_ITEM = 2;
    private static final int TYPE_WATCHLIST_HEADER = 3;
    private static final int TYPE_WATCHLIST_ITEM = 4;
    private static final int TYPE_PORTFOLIO_FOOTER = 5;

    private static final int PORTFOLIO_HEADER_POSITION = 0;
    private static final int ASSETLIST_HEADER_POSITION = 1;
    private int WATCHLIST_HEADER_POSITION = 2;
    private int PORTFOLIO_FOOTER_POSITION = 3;

    private final OnObjectItemClickListener<Asset> mOnObjectItemClickListener;
    private final AdapterView.OnItemSelectedListener mOnItemSelectedListener;
    private final OnPortfolioItemDragFinishedListener mOnDragFinishedListener;
    private final TabLayout.OnTabSelectedListener mOnTabSelectedListener;

    private List<PortfolioAsset> mPortfolioAssets;
    private int mPortfolioAssetsSize;
    private List<WatchlistAsset> mWatchlistAssets;
    private int mWatchlistAssetsSize;
    private long mLastUpdated;

    private int mAssetInfoShown;
    private int mTimeframe;

    public PortfolioRecyclerViewAdapter(OnObjectItemClickListener<Asset> onObjectItemClickListener,
                                        AdapterView.OnItemSelectedListener onItemSelectedListener,
                                        OnPortfolioItemDragFinishedListener onDragFinishedListener,
                                        TabLayout.OnTabSelectedListener onTabSelectedListener) {
        this(new ArrayList<>(), new ArrayList<>(), onObjectItemClickListener, onItemSelectedListener,
                onDragFinishedListener, onTabSelectedListener, Constants.TIMEFRAME_HOUR, Constants.PORTFOLIO_LIST_ITEM_SHOW_PRICE);
    }
    public PortfolioRecyclerViewAdapter(List<PortfolioAsset> portfolioAssets, List<WatchlistAsset> watchlistAssets,
                                        OnObjectItemClickListener<Asset> onObjectItemClickListener,
                                        AdapterView.OnItemSelectedListener onItemSelectedListener,
                                        OnPortfolioItemDragFinishedListener onDragFinishedListener,
                                        TabLayout.OnTabSelectedListener onTabSelectedListener,
                                        int timeframe, int assetInfoShown) {
        mPortfolioAssets = portfolioAssets;
        mPortfolioAssetsSize = portfolioAssets.size();
        mWatchlistAssets = watchlistAssets;
        mWatchlistAssetsSize = watchlistAssets.size();

        mLastUpdated = PreferencesRepositoryImpl.getInstance().getLastFullUpdate();

        WATCHLIST_HEADER_POSITION = mPortfolioAssetsSize + 2;

        mOnObjectItemClickListener = onObjectItemClickListener;
        mOnItemSelectedListener = onItemSelectedListener;
        mOnDragFinishedListener = onDragFinishedListener;
        mOnTabSelectedListener = onTabSelectedListener;

        mTimeframe = timeframe;
        mAssetInfoShown = assetInfoShown;
    }

    @Override
    public int getItemViewType(int position) {
        if(position == PORTFOLIO_HEADER_POSITION) {
            return TYPE_PORTFOLIO_HEADER;
        } else if(position == ASSETLIST_HEADER_POSITION) {
            return TYPE_ASSETLIST_HEADER;
        } else if(position > ASSETLIST_HEADER_POSITION && position < WATCHLIST_HEADER_POSITION) {
            return TYPE_ASSETLIST_ITEM;
        } else if(position == WATCHLIST_HEADER_POSITION) {
            return TYPE_WATCHLIST_HEADER;
        } else if(position > WATCHLIST_HEADER_POSITION && position < PORTFOLIO_FOOTER_POSITION) {
            return TYPE_WATCHLIST_ITEM;
        } else {
            return TYPE_PORTFOLIO_FOOTER;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, final int viewType) {
        switch(viewType) {
            case TYPE_PORTFOLIO_HEADER:
                return new PortfolioHeaderViewHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.card_view_portfolio_header, parent, false));
            case TYPE_ASSETLIST_HEADER:
                return new PortfolioAssetSectionHeaderViewHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.card_view_portfolio_asset_section_header, parent, false));
            case TYPE_ASSETLIST_ITEM:
                return new PortfolioAssetItemViewHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.card_view_portfolio_asset_item, parent, false));
            case TYPE_WATCHLIST_HEADER:
                return new WatchlistSectionHeaderViewHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.card_view_portfolio_watchlist_section_header, parent, false));
            case TYPE_WATCHLIST_ITEM:
                return new WatchlistItemViewHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.card_view_portfolio_watchlist_item, parent, false));
            case TYPE_PORTFOLIO_FOOTER:
                return new PortfolioFooterViewHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.card_view_portfolio_footer, parent, false));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        if(viewHolder instanceof PortfolioHeaderViewHolder) {
            ((PortfolioHeaderViewHolder) viewHolder).bind(mPortfolioAssets, mTimeframe, mOnTabSelectedListener);
        } else if(viewHolder instanceof PortfolioAssetSectionHeaderViewHolder) {
            ((PortfolioAssetSectionHeaderViewHolder) viewHolder).bind(mPortfolioAssetsSize, mOnItemSelectedListener, mAssetInfoShown);
        } else if(viewHolder instanceof PortfolioAssetItemViewHolder) {
            ((PortfolioAssetItemViewHolder) viewHolder).bind(mPortfolioAssets.get(position - 2),
                    mOnObjectItemClickListener, mAssetInfoShown, mTimeframe);
        } else if(viewHolder instanceof WatchlistSectionHeaderViewHolder) {
            ((WatchlistSectionHeaderViewHolder) viewHolder).bind(mWatchlistAssetsSize, mOnItemSelectedListener, mAssetInfoShown);
        } else if(viewHolder instanceof WatchlistItemViewHolder) {
            ((WatchlistItemViewHolder) viewHolder).bind(mWatchlistAssets.get(position - 3 - mPortfolioAssetsSize),
                    mOnObjectItemClickListener, mAssetInfoShown, mTimeframe);
        } else if(viewHolder instanceof PortfolioFooterViewHolder) {
            ((PortfolioFooterViewHolder) viewHolder).bind(mLastUpdated);
        }
    }

    @Override
    public int getItemCount() {
        return mPortfolioAssetsSize + mWatchlistAssetsSize + 4;
    }


    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        if(fromPosition > ASSETLIST_HEADER_POSITION && fromPosition < WATCHLIST_HEADER_POSITION) {
            int from = fromPosition - ASSETLIST_HEADER_POSITION - 1;
            int to = toPosition - ASSETLIST_HEADER_POSITION - 1;
            Collections.swap(mPortfolioAssets, from, to);
            notifyItemMoved(fromPosition, toPosition);
            return true;
        } else if(fromPosition > WATCHLIST_HEADER_POSITION && fromPosition < PORTFOLIO_FOOTER_POSITION) {
            int from = fromPosition - WATCHLIST_HEADER_POSITION - 1;
            int to = toPosition - WATCHLIST_HEADER_POSITION - 1;
            Collections.swap(mWatchlistAssets, from, to);
            notifyItemMoved(fromPosition, toPosition);
            return true;
        }
        return false;
    }

    @Override
    public void onItemDismiss(int position) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void onDragFinished() {
        mOnDragFinishedListener.onDragFinished(mPortfolioAssets, mWatchlistAssets);
    }


    public void setPortfolioAssets(final List<PortfolioAsset> newPortfolioAssets) {
        mPortfolioAssets.clear();
        mPortfolioAssets.addAll(newPortfolioAssets);
        mPortfolioAssetsSize = mPortfolioAssets.size();

        WATCHLIST_HEADER_POSITION = mPortfolioAssetsSize + 2;
        PORTFOLIO_FOOTER_POSITION = mPortfolioAssetsSize + mWatchlistAssetsSize + 3;

        notifyItemRangeChanged(ASSETLIST_HEADER_POSITION, ASSETLIST_HEADER_POSITION + mPortfolioAssetsSize);
        notifyItemChanged(PORTFOLIO_HEADER_POSITION);
    }
    public void replacePortfolioAssets(final List<PortfolioAsset> newPortfolioAssets) {
        List<PortfolioAsset> oldList = new ArrayList<>(mPortfolioAssets);

        Single.fromCallable(() -> DiffUtil.calculateDiff(new AssetDiffCallback(oldList, newPortfolioAssets)))
                .subscribeOn(Schedulers.newThread())
                .doOnSuccess(__ -> {
                    mPortfolioAssets.clear();
                    mPortfolioAssets.addAll(newPortfolioAssets);
                    mPortfolioAssetsSize = mPortfolioAssets.size();

                    WATCHLIST_HEADER_POSITION = mPortfolioAssetsSize + 2;
                    PORTFOLIO_FOOTER_POSITION = mPortfolioAssetsSize + mWatchlistAssetsSize + 3;
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::updateAdapterWithDiffResultPortfolioAssets);
    }


    public void setWatchlistAssets(final List<WatchlistAsset> watchlistAssets) {
        mWatchlistAssets.clear();
        notifyDataSetChanged();
        mWatchlistAssets.addAll(watchlistAssets);
        mWatchlistAssetsSize = mWatchlistAssets.size();

        PORTFOLIO_FOOTER_POSITION = mPortfolioAssetsSize + mWatchlistAssetsSize + 3;

        notifyItemRangeChanged(WATCHLIST_HEADER_POSITION, WATCHLIST_HEADER_POSITION + mWatchlistAssetsSize);
    }
    public void replaceWatchlistAssets(final List<WatchlistAsset> newWatchlistAssets) {
        List<WatchlistAsset> oldList = new ArrayList<>(mWatchlistAssets);

        Single.fromCallable(() -> DiffUtil.calculateDiff(new AssetDiffCallback(oldList, newWatchlistAssets)))
                .subscribeOn(Schedulers.newThread())
                .doOnSuccess(__ -> {
                    mWatchlistAssets.clear();
                    mWatchlistAssets.addAll(newWatchlistAssets);
                    mWatchlistAssetsSize = mWatchlistAssets.size();

                    PORTFOLIO_FOOTER_POSITION = mPortfolioAssetsSize + mWatchlistAssetsSize + 3;
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::updateAdapterWithDiffResultWatchlistAssets);
    }

    public void setLastUpdated(long lastUpdated) {
        mLastUpdated = lastUpdated;
        notifyItemChanged(PORTFOLIO_FOOTER_POSITION);
    }

    public void setTimeframe(@Constants.Timeframe int timeframe) {
        mTimeframe = timeframe;
        notifyDataSetChanged();
    }

    public void setAssetInfoShown(int assetInfoShown) {
        mAssetInfoShown = assetInfoShown;
        notifyDataSetChanged();
    }


    private void updateAdapterWithDiffResultPortfolioAssets(@NonNull final DiffUtil.DiffResult diffResult) {
        notifyItemChanged(PORTFOLIO_HEADER_POSITION);
        notifyItemChanged(ASSETLIST_HEADER_POSITION);
        diffResult.dispatchUpdatesTo(new OffsetListUpdateCallback(this, ASSETLIST_HEADER_POSITION + 1));
    }
    private void updateAdapterWithDiffResultWatchlistAssets(@NonNull final DiffUtil.DiffResult diffResult) {
        notifyItemChanged(WATCHLIST_HEADER_POSITION);
        diffResult.dispatchUpdatesTo(new OffsetListUpdateCallback(this, WATCHLIST_HEADER_POSITION + 1));
    }

    public void clear() {
        mPortfolioAssets.clear();
        mPortfolioAssetsSize = 0;

        WATCHLIST_HEADER_POSITION = mPortfolioAssetsSize + 2;
        PORTFOLIO_FOOTER_POSITION = mPortfolioAssetsSize + mWatchlistAssetsSize + 3;

        mWatchlistAssets.clear();
        mWatchlistAssetsSize = 0;

        notifyDataSetChanged();
    }
}