package personal.calebcordell.coinnection.presentation.util;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;


public class RecyclerViewScrollDisabler implements RecyclerView.OnItemTouchListener {

    private boolean mIsScrollEnabled = true;

    public RecyclerViewScrollDisabler(boolean isScrollEnabled) {
        this.mIsScrollEnabled = isScrollEnabled;
    }

    public boolean isScrollEnabled() {
        return mIsScrollEnabled;
    }

    public void setScrollEnabled(boolean scrollEnabled) {
        mIsScrollEnabled = scrollEnabled;
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        Log.d("RecycViewScrollDisabler", "MotionEvent = "+e.getAction());
        switch(e.getAction()) {
            case MotionEvent.ACTION_SCROLL:
                return !mIsScrollEnabled;
            default:
                return false;
        }
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {}

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept){}
}