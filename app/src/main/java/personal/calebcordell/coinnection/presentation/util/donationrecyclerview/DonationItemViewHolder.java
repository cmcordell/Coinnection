package personal.calebcordell.coinnection.presentation.util.donationrecyclerview;

import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import personal.calebcordell.coinnection.R;
import personal.calebcordell.coinnection.domain.model.DonationItem;
import personal.calebcordell.coinnection.presentation.util.OnObjectItemClickListener;


public class DonationItemViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.donation_item_icon) ImageView mDonationItemIcon;
    @BindView(R.id.donation_item_name) TextView mDonationItemName;
    @BindView(R.id.donation_item_address) TextView mDonationItemAddress;

    DonationItemViewHolder(final View itemView) {
        super(itemView);

        ButterKnife.bind(this, itemView);
    }

    public void bind(final DonationItem item, final OnObjectItemClickListener<DonationItem> onObjectItemClickListener) {
        mDonationItemIcon.setImageResource(item.getIconDrawableRes());
        if(item.getName() != null) {
            mDonationItemName.setText(item.getName());
        }
        if(item.getAddress() != null) {
            mDonationItemAddress.setText(item.getAddress());
        }
        if(onObjectItemClickListener != null) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ViewCompat.postOnAnimationDelayed(view, new Runnable() {
                        @Override
                        public void run() {
                            onObjectItemClickListener.onObjectItemClick(item);
                        }
                    }, 50);
                }
            });
        }
    }
}