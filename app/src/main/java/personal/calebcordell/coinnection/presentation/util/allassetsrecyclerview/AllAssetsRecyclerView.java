package personal.calebcordell.coinnection.presentation.util.allassetsrecyclerview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;


public class AllAssetsRecyclerView extends RecyclerView {
    private static final String TAG = AllAssetsRecyclerView.class.getSimpleName();

    public AllAssetsRecyclerView(Context context) {
        super(context);
    }

    public AllAssetsRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public AllAssetsRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    private static final long FLING_TIMEOUT = 600;
    private long lastFlingTime = 0;
    private int lastFlingVelocityY = 0;

    @Override
    public boolean fling(int velocityX, int velocityY) {
        if (lastFlingVelocityY >= 0 && velocityY > 0 || lastFlingVelocityY <= 0 && velocityY < 0) {
            if (System.currentTimeMillis() - lastFlingTime < FLING_TIMEOUT) {
                velocityY += lastFlingVelocityY;
            }
        }

        lastFlingVelocityY = velocityY;

        lastFlingTime = System.currentTimeMillis();

        return super.fling(velocityX, velocityY);
    }
}