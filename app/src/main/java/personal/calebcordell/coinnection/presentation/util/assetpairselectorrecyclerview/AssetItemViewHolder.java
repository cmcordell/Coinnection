package personal.calebcordell.coinnection.presentation.util.assetpairselectorrecyclerview;

import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;
import personal.calebcordell.coinnection.R;
import personal.calebcordell.coinnection.domain.model.Asset;
import personal.calebcordell.coinnection.presentation.Constants;
import personal.calebcordell.coinnection.presentation.util.OnObjectItemClickListener;


public class AssetItemViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.asset_logo)
    ImageView mAssetLogo;
    @BindView(R.id.asset_name)
    TextView mAssetName;
    @BindView(R.id.asset_symbol)
    TextView mAssetSymbol;

    AssetItemViewHolder(final View itemView) {
        super(itemView);

        ButterKnife.bind(this, itemView);
    }

    public void bind(final Asset asset, final OnObjectItemClickListener<Asset> onObjectItemClickListener) {
        Glide.with(itemView.getContext())
                .load(asset.getLogo())
                .into(mAssetLogo);

        mAssetName.setText(asset.getName());
        mAssetSymbol.setText(asset.getSymbol());

        if (onObjectItemClickListener != null) {
            itemView.setOnClickListener((view) ->
                    ViewCompat.postOnAnimationDelayed(view, () ->
                            onObjectItemClickListener.onObjectItemClick(asset), Constants.SELECTABLE_VIEW_ANIMATION_DELAY));
        }
    }
}