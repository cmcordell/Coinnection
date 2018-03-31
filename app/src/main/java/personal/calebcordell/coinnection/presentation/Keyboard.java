package personal.calebcordell.coinnection.presentation;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import personal.calebcordell.coinnection.dagger.module.AppModule;


@Singleton
public class Keyboard {

    private final Context mContext;

    @Inject
    public Keyboard(@Named(AppModule.APP_CONTEXT) Context context) {
        mContext = context;
    }

    public void show(final View view) {
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
            }
        }
    }

    public void hide(final View view) {
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
    }
}