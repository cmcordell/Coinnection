package personal.calebcordell.coinnection.presentation.util.assetrecyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import personal.calebcordell.coinnection.R;
import personal.calebcordell.coinnection.domain.model.Asset;
import personal.calebcordell.coinnection.presentation.Utils;


public class AssetStatisticsViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.rank_text_view) TextView mRankTextView;
    @BindView(R.id.market_cap_text_view) TextView mMarketCapTextView;
    @BindView(R.id.volume_24_hour_text_view) TextView mVolume24HourTextView;
    @BindView(R.id.available_supply_text_view) TextView mAvailableSupplyTextView;
    @BindView(R.id.total_supply_text_view) TextView mTotalSupplyTextView;

    AssetStatisticsViewHolder(final View itemView) {
        super(itemView);

        ButterKnife.bind(this, itemView);
    }

    public void bind(Asset asset) {
        mRankTextView.setText(String.format("#%s", Utils.getFormattedNumberString(asset.getRank())));
        mMarketCapTextView.setText(Utils.getFormattedCurrencyString(asset.getMarketCap()));
        mVolume24HourTextView.setText(Utils.getFormattedCurrencyString(asset.getVolume24Hour()));
        mAvailableSupplyTextView.setText(String.format("%s %s", Utils.getFormattedNumberString(asset.getAvailableSupply()), asset.getSymbol()));
        mTotalSupplyTextView.setText(String.format("%s %s", Utils.getFormattedNumberString(asset.getTotalSupply()), asset.getSymbol()));
    }
}