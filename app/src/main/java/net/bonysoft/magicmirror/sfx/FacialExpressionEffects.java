package net.bonysoft.magicmirror.sfx;

import android.support.annotation.ColorRes;

import net.bonysoft.magicmirror.R;

public enum FacialExpressionEffects {
    SEARCHING(R.color.black),
    NO_EXPRESSION(R.color.green),
    SADNESS(R.color.blue_of_sadness),
    HAPPINESS(R.color.golden_hapiness),
    JOYFULNESS(R.color.jolly_pink);

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
