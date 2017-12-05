package personal.calebcordell.coinnection.presentation.util.allassetsrecyclerview;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import personal.calebcordell.coinnection.R;
import personal.calebcordell.coinnection.domain.model.Asset;
import personal.calebcordell.coinnection.presentation.Constants;
import personal.calebcordell.coinnection.presentation.util.OnObjectItemClickListener;


public class AllAssetsRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_ASSET = 0;

    private List<Asset> mAssets;
    private List<Asset> mAssetsVisible;

    private OnObjectItemClickListener<Asset> mOnAssetItemClickListener;

    private String mCurrentFilterText;

    private int mAssetInfoShown;
    private int mSortDirection;


    public AllAssetsRecyclerViewAdapter() {
        this(new ArrayList<>(), Constants.ALL_ASSETS_LIST_ITEM_SHOW_MARKET_CAP);
    }
    public AllAssetsRecyclerViewAdapter(List<Asset> assets, int assetInfoShown) {
        mAssets = assets;
        mAssetsVisible = new ArrayList<>(assets.size());

        mSortDirection = Constants.SORT_DIRECTION_DESCENDING;
        mAssetInfoShown = assetInfoShown;
        mCurrentFilterText = "";
    }

    @Override
    public int getItemViewType(int position) {
        return TYPE_ASSET;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch(viewType) {
            case TYPE_ASSET:
                return new AssetItemViewHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.card_view_all_asset_item, parent, false));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        if(viewHolder instanceof AssetItemViewHolder) {
            Asset asset = mAssetsVisible.get(position);
            ((AssetItemViewHolder) viewHolder).bind(asset, mOnAssetItemClickListener, mAssetInfoShown);
        }
    }

    @Override
    public int getItemCount() {
        return mAssetsVisible.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }


    public void setAssets(final List<Asset> newAssets) {
        mAssets.clear();
        mAssets.addAll(newAssets);

        filter(mCurrentFilterText);
        sort(mAssetInfoShown);

        notifyDataSetChanged();
    }
    public void replaceAssets(final List<Asset> newAssets) {
        Completable.fromRunnable(() -> {
            mAssets.clear();
            mAssets.addAll(newAssets);
        })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                    filter(mCurrentFilterText);
                    sort(mAssetInfoShown);
                });
    }

    public void filter(@NonNull String text) {
        mCurrentFilterText = text;

        mAssetsVisible.clear();

        if(!text.isEmpty()) {
            text = text.toLowerCase();
            for(Asset asset : mAssets) {
                String assetId = asset.getId();
                if(assetId.toLowerCase().contains(text) || asset.getName().toLowerCase().contains(text)
                        || asset.getSymbol().toLowerCase().contains(text)) {
                    mAssetsVisible.add(asset);
                }
            }
        } else {
            mAssetsVisible.addAll(mAssets);
        }
        notifyDataSetChanged();
    }

    public void sort(int assetInfoShown) {
        mAssetInfoShown = assetInfoShown;
        Comparator<Asset> comparator;

        switch(Constants.ALL_ASSETS_LIST_ITEM_INFO[assetInfoShown]) {
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
            if(mSortDirection == Constants.SORT_DIRECTION_ASCENDING) {
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
}