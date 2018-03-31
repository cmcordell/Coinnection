package personal.calebcordell.coinnection.presentation.util.assetrecyclerview;

import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

import javax.inject.Inject;

import butterknife.BindColor;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import personal.calebcordell.coinnection.R;
import personal.calebcordell.coinnection.domain.model.Asset;
import personal.calebcordell.coinnection.domain.model.AssetPair;
import personal.calebcordell.coinnection.domain.model.PortfolioAsset;
import personal.calebcordell.coinnection.presentation.Constants;
import personal.calebcordell.coinnection.presentation.Preferences;
import personal.calebcordell.coinnection.presentation.StringUtils;
import personal.calebcordell.coinnection.presentation.util.EmptyListItemViewHolder;


public class AssetRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_ASSET_HEADER = 0;
    private static final int TYPE_ASSET_BUTTONS = 1;
    private static final int TYPE_ASSET_BALANCE_HEADER = 2;
    private static final int TYPE_ASSET_BALANCE = 3;
    private static final int TYPE_ASSET_STATISTICS_HEADER = 4;
    private static final int TYPE_ASSET_STATISTICS = 5;

    private Asset mAsset = new Asset();

    private int mTimeframe = Constants.TIMEFRAME_HOUR;

    private View.OnClickListener mOnClickListener;
    private TabLayout.OnTabSelectedListener mOnTabSelectedListener;

    private Preferences mPreferences;

    @Inject
    public AssetRecyclerViewAdapter(Preferences preferences) {
        mPreferences = preferences;
    }

    @Override
    public int getItemViewType(int position) {
        switch (position) {
            case 0:
                return TYPE_ASSET_HEADER;
            case 1:
                return TYPE_ASSET_BUTTONS;
            case 2:
                if (mAsset instanceof PortfolioAsset) {
                    return TYPE_ASSET_BALANCE_HEADER;
                } else {
                    return TYPE_ASSET_STATISTICS_HEADER;
                }
            case 3:
                if (mAsset instanceof PortfolioAsset) {
                    return TYPE_ASSET_BALANCE;
                } else {
                    return TYPE_ASSET_STATISTICS;
                }
            case 4:
                return TYPE_ASSET_STATISTICS_HEADER;
            case 5:
                return TYPE_ASSET_STATISTICS;
            default:
                return -1;
        }
    }

    @Override
    @NonNull
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, final int viewType) {
        switch (viewType) {
            case TYPE_ASSET_HEADER:
                return new AssetHeaderViewHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.card_view_asset_header, parent, false));
            case TYPE_ASSET_BUTTONS:
                return new AssetButtonsViewHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.card_view_asset_buttons, parent, false));
            case TYPE_ASSET_BALANCE_HEADER:
                return new AssetBalanceHeaderViewHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.card_view_asset_balance_header, parent, false));
            case TYPE_ASSET_BALANCE:
                return new AssetBalanceViewHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.card_view_asset_balance, parent, false));
            case TYPE_ASSET_STATISTICS_HEADER:
                return new AssetStatisticsHeaderViewHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.card_view_asset_statistics_header, parent, false));
            case TYPE_ASSET_STATISTICS:
                return new AssetStatisticsViewHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.card_view_asset_statistics, parent, false));
        }
        return new EmptyListItemViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.empty_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof AssetHeaderViewHolder) {
            ((AssetHeaderViewHolder) viewHolder).bind(mAsset, mTimeframe, mOnTabSelectedListener);
        } else if (viewHolder instanceof AssetButtonsViewHolder) {
            int assetType = Constants.ASSET_TYPE_ASSET;
            if (mAsset instanceof PortfolioAsset) {
                assetType = Constants.ASSET_TYPE_PORTFOLIO;
            } else if (mAsset instanceof AssetPair) {
                assetType = Constants.ASSET_TYPE_PAIR;
            }
            ((AssetButtonsViewHolder) viewHolder).bind(assetType, mOnClickListener);
        } else if (viewHolder instanceof AssetBalanceViewHolder) {
            ((AssetBalanceViewHolder) viewHolder).bind((PortfolioAsset) mAsset);
        } else if (viewHolder instanceof AssetStatisticsViewHolder) {
            ((AssetStatisticsViewHolder) viewHolder).bind(mAsset);
        }
    }

    @Override
    public int getItemCount() {
        if (mAsset instanceof PortfolioAsset) {
            return 6;
        } else {
            return 4;
        }
    }

    public void setAsset(Asset asset) {
        mAsset = asset;
        notifyDataSetChanged();
    }

    public void setTimeframe(final int timeframe) {
        mTimeframe = timeframe;
        notifyItemChanged(0);
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        mOnClickListener = onClickListener;
    }

    public void setOnTabSelectedListener(TabLayout.OnTabSelectedListener onTabSelectedListener) {
        mOnTabSelectedListener = onTabSelectedListener;
    }



    protected class AssetHeaderViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.asset_icon) ImageView mAssetIcon;
        @BindView(R.id.asset_price_text) TextView mAssetPriceTextView;
//          @BindView(R.id.asset_price_change_text) TextView mAssetPriceChangeTextView;
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

        AssetHeaderViewHolder(final View itemView) {
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

        public void bind(final Asset asset, final int timeframe, final TabLayout.OnTabSelectedListener onTabSelectedListener) {
            final double assetPrice = asset.getPrice();

            Glide.with(itemView.getContext())
                    .load(asset.getLogo())
                    .into(mAssetIcon);

            if (asset instanceof AssetPair) {
                mAssetPriceTextView.setText(StringUtils.getFormattedDecimalString(assetPrice));
            } else {
                mAssetPriceTextView.setText(StringUtils.getFormattedCurrencyString(assetPrice, mPreferences.getCurrencyCode()));

                final double pricePercentChangeHour = asset.getPercentChange1Hour();
                hourChange.setText(StringUtils.getFormattedPercentString(pricePercentChangeHour));
                if (pricePercentChangeHour < 0) {
                    hourChange.setTextColor(negative);
                } else {
                    hourChange.setTextColor(positive);
                }

                final double pricePercentChangeDay = asset.getPercentChange24Hour();
                dayChange.setText(StringUtils.getFormattedPercentString(pricePercentChangeDay));
                if (pricePercentChangeDay < 0) {
                    dayChange.setTextColor(negative);
                } else {
                    dayChange.setTextColor(positive);
                }

                final double pricePercentChangeWeek = asset.getPercentChange7Day();
                weekChange.setText(StringUtils.getFormattedPercentString(pricePercentChangeWeek));
                if (pricePercentChangeWeek < 0) {
                    weekChange.setTextColor(negative);
                } else {
                    weekChange.setTextColor(positive);
                }
            }
        }
        /*
        public void bind(final Asset asset, final int timeframe, final TabLayout.OnTabSelectedListener onTabSelectedListener) {
            final double assetPrice = asset.getPrice();

            mAssetPriceTextView.setText(StringUtils.getFormattedCurrencyString(assetPrice));

            final double assetValueChange;
            final double assetPercentChange;
            final String assetChangeText;

            float[] labels;
            float[] values;

            switch(timeframe) {
                case Constants.TIMEFRAME_HOUR:
                    assetPercentChange = asset.getPercentChange1Hour();
                    assetValueChange = assetPrice - (assetPrice / (1.00 + assetPercentChange));
                    assetChangeText = mHourChangeText;

                    //Test Values
                    values = new float[] {460.64f, 461.89f, 462.79f, 464.49f, 462.76f, 465.56f, 467.25f, 468.79f, 464.49f, 470.76f, 472.56f, 467.25f};
                    labels = new float[] {1f, 2f, 3f, 4f, 5f, 6f, 7f, 8f, 9f, 10f, 11f, 12f};
                    break;
                case Constants.TIMEFRAME_DAY:
                    assetPercentChange = asset.getPercentChange24Hour();
                    assetValueChange = assetPrice - (assetPrice / (1.00 + assetPercentChange));
                    assetChangeText = mDayChangeText;

                    //Test Values
                    values = new float[] {467.25f, 468.79f, 464.49f, 470.76f, 472.56f, 467.25f, 460.64f, 461.89f, 462.79f, 464.49f, 462.76f, 465.56f};
                    labels = new float[] {1f, 2f, 3f, 4f, 5f, 6f, 7f, 8f, 9f, 10f, 11f, 12f};
                    break;
                case Constants.TIMEFRAME_WEEK:
                    assetPercentChange = asset.getPercentChange7Day();
                    assetValueChange = assetPrice - (assetPrice / (1.00 + assetPercentChange));
                    assetChangeText = mWeekChangeText;

                    //Test Values
                    values = new float[] {460.64f, 471.89f, 478.79f, 474.49f, 432.76f, 475.56f, 407.25f};
                    labels = new float[] {1f, 2f, 3f, 4f, 5f, 6f, 7f};
                    break;
                case Constants.TIMEFRAME_MONTH:
                    assetPercentChange = asset.getPercentChange1Month();
                    assetValueChange = assetPrice - (assetPrice / (1.00 + assetPercentChange));
                    assetChangeText = mMonthChangeText;
                    break;
                case Constants.TIMEFRAME_YEAR:
                    assetPercentChange = asset.getPercentChange1Year();
                    assetValueChange = assetPrice - (assetPrice / (1.00 + assetPercentChange));
                    assetChangeText = mYearChangeText;
                    break;
                default:
                    assetPercentChange = asset.getPercentChange1Hour();
                    assetValueChange = assetPrice - (assetPrice / (1.00 + assetPercentChange));
                    assetChangeText = mHourChangeText;

                    //Test Values
                    values = new float[] {460.64f, 461.89f, 462.79f, 464.49f, 462.76f, 465.56f, 467.25f, 468.79f, 464.49f, 470.76f, 472.56f, 467.25f};
                    labels = new float[] {1f, 2f, 3f, 4f, 5f, 6f, 7f, 8f, 9f, 10f, 11f, 12f};
                    break;
            }

            final String changeDirection;
            if(assetValueChange <= 0) {
                changeDirection = "";
            } else {
                changeDirection = "+";
            }

            mAssetPriceChangeTextView.setText(String.format("%s%s (%s%s) %s", changeDirection,
                    StringUtils.getFormattedCurrencyString(assetValueChange), changeDirection,
                    StringUtils.getFormattedPercentString(assetPercentChange), assetChangeText));

            mTabLayout.clearOnTabSelectedListeners();
            mTabLayout.addOnTabSelectedListener(onTabSelectedListener);


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
                    mAssetPriceTextView.setText(""+e.getY());
                    mAssetPriceChangeTextView.setText(""+e.getX());
                }
                @Override
                public void onNothingSelected() {
                    mAssetPriceTextView.setText(StringUtils.getFormattedCurrencyString(assetPrice));
                    mAssetPriceChangeTextView.setText(String.format("%s%s (%s%s) %s", changeDirection,
                            StringUtils.getFormattedCurrencyString(assetValueChange), changeDirection,
                            StringUtils.getFormattedPercentString(assetPercentChange), assetChangeText));
                }
            });

            mChart.notifyDataSetChanged();
        }
        */
    }
    protected class AssetBalanceViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.fiat_balance_text_view)
        TextView mFiatBalanceTextView;
        @BindView(R.id.asset_balance_text_view)
        TextView mAssetBalanceTextView;

        AssetBalanceViewHolder(final View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }

        public void bind(PortfolioAsset asset) {
            MathContext mathContext = new MathContext(4, RoundingMode.DOWN);
            BigDecimal bigDecimal;

            double assetBalance = asset.getBalance();
            //Set Balance Fiat
            double fiatBalance = (assetBalance * asset.getPrice());
            if (fiatBalance < 1.0) {
                bigDecimal = new BigDecimal(fiatBalance, mathContext);
                mFiatBalanceTextView.setText(StringUtils.getFormattedCurrencyString(bigDecimal.doubleValue(), mPreferences.getCurrencyCode()));
            } else {
                mFiatBalanceTextView.setText(StringUtils.getFormattedCurrencyString(fiatBalance, mPreferences.getCurrencyCode()));
            }

            if (assetBalance < 1.0) {
                bigDecimal = new BigDecimal(assetBalance, mathContext);
                mAssetBalanceTextView.setText(String.format("%s %s", StringUtils.getFormattedNumberString(bigDecimal.doubleValue()), asset.getSymbol()));
            } else {
                mAssetBalanceTextView.setText(String.format("%s %s", StringUtils.getFormattedNumberString(assetBalance), asset.getSymbol()));
            }
        }
    }
    protected class AssetStatisticsViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.rank_text_view)
        TextView mRankTextView;
        @BindView(R.id.market_cap_text_view)
        TextView mMarketCapTextView;
        @BindView(R.id.volume_24_hour_text_view)
        TextView mVolume24HourTextView;
        @BindView(R.id.available_supply_text_view)
        TextView mAvailableSupplyTextView;
        @BindView(R.id.total_supply_text_view)
        TextView mTotalSupplyTextView;
        @BindView(R.id.max_supply_text_view)
        TextView mMaxSupplyTextView;

        AssetStatisticsViewHolder(final View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }

        public void bind(Asset asset) {
            mRankTextView.setText(String.format("#%s", StringUtils.getFormattedNumberString(asset.getRank())));

            if (asset instanceof AssetPair) {
                mMarketCapTextView.setText(String.format("%s %s", StringUtils.getFormattedDecimalString(asset.getMarketCap()), ((AssetPair) asset).getQuoteCurrencySymbol()));
                mVolume24HourTextView.setText(String.format("%s %s", StringUtils.getFormattedDecimalString(asset.getVolume24Hour()), ((AssetPair) asset).getQuoteCurrencySymbol()));
            } else {
                mMarketCapTextView.setText(StringUtils.getFormattedCurrencyString(asset.getMarketCap(), mPreferences.getCurrencyCode()));
                mVolume24HourTextView.setText(StringUtils.getFormattedCurrencyString(asset.getVolume24Hour(), mPreferences.getCurrencyCode()));
            }

            mAvailableSupplyTextView.setText(String.format("%s %s", StringUtils.getFormattedNumberString(asset.getAvailableSupply()), asset.getSymbol()));
            mTotalSupplyTextView.setText(String.format("%s %s", StringUtils.getFormattedNumberString(asset.getTotalSupply()), asset.getSymbol()));
            mMaxSupplyTextView.setText(String.format("%s %s", StringUtils.getFormattedNumberString(asset.getMaxSupply()), asset.getSymbol()));
        }
    }
}