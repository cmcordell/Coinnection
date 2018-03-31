package personal.calebcordell.coinnection.presentation.util.assetsearchrecyclerview;

import android.annotation.SuppressLint;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import personal.calebcordell.coinnection.R;
import personal.calebcordell.coinnection.domain.model.Asset;
import personal.calebcordell.coinnection.presentation.Constants;
import personal.calebcordell.coinnection.presentation.Preferences;


public class AssetSearchRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Asset> mAssets = new ArrayList<>();
    private List<Asset> mAssetsVisible = new ArrayList<>();
    private Map<String, Integer> mVisibleAssetPositions = new HashMap<>();

    private List<String> mPortfolioAssetIds = new ArrayList<>();
    private List<String> mWatchlistAssetIds = new ArrayList<>();

    private AssetSearchItemListener mAssetSearchItemListener;

    private String mCurrentFilterText = "";

    private final Preferences mPreferences;

    @Inject
    public AssetSearchRecyclerViewAdapter(Preferences preferences) {
        mPreferences = preferences;
    }

    @Override
    @NonNull
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_view_search_asset_item, parent, false);
        return new AssetSearchItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        Asset asset = mAssetsVisible.get(position);
        ((AssetSearchItemViewHolder) viewHolder).bind(asset, mPortfolioAssetIds.contains(asset.getId()),
                mWatchlistAssetIds.contains(asset.getId()), mAssetSearchItemListener);
    }

    @Override
    public int getItemCount() {
        return mAssetsVisible.size();
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }


    public void replaceAll(@NonNull List<Asset> assets) {
        mAssets = assets;
        filter(mCurrentFilterText);
        notifyDataSetChanged();
    }

    public void filter(@NonNull final String text) {
//        Completable.fromRunnable(() -> {
//            mCurrentFilterText = text;
//
//            mAssetsVisible.clear();
//            mVisibleAssetPositions.clear();
//
//            if (!mCurrentFilterText.isEmpty()) {
//                mCurrentFilterText = mCurrentFilterText.toLowerCase();
//                for (Asset asset : mAssets) {
//                    String assetId = asset.getAssetId();
//                    if (assetId.toLowerCase().contains(mCurrentFilterText) || asset.getName().toLowerCase().contains(mCurrentFilterText)
//                            || asset.getSymbol().toLowerCase().contains(mCurrentFilterText)) {
//                        mVisibleAssetPositions.put(assetId, mAssetsVisible.size());
//                        mAssetsVisible.add(asset);
//                    }
//                }
//            }
//        })
//                .subscribeOn(Schedulers.newThread())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(this::notifyDataSetChanged);

        mCurrentFilterText = text;

        mAssetsVisible.clear();
        mVisibleAssetPositions.clear();

        if (!mCurrentFilterText.isEmpty()) {
            mCurrentFilterText = mCurrentFilterText.toLowerCase();
            for (Asset asset : mAssets) {
                String assetId = asset.getId();
                if (assetId.toLowerCase().contains(mCurrentFilterText) || asset.getName().toLowerCase().contains(mCurrentFilterText)
                        || asset.getSymbol().toLowerCase().contains(mCurrentFilterText)) {
                    mVisibleAssetPositions.put(assetId, mAssetsVisible.size());
                    mAssetsVisible.add(asset);
                }
            }
        }

        notifyDataSetChanged();
    }

    public void setPortfolioAssetIds(@NonNull List<String> portfolioAssetIds) {
        mPortfolioAssetIds = portfolioAssetIds;

        for (String assetId : portfolioAssetIds) {
            Integer position = mVisibleAssetPositions.get(assetId);
            if (position != null) {
                notifyItemChanged(position);
            }
        }
    }

    public void setWatchlistAssetIds(@NonNull List<String> watchlistAssetIds) {
        mWatchlistAssetIds = watchlistAssetIds;

        for (String assetId : watchlistAssetIds) {
            Integer position = mVisibleAssetPositions.get(assetId);
            if (position != null) {
                notifyItemChanged(position);
            }
        }
    }

    public void setAssetOnWatchlist(@NonNull String assetId, boolean isOnWatchlist) {
        if (isOnWatchlist) {
            if (!mWatchlistAssetIds.contains(assetId)) {
                mWatchlistAssetIds.add(assetId);
            }
        } else {
            mWatchlistAssetIds.remove(assetId);
        }

        Integer position = mVisibleAssetPositions.get(assetId);
        if (position != null) {
            notifyItemChanged(position);
        }
    }

    public void setAssetSearchItemListener(final AssetSearchItemListener assetSearchItemListener) {
        mAssetSearchItemListener = assetSearchItemListener;
    }



    protected class AssetSearchItemViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.asset_logo)
        ImageView mAssetLogo;
        @BindView(R.id.asset_name)
        TextView mAssetName;
        @BindView(R.id.asset_symbol)
        TextView mAssetSymbol;
        @BindView(R.id.asset_favorite_button)
        ImageButton mAssetFavoriteButton;

        AssetSearchItemViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }

        public void bind(final Asset asset, final boolean isInPortfolio, final boolean isOnWatchlist, final AssetSearchItemListener assetSearchItemListener) {
            Glide.with(itemView.getContext())
                    .load(asset.getLogo())
                    .into(mAssetLogo);
            mAssetName.setText(asset.getName());
            mAssetSymbol.setText(asset.getSymbol());

            int[] attrs = {R.attr.drawableFavoriteBorderOverBackground, R.attr.drawableFavoriteOverBackground};
            TypedArray styles = itemView.getContext().getApplicationContext().obtainStyledAttributes(mPreferences.getAppThemeStyleAttr(), attrs);
            Drawable addToWatchlistDrawable = styles.getDrawable(0);
            @SuppressLint("ResourceType") Drawable removeFromWatchlistDrawable = styles.getDrawable(1);
            styles.recycle();

            if (isInPortfolio) {
                mAssetFavoriteButton.setVisibility(View.GONE);
            } else {
                mAssetFavoriteButton.setVisibility(View.VISIBLE);
                if (isOnWatchlist) {
                    mAssetFavoriteButton.setImageDrawable(removeFromWatchlistDrawable);
                } else {
                    mAssetFavoriteButton.setImageDrawable(addToWatchlistDrawable);
                }
            }

            if (assetSearchItemListener != null) {
                itemView.setOnClickListener((View view) ->
                        ViewCompat.postOnAnimationDelayed(view, () ->
                                assetSearchItemListener.onAssetItemClick(asset), Constants.SELECTABLE_VIEW_ANIMATION_DELAY));

                mAssetFavoriteButton.setOnClickListener((view) ->
                        ViewCompat.postOnAnimationDelayed(view, () -> {
                            if (isOnWatchlist) {
                                mAssetFavoriteButton.setImageDrawable(addToWatchlistDrawable);
                            } else {
                                mAssetFavoriteButton.setImageDrawable(removeFromWatchlistDrawable);
                            }

                            assetSearchItemListener.onFavoriteClick(asset, !isOnWatchlist);
                        }, Constants.SELECTABLE_VIEW_ANIMATION_DELAY));
            }
        }
    }
}