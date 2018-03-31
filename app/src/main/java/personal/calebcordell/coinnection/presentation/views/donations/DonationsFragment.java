package personal.calebcordell.coinnection.presentation.views.donations;

import android.content.res.TypedArray;
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
import personal.calebcordell.coinnection.domain.model.DonationItem;
import personal.calebcordell.coinnection.presentation.util.OnObjectItemClickListener;
import personal.calebcordell.coinnection.presentation.util.donationrecyclerview.DonationRecyclerViewAdapter;
import personal.calebcordell.coinnection.presentation.views.base.BaseFragment;
import personal.calebcordell.coinnection.presentation.views.mainactivity.MainActivity;


public class DonationsFragment extends BaseFragment {
    private static final String TAG = DonationsFragment.class.getSimpleName();

    @BindView(R.id.donations_recycler_view)
    RecyclerView mDonationsRecyclerView;

    @BindArray(R.array.donation_wallet_names)
    String[] mDonationWalletNames;
    @BindArray(R.array.donation_wallet_addresses)
    String[] mDonationWalletAddresses;

    @Inject
    protected MainActivity mActivity;
    protected LinearLayoutManager mLinearLayoutManager;
    @Inject
    protected DonationRecyclerViewAdapter mDonationRecyclerViewAdapter;
    private Unbinder mUnbinder;

    public DonationsFragment() {
        // Required empty public constructor
    }

    public static Fragment newInstance() {
        return new DonationsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_donations, container, false);

        mUnbinder = ButterKnife.bind(this, view);

        List<DonationItem> donationItems = new ArrayList<>(mDonationWalletNames.length);

        TypedArray donationWalletIcons = getResources().obtainTypedArray(R.array.donation_wallet_icons);
        TypedArray donationWalletQRCodes = getResources().obtainTypedArray(R.array.donation_wallet_qrcodes);

        donationItems.add(DonationItem.Empty());
        for (int i = 0; i < mDonationWalletNames.length; i++) {
            donationItems.add(new DonationItem(
                    donationWalletIcons.getResourceId(i, -1),
                    donationWalletQRCodes.getResourceId(i, -1),
                    mDonationWalletNames[i],
                    mDonationWalletAddresses[i]));
        }

        donationWalletIcons.recycle();
        donationWalletQRCodes.recycle();

        mDonationRecyclerViewAdapter.setItems(donationItems);
        mDonationRecyclerViewAdapter.setOnDonationItemClickListener(mOnObjectItemClickListener);
        mLinearLayoutManager = new LinearLayoutManager(getContext());
        mDonationsRecyclerView.setAdapter(mDonationRecyclerViewAdapter);
        mDonationsRecyclerView.setLayoutManager(mLinearLayoutManager);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mActivity.setTitle(getString(R.string.title_donate));
        mActivity.setHomeAsUp(true);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        mDonationRecyclerViewAdapter.setOnDonationItemClickListener(null);

        mUnbinder.unbind();
    }

    public boolean onBackPressed() {
        mFragmentManager.popBackStack();
        return true;
    }

    private OnObjectItemClickListener<DonationItem> mOnObjectItemClickListener = (donationItem) ->
        mFragmentManager.beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .add(DonationWalletDialogFragment.newInstance(donationItem), TAG)
                .commit();
}
