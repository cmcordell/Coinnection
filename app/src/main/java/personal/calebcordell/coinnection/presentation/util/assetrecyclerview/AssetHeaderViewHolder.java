package personal.calebcordell.coinnection.presentation.util.assetrecyclerview;

import android.content.res.TypedArray;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindColor;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import personal.calebcordell.coinnection.R;
import personal.calebcordell.coinnection.domain.model.Asset;
import personal.calebcordell.coinnection.presentation.App;
import personal.calebcordell.coinnection.presentation.Constants;
import personal.calebcordell.coinnection.presentation.Utils;
import personal.calebcordell.coinnection.presentation.util.RecyclerViewScrollDisabler;


public class AssetHeaderViewHolder extends RecyclerView.ViewHolder {
    
    @BindView(R.id.asset_price_text) TextView mAssetPriceTextView;
    @BindView(R.id.asset_price_change_text) TextView mAssetPriceChangeTextView;
    @BindView(R.id.tab_layout) TabLayout mTabLayout;
    @BindView(R.id.chart) LineChart mChart;

    @BindString(R.string.one_hour_change_text) String mHourChangeText;
    @BindString(R.string.one_day_change_text) String mDayChangeText;
    @BindString(R.string.one_week_change_text) String mWeekChangeText;

    @BindColor(R.color.colorPrimary) int mGraphLineColor;
    @BindColor(R.color.colorCardBackgroundLight) int mCardBackgroundColor;
    @BindColor(R.color.colorGraphFillLight) int mGraphFillColor;
    @BindColor(R.color.colorTextBlackPrimary) int mGraphHighlightColor;

    AssetHeaderViewHolder(final View itemView) {
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

    public void bind(final Asset asset, final int timeframe, final TabLayout.OnTabSelectedListener onTabSelectedListener) {
        final double assetPrice = asset.getPrice();

        mAssetPriceTextView.setText(Utils.getFormattedCurrencyString(assetPrice));

        final double assetValueChange;
        final double assetPercentChange;
        final String assetChangeText;

        float[] labels;
        float[] values;
        //TODO Update to get historical data from portfolio

        switch(timeframe) {
            case Constants.TIMEFRAME_HOUR:
                assetPercentChange = asset.getPercentChange1Hour();
                assetValueChange = assetPrice - (assetPrice / (1.00 + assetPercentChange));
                assetChangeText = mHourChangeText;
                values = new float[] {460.64f, 461.89f, 462.79f, 464.49f, 462.76f, 465.56f, 467.25f, 468.79f, 464.49f, 470.76f, 472.56f, 467.25f};
                labels = new float[] {1f, 2f, 3f, 4f, 5f, 6f, 7f, 8f, 9f, 10f, 11f, 12f};
                break;
            case Constants.TIMEFRAME_DAY:
                assetPercentChange = asset.getPercentChange24Hour();
                assetValueChange = assetPrice - (assetPrice / (1.00 + assetPercentChange));
                assetChangeText = mDayChangeText;
                values = new float[] {467.25f, 468.79f, 464.49f, 470.76f, 472.56f, 467.25f, 460.64f, 461.89f, 462.79f, 464.49f, 462.76f, 465.56f};
                labels = new float[] {1f, 2f, 3f, 4f, 5f, 6f, 7f, 8f, 9f, 10f, 11f, 12f};
                break;
            case Constants.TIMEFRAME_WEEK:
                assetPercentChange = asset.getPercentChange7Day();
                assetValueChange = assetPrice - (assetPrice / (1.00 + assetPercentChange));
                assetChangeText = mWeekChangeText;
                values = new float[] {460.64f, 471.89f, 478.79f, 474.49f, 432.76f, 475.56f, 407.25f};
                labels = new float[] {1f, 2f, 3f, 4f, 5f, 6f, 7f};
                break;
//            case Constants.TIMEFRAME_MONTH:
//                assetPercentChange = asset.getPercentChange1Month();
//                assetValueChange = assetPrice - (assetPrice / (1.00 + assetPercentChange));
//                assetChangeText = mMonthChangeText;
//                break;
//            case Constants.TIMEFRAME_YEAR:
//                assetPercentChange = asset.getPercentChange1Year();
//                assetValueChange = assetPrice - (assetPrice / (1.00 + assetPercentChange));
//                assetChangeText = mYearChangeText;
//                break;
            default:
                assetPercentChange = asset.getPercentChange1Hour();
                assetValueChange = assetPrice - (assetPrice / (1.00 + assetPercentChange));
                assetChangeText = mHourChangeText;
                values = new float[] {460.64f, 461.89f, 462.79f, 464.49f, 462.76f, 465.56f, 467.25f, 468.79f, 464.49f, 470.76f, 472.56f, 467.25f};
                labels = new float[] {1f, 2f, 3f, 4f, 5f, 6f, 7f, 8f, 9f, 10f, 11f, 12f};
                break;
        }

        final String changeDirection;
        if(assetValueChange < -0.00005) {
            changeDirection = "";
        } else {
            changeDirection = "+";
        }

        mAssetPriceChangeTextView.setText(String.format("%s%s (%s%s) %s", changeDirection,
                Utils.getFormattedCurrencyString(assetValueChange), changeDirection,
                Utils.getFormattedPercentString(assetPercentChange), assetChangeText));

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
                mAssetPriceTextView.setText(""+e.getY());
                mAssetPriceChangeTextView.setText(""+e.getX());
            }
            @Override
            public void onNothingSelected() {
                mAssetPriceTextView.setText(Utils.getFormattedCurrencyString(assetPrice));
                mAssetPriceChangeTextView.setText(String.format("%s%s (%s%s) %s", changeDirection,
                        Utils.getFormattedCurrencyString(assetValueChange), changeDirection,
                        Utils.getFormattedPercentString(assetPercentChange), assetChangeText));
            }
        });

        mChart.notifyDataSetChanged();
    }
}