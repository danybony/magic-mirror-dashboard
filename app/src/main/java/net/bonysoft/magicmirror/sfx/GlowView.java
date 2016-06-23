package net.bonysoft.magicmirror.sfx;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.support.annotation.ColorRes;
import android.util.AttributeSet;
import android.view.View;

public class GlowView extends View {

    public GlowView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void transistToColor(@ColorRes int colorRes) {
        // TODO timeout if too fast
        Drawable previousBackground = getPreviousDrawableSafely();
        TransitionDrawable transitionDrawable = new TransitionDrawable(new Drawable[]{
                previousBackground,
                new ColorDrawable(getColor(colorRes))
        });
        setBackground(transitionDrawable);
        transitionDrawable.startTransition(400);
    }

    private Drawable getPreviousDrawableSafely() {
        Drawable previousDrawable = getBackground();
        if (previousDrawable == null) {
            int transparentRes = getColor(android.R.color.transparent);
            return new ColorDrawable(transparentRes);
        }
        return previousDrawable;
    }

    private int getColor(@ColorRes int colorRes) {
        return getResources().getColor(colorRes);
    }
}
