package personal.calebcordell.coinnection.presentation.util.allassetsrecyclerview;

import android.content.res.TypedArray;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindColor;
import butterknife.BindView;
import butterknife.ButterKnife;
import personal.calebcordell.coinnection.R;
import personal.calebcordell.coinnection.domain.model.Asset;
import personal.calebcordell.coinnection.presentation.App;
import personal.calebcordell.coinnection.presentation.Constants;
import personal.calebcordell.coinnection.presentation.Utils;
import personal.calebcordell.coinnection.presentation.util.OnObjectItemClickListener;


public class AssetItemViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.card_view) CardView mCardView;
    @BindView(R.id.asset_logo) ImageView mAssetLogo;
    @BindView(R.id.asset_name) TextView mAssetNameTextView;
    @BindView(R.id.asset_symbol) TextView mAssetSymbolTextView;
    @BindView(R.id.asset_info_field) TextView mAssetInfoFieldTextView;

    @BindColor(R.color.colorTextBlackPrimary) int mUpdatedColorLight;
    @BindColor(R.color.colorTextBlackSecondary) int mUpdatingColorLight;
    @BindColor(R.color.colorPositiveNumber) int mPositiveColor;
    @BindColor(R.color.colorNegativeNumber) int mNegativeColor;

    AssetItemViewHolder(final View itemView) {
        super(itemView);

        ButterKnife.bind(this, itemView);
    }

    public void bind(final Asset asset, final OnObjectItemClickListener<Asset> onObjectItemClickListener,
                     final int infoShown) {
        mAssetLogo.setImageResource(asset.getLogo());
        mAssetNameTextView.setText(asset.getName());
        mAssetSymbolTextView.setText(asset.getSymbol());

        double percentChange = Double.MIN_VALUE;

        switch (infoShown) {
            case Constants.ALL_ASSETS_LIST_ITEM_SHOW_PRICE:
                mAssetInfoFieldTextView.setText(Utils.getFormattedCurrencyString(asset.getPrice()));
                percentChange = asset.getPercentChange1Hour();
                break;
            case Constants.ALL_ASSETS_LIST_ITEM_SHOW_PERCENT_CHANGE_1H:
                mAssetInfoFieldTextView.setText(Utils.getFormattedPercentString(asset.getPercentChange1Hour()));
                percentChange = asset.getPercentChange1Hour();
                break;
            case Constants.ALL_ASSETS_LIST_ITEM_SHOW_PERCENT_CHANGE_24H:
                mAssetInfoFieldTextView.setText(Utils.getFormattedPercentString(asset.getPercentChange24Hour()));
                percentChange = asset.getPercentChange24Hour();
                break;
            case Constants.ALL_ASSETS_LIST_ITEM_SHOW_PERCENT_CHANGE_7D:
                mAssetInfoFieldTextView.setText(Utils.getFormattedPercentString(asset.getPercentChange7Day()));
                percentChange = asset.getPercentChange7Day();
                break;
            case Constants.ALL_ASSETS_LIST_ITEM_SHOW_MARKET_CAP:
                mAssetInfoFieldTextView.setText(Utils.getFormattedCurrencyString(asset.getMarketCap()));
                percentChange = asset.getPercentChange1Hour();
                break;
            case Constants.ALL_ASSETS_LIST_ITEM_SHOW_24_HOUR_VOLUME:
                mAssetInfoFieldTextView.setText(Utils.getFormattedCurrencyString(asset.getVolume24Hour()));
                break;
            case Constants.ALL_ASSETS_LIST_ITEM_SHOW_AVAILABLE_SUPPLY:
                mAssetInfoFieldTextView.setText(Utils.getFormattedNumberString(asset.getAvailableSupply()));
                break;
            case Constants.ALL_ASSETS_LIST_ITEM_SHOW_NAME:
                mAssetInfoFieldTextView.setText(Utils.getFormattedCurrencyString(asset.getPrice()));
                percentChange = asset.getPercentChange1Hour();
                break;
            default:
                mAssetInfoFieldTextView.setText(Utils.getFormattedCurrencyString(asset.getPrice()));
                percentChange = asset.getPercentChange1Hour();
                break;
        }

        int[] attrs = {R.attr.colorTextOverBackgroundPrimary, R.attr.colorTextOverBackgroundSecondary};
        TypedArray styles = App.getAppContext().obtainStyledAttributes(App.getApp().getAppTheme(), attrs);

        int mUpdatedColor = styles.getColor(0, mUpdatedColorLight);
        int mUpdatingColor = styles.getColor(1, mUpdatingColorLight);
        styles.recycle();


        if (asset.isUpToDate()) {
            mAssetNameTextView.setTextColor(mUpdatedColor);
            if(percentChange == Double.MIN_VALUE) {
                mAssetInfoFieldTextView.setTextColor(mUpdatedColor);
            }
            else if (percentChange >= 0) {
                mAssetInfoFieldTextView.setTextColor(mPositiveColor);
            } else {
                mAssetInfoFieldTextView.setTextColor(mNegativeColor);
            }
        } else {
            mAssetNameTextView.setTextColor(mUpdatingColor);
            mAssetInfoFieldTextView.setTextColor(mUpdatingColor);
        }


        if (onObjectItemClickListener != null) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ViewCompat.postOnAnimationDelayed(view, new Runnable() {
                        @Override
                        public void run() {
                            onObjectItemClickListener.onObjectItemClick(asset);
                        }
                    }, 50);
                }
            });
        }
    }
}