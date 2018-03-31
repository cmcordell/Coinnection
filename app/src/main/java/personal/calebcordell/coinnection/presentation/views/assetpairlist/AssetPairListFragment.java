package personal.calebcordell.coinnection.presentation.views.assetpairlist;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import personal.calebcordell.coinnection.R;
import personal.calebcordell.coinnection.domain.model.AssetPair;
import personal.calebcordell.coinnection.presentation.Constants;
import personal.calebcordell.coinnection.presentation.util.OnItemDragFinishedListener;
import personal.calebcordell.coinnection.presentation.util.OnObjectItemClickListener;
import personal.calebcordell.coinnection.presentation.util.assetpairlistrecyclerview.AssetPairItemTouchHelperCallback;
import personal.calebcordell.coinnection.presentation.util.assetpairlistrecyclerview.AssetPairListRecyclerViewAdapter;
import personal.calebcordell.coinnection.presentation.views.assetpairdetail.AssetPairDetailFragment;
import personal.calebcordell.coinnection.presentation.views.assetpairsetup.AssetPairSetupFragment;
import personal.calebcordell.coinnection.presentation.views.base.BaseFragment;
import personal.calebcordell.coinnection.presentation.views.mainactivity.MainActivity;


public class AssetPairListFragment extends BaseFragment implements AssetPairListContract.View {

    @BindView(R.id.empty_text_view)
    TextView mEmptyTextView;
    @BindView(R.id.asset_pair_list_recycler_view)
    RecyclerView mAssetPairListRecyclerView;
    @BindView(R.id.add_asset_pair_fab)
    FloatingActionButton mAddAssetPairButton;

    @Inject
    protected AssetPairListRecyclerViewAdapter mAssetPairListRecyclerViewAdapter;
    protected LinearLayoutManager mLinearLayoutManager;
    @Inject
    protected AssetPairItemTouchHelperCallback mCallback;

    @Inject
    protected AssetPairListContract.Presenter mPresenter;
    @Inject
    protected MainActivity mActivity;
    private Unbinder mUnbinder;

    public AssetPairListFragment() {}
    public static Fragment newInstance() {
        return new AssetPairListFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_asset_pair_list, container, false);

        mUnbinder = ButterKnife.bind(this, view);

//        mAssetPairListRecyclerViewAdapter.setOnAssetPairItemClickListener(mOnAssetPairItemClickListener);
        mAssetPairListRecyclerViewAdapter.setOnAssetPairItemDragFinishedListener(mOnAssetPairItemDragFinishedListener);

        mCallback.setItemTouchHelperAdapter(mAssetPairListRecyclerViewAdapter);
        ItemTouchHelper helper = new ItemTouchHelper(mCallback);
        helper.attachToRecyclerView(mAssetPairListRecyclerView);

        mLinearLayoutManager = new LinearLayoutManager(getContext());
        mAssetPairListRecyclerView.setAdapter(mAssetPairListRecyclerViewAdapter);
        mAssetPairListRecyclerView.setLayoutManager(mLinearLayoutManager);

        mAddAssetPairButton.setOnClickListener(v ->
                ViewCompat.postOnAnimationDelayed(mAddAssetPairButton, mPresenter::onAddAssetPairClicked, Constants.SELECTABLE_VIEW_ANIMATION_DELAY));

        mPresenter.setView(this);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mActivity.setTitle(getString(R.string.title_asset_pair_list));
        mActivity.setHomeAsUp(false);
        mAddAssetPairButton.show();
        mPresenter.resume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mAddAssetPairButton.hide();
        mPresenter.pause();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mPresenter.destroy();

//        mAssetPairListRecyclerViewAdapter.setOnAssetPairItemClickListener(null);

        mAddAssetPairButton.setOnClickListener(null);

        mUnbinder.unbind();
    }

    public boolean onBackPressed() {
        mFragmentManager.popBackStack(Constants.MAIN_FRAGMENT, 0);
        return true;
    }

    public void showEmptyText() {
        mAssetPairListRecyclerView.setVisibility(View.INVISIBLE);
        mEmptyTextView.setVisibility(View.VISIBLE);
    }

    public void hideEmptyText() {
        mEmptyTextView.setVisibility(View.INVISIBLE);
        mAssetPairListRecyclerView.setVisibility(View.VISIBLE);
    }

    public void showAssetPairs(@NonNull List<AssetPair> assetPairs) {
        mAssetPairListRecyclerViewAdapter.setAssetPairs(assetPairs);
        mAssetPairListRecyclerView.setVisibility(View.VISIBLE);
    }

    public void openAssetPairDetailViewUI(@NonNull AssetPair assetPair) {
        mFragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.slide_left_enter, R.anim.slide_left_exit,
                        R.anim.slide_right_enter, R.anim.slide_right_exit)
                .replace(R.id.content_frame, AssetPairDetailFragment.newInstance(assetPair), AssetPairDetailFragment.class.getName())
                .addToBackStack(null)
                .commit();
    }

    public void openAssetPairSetupUI() {
        mFragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.slide_left_enter, R.anim.slide_left_exit,
                        R.anim.slide_right_enter, R.anim.slide_right_exit)
                .replace(R.id.content_frame, AssetPairSetupFragment.newInstance(), AssetPairSetupFragment.class.getName())
                .addToBackStack(null)
                .commit();
    }

    public void showMessage(@NonNull String message) {
        Toast.makeText(mActivity, message, Toast.LENGTH_SHORT).show();
    }

    private OnObjectItemClickListener<AssetPair> mOnAssetPairItemClickListener =
            (assetPair) -> mPresenter.onAssetPairClicked(assetPair);

    private OnItemDragFinishedListener<List<AssetPair>> mOnAssetPairItemDragFinishedListener =
            (assetPairs) -> mPresenter.onAssetPairMoved(assetPairs);
}