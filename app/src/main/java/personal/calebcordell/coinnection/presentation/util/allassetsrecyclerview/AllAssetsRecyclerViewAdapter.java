package personal.calebcordell.coinnection.presentation.util.allassetsrecyclerview;

import android.annotation.SuppressLint;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import java.util.Comparator;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindColor;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import personal.calebcordell.coinnection.R;
import personal.calebcordell.coinnection.domain.model.Asset;
import personal.calebcordell.coinnection.domain.model.GlobalMarketData;
import personal.calebcordell.coinnection.presentation.Constants;
import personal.calebcordell.coinnection.presentation.Preferences;
import personal.calebcordell.coinnection.presentation.StringUtils;
import personal.calebcordell.coinnection.presentation.util.EmptyListItemViewHolder;
import personal.calebcordell.coinnection.presentation.util.OnObjectItemClickListener;


public class AllAssetsRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = AllAssetsRecyclerViewAdapter.class.getSimpleName();

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ASSET = 1;

    private static final int HEADER_POSITION = 0;

    private List<Asset> mAssets = new ArrayList<>();
    private List<Asset> mAssetsVisible = new ArrayList<>();
    private GlobalMarketData mGlobalMarketData;

    private OnObjectItemClickListener<Asset> mOnAssetItemClickListener;

    private String mCurrentFilterText = "";

    private int mAssetInfoShown = Constants.ALL_ASSETS_LIST_ITEM_SHOW_MARKET_CAP;
    private int mSortDirection = Constants.SORT_DIRECTION_DESCENDING;

    private final Preferences mPreferences;

    @Inject
    public AllAssetsRecyclerViewAdapter(Preferences preferences) {
        mPreferences = preferences;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == HEADER_POSITION) {
            return TYPE_HEADER;
        }
        return TYPE_ASSET;
    }

    @Override @NonNull
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_HEADER:
                if (mGlobalMarketData != null) {
                    return new AllAssetHeaderViewHolder(LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.card_view_all_asset_header, parent, false));
                }
                return new EmptyListItemViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.empty_view, parent, false));
            case TYPE_ASSET:
                return new AssetItemViewHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.card_view_all_asset_item, parent, false));
            default:
                return new EmptyListItemViewHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.empty_view, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof AllAssetHeaderViewHolder) {
            ((AllAssetHeaderViewHolder) viewHolder).bind(mGlobalMarketData);
        } else if (viewHolder instanceof AssetItemViewHolder) {
            Asset asset = mAssetsVisible.get(position - 1);
            ((AssetItemViewHolder) viewHolder).bind(asset, mOnAssetItemClickListener, mAssetInfoShown);
        }
    }

    @Override
    public int getItemCount() {
        return mAssetsVisible.size() + 1;
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public void setGlobalMarketData(@Nullable final GlobalMarketData globalMarketData) {
        mGlobalMarketData = globalMarketData;

        notifyItemChanged(0);
    }

    public void setAssets(final List<Asset> newAssets) {
        mAssets = newAssets;

        filter(mCurrentFilterText);
        sort(mAssetInfoShown);

        notifyDataSetChanged();
    }

    public void filter(@NonNull final String text) {
        Completable.fromRunnable(() -> {
            mCurrentFilterText = text;

            mAssetsVisible.clear();

            if (!mCurrentFilterText.isEmpty()) {
                mCurrentFilterText = mCurrentFilterText.toLowerCase();
                for (Asset asset : mAssets) {
                    String assetId = asset.getId();
                    if (assetId.toLowerCase().contains(mCurrentFilterText) || asset.getName().toLowerCase().contains(mCurrentFilterText)
                            || asset.getSymbol().toLowerCase().contains(mCurrentFilterText)) {
                        mAssetsVisible.add(asset);
                    }
                }
            } else {
                mAssetsVisible.addAll(mAssets);
            }
        })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::notifyDataSetChanged);
    }

    public void sort(final int assetInfoShown) {
        mAssetInfoShown = assetInfoShown;
        final Comparator<Asset> comparator;

        switch (Constants.ALL_ASSETS_LIST_ITEM_INFO[assetInfoShown]) {
            case Constants.ALL_ASSETS_LIST_ITEM_SHOW_PRICE:
                comparator = new Asset.PriceComparator();
                break;
            case Constants.ALL_ASSETS_LIST_ITEM_SHOW_PERCENT_CHANGE_1H:
                comparator = new Asset.PercentChangeHourComparator();
                break;
            case Constants.ALL_ASSETS_LIST_ITEM_SHOW_PERCENT_CHANGE_24H:
                comparator = new Asset.PercentChangeDayComparator();
                break;
            case Constants.ALL_ASSETS_LIST_ITEM_SHOW_PERCENT_CHANGE_7D:
                comparator = new Asset.PercentChangeWeekComparator();
                break;
            case Constants.ALL_ASSETS_LIST_ITEM_SHOW_MARKET_CAP:
                comparator = new Asset.MarketCapComparator();
                break;
            case Constants.ALL_ASSETS_LIST_ITEM_SHOW_24_HOUR_VOLUME:
                comparator = new Asset.Volume24HourComparator();
                break;
            case Constants.ALL_ASSETS_LIST_ITEM_SHOW_AVAILABLE_SUPPLY:
                comparator = new Asset.AvailableSupplyComparator();
                break;
            case Constants.ALL_ASSETS_LIST_ITEM_SHOW_NAME:
                comparator = new Asset.NameComparator();
                break;
            default:
                comparator = new Asset.MarketCapComparator();
                break;
        }

        Completable.fromRunnable(() -> {
            if (mSortDirection == Constants.SORT_DIRECTION_ASCENDING) {
                Collections.sort(mAssets, comparator);
                Collections.sort(mAssetsVisible, comparator);
            } else {
                Collections.sort(mAssets, Collections.reverseOrder(comparator));
                Collections.sort(mAssetsVisible, Collections.reverseOrder(comparator));
            }
        })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::notifyDataSetChanged);
    }

    public void setSortDirection(int direction) {
        mSortDirection = direction;
        sort(mAssetInfoShown);
    }

    public void setOnAssetItemClickListener(OnObjectItemClickListener<Asset> onAssetItemClickListener) {
        mOnAssetItemClickListener = onAssetItemClickListener;
    }


    protected class AllAssetHeaderViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.global_market_cap_text_view)
        TextView mGlobalMarketCapTextView;

        @BindString(R.string.global_market_cap_header)
        String mGlobalMarketCapString;

        public AllAssetHeaderViewHolder(final View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }

        public void bind(final GlobalMarketData globalMarketData) {
            if (globalMarketData != null) {
                mGlobalMarketCapTextView.setText(String.format("%s %s", mGlobalMarketCapString,
                        StringUtils.getFormattedCurrencyString(globalMarketData.getTotalMarketCap(), mPreferences.getCurrencyCode())));
            }
        }
    }

    protected class AssetItemViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.asset_logo)
        ImageView mAssetLogo;
        @BindView(R.id.asset_name)
        TextView mAssetNameTextView;
        @BindView(R.id.asset_symbol)
        TextView mAssetSymbolTextView;
        @BindView(R.id.asset_info_field)
        TextView mAssetInfoFieldTextView;

        @BindColor(R.color.colorTextBlackPrimary)
        int mUpdatedColorLight;
        @BindColor(R.color.colorTextBlackSecondary)
        int mUpdatingColorLight;
        @BindColor(R.color.colorPositiveNumber)
        int mPositiveColor;
        @BindColor(R.color.colorNegativeNumber)
        int mNegativeColor;

        public AssetItemViewHolder(final View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }

        public void bind(final Asset asset,
                         final OnObjectItemClickListener<Asset> onObjectItemClickListener,
                         final int infoShown) {
            Glide.with(itemView.getContext())
                    .load(asset.getLogo())
                    .into(mAssetLogo);

            mAssetNameTextView.setText(asset.getName());
            mAssetSymbolTextView.setText(asset.getSymbol());

            double percentChange = Double.MIN_VALUE;

            switch (infoShown) {
                case Constants.ALL_ASSETS_LIST_ITEM_SHOW_PRICE:
                    mAssetInfoFieldTextView.setText(StringUtils.getFormattedCurrencyString(asset.getPrice(), mPreferences.getCurrencyCode()));
                    percentChange = asset.getPercentChange1Hour();
                    break;
                case Constants.ALL_ASSETS_LIST_ITEM_SHOW_PERCENT_CHANGE_1H:
                    mAssetInfoFieldTextView.setText(StringUtils.getFormattedPercentString(asset.getPercentChange1Hour()));
                    percentChange = asset.getPercentChange1Hour();
                    break;
                case Constants.ALL_ASSETS_LIST_ITEM_SHOW_PERCENT_CHANGE_24H:
                    mAssetInfoFieldTextView.setText(StringUtils.getFormattedPercentString(asset.getPercentChange24Hour()));
                    percentChange = asset.getPercentChange24Hour();
                    break;
                case Constants.ALL_ASSETS_LIST_ITEM_SHOW_PERCENT_CHANGE_7D:
                    mAssetInfoFieldTextView.setText(StringUtils.getFormattedPercentString(asset.getPercentChange7Day()));
                    percentChange = asset.getPercentChange7Day();
                    break;
                case Constants.ALL_ASSETS_LIST_ITEM_SHOW_MARKET_CAP:
                    mAssetInfoFieldTextView.setText(StringUtils.getFormattedCurrencyString(asset.getMarketCap(), mPreferences.getCurrencyCode()));
                    percentChange = asset.getPercentChange1Hour();
                    break;
                case Constants.ALL_ASSETS_LIST_ITEM_SHOW_24_HOUR_VOLUME:
                    mAssetInfoFieldTextView.setText(StringUtils.getFormattedCurrencyString(asset.getVolume24Hour(), mPreferences.getCurrencyCode()));
                    break;
                case Constants.ALL_ASSETS_LIST_ITEM_SHOW_AVAILABLE_SUPPLY:
                    mAssetInfoFieldTextView.setText(StringUtils.getFormattedNumberString(asset.getAvailableSupply()));
                    break;
                case Constants.ALL_ASSETS_LIST_ITEM_SHOW_NAME:
                    mAssetInfoFieldTextView.setText(StringUtils.getFormattedCurrencyString(asset.getPrice(), mPreferences.getCurrencyCode()));
                    percentChange = asset.getPercentChange1Hour();
                    break;
                default:
                    mAssetInfoFieldTextView.setText(StringUtils.getFormattedCurrencyString(asset.getPrice(), mPreferences.getCurrencyCode()));
                    percentChange = asset.getPercentChange1Hour();
                    break;
            }

            int[] attrs = {R.attr.colorTextOverBackgroundPrimary, R.attr.colorTextOverBackgroundSecondary};
            TypedArray styles = itemView.getContext().getApplicationContext().obtainStyledAttributes(mPreferences.getAppThemeStyleAttr(), attrs);

            int mUpdatedColor = styles.getColor(0, mUpdatedColorLight);
            @SuppressLint("ResourceType") int mUpdatingColor = styles.getColor(1, mUpdatingColorLight);
            styles.recycle();

            if (asset.isUpToDate(Constants.RELOAD_TIME_ALL_ASSETS)) {
                mAssetNameTextView.setTextColor(mUpdatedColor);
                if (percentChange == Double.MIN_VALUE) {
                    mAssetInfoFieldTextView.setTextColor(mUpdatedColor);
                } else if (percentChange >= 0) {
                    mAssetInfoFieldTextView.setTextColor(mPositiveColor);
                } else {
                    mAssetInfoFieldTextView.setTextColor(mNegativeColor);
                }
            } else {
                mAssetNameTextView.setTextColor(mUpdatingColor);
                mAssetInfoFieldTextView.setTextColor(mUpdatingColor);
            }


            if (onObjectItemClickListener != null) {
                itemView.setOnClickListener((view) ->
                        ViewCompat.postOnAnimationDelayed(view, () ->
                                onObjectItemClickListener.onObjectItemClick(asset), Constants.SELECTABLE_VIEW_ANIMATION_DELAY)
                );
            }
        }
    }
}