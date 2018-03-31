package personal.calebcordell.coinnection.presentation.views.assetpairsetup;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindArray;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import personal.calebcordell.coinnection.R;
import personal.calebcordell.coinnection.domain.model.Asset;
import personal.calebcordell.coinnection.domain.model.AssetPair;
import personal.calebcordell.coinnection.domain.model.Currency;
import personal.calebcordell.coinnection.presentation.util.MaterialPaddingItemDecoration;
import personal.calebcordell.coinnection.presentation.util.OnObjectItemClickListener;
import personal.calebcordell.coinnection.presentation.util.assetpairselectorrecyclerview.AssetPairSelectorRecyclerViewAdapter;
import personal.calebcordell.coinnection.presentation.views.base.BaseFragment;
import personal.calebcordell.coinnection.presentation.views.mainactivity.MainActivity;
import personal.calebcordell.coinnection.widgets.assetpair.AssetPairWidgetSetupActivity;


public class AssetPairSetupFragment extends BaseFragment implements AssetPairSetupContract.View {
    private static final String TAG = AssetPairSetupFragment.class.getSimpleName();

    @Inject
    protected AssetPairSetupContract.Presenter mPresenter;

    @BindView(R.id.search_view)
    protected SearchView mSearchView;
    @BindView(R.id.asset_search_results_recycler_view)
    protected RecyclerView mRecyclerView;
    @Inject
    protected AssetPairSelectorRecyclerViewAdapter mAdapter;
    protected LinearLayoutManager mLinearLayoutManager;

    @Inject
    protected Activity mActivity;
    private Unbinder mUnbinder;

    @BindString(R.string.search_hint)
    protected String mSearchHint;
    @BindString(R.string.title_widget_asset_picker)
    String mTitleString;
    @BindString(R.string.symbol_dash)
    String mDashSymbolString;

    @BindArray(R.array.currency_codes)
    String[] mCurrencyCodes;
    @BindArray(R.array.currency_names)
    String[] mCurrencyNames;

    private AssetPair mAssetPair = new AssetPair();


    public AssetPairSetupFragment() {}
    public static Fragment newInstance() {
        return new AssetPairSetupFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_asset_pair_setup, container, false);

        mUnbinder = ButterKnife.bind(this, view);

        if (mActivity instanceof MainActivity) {
            ((MainActivity) mActivity).setHomeAsUp(true);
        } else if (mActivity instanceof AssetPairWidgetSetupActivity) {
            mPresenter.setIsWidgetSetup();
        }

        mSearchView.setIconifiedByDefault(false);
        mSearchView.setQueryHint(mSearchHint);
        mSearchView.setOnQueryTextListener(mOnQueryTextListener);

        mAdapter.setOnAssetItemClickListener(mOnAssetItemClickListener);
        mAdapter.setOnCurrencyItemClickListener(mOnCurrencyItemClickListener);

        mLinearLayoutManager = new LinearLayoutManager(getContext());

        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.addItemDecoration(new MaterialPaddingItemDecoration(mActivity));

        List<Currency> currencies = new ArrayList<>(mCurrencyCodes.length);
        for (int i = 0; i < mCurrencyCodes.length; i++) {
            currencies.add(new Currency(mCurrencyCodes[i], mCurrencyNames[i]));
        }
        setCurrencies(currencies);

        mPresenter.setView(this);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateTitle();
        if (mActivity instanceof MainActivity) {
            ((MainActivity) mActivity).setActionBarElevation(0);
        } else if (mActivity instanceof AssetPairWidgetSetupActivity) {
            ((AssetPairWidgetSetupActivity) mActivity).setActionBarElevation(0);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mActivity instanceof MainActivity) {
            ((MainActivity) mActivity).setActionBarElevation(4);
        } else if (mActivity instanceof AssetPairWidgetSetupActivity) {
            ((AssetPairWidgetSetupActivity) mActivity).setActionBarElevation(4);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mPresenter.destroy();

        mSearchView.setOnQueryTextListener(null);

        mAdapter.setOnAssetItemClickListener(null);
        mAdapter.setOnCurrencyItemClickListener(null);

        mUnbinder.unbind();
    }

    @Override
    public boolean onBackPressed() {
        if (mAdapter.areCurrenciesVisible()) {
            mAssetPair.setId("");
            mAssetPair.setSymbol("");
            mAssetPair.setQuoteCurrencySymbol("");
            setCurrenciesVisible(false);
            updateTitle();
            return true;
        } else {
            if (mActivity instanceof MainActivity) {
                ((MainActivity) mActivity).hideKeyboard();
                mFragmentManager.popBackStack();
                return true;
            }
            return false;
        }
    }

    public void setAssets(@NonNull List<Asset> assets) {
        mAdapter.setAssets(assets);
    }

    public void setCurrencies(@NonNull List<Currency> currencies) {
        mAdapter.setCurrencies(currencies);
    }

    public void setCurrenciesVisible(boolean visible) {
        mAdapter.setCurrenciesVisible(visible);
    }

    public void assetPairLoaded(@NonNull AssetPair assetPair) {
        if (mActivity instanceof AssetPairWidgetSetupActivity) {
            ((AssetPairWidgetSetupActivity) mActivity).assetPairSelected(assetPair);
        } else if (mActivity instanceof MainActivity) {
            ((MainActivity) mActivity).hideKeyboard();
            mFragmentManager.popBackStack();
        }
    }

    public void showMessage(@NonNull String message) {
        Toast.makeText(mActivity, message, Toast.LENGTH_SHORT).show();
    }

    private void updateTitle() {
        String firstAssetString = mAssetPair.getSymbol();
        if (firstAssetString.equals("")) {
            firstAssetString = mDashSymbolString;
        }

        String secondAssetString = mAssetPair.getQuoteCurrencySymbol();
        if (secondAssetString.equals("")) {
            secondAssetString = mDashSymbolString;
        }

        mActivity.setTitle(String.format("%s %s/%s", mTitleString, firstAssetString, secondAssetString));
    }

    public void setQuery(String query) {
        mAdapter.filter(query);
    }


    private SearchView.OnQueryTextListener mOnQueryTextListener = new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String query) {
            setQuery(query);
            return true;
        }

        @Override
        public boolean onQueryTextChange(String newText) {
            setQuery(newText);
            return true;
        }
    };

    private OnObjectItemClickListener<Asset> mOnAssetItemClickListener = new OnObjectItemClickListener<Asset>() {
        @Override
        public void onObjectItemClick(Asset asset) {
            if (mAdapter.areCurrenciesVisible()) {
                mAssetPair.setQuoteCurrencySymbol(asset.getSymbol());
                updateTitle();
                mPresenter.onAssetPairSelected(mAssetPair);
            } else {
                mAssetPair.setId(asset.getId());
                mAssetPair.setSymbol(asset.getSymbol());
                setCurrenciesVisible(true);
                mLinearLayoutManager.scrollToPosition(0);
                updateTitle();
            }
        }
    };

    private OnObjectItemClickListener<Currency> mOnCurrencyItemClickListener = new OnObjectItemClickListener<Currency>() {
        @Override
        public void onObjectItemClick(Currency currency) {
            mAssetPair.setQuoteCurrencySymbol(currency.getCode());
            updateTitle();
            mPresenter.onAssetPairSelected(mAssetPair);
        }
    };
}