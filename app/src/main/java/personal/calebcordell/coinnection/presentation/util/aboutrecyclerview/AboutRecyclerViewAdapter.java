package personal.calebcordell.coinnection.presentation.util.aboutrecyclerview;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import personal.calebcordell.coinnection.R;
import personal.calebcordell.coinnection.domain.model.AboutItem;
import personal.calebcordell.coinnection.presentation.util.EmptyListItemViewHolder;


public class AboutRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 2;

    private static final int HEADER_POSITION = 0;

    private List<AboutItem> mItems = new ArrayList<>();

    @Inject
    public AboutRecyclerViewAdapter() {}

    @Override
    public int getItemViewType(int position) {
        if (position == HEADER_POSITION) {
            return TYPE_HEADER;
        } else {
            return TYPE_ITEM;
        }
    }

    @Override
    @NonNull
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, final int viewType) {
        switch (viewType) {
            case TYPE_HEADER:
                return new AboutHeaderViewHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.card_view_about_header, parent, false));
            case TYPE_ITEM:
                return new AboutItemViewHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.card_view_about_item, parent, false));
        }
        return new EmptyListItemViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.empty_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof AboutItemViewHolder) {
            ((AboutItemViewHolder) viewHolder).bind(mItems.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public void setItems(List<AboutItem> items) {
        mItems = items;
        notifyDataSetChanged();
    }
}