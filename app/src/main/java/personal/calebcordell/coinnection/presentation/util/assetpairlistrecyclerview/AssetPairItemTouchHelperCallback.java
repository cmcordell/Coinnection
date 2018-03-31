package personal.calebcordell.coinnection.presentation.util.assetpairlistrecyclerview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import javax.inject.Inject;

import personal.calebcordell.coinnection.R;
import personal.calebcordell.coinnection.presentation.util.ItemTouchHelperAdapter;


public class AssetPairItemTouchHelperCallback extends ItemTouchHelper.Callback {

    private ItemTouchHelperAdapter mAdapter;

    private boolean mIsDragging = false;
    private boolean mIsSwiping = false;

    private Drawable mSwipeBackground;
    private VectorDrawableCompat mDeleteIcon;
    private boolean mInitiated = false;

    private void init(@NonNull final Context context) {
        mSwipeBackground = new ColorDrawable(ContextCompat.getColor(context, R.color.colorDeleteBackground));
        mDeleteIcon = VectorDrawableCompat.create(context.getResources(), R.drawable.ic_delete_white, null);
        mInitiated = true;
    }

    @Inject
    public AssetPairItemTouchHelperCallback() {}

    @Override
    public boolean isLongPressDragEnabled() {
        return true;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return true;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        return makeMovementFlags(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.LEFT);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        mAdapter.onItemMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());
        mIsDragging = true;
        return true;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        mAdapter.onItemDismiss(viewHolder.getAdapterPosition());
        mIsSwiping = true;
    }

    //Called when action is finished
    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);

        if (mIsDragging) {
            mAdapter.onDragFinished();
            mIsDragging = false;
        } else if (mIsSwiping) {
            mAdapter.onDragFinished();
            mIsSwiping = false;
        }
    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                            float dX, float dY, int actionState, boolean isCurrentlyActive) {

        View itemView = viewHolder.itemView;

        if(!mInitiated) {
            init(itemView.getContext());
        }

        mSwipeBackground.setBounds(itemView.getRight() + (int) dX, itemView.getTop(), itemView.getRight(), itemView.getBottom());
        mSwipeBackground.draw(c);

        int itemHeight = itemView.getBottom() - itemView.getTop();
        int intrinsicWidth = mDeleteIcon.getIntrinsicWidth();
        int intrinsicHeight = mDeleteIcon.getIntrinsicWidth();

        int iconTop = itemView.getTop() + (itemHeight - intrinsicHeight) / 2;
        int iconMargin = (itemHeight - intrinsicHeight) / 2;
        int iconLeft = itemView.getRight() - iconMargin - intrinsicWidth;
        int iconRight = itemView.getRight() - iconMargin;
        int iconBottom = iconTop + intrinsicHeight;

        mDeleteIcon.setBounds(iconLeft, iconTop, iconRight, iconBottom);
        mDeleteIcon.draw(c);


        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
    }

    public void setItemTouchHelperAdapter(final ItemTouchHelperAdapter adapter) {
        mAdapter = adapter;
    }
}
