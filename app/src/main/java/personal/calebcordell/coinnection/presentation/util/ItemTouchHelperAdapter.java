package personal.calebcordell.coinnection.presentation.util;


public interface ItemTouchHelperAdapter {
    boolean onItemMove(int fromPosition, int toPosition);

    void onItemDismiss(int position);

    void onDragFinished();
}