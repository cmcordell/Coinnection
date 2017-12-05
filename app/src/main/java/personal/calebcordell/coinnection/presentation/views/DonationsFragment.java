package personal.calebcordell.coinnection.presentation.views;

import android.app.FragmentTransaction;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindArray;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import personal.calebcordell.coinnection.R;
import personal.calebcordell.coinnection.presentation.util.OnObjectItemClickListener;
import personal.calebcordell.coinnection.domain.model.DonationItem;
import personal.calebcordell.coinnection.presentation.util.donationrecyclerview.DonationRecyclerViewAdapter;


public class DonationsFragment extends BaseFragment {
    private static final String TAG = DonationsFragment.class.getSimpleName();

    @BindView(R.id.donations_recycler_view) RecyclerView mDonationsRecyclerView;

    @BindArray(R.array.donation_wallet_names) String[] mDonationWalletNames;
    @BindArray(R.array.donation_wallet_addresses) String[] mDonationWalletAddresses;

    private MainActivity mActivity;
    private Unbinder mUnbinder;

    public DonationsFragment() {
        // Required empty public constructor
    }
    public static Fragment newInstance() {
        return new DonationsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = (MainActivity) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_donations, container, false);

        mUnbinder = ButterKnife.bind(this, view);

        List<DonationItem> donationItems = new ArrayList<>(mDonationWalletNames.length);

        TypedArray donationWalletIcons = getResources().obtainTypedArray(R.array.donation_wallet_icons);
        TypedArray donationWalletQRCodes = getResources().obtainTypedArray(R.array.donation_wallet_qrcodes);

        for(int i=0; i < mDonationWalletNames.length; i++) {
            donationItems.add(new DonationItem(
                    donationWalletIcons.getResourceId(i, -1),
                    donationWalletQRCodes.getResourceId(i, -1),
                    mDonationWalletNames[i],
                    mDonationWalletAddresses[i]));
        }

        donationWalletIcons.recycle();
        donationWalletQRCodes.recycle();

        DonationRecyclerViewAdapter adapter = new DonationRecyclerViewAdapter(donationItems, mOnObjectItemClickListener);
        mDonationsRecyclerView.setAdapter(adapter);
        mDonationsRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mActivity.setTitle(getString(R.string.title_donate));
    }

    @Override
    public void onDestroyView() {
        mUnbinder.unbind();
        super.onDestroyView();
    }

    public boolean onBackPressed() {
        getFragmentManager().popBackStackImmediate();
        return true;
    }

    private OnObjectItemClickListener<DonationItem> mOnObjectItemClickListener = new OnObjectItemClickListener<DonationItem>() {
        @Override
        public void onObjectItemClick(DonationItem item) {
            getFragmentManager().beginTransaction()
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .add(DonationWalletDialogFragment.newInstance(item), TAG)
                    .commit();
        }
    };
}
