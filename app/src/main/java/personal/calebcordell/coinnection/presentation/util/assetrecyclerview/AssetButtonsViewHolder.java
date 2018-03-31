package personal.calebcordell.coinnection.presentation.util.assetrecyclerview;

import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import personal.calebcordell.coinnection.R;
import personal.calebcordell.coinnection.presentation.Constants;


public class AssetButtonsViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.edit_add_asset_button) Button mEditAddAssetButton;
    @BindView(R.id.remove_asset_button) Button mRemoveAssetButton;

    @BindString(R.string.add) String mAddBalanceString;
    @BindString(R.string.edit) String mEditBalanceString;

    AssetButtonsViewHolder(final View itemView) {
        super(itemView);

        ButterKnife.bind(this, itemView);
    }

    public void bind(final int assetType, final View.OnClickListener onClickListener) {
        View.OnClickListener delayedOnClickListener = (view) ->
            ViewCompat.postOnAnimationDelayed(view,
                    () -> onClickListener.onClick(view), Constants.SELECTABLE_VIEW_ANIMATION_DELAY);

        if (assetType == Constants.ASSET_TYPE_PORTFOLIO) {
            mRemoveAssetButton.setVisibility(View.VISIBLE);
            mEditAddAssetButton.setText(mEditBalanceString);
            mRemoveAssetButton.setOnClickListener(delayedOnClickListener);
            mEditAddAssetButton.setOnClickListener(delayedOnClickListener);
        } else if (assetType == Constants.ASSET_TYPE_PAIR) {
            mEditAddAssetButton.setVisibility(View.GONE);
            mRemoveAssetButton.setOnClickListener(delayedOnClickListener);
        } else {
            mRemoveAssetButton.setVisibility(View.GONE);
            mEditAddAssetButton.setText(mAddBalanceString);
            mEditAddAssetButton.setOnClickListener(delayedOnClickListener);
        }
    }
}
