package personal.calebcordell.coinnection.presentation.util.assetpairlistrecyclerview;

import android.annotation.SuppressLint;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindColor;
import butterknife.BindView;
import butterknife.ButterKnife;
import personal.calebcordell.coinnection.R;
import personal.calebcordell.coinnection.domain.model.AssetPair;
import personal.calebcordell.coinnection.presentation.Constants;
import personal.calebcordell.coinnection.presentation.Preferences;
import personal.calebcordell.coinnection.presentation.StringUtils;
import personal.calebcordell.coinnection.presentation.util.ItemTouchHelperAdapter;
import personal.calebcordell.coinnection.presentation.util.OnItemDragFinishedListener;
import personal.calebcordell.coinnection.presentation.util.OnObjectItemClickListener;


public class AssetPairListRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements ItemTouchHelperAdapter {

    private List<AssetPair> mAssetPairs = new ArrayList<>();

    private OnObjectItemClickListener<AssetPair> mOnAssetPairItemClickListener;
    private OnItemDragFinishedListener<List<AssetPair>> mOnAssetPairItemDragFinishedListener;

    private Preferences mPreferences;

    @Inject
    public AssetPairListRecyclerViewAdapter(Preferences preferences) {
        mPreferences = preferences;
    }

    @Override
    @NonNull
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AssetPairItemViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_view_asset_pair_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof AssetPairItemViewHolder) {
            ((AssetPairItemViewHolder) viewHolder).bind(mAssetPairs.get(position), mOnAssetPairItemClickListener);
        }
    }

    @Override
    public int getItemCount() {
        return mAssetPairs.size();
    }

    public void setAssetPairs(final List<AssetPair> assetPairs) {
        mAssetPairs = assetPairs;

        notifyDataSetChanged();
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        Collections.swap(mAssetPairs, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    @Override
    public void onItemDismiss(int position) {
        mAssetPairs.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public void onDragFinished() {
        if(mOnAssetPairItemDragFinishedListener != null) {
            mOnAssetPairItemDragFinishedListener.onDragFinished(mAssetPairs);
        }
    }

    public void setOnAssetPairItemClickListener(OnObjectItemClickListener<AssetPair> onAssetPairItemClickListener) {
        mOnAssetPairItemClickListener = onAssetPairItemClickListener;
    }
    public void setOnAssetPairItemDragFinishedListener(OnItemDragFinishedListener<List<AssetPair>> onAssetPairItemDragFinishedListener) {
        mOnAssetPairItemDragFinishedListener = onAssetPairItemDragFinishedListener;
    }

    protected class AssetPairItemViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.asset_pair_logo)
        ImageView mAssetLogo;
        @BindView(R.id.asset_pair_name)
        TextView mAssetPairNameTextView;
        @BindView(R.id.asset_pair_price_field)
        TextView mAssetPairPriceFieldTextView;

        @BindColor(R.color.colorTextBlackPrimary)
        int mUpdatedColorLight;
        @BindColor(R.color.colorTextBlackSecondary)
        int mUpdatingColorLight;
        @BindColor(R.color.colorPositiveNumber)
        int mPositiveColor;
        @BindColor(R.color.colorNegativeNumber)
        int mNegativeColor;

        AssetPairItemViewHolder(final View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }

        public void bind(final AssetPair assetPair, final OnObjectItemClickListener<AssetPair> onObjectItemClickListener) {
            Glide.with(itemView.getContext())
                    .load(assetPair.getLogo())
                    .into(mAssetLogo);
            mAssetPairNameTextView.setText(String.format("%s/%s", assetPair.getSymbol(), assetPair.getQuoteCurrencySymbol()));
            mAssetPairPriceFieldTextView.setText(StringUtils.getFormattedDecimalString(assetPair.getPrice()));


            //Set text color based on whether data is current/out-of-date and positive/negative
            int[] attrs = {R.attr.colorTextOverBackgroundPrimary, R.attr.colorTextOverBackgroundSecondary};
            TypedArray styles = itemView.getContext().getApplicationContext().obtainStyledAttributes(mPreferences.getAppThemeStyleAttr(), attrs);

            int mUpdatedColor = styles.getColor(0, mUpdatedColorLight);
            @SuppressLint("ResourceType") int mUpdatingColor = styles.getColor(1, mUpdatingColorLight);
            styles.recycle();

            if (assetPair.isUpToDate(Constants.RELOAD_TIME_SINGLE_ASSET)) {
                mAssetPairNameTextView.setTextColor(mUpdatedColor);
                if (assetPair.getPercentChange1Hour() >= 0) {
                    mAssetPairPriceFieldTextView.setTextColor(mPositiveColor);
                } else {
                    mAssetPairPriceFieldTextView.setTextColor(mNegativeColor);
                }
            } else {
                mAssetPairNameTextView.setTextColor(mUpdatingColor);
                mAssetPairPriceFieldTextView.setTextColor(mUpdatingColor);
            }

            if (onObjectItemClickListener != null) {
                itemView.setOnClickListener((view) ->
                        ViewCompat.postOnAnimationDelayed(view,
                                () -> onObjectItemClickListener.onObjectItemClick(assetPair),
                                Constants.SELECTABLE_VIEW_ANIMATION_DELAY));
            }
        }
    }
}