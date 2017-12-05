package personal.calebcordell.coinnection.presentation.util.portfoliorecyclerview;

import android.support.v7.util.ListUpdateCallback;
import android.support.v7.widget.RecyclerView;


public class OffsetListUpdateCallback implements ListUpdateCallback {

    private RecyclerView.Adapter<RecyclerView.ViewHolder> mAdapter;
    private int mOffset;

    public OffsetListUpdateCallback(RecyclerView.Adapter<RecyclerView.ViewHolder> adapter, int offset) {
        mAdapter = adapter;
        mOffset = offset;
    }

    @Override
    public void onInserted(int position, int count) {
        mAdapter.notifyItemRangeInserted(mOffset + position, count);
    }

    @Override
    public void onRemoved(int position, int count) {
        mAdapter.notifyItemRangeRemoved(mOffset + position, count);
    }

    @Override
    public void onMoved(int fromPosition, int toPosition) {
        mAdapter.notifyItemMoved(mOffset + fromPosition, mOffset + toPosition);
    }

    @Override
    public void onChanged(int position, int count, Object payload) {
        mAdapter.notifyItemRangeChanged(mOffset + position, count, payload);
    }
}
