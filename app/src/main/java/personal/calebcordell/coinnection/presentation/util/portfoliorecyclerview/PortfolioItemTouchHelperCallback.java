package personal.calebcordell.coinnection.presentation.util.portfoliorecyclerview;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import javax.inject.Inject;

import personal.calebcordell.coinnection.presentation.util.ItemTouchHelperAdapter;


public class PortfolioItemTouchHelperCallback extends ItemTouchHelper.Callback {

    private ItemTouchHelperAdapter mAdapter;

    private boolean isDragging = false;
    private boolean isSwiping = false;

    @Inject
    public PortfolioItemTouchHelperCallback() {}

    @Override
    public boolean isLongPressDragEnabled() {
        return true;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return false;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        int dragFlags = 0;
        if (viewHolder instanceof PortfolioRecyclerViewAdapter.PortfolioAssetItemViewHolder || viewHolder instanceof PortfolioRecyclerViewAdapter.WatchlistItemViewHolder) {
            dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        }
        return makeFlag(ItemTouchHelper.ACTION_STATE_DRAG, dragFlags);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        if ((viewHolder instanceof PortfolioRecyclerViewAdapter.PortfolioAssetItemViewHolder && target instanceof PortfolioRecyclerViewAdapter.PortfolioAssetItemViewHolder) ||
                (viewHolder instanceof PortfolioRecyclerViewAdapter.WatchlistItemViewHolder && target instanceof PortfolioRecyclerViewAdapter.WatchlistItemViewHolder)) {
            mAdapter.onItemMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());
            isDragging = true;
            return true;
        }

        return false;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        throw new UnsupportedOperationException();
    }

    //Called when action is finished
    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);

        if (isDragging) {
            mAdapter.onDragFinished();
            isDragging = false;
        } else if (isSwiping) {
            //Do something
            isSwiping = false;
        }
    }

    public void setItemTouchHelperAdapter(final ItemTouchHelperAdapter adapter) {
        mAdapter = adapter;
    }
}