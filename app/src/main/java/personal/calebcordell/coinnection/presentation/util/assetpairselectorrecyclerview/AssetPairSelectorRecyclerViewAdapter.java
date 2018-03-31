package personal.calebcordell.coinnection.presentation.util.assetpairselectorrecyclerview;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import personal.calebcordell.coinnection.R;
import personal.calebcordell.coinnection.domain.model.Asset;
import personal.calebcordell.coinnection.domain.model.Currency;
import personal.calebcordell.coinnection.presentation.util.EmptyListItemViewHolder;
import personal.calebcordell.coinnection.presentation.util.OnObjectItemClickListener;


public class AssetPairSelectorRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_CURRENCY_HEADER = 0;
    private static final int TYPE_CURRENCY = 1;
    private static final int TYPE_ASSET_HEADER = 2;
    private static final int TYPE_ASSET = 3;

    private int mAssetHeaderPosition = 0;

    private List<Currency> mCurrencies = new ArrayList<>();
    private List<Asset> mAssets = new ArrayList<>();

    private List<Currency> mCurrenciesVisible = new ArrayList<>();
    private List<Asset> mAssetsVisible = new ArrayList<>();

    private OnObjectItemClickListener<Currency> mOnCurrencyItemClickListener;
    private OnObjectItemClickListener<Asset> mOnAssetItemClickListener;

    private String mCurrentFilterText = "";
    private boolean mCurrenciesAreVisible = false;

    @Inject
    public AssetPairSelectorRecyclerViewAdapter() {}

    @Override
    public int getItemViewType(int position) {
        if (mCurrenciesAreVisible) {
            if (position == 0) {
                return TYPE_CURRENCY_HEADER;
            } else if (position < mAssetHeaderPosition) {
                return TYPE_CURRENCY;
            } else if (position == mAssetHeaderPosition) {
                return TYPE_ASSET_HEADER;
            }
            return TYPE_ASSET;
        } else {
            return TYPE_ASSET;
        }
    }

    @Override
    @NonNull
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_CURRENCY_HEADER:
                return new CurrencyHeaderViewHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.card_view_currency_header, parent, false));
            case TYPE_CURRENCY:
                return new CurrencyItemViewHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.card_view_currency_item, parent, false));
            case TYPE_ASSET_HEADER:
                return new AssetSectionHeaderViewHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.card_view_asset_section_header, parent, false));
            case TYPE_ASSET:
                return new AssetItemViewHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.card_view_all_asset_item, parent, false));
        }
        return new EmptyListItemViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.empty_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof CurrencyItemViewHolder) {
            final Currency currency = mCurrenciesVisible.get(position - 1);
            ((CurrencyItemViewHolder) viewHolder).bind(currency, mOnCurrencyItemClickListener);
        } else if (viewHolder instanceof AssetItemViewHolder) {
            int assetPosition = mCurrenciesAreVisible ? (position - mAssetHeaderPosition - 1) : position;

            final Asset asset = mAssetsVisible.get(assetPosition);
            ((AssetItemViewHolder) viewHolder).bind(asset, mOnAssetItemClickListener);
        }
    }

    @Override
    public int getItemCount() {
        if (mCurrenciesAreVisible) {
            return 1 + mCurrenciesVisible.size() + 1 + mAssetsVisible.size();
        }
        return mAssetsVisible.size();
    }

    public void setCurrencies(final List<Currency> newCurrencies) {
        mCurrencies = newCurrencies;

        filter(mCurrentFilterText);

        notifyDataSetChanged();
    }

    public void setAssets(final List<Asset> newAssets) {
        mAssets = newAssets;

        filter(mCurrentFilterText);

        notifyDataSetChanged();
    }

    public void filter(@NonNull final String text) {
        Completable.fromRunnable(() -> {
            mCurrentFilterText = text;

            mCurrenciesVisible.clear();
            mAssetsVisible.clear();

            if (!mCurrentFilterText.isEmpty()) {
                mCurrentFilterText = mCurrentFilterText.toLowerCase();
                for (Currency currency : mCurrencies) {
                    if (currency.getCode().toLowerCase().contains(mCurrentFilterText) || currency.getName().toLowerCase().contains(mCurrentFilterText)) {
                        mCurrenciesVisible.add(currency);
                    }
                }

                for (Asset asset : mAssets) {
                    if (asset.getId().toLowerCase().contains(mCurrentFilterText) || asset.getName().toLowerCase().contains(mCurrentFilterText)
                            || asset.getSymbol().toLowerCase().contains(mCurrentFilterText)) {
                        mAssetsVisible.add(asset);
                    }
                }
            } else {
                mCurrenciesVisible.addAll(mCurrencies);
                mAssetsVisible.addAll(mAssets);
            }

            mAssetHeaderPosition = 0;
            if (mCurrenciesAreVisible) {
                mAssetHeaderPosition = mCurrenciesVisible.size() + 1;
            }
        })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::notifyDataSetChanged);
    }

    public void setCurrenciesVisible(final boolean visible) {
        mCurrenciesAreVisible = visible;

        mAssetHeaderPosition = 0;
        if (mCurrenciesAreVisible) {
            mAssetHeaderPosition = mCurrenciesVisible.size() + 1;
        }

        notifyDataSetChanged();
    }

    public boolean areCurrenciesVisible() {
        return mCurrenciesAreVisible;
    }

    public void setOnCurrencyItemClickListener(OnObjectItemClickListener<Currency> onCurrencyItemClickListener) {
        mOnCurrencyItemClickListener = onCurrencyItemClickListener;
    }

    public void setOnAssetItemClickListener(OnObjectItemClickListener<Asset> onAssetItemClickListener) {
        mOnAssetItemClickListener = onAssetItemClickListener;
    }
}