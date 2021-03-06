package personal.calebcordell.coinnection.presentation.util.donationrecyclerview;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import personal.calebcordell.coinnection.R;
import personal.calebcordell.coinnection.domain.model.DonationItem;
import personal.calebcordell.coinnection.presentation.util.EmptyListItemViewHolder;
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

    private List<DonationItem> mItems = new ArrayList<>();
    private OnObjectItemClickListener<DonationItem> mOnDonationItemClickListener;

    @Inject
    public DonationRecyclerViewAdapter() {}

    @Override
    public int getItemViewType(int position) {
        if (position == HEADER_POSITION) {
            return TYPE_HEADER;
        }
        return TYPE_ITEM;
    }

    @Override
    @NonNull
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, final int viewType) {
        switch (viewType) {
            case TYPE_HEADER:
                return new DonationHeaderViewHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.card_view_donation_header, parent, false));
            case TYPE_ITEM:
                return new DonationItemViewHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.card_view_donation_item, parent, false));
        }
        return new EmptyListItemViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.empty_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof DonationItemViewHolder) {
            ((DonationItemViewHolder) viewHolder).bind(mItems.get(position), mOnDonationItemClickListener);
        }
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public void setItems(@NonNull final List<DonationItem> items) {
        mItems = items;
        notifyDataSetChanged();
    }

    public void setOnDonationItemClickListener(final OnObjectItemClickListener<DonationItem> onDonationItemClickListener) {
        mOnDonationItemClickListener = onDonationItemClickListener;
    }
}