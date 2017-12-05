package personal.calebcordell.coinnection.presentation.util.assetrecyclerview;

import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.Space;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import personal.calebcordell.coinnection.R;


public class AssetButtonsViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.edit_add_asset_button) Button mEditAddAssetButton;
    @BindView(R.id.remove_asset_button) Button mRemoveAssetButton;
    @BindView(R.id.space) Space mSpace;

    @BindString(R.string.add) String mAddBalanceString;
    @BindString(R.string.edit) String mEditBalanceString;

    AssetButtonsViewHolder(final View itemView) {
        super(itemView);

        ButterKnife.bind(this, itemView);
    }

    public void bind(boolean isPortfolioAsset, View.OnClickListener onClickListener) {

        View.OnClickListener delayedOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ViewCompat.postOnAnimationDelayed(view, new Runnable() {
                    @Override
                    public void run() {
                        onClickListener.onClick(view);
                    }
                }, 100);
            }
        };

        if(isPortfolioAsset) {
            mRemoveAssetButton.setVisibility(View.VISIBLE);
            mSpace.setVisibility(View.VISIBLE);
            mEditAddAssetButton.setText(mEditBalanceString);
            mRemoveAssetButton.setOnClickListener(delayedOnClickListener);
        } else {
            mRemoveAssetButton.setVisibility(View.GONE);
            mSpace.setVisibility(View.GONE);
            mEditAddAssetButton.setText(mAddBalanceString);
        }

        mEditAddAssetButton.setOnClickListener(delayedOnClickListener);
    }
}
