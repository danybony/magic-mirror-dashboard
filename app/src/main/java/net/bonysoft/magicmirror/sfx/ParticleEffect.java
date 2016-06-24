package net.bonysoft.magicmirror.sfx;

import android.widget.ImageView;

import java.util.List;

public interface ParticleEffect {

    void animateParticles(List<ImageView> particleViews, int parentWidth, int parentHeight);

}
