package personal.calebcordell.coinnection.presentation.util.portfoliorecyclerview;

import android.animation.ObjectAnimator;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindColor;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import personal.calebcordell.coinnection.R;
import personal.calebcordell.coinnection.domain.model.PortfolioAsset;
import personal.calebcordell.coinnection.presentation.App;
import personal.calebcordell.coinnection.presentation.Constants;
import personal.calebcordell.coinnection.presentation.Utils;
import personal.calebcordell.coinnection.presentation.util.RecyclerViewScrollDisabler;


public class PortfolioHeaderViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.total_balance_text) TextView mTotalBalanceTextView;
    @BindView(R.id.total_balance_change_text) TextView mTotalBalanceChangeTextView;
    @BindView(R.id.tab_layout) TabLayout mTabLayout;
    @BindView(R.id.chart) LineChart mChart;

    @BindString(R.string.one_hour_change_text) String mHourChangeText;
    @BindString(R.string.one_day_change_text) String mDayChangeText;
    @BindString(R.string.one_week_change_text) String mWeekChangeText;

    @BindColor(R.color.colorPrimary) int mGraphLineColor;
    @BindColor(R.color.colorCardBackgroundLight) int mCardBackgroundColor;
    @BindColor(R.color.colorGraphFillLight) int mGraphFillColor;
    @BindColor(R.color.colorTextBlackPrimary) int mGraphHighlightColor;


    PortfolioHeaderViewHolder(final View itemView) {
        super(itemView);

        ButterKnife.bind(this, itemView);

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
    }

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

        mTotalBalanceTextView.setText(Utils.getFormattedCurrencyString(totalBalance));

        float[] labels;
        float[] values;

        //TODO Update to get historical data from portfolio

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
//            case Constants.TIMEFRAME_MONTH:
//                portfolioValueChange = totalBalance - totalBalanceMonthAgo;
//                portfolioPercentChange = portfolioValueChange / totalBalanceMonthAgo;
//                portfolioChangeText = mMonthChangeText;
//                break;
//            case Constants.TIMEFRAME_YEAR:
//                portfolioValueChange = totalBalance - totalBalanceYearAgo;
//                portfolioPercentChange = portfolioValueChange / totalBalanceYearAgo;
//                portfolioChangeText = mYearChangeText;
//                break;
            default:
                portfolioValueChange = totalBalance - totalBalanceHourAgo;
                portfolioPercentChange = portfolioValueChange / totalBalanceHourAgo;
                portfolioChangeText = mHourChangeText;
                values = new float[] {460.64f, 461.89f, 462.79f, 464.49f, 462.76f, 465.56f, 467.25f, 468.79f, 464.49f, 470.76f, 472.56f, 467.25f};
                labels = new float[] {1f, 2f, 3f, 4f, 5f, 6f, 7f, 8f, 9f, 10f, 11f, 12f};
                break;
        }

        final String changeDirection;
        if(portfolioValueChange < 0) {
            changeDirection = "";
        } else {
            changeDirection = "+";
        }
        mTotalBalanceChangeTextView.setText(String.format("%s%s (%s%s) %s", changeDirection,
                Utils.getFormattedCurrencyString(portfolioValueChange), changeDirection,
                Utils.getFormattedPercentString(portfolioPercentChange), portfolioChangeText));

        mTabLayout.clearOnTabSelectedListeners();
        mTabLayout.addOnTabSelectedListener(onTabSelectedListener);


        int[] attrs = {R.attr.colorPrimary, R.attr.colorCardBackground, R.attr.colorGraphFill, R.attr.colorTextOverBackgroundPrimary};
        TypedArray styles = App.getAppContext().obtainStyledAttributes(App.getApp().getAppTheme(), attrs);

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
        dataset.setFillAlpha(50);
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
                mTotalBalanceTextView.setText(Utils.getFormattedCurrencyString(totalBalance));
                mTotalBalanceChangeTextView.setText(String.format("%s%s (%s%s) %s", changeDirection,
                        Utils.getFormattedCurrencyString(portfolioValueChange), changeDirection,
                        Utils.getFormattedPercentString(portfolioPercentChange), portfolioChangeText));
            }
        });

        mChart.notifyDataSetChanged();
    }
}