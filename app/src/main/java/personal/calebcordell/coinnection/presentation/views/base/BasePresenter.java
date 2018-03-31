package personal.calebcordell.coinnection.presentation.views.base;

import android.support.annotation.CallSuper;


public abstract class BasePresenter<V> {

    protected abstract void initialize();

    @CallSuper
    public void destroy() {
        mView = null;
    }

    protected V mView;

    public void setView(V view) {
        mView = view;
        initialize();
    }
}