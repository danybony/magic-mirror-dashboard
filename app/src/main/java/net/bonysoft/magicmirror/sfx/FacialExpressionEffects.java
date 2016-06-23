package net.bonysoft.magicmirror.sfx;

import android.support.annotation.ColorRes;

import net.bonysoft.magicmirror.R;

public enum FacialExpressionEffects {
    LOOKING(R.color.black),
    NEUTRAL(R.color.green),
    SAD(R.color.blue_of_sadness),
    HAPPY(R.color.golden_hapiness),
    JOYFUL(R.color.jolly_pink);

    @ColorRes
    private final int glowColorRes;

    FacialExpressionEffects(@ColorRes int glowColorRes) {
        this.glowColorRes = glowColorRes;
    }

    @ColorRes
    public int glowColorRes() {
        return glowColorRes;
    }

}
