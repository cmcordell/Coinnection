package personal.calebcordell.coinnection.presentation;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.animation.FastOutLinearInInterpolator;
import android.view.View;
import android.view.ViewAnimationUtils;

import personal.calebcordell.coinnection.domain.model.animation.RevealAnimationSetting;


public class AnimationUtils {
    public static boolean isAnimating = false;

    public static void registerCircularRevealAnimation(final Context context, final View view, final RevealAnimationSetting revealSettings) {
        view.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                v.removeOnLayoutChangeListener(this);
                int cx = revealSettings.getCenterX();
                int cy = revealSettings.getCenterY();
                int width = revealSettings.getWidth();
                int height = revealSettings.getHeight();
                int duration = context.getResources().getInteger(android.R.integer.config_shortAnimTime);

                //Simply use the diagonal of the view
                float finalRadius = (float) Math.sqrt(width * width + height * height);
                Animator anim = ViewAnimationUtils.createCircularReveal(v, cx, cy, 0, finalRadius).setDuration(duration);
                anim.setInterpolator(new FastOutLinearInInterpolator());
                anim.start();
            }
        });
    }

    public static void startCircularExitAnimation(final Context context, final View view, final RevealAnimationSetting revealSettings, final OnDismissedListener listener) {
        int cx = revealSettings.getCenterX();
        int cy = revealSettings.getCenterY();
        int width = revealSettings.getWidth();
        int height = revealSettings.getHeight();
        int duration = context.getResources().getInteger(android.R.integer.config_shortAnimTime);

        float initRadius = (float) Math.sqrt(width * width + height * height);
        Animator anim = ViewAnimationUtils.createCircularReveal(view, cx, cy, initRadius, 0);
        anim.setDuration(duration);
        anim.setInterpolator(new FastOutLinearInInterpolator());
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                ViewCompat.postOnAnimationDelayed(view, () -> {
                    view.setDrawingCacheEnabled(false);
                    listener.onDismissed();
                    isAnimating = false;
                }, Constants.SELECTABLE_VIEW_ANIMATION_DELAY);
            }
        });
        isAnimating = true;
        view.setDrawingCacheEnabled(true);
        anim.start();
    }

    public interface OnDismissedListener {
        void onDismissed();
    }
}