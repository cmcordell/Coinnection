package personal.calebcordell.coinnection.presentation.util.assetrecyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import personal.calebcordell.coinnection.R;
import personal.calebcordell.coinnection.domain.model.PortfolioAsset;
import personal.calebcordell.coinnection.presentation.Utils;


public class AssetBalanceViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.fiat_balance_text_view) TextView mFiatBalanceTextView;
    @BindView(R.id.asset_balance_text_view) TextView mAssetBalanceTextView;

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
        if(fiatBalance < 1.0) {
            bigDecimal = new BigDecimal(fiatBalance, mathContext);
            mFiatBalanceTextView.setText(Utils.getFormattedCurrencyString(bigDecimal.doubleValue()));
        }
        else {
            mFiatBalanceTextView.setText(Utils.getFormattedCurrencyString(fiatBalance));
        }

        if(assetBalance < 1.0) {
            bigDecimal = new BigDecimal(assetBalance, mathContext);
            mAssetBalanceTextView.setText(String.format("%s %s", Utils.getFormattedNumberString(bigDecimal.doubleValue()), asset.getSymbol()));
        }
        else {
            mAssetBalanceTextView.setText(String.format("%s %s", Utils.getFormattedNumberString(assetBalance), asset.getSymbol()));
        }
    }
}