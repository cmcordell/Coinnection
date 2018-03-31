package personal.calebcordell.coinnection.presentation.views.licenses;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindArray;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import personal.calebcordell.coinnection.R;
import personal.calebcordell.coinnection.domain.model.LicenseItem;
import personal.calebcordell.coinnection.presentation.util.OnObjectItemClickListener;
import personal.calebcordell.coinnection.presentation.util.licenserecyclerview.LicenseRecyclerViewAdapter;
import personal.calebcordell.coinnection.presentation.views.base.BaseFragment;
import personal.calebcordell.coinnection.presentation.views.mainactivity.MainActivity;


public class LicensesFragment extends BaseFragment {
    private static final String TAG = LicensesFragment.class.getSimpleName();

    @BindView(R.id.licenses_recycler_view)
    RecyclerView mLicensesRecyclerView;

    @BindArray(R.array.license_titles)
    String[] mLicenseTitles;
    @BindArray(R.array.license_texts)
    String[] mLicenseTexts;

    @Inject
    protected MainActivity mActivity;
    @Inject
    protected LicenseRecyclerViewAdapter mLicenseRecyclerViewAdapter;
    protected LinearLayoutManager mLinearLayoutManager;
    private Unbinder mUnbinder;

    public LicensesFragment() {}
    public static Fragment newInstance() {
        return new LicensesFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_licenses, container, false);

        mUnbinder = ButterKnife.bind(this, view);

        List<LicenseItem> licenseItems = new ArrayList<>(mLicenseTitles.length);

        for (int i = 0; i < mLicenseTitles.length; i++) {
            licenseItems.add(new LicenseItem(mLicenseTitles[i], mLicenseTexts[i]));
        }

        mLicenseRecyclerViewAdapter.setItems(licenseItems);
        mLicenseRecyclerViewAdapter.setOnLicenseItemClickListener(mOnLicenseItemClickListener);
        mLinearLayoutManager = new LinearLayoutManager(getContext());
        mLicensesRecyclerView.setAdapter(mLicenseRecyclerViewAdapter);
        mLicensesRecyclerView.setLayoutManager(mLinearLayoutManager);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mActivity.setTitle(getString(R.string.title_licenses));
        mActivity.setHomeAsUp(true);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        mLicenseRecyclerViewAdapter.setOnLicenseItemClickListener(null);

        mUnbinder.unbind();
    }

    public boolean onBackPressed() {
        mFragmentManager.popBackStack();
        return true;
    }

    private OnObjectItemClickListener<LicenseItem> mOnLicenseItemClickListener = (licenseItem) ->
        mFragmentManager.beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .add(LicenseDialogFragment.newInstance(licenseItem), TAG)
                .commit();
}
