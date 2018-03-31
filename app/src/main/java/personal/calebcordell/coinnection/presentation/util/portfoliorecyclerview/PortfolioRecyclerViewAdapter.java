package personal.calebcordell.coinnection.presentation.util.portfoliorecyclerview;

import android.annotation.SuppressLint;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewCompat;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindColor;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import personal.calebcordell.coinnection.R;
import personal.calebcordell.coinnection.domain.model.Asset;
import personal.calebcordell.coinnection.domain.model.PortfolioAsset;
import personal.calebcordell.coinnection.domain.model.WatchlistAsset;
import personal.calebcordell.coinnection.presentation.Constants;
import personal.calebcordell.coinnection.presentation.Preferences;
import personal.calebcordell.coinnection.presentation.StringUtils;
import personal.calebcordell.coinnection.presentation.util.EmptyListItemViewHolder;
import personal.calebcordell.coinnection.presentation.util.ItemTouchHelperAdapter;
import personal.calebcordell.coinnection.presentation.util.OnObjectItemClickListener;


public class PortfolioRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements ItemTouchHelperAdapter {

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

    private OnObjectItemClickListener<Asset> mOnAssetItemClickListener;
    private AdapterView.OnItemSelectedListener mOnAssetInfoSelectedListener;
    private OnPortfolioItemDragFinishedListener mOnAssetDragFinishedListener;
    private TabLayout.OnTabSelectedListener mOnTimeframeTabSelectedListener;

    private List<PortfolioAsset> mPortfolioAssets = new ArrayList<>();
    private int mPortfolioAssetsSize = 0;
    private List<WatchlistAsset> mWatchlistAssets = new ArrayList<>();
    private int mWatchlistAssetsSize = 0;
    private long mLastUpdated = 0;

    private int mAssetInfoShown = Constants.PORTFOLIO_LIST_ITEM_SHOW_PRICE;
    private int mTimeframe = Constants.TIMEFRAME_HOUR;

    private final Preferences mPreferences;

    @Inject
    public PortfolioRecyclerViewAdapter(Preferences preferences) {
        mPreferences = preferences;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == PORTFOLIO_HEADER_POSITION) {
            return TYPE_PORTFOLIO_HEADER;
        } else if (position == ASSETLIST_HEADER_POSITION) {
            return TYPE_ASSETLIST_HEADER;
        } else if (position > ASSETLIST_HEADER_POSITION && position < WATCHLIST_HEADER_POSITION) {
            return TYPE_ASSETLIST_ITEM;
        } else if (position == WATCHLIST_HEADER_POSITION) {
            return TYPE_WATCHLIST_HEADER;
        } else if (position > WATCHLIST_HEADER_POSITION && position < PORTFOLIO_FOOTER_POSITION) {
            return TYPE_WATCHLIST_ITEM;
        } else {
            return TYPE_PORTFOLIO_FOOTER;
        }
    }

    @Override @NonNull
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, final int viewType) {
        switch (viewType) {
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
            default:
                return new EmptyListItemViewHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.empty_view, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof PortfolioHeaderViewHolder) {
            ((PortfolioHeaderViewHolder) viewHolder).bind(mPortfolioAssets, mTimeframe, mOnTimeframeTabSelectedListener);
        } else if (viewHolder instanceof PortfolioAssetSectionHeaderViewHolder) {
            ((PortfolioAssetSectionHeaderViewHolder) viewHolder).bind(mPortfolioAssetsSize, mOnAssetInfoSelectedListener, mAssetInfoShown);
        } else if (viewHolder instanceof PortfolioAssetItemViewHolder) {
            ((PortfolioAssetItemViewHolder) viewHolder).bind(mPortfolioAssets.get(position - 2),
                    mOnAssetItemClickListener, mAssetInfoShown, mTimeframe);
        } else if (viewHolder instanceof WatchlistSectionHeaderViewHolder) {
            ((WatchlistSectionHeaderViewHolder) viewHolder).bind(mWatchlistAssetsSize, mOnAssetInfoSelectedListener, mAssetInfoShown);
        } else if (viewHolder instanceof WatchlistItemViewHolder) {
            ((WatchlistItemViewHolder) viewHolder).bind(mWatchlistAssets.get(position - 3 - mPortfolioAssetsSize),
                    mOnAssetItemClickListener, mAssetInfoShown, mTimeframe);
        } else if (viewHolder instanceof PortfolioFooterViewHolder) {
            ((PortfolioFooterViewHolder) viewHolder).bind(mLastUpdated);
        }
    }

    @Override
    public int getItemCount() {
        return mPortfolioAssetsSize + mWatchlistAssetsSize + 4;
    }


    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        if (fromPosition > ASSETLIST_HEADER_POSITION && fromPosition < WATCHLIST_HEADER_POSITION) {
            int from = fromPosition - ASSETLIST_HEADER_POSITION - 1;
            int to = toPosition - ASSETLIST_HEADER_POSITION - 1;
            Collections.swap(mPortfolioAssets, from, to);
            notifyItemMoved(fromPosition, toPosition);
            return true;
        } else if (fromPosition > WATCHLIST_HEADER_POSITION && fromPosition < PORTFOLIO_FOOTER_POSITION) {
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
        if(mOnAssetDragFinishedListener != null) {
            mOnAssetDragFinishedListener.onDragFinished(mPortfolioAssets, mWatchlistAssets);
        }
    }


    public void setPortfolioAssets(final List<PortfolioAsset> portfolioAssets) {
        mPortfolioAssets = portfolioAssets;
        mPortfolioAssetsSize = mPortfolioAssets.size();

        WATCHLIST_HEADER_POSITION = mPortfolioAssetsSize + 2;
        PORTFOLIO_FOOTER_POSITION = mPortfolioAssetsSize + mWatchlistAssetsSize + 3;

        notifyDataSetChanged();
    }
//    public void setPortfolioAssets(final List<PortfolioAsset> newPortfolioAssets) {
//        List<PortfolioAsset> oldList = new ArrayList<>(mPortfolioAssets);
//
//        Single.fromCallable(() -> DiffUtil.calculateDiff(new AssetDiffCallback(oldList, newPortfolioAssets)))
//                .subscribeOn(Schedulers.computation())
//                .doOnSuccess(__ -> {
//                    mPortfolioAssets.clear();
//                    mPortfolioAssets.addAll(newPortfolioAssets);
//                    mPortfolioAssetsSize = mPortfolioAssets.size();
//
//                    WATCHLIST_HEADER_POSITION = mPortfolioAssetsSize + 2;
//                    PORTFOLIO_FOOTER_POSITION = mPortfolioAssetsSize + mWatchlistAssetsSize + 3;
//                })
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(this::updateAdapterWithDiffResultPortfolioAssets);
//    }


    public void setWatchlistAssets(final List<WatchlistAsset> watchlistAssets) {
        mWatchlistAssets = watchlistAssets;
        mWatchlistAssetsSize = mWatchlistAssets.size();

        PORTFOLIO_FOOTER_POSITION = mPortfolioAssetsSize + mWatchlistAssetsSize + 3;

        notifyDataSetChanged();
    }
//    public void setWatchlistAssets(final List<WatchlistAsset> newWatchlistAssets) {
//        List<WatchlistAsset> oldList = new ArrayList<>(mWatchlistAssets);
//
//        Single.fromCallable(() -> DiffUtil.calculateDiff(new AssetDiffCallback(oldList, newWatchlistAssets)))
//                .subscribeOn(Schedulers.computation())
//                .doOnSuccess(__ -> {
//                    mWatchlistAssets.clear();
//                    mWatchlistAssets.addAll(newWatchlistAssets);
//                    mWatchlistAssetsSize = mWatchlistAssets.size();
//
//                    PORTFOLIO_FOOTER_POSITION = mPortfolioAssetsSize + mWatchlistAssetsSize + 3;
//                })
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(this::updateAdapterWithDiffResultWatchlistAssets);
//    }

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

        mWatchlistAssets.clear();
        mWatchlistAssetsSize = 0;

        WATCHLIST_HEADER_POSITION = 2;
        PORTFOLIO_FOOTER_POSITION = 3;

        notifyDataSetChanged();
    }

    public void setOnAssetItemClickListener(OnObjectItemClickListener<Asset> onAssetItemClickListener) {
        mOnAssetItemClickListener = onAssetItemClickListener;
    }

    public void setOnAssetDragFinishedListener(OnPortfolioItemDragFinishedListener onAssetDragFinishedListener) {
        mOnAssetDragFinishedListener = onAssetDragFinishedListener;
    }

    public void setOnAssetInfoSelectedListener(AdapterView.OnItemSelectedListener onAssetInfoSelectedListener) {
        mOnAssetInfoSelectedListener = onAssetInfoSelectedListener;
    }

    public void setOnTimeframeTabSelectedListener(TabLayout.OnTabSelectedListener onTimeframeTabSelectedListener) {
        mOnTimeframeTabSelectedListener = onTimeframeTabSelectedListener;
    }



    protected class PortfolioHeaderViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.total_balance_text) TextView mTotalBalanceTextView;
//          @BindView(R.id.total_balance_change_text) TextView mTotalBalanceChangeTextView
//          @BindView(R.id.tab_layout) TabLayout mTabLayout;
//          @BindView(R.id.chart) LineChart mChart;
        @BindView(R.id.hour_change) TextView hourChange;
        @BindView(R.id.day_change) TextView dayChange;
        @BindView(R.id.week_change) TextView weekChange;

        @BindString(R.string.one_hour_change_text) String mHourChangeText;
        @BindString(R.string.one_day_change_text) String mDayChangeText;
        @BindString(R.string.one_week_change_text) String mWeekChangeText;

        @BindColor(R.color.colorPrimary) int mGraphLineColor;
        @BindColor(R.color.colorCardBackgroundLight) int mCardBackgroundColor;
        @BindColor(R.color.colorGraphFillLight) int mGraphFillColor;
        @BindColor(R.color.colorTextBlackPrimary) int mGraphHighlightColor;
        @BindColor(R.color.colorPositiveNumber) int positive;
        @BindColor(R.color.colorNegativeNumber) int negative;


        PortfolioHeaderViewHolder(final View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);

            /*
            TabLayout.Tab hourTab = mTabLayout.newTab().setText(R.string.one_hour_abbreviation_label);
            mTabLayout.addTab(hourTab);

            TabLayout.Tab dayTab = mTabLayout.newTab().setText(R.string.twenty_four_hour_abbreviation_label);
            mTabLayout.addTab(dayTab);

            TabLayout.Tab weekTab = mTabLayout.newTab().setText(R.string.seven_day_abbreviation_label);
            mTabLayout.addTab(weekTab);

            mChart.setViewPortOffsets(0f, 0f, 0f, 0f);
            mChart.setDrawBorders(false);
            mChart.setDrawGridBackground(false);
            mChart.setScaleEnabled(false);
            mChart.getLegend().setEnabled(false);
            mChart.getDescription().setEnabled(false);
            mChart.getXAxis().setEnabled(false);
            mChart.getAxisLeft().setEnabled(false);
            mChart.getAxisRight().setEnabled(false);
            mChart.invalidate();
             */
        }


        public void bind(final List<PortfolioAsset> portfolioAssets, final int timeframe, final TabLayout.OnTabSelectedListener onTabSelectedListener) {
            double totalBalanceTemp = 0;
            double totalBalanceHourAgo = 0;
            double totalBalanceDayAgo = 0;
            double totalBalanceWeekAgo = 0;

            for (PortfolioAsset asset : portfolioAssets) {
                final double currentCryptoFiatBalance = asset.getPrice() * asset.getBalance();

                totalBalanceTemp += currentCryptoFiatBalance;
                totalBalanceHourAgo += (currentCryptoFiatBalance / (1.00 + asset.getPercentChange1Hour()));
                totalBalanceDayAgo += (currentCryptoFiatBalance / (1.00 + asset.getPercentChange24Hour()));
                totalBalanceWeekAgo += (currentCryptoFiatBalance / (1.00 + asset.getPercentChange7Day()));
            }

            final double totalBalance = totalBalanceTemp;

            mTotalBalanceTextView.setText(StringUtils.getFormattedCurrencyString(totalBalance, mPreferences.getCurrencyCode()));

            final double portfolioPercentChangeHour = (totalBalance - totalBalanceHourAgo) / totalBalanceHourAgo;
            hourChange.setText(StringUtils.getFormattedPercentString(portfolioPercentChangeHour));
            if (portfolioPercentChangeHour < 0) {
                hourChange.setTextColor(negative);
            } else {
                hourChange.setTextColor(positive);
            }

            final double portfolioPercentChangeDay = (totalBalance - totalBalanceDayAgo) / totalBalanceDayAgo;
            dayChange.setText(StringUtils.getFormattedPercentString(portfolioPercentChangeDay));
            if (portfolioPercentChangeDay < 0) {
                dayChange.setTextColor(negative);
            } else {
                dayChange.setTextColor(positive);
            }

            final double portfolioPercentChangeWeek = (totalBalance - totalBalanceWeekAgo) / totalBalanceWeekAgo;
            weekChange.setText(StringUtils.getFormattedPercentString(portfolioPercentChangeWeek));
            if (portfolioPercentChangeWeek < 0) {
                weekChange.setTextColor(negative);
            } else {
                weekChange.setTextColor(positive);
            }
        }

        /*
        public void bind(final List<PortfolioAsset> portfolioAssets, final int timeframe, final TabLayout.OnTabSelectedListener onTabSelectedListener) {
            double totalBalanceTemp = 0;
            double totalBalanceHourAgo = 0;
            double totalBalanceDayAgo = 0;
            double totalBalanceWeekAgo = 0;

            for(PortfolioAsset asset : portfolioAssets) {
                final double currentCryptoFiatBalance = asset.getPrice() * asset.getBalance();

                totalBalanceTemp += currentCryptoFiatBalance;
                totalBalanceHourAgo += (currentCryptoFiatBalance / (1.00 + asset.getPercentChange1Hour()));
                totalBalanceDayAgo += (currentCryptoFiatBalance / (1.00 + asset.getPercentChange24Hour()));
                totalBalanceWeekAgo += (currentCryptoFiatBalance / (1.00 + asset.getPercentChange7Day()));
            }

            final double totalBalance = totalBalanceTemp;

            mTotalBalanceTextView.setText(StringUtils.getFormattedCurrencyString(totalBalance));

            float[] labels;
            float[] values;


            final double portfolioValueChange;
            final double portfolioPercentChange;
            final String portfolioChangeText;
            switch(timeframe) {
                case Constants.TIMEFRAME_HOUR:
                    portfolioValueChange = totalBalance - totalBalanceHourAgo;
                    portfolioPercentChange = portfolioValueChange / totalBalanceHourAgo;
                    portfolioChangeText = mHourChangeText;
                    values = new float[] {460.64f, 461.89f, 462.79f, 464.49f, 462.76f, 465.56f, 467.25f, 468.79f, 464.49f, 470.76f, 472.56f, 467.25f};
                    labels = new float[] {1f, 2f, 3f, 4f, 5f, 6f, 7f, 8f, 9f, 10f, 11f, 12f};
                    break;
                case Constants.TIMEFRAME_DAY:
                    portfolioValueChange = totalBalance - totalBalanceDayAgo;
                    portfolioPercentChange = portfolioValueChange / totalBalanceDayAgo;
                    portfolioChangeText = mDayChangeText;
                    values = new float[] {467.25f, 468.79f, 464.49f, 470.76f, 472.56f, 467.25f, 460.64f, 461.89f, 462.79f, 464.49f, 462.76f, 465.56f};
                    labels = new float[] {1f, 2f, 3f, 4f, 5f, 6f, 7f, 8f, 9f, 10f, 11f, 12f};
                    break;
                case Constants.TIMEFRAME_WEEK:
                    portfolioValueChange = totalBalance - totalBalanceWeekAgo;
                    portfolioPercentChange = portfolioValueChange / totalBalanceWeekAgo;
                    portfolioChangeText = mWeekChangeText;
                    values = new float[] {460.64f, 471.89f, 478.79f, 474.49f, 432.76f, 475.56f, 407.25f};
                    labels = new float[] {1f, 2f, 3f, 4f, 5f, 6f, 7f};
                    break;
                case Constants.TIMEFRAME_MONTH:
                    portfolioValueChange = totalBalance - totalBalanceMonthAgo;
                    portfolioPercentChange = portfolioValueChange / totalBalanceMonthAgo;
                    portfolioChangeText = mMonthChangeText;
                    break;
                case Constants.TIMEFRAME_YEAR:
                    portfolioValueChange = totalBalance - totalBalanceYearAgo;
                    portfolioPercentChange = portfolioValueChange / totalBalanceYearAgo;
                    portfolioChangeText = mYearChangeText;
                    break;
                default:
                    portfolioValueChange = totalBalance - totalBalanceHourAgo;
                    portfolioPercentChange = portfolioValueChange / totalBalanceHourAgo;
                    portfolioChangeText = mHourChangeText;
                    values = new float[] {460.64f, 461.89f, 462.79f, 464.49f, 462.76f, 465.56f, 467.25f, 468.79f, 464.49f, 470.76f, 472.56f, 467.25f};
                    labels = new float[] {1f, 2f, 3f, 4f, 5f, 6f, 7f, 8f, 9f, 10f, 11f, 12f};
                    break;
            }

            final String changeDirection;
            if(portfolioValueChange <= 0) {
                changeDirection = "";
            } else {
                changeDirection = "+";
            }
            mTotalBalanceChangeTextView.setText(String.format("%s%s (%s%s) %s", changeDirection,
                    StringUtils.getFormattedCurrencyString(portfolioValueChange), changeDirection,
                    StringUtils.getFormattedPercentString(portfolioPercentChange), portfolioChangeText));

            mTabLayout.clearOnTabSelectedListeners();

            if(onTabSelectedListener != null) {
                mTabLayout.addOnTabSelectedListener(onTabSelectedListener);
            }


            int[] attrs = {R.attr.colorPrimary, R.attr.colorCardBackground, R.attr.colorGraphFill, R.attr.colorTextOverBackgroundPrimary};
            TypedArray styles = App.getApp().obtainStyledAttributes(App.getApp().getAppThemeStyleAttr(), attrs);

            int graphLineColor = styles.getColor(0, mGraphLineColor);
            int cardBackgroundColor = styles.getColor(1, mCardBackgroundColor);
            int graphFillColor = styles.getColor(2, mGraphFillColor);
            int graphHighlightColor = styles.getColor(3, mGraphHighlightColor);
            styles.recycle();

            //Chart setup
            List<Entry> entries = new ArrayList<>(values.length);
            for(int i=0; i<values.length; i++) {
                entries.add(new Entry(labels[i], values[i]));
            }

            LineDataSet dataset = new LineDataSet(entries, "");

            dataset.setDrawCircles(false);
            dataset.setDrawValues(false);

            dataset.setLineWidth(4);
            dataset.setColor(graphLineColor);
            dataset.setMode(LineDataSet.Mode.CUBIC_BEZIER);

            dataset.setDrawFilled(true);
            dataset.setFillAlpha(75);
            dataset.setFillColor(graphFillColor);

            dataset.setHighlightLineWidth(2);
            dataset.setHighLightColor(graphHighlightColor);
            dataset.setDrawHorizontalHighlightIndicator(false);

            LineData lineData = new LineData(dataset);
            lineData.setDrawValues(false);
            mChart.setData(lineData);

            mChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
                @Override
                public void onValueSelected(Entry e, Highlight h) {
                    mTotalBalanceTextView.setText(""+e.getY());
                    mTotalBalanceChangeTextView.setText(""+e.getX());
                }
                @Override
                public void onNothingSelected() {
                    mTotalBalanceTextView.setText(StringUtils.getFormattedCurrencyString(totalBalance));
                    mTotalBalanceChangeTextView.setText(String.format("%s%s (%s%s) %s", changeDirection,
                            StringUtils.getFormattedCurrencyString(portfolioValueChange), changeDirection,
                            StringUtils.getFormattedPercentString(portfolioPercentChange), portfolioChangeText));
                }
            });

            mChart.notifyDataSetChanged();
        }
        */
    }
    protected class PortfolioAssetItemViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.asset_logo)
        ImageView mAssetLogo;
        @BindView(R.id.asset_name)
        TextView mAssetNameTextView;
        @BindView(R.id.asset_balance)
        TextView mAssetBalanceTextView;
        @BindView(R.id.asset_info_field)
        TextView mAssetInfoFieldTextView;

        @BindColor(R.color.colorTextBlackPrimary)
        int mUpdatedColorLight;
        @BindColor(R.color.colorTextBlackSecondary)
        int mUpdatingColorLight;
        @BindColor(R.color.colorPositiveNumber)
        int mPositiveColor;
        @BindColor(R.color.colorNegativeNumber)
        int mNegativeColor;

        PortfolioAssetItemViewHolder(final View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }

        public void bind(final PortfolioAsset portfolioAsset, final OnObjectItemClickListener<Asset> onObjectItemClickListener, final int infoShown, final int percentChangeTimeframe) {
            Glide.with(itemView.getContext()).load(portfolioAsset.getLogo()).into(mAssetLogo);
            mAssetNameTextView.setText(portfolioAsset.getName());
            mAssetBalanceTextView.setText(StringUtils.getFormattedNumberString(portfolioAsset.getBalance()) + " " + portfolioAsset.getSymbol());

            //Get the correct percent change based on the timeframe
            double percentChange;
            switch (percentChangeTimeframe) {
                case Constants.TIMEFRAME_HOUR:
                    percentChange = portfolioAsset.getPercentChange1Hour();
                    break;
                case Constants.TIMEFRAME_DAY:
                    percentChange = portfolioAsset.getPercentChange24Hour();
                    break;
                case Constants.TIMEFRAME_WEEK:
                    percentChange = portfolioAsset.getPercentChange7Day();
                    break;
                case Constants.TIMEFRAME_MONTH:
                    percentChange = 0;
                    break;
                case Constants.TIMEFRAME_YEAR:
                    percentChange = 0;
                    break;
                case Constants.TIMEFRAME_ALL:
                    percentChange = 0;
                    break;
                default:
                    percentChange = portfolioAsset.getPercentChange1Hour();
                    break;
            }

            //Determine which info to show in the AssetInfoFieldTextView
            switch (Constants.PORTFOLIO_LIST_ITEM_INFO[infoShown]) {
                case Constants.PORTFOLIO_LIST_ITEM_SHOW_PRICE:
                    mAssetInfoFieldTextView.setText(StringUtils.getFormattedCurrencyString(portfolioAsset.getPrice(), mPreferences.getCurrencyCode()));
                    break;
                case Constants.PORTFOLIO_LIST_ITEM_SHOW_PERCENT_CHANGE:
                    mAssetInfoFieldTextView.setText(StringUtils.getFormattedPercentString(percentChange));
                    break;
                case Constants.PORTFOLIO_LIST_ITEM_SHOW_BALANCE_VALUE:
                    mAssetInfoFieldTextView.setText(StringUtils.getFormattedCurrencyString(portfolioAsset.getBalance() * portfolioAsset.getPrice(), mPreferences.getCurrencyCode()));
                    break;
                default:
                    mAssetInfoFieldTextView.setText(StringUtils.getFormattedCurrencyString(portfolioAsset.getPrice(), mPreferences.getCurrencyCode()));
                    break;
            }


            //Set text color based on whether data is current/out-of-date and positive/negative
            int[] attrs = {R.attr.colorTextOverBackgroundPrimary, R.attr.colorTextOverBackgroundSecondary};
            TypedArray styles = itemView.getContext().getApplicationContext().obtainStyledAttributes(mPreferences.getAppThemeStyleAttr(), attrs);

            int mUpdatedColor = styles.getColor(0, mUpdatedColorLight);
            @SuppressLint("ResourceType") int mUpdatingColor = styles.getColor(1, mUpdatingColorLight);
            styles.recycle();

            if (portfolioAsset.isUpToDate(Constants.RELOAD_TIME_SINGLE_ASSET)) {
                mAssetNameTextView.setTextColor(mUpdatedColor);
                if (percentChange >= 0) {
                    mAssetInfoFieldTextView.setTextColor(mPositiveColor);
                } else {
                    mAssetInfoFieldTextView.setTextColor(mNegativeColor);
                }
            } else {
                mAssetNameTextView.setTextColor(mUpdatingColor);
                mAssetInfoFieldTextView.setTextColor(mUpdatingColor);
            }

            if (onObjectItemClickListener != null) {
                itemView.setOnClickListener(
                        (view) -> ViewCompat.postOnAnimationDelayed(view,
                                () -> onObjectItemClickListener.onObjectItemClick(portfolioAsset),
                                Constants.SELECTABLE_VIEW_ANIMATION_DELAY)
                );
            }
        }
    }
    protected class WatchlistItemViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.asset_logo)
        ImageView mAssetLogo;
        @BindView(R.id.asset_name)
        TextView mAssetNameTextView;
        @BindView(R.id.asset_symbol)
        TextView mAssetSymbolTextView;
        @BindView(R.id.asset_info_field)
        TextView mAssetInfoFieldTextView;

        @BindColor(R.color.colorTextBlackPrimary)
        int mUpdatedColorLight;
        @BindColor(R.color.colorTextBlackSecondary)
        int mUpdatingColorLight;
        @BindColor(R.color.colorPositiveNumber)
        int mPositiveColor;
        @BindColor(R.color.colorNegativeNumber)
        int mNegativeColor;

        WatchlistItemViewHolder(final View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }

        public void bind(final Asset asset, final OnObjectItemClickListener<Asset> onObjectItemClickListener, final int infoShown, final int percentChangeTimeframe) {
            Glide.with(itemView.getContext())
                    .load(asset.getLogo())
                    .into(mAssetLogo);
            mAssetNameTextView.setText(asset.getName());
            mAssetSymbolTextView.setText(asset.getSymbol());

            //Get the correct percent change based on the timeframe
            double percentChange;
            switch (percentChangeTimeframe) {
                case Constants.TIMEFRAME_HOUR:
                    percentChange = asset.getPercentChange1Hour();
                    break;
                case Constants.TIMEFRAME_DAY:
                    percentChange = asset.getPercentChange24Hour();
                    break;
                case Constants.TIMEFRAME_WEEK:
                    percentChange = asset.getPercentChange7Day();
                    break;
                case Constants.TIMEFRAME_MONTH:
                    percentChange = 0;
                    break;
                case Constants.TIMEFRAME_YEAR:
                    percentChange = 0;
                    break;
                case Constants.TIMEFRAME_ALL:
                    percentChange = 0;
                    break;
                default:
                    percentChange = asset.getPercentChange1Hour();
                    break;
            }

            switch (Constants.PORTFOLIO_LIST_ITEM_INFO[infoShown]) {
                case Constants.PORTFOLIO_LIST_ITEM_SHOW_PRICE:
                    mAssetInfoFieldTextView.setText(StringUtils.getFormattedCurrencyString(asset.getPrice(), mPreferences.getCurrencyCode()));
                    break;
                case Constants.PORTFOLIO_LIST_ITEM_SHOW_PERCENT_CHANGE:
                    mAssetInfoFieldTextView.setText(StringUtils.getFormattedPercentString(percentChange));
                    break;
                case Constants.PORTFOLIO_LIST_ITEM_SHOW_BALANCE_VALUE:
                    mAssetInfoFieldTextView.setText(StringUtils.getFormattedCurrencyString(0, mPreferences.getCurrencyCode()));
                    break;
                default:
                    mAssetInfoFieldTextView.setText(StringUtils.getFormattedCurrencyString(asset.getPrice(), mPreferences.getCurrencyCode()));
                    break;
            }

            int[] attrs = {R.attr.colorTextOverBackgroundPrimary, R.attr.colorTextOverBackgroundSecondary};
            TypedArray styles = itemView.getContext().getApplicationContext().obtainStyledAttributes(mPreferences.getAppThemeStyleAttr(), attrs);


            int mUpdatedColor = styles.getColor(0, mUpdatedColorLight);
            @SuppressLint("ResourceType") int mUpdatingColor = styles.getColor(1, mUpdatingColorLight);
            styles.recycle();


            if (asset.isUpToDate(Constants.RELOAD_TIME_SINGLE_ASSET)) {
                mAssetNameTextView.setTextColor(mUpdatedColor);
                if (percentChange >= 0) {
                    mAssetInfoFieldTextView.setTextColor(mPositiveColor);
                } else {
                    mAssetInfoFieldTextView.setTextColor(mNegativeColor);
                }
            } else {
                mAssetNameTextView.setTextColor(mUpdatingColor);
                mAssetInfoFieldTextView.setTextColor(mUpdatingColor);
            }


            if (onObjectItemClickListener != null) {
                itemView.setOnClickListener((view) ->
                        ViewCompat.postOnAnimationDelayed(view, () ->
                                onObjectItemClickListener.onObjectItemClick(asset), Constants.SELECTABLE_VIEW_ANIMATION_DELAY));
            }
        }
    }
}