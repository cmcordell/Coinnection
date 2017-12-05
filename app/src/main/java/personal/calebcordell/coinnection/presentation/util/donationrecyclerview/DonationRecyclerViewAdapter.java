package personal.calebcordell.coinnection.presentation.util.donationrecyclerview;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import personal.calebcordell.coinnection.R;
import personal.calebcordell.coinnection.domain.model.DonationItem;
import personal.calebcordell.coinnection.presentation.util.OnObjectItemClickListener;

public class DonationRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_FIRST_ITEM = 1;
    private static final int TYPE_ITEM = 2;
    private static final int TYPE_LAST_ITEM = 3;
    private static final int TYPE_ONLY_ITEM = 4;

    private static final int HEADER_POSITION = 0;
    private static final int FIRST_ITEM_POSITION = 1;
    private int LAST_ITEM_POSITION = 1;

    private List<DonationItem> mItems = new ArrayList<>(9);
    private OnObjectItemClickListener<DonationItem> mOnObjectItemClickListener;

    public DonationRecyclerViewAdapter(List<DonationItem> items, OnObjectItemClickListener<DonationItem> onObjectItemClickListener) {
        mItems.add(DonationItem.Empty());
        mItems.addAll(items);
        mOnObjectItemClickListener = onObjectItemClickListener;
    }

    @Override
    public int getItemViewType(int position) {
        if(position == HEADER_POSITION) {
            return TYPE_HEADER;
        }
        return TYPE_ITEM;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, final int viewType) {
        switch (viewType) {
            case TYPE_HEADER:
                return new DonationHeaderViewHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.card_view_donation_header, parent, false));
            case TYPE_ITEM:
                return new DonationItemViewHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.card_view_donation_item, parent, false));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof DonationItemViewHolder) {
            ((DonationItemViewHolder) viewHolder).bind(mItems.get(position), mOnObjectItemClickListener);
        }
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }
}