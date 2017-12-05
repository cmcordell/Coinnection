package personal.calebcordell.coinnection.presentation.util.portfoliorecyclerview;

import android.content.res.TypedArray;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindColor;
import butterknife.BindView;
import butterknife.ButterKnife;
import personal.calebcordell.coinnection.R;
import personal.calebcordell.coinnection.domain.model.Asset;
import personal.calebcordell.coinnection.domain.model.PortfolioAsset;
import personal.calebcordell.coinnection.presentation.App;
import personal.calebcordell.coinnection.presentation.Constants;
import personal.calebcordell.coinnection.presentation.Utils;
import personal.calebcordell.coinnection.presentation.util.OnObjectItemClickListener;


public class PortfolioAssetItemViewHolder extends RecyclerView.ViewHolder {
    
    @BindView(R.id.card_view) CardView mCardView;
    @BindView(R.id.asset_logo) ImageView mAssetLogo;
    @BindView(R.id.asset_name) TextView mAssetNameTextView;
    @BindView(R.id.asset_balance) TextView mAssetBalanceTextView;
    @BindView(R.id.asset_info_field) TextView mAssetInfoFieldTextView;

    @BindColor(R.color.colorTextBlackPrimary) int mUpdatedColorLight;
    @BindColor(R.color.colorTextBlackSecondary) int mUpdatingColorLight;
    @BindColor(R.color.colorPositiveNumber) int mPositiveColor;
    @BindColor(R.color.colorNegativeNumber) int mNegativeColor;

    PortfolioAssetItemViewHolder(final View itemView) {
        super(itemView);

        ButterKnife.bind(this, itemView);
    }

    public void bind(final PortfolioAsset portfolioAsset, final OnObjectItemClickListener<Asset> onObjectItemClickListener, final int infoShown, final int percentChangeTimeframe) {
        mAssetLogo.setImageResource(portfolioAsset.getLogo());
        mAssetNameTextView.setText(portfolioAsset.getName());
        mAssetBalanceTextView.setText(Utils.getFormattedNumberString(portfolioAsset.getBalance()) + " " + portfolioAsset.getSymbol());

        //Get the correct percent change based on the timeframe
        double percentChange;
        switch(percentChangeTimeframe) {
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
        switch(Constants.PORTFOLIO_LIST_ITEM_INFO[infoShown]) {
            case Constants.PORTFOLIO_LIST_ITEM_SHOW_PRICE:
                mAssetInfoFieldTextView.setText(Utils.getFormattedCurrencyString(portfolioAsset.getPrice()));
                break;
            case Constants.PORTFOLIO_LIST_ITEM_SHOW_PERCENT_CHANGE:
                mAssetInfoFieldTextView.setText(Utils.getFormattedPercentString(percentChange));
                break;
            case Constants.PORTFOLIO_LIST_ITEM_SHOW_BALANCE_VALUE:
                mAssetInfoFieldTextView.setText(Utils.getFormattedCurrencyString(portfolioAsset.getBalance() * portfolioAsset.getPrice()));
                break;
            default:
                mAssetInfoFieldTextView.setText(Utils.getFormattedCurrencyString(portfolioAsset.getPrice()));
                break;
        }


        //Set text color based on whether data is current/out-of-date and positive/negative
        int[] attrs = {R.attr.colorTextOverBackgroundPrimary, R.attr.colorTextOverBackgroundSecondary};
        TypedArray styles = App.getAppContext().obtainStyledAttributes(App.getApp().getAppTheme(), attrs);

        int mUpdatedColor = styles.getColor(0, mUpdatedColorLight);
        int mUpdatingColor = styles.getColor(1, mUpdatingColorLight);
        styles.recycle();

        if(portfolioAsset.isUpToDate()) {
            mAssetNameTextView.setTextColor(mUpdatedColor);
            if(percentChange >= 0) {
                mAssetInfoFieldTextView.setTextColor(mPositiveColor);
            } else {
                mAssetInfoFieldTextView.setTextColor(mNegativeColor);
            }
        } else {
            mAssetNameTextView.setTextColor(mUpdatingColor);
            mAssetInfoFieldTextView.setTextColor(mUpdatingColor);
        }

        if(onObjectItemClickListener != null) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ViewCompat.postOnAnimationDelayed(view, new Runnable() {
                        @Override
                        public void run() {
                            onObjectItemClickListener.onObjectItemClick(portfolioAsset);
                        }
                    }, 50);
                }
            });
        }
    }
}
