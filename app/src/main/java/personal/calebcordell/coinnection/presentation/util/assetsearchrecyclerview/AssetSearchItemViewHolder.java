package personal.calebcordell.coinnection.presentation.util.assetsearchrecyclerview;

import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import personal.calebcordell.coinnection.R;
import personal.calebcordell.coinnection.domain.model.Asset;
import personal.calebcordell.coinnection.presentation.App;


public class AssetSearchItemViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.asset_logo) ImageView mAssetLogo;
    @BindView(R.id.asset_name) TextView mAssetName;
    @BindView(R.id.asset_symbol) TextView mAssetSymbol;
    @BindView(R.id.asset_favorite_button) ImageButton mAssetFavoriteButton;

    AssetSearchItemViewHolder(View itemView) {
        super(itemView);

        ButterKnife.bind(this, itemView);
    }

    public void bind(final Asset asset, final boolean isInPortfolio, final boolean isOnWatchlist, final AssetSearchItemListener assetSearchItemListener) {
        mAssetLogo.setImageResource(asset.getLogo());
        mAssetName.setText(asset.getName());
        mAssetSymbol.setText(asset.getSymbol());

        int[] attrs = {R.attr.drawableFavoriteBorderOverBackground, R.attr.drawableFavoriteOverBackground};
        TypedArray styles = App.getAppContext().obtainStyledAttributes(App.getApp().getAppTheme(), attrs);
        Drawable addToWatchlistDrawable = styles.getDrawable(0);
        Drawable removeFromWatchlistDrawable = styles.getDrawable(1);
        styles.recycle();

        if(isInPortfolio) {
            mAssetFavoriteButton.setVisibility(View.GONE);
        } else {
            mAssetFavoriteButton.setVisibility(View.VISIBLE);
            if (isOnWatchlist) {
                mAssetFavoriteButton.setImageDrawable(removeFromWatchlistDrawable);
            } else {
                mAssetFavoriteButton.setImageDrawable(addToWatchlistDrawable);
            }
        }

        if(assetSearchItemListener != null) {
            itemView.setOnClickListener((View view) -> {
                ViewCompat.postOnAnimationDelayed(view, new Runnable()
                {
                    @Override
                    public void run()
                    {
                        assetSearchItemListener.onAssetItemClick(asset);
                    }
                }, 200);
            });

            mAssetFavoriteButton.setOnClickListener((View view) -> {
                ViewCompat.postOnAnimationDelayed(view, new Runnable() {
                    @Override
                    public void run()
                    {
                        if (isOnWatchlist) {
                            mAssetFavoriteButton.setImageDrawable(addToWatchlistDrawable);
                        } else {
                            mAssetFavoriteButton.setImageDrawable(removeFromWatchlistDrawable);
                        }

                        assetSearchItemListener.onFavoriteClick(asset, !isOnWatchlist);
                    }
                }, 50);
            });
        }
    }
}