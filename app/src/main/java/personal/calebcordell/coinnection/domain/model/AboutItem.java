package personal.calebcordell.coinnection.domain.model;

import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.view.View;


public class AboutItem {

    private Drawable iconDrawable;
    private String title;
    private View.OnClickListener onClickListener;

    public AboutItem(final Drawable iconDrawable, final String title) {
        this.iconDrawable = iconDrawable;
        this.title = title;
    }

    public AboutItem setIconDrawable(final Drawable iconDrawable) {
        this.iconDrawable = iconDrawable;
        return this;
    }
    @Nullable public Drawable getIconDrawable() {
        return iconDrawable;
    }

    public AboutItem setTitle(final String title) {
        this.title = title;
        return this;
    }
    @Nullable public String getTitle() {
        return title;
    }

    public AboutItem setOnClickListener(final View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
        return this;
    }
    public View.OnClickListener getOnClickListener() {
        return onClickListener;
    }

    public static AboutItem Empty() {
        return new AboutItem(null, null);
    }
}
