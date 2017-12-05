package personal.calebcordell.coinnection.presentation.util.aboutrecyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import personal.calebcordell.coinnection.R;
import personal.calebcordell.coinnection.domain.model.AboutItem;


public class AboutRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_FIRST_ITEM = 1;
    private static final int TYPE_ITEM = 2;
    private static final int TYPE_LAST_ITEM = 3;

    private static final int HEADER_POSITION = 0;
    private static final int FIRST_ITEM_POSITION = 1;

    private List<AboutItem> mItems = new ArrayList<>(7);

    public AboutRecyclerViewAdapter(List<AboutItem> items) {
        mItems.add(AboutItem.Empty());
        mItems.addAll(items);
    }

    @Override
    public int getItemViewType(int position) {
        if(position == HEADER_POSITION) {
            return TYPE_HEADER;
        } else if(position == FIRST_ITEM_POSITION) {
            return TYPE_FIRST_ITEM;
        } else if(position < getItemCount() - 1) {
            return TYPE_ITEM;
        } else {
            return TYPE_LAST_ITEM;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, final int viewType) {
        switch(viewType) {
            case TYPE_HEADER:
                return new AboutHeaderViewHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.card_view_about_header, parent, false));
            case TYPE_FIRST_ITEM:
                return new AboutItemViewHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.card_view_about_first_item, parent, false));
            case TYPE_ITEM:
                return new AboutItemViewHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.card_view_about_item, parent, false));
            case TYPE_LAST_ITEM:
                return new AboutItemViewHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.card_view_about_item, parent, false));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        if(viewHolder instanceof AboutItemViewHolder) {
            ((AboutItemViewHolder) viewHolder).bind(mItems.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }
}