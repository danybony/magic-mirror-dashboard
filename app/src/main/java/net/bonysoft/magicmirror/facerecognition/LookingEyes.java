package net.bonysoft.magicmirror.facerecognition;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

import net.bonysoft.magicmirror.R;

public class LookingEyes extends ImageView {

    public LookingEyes(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LookingEyes(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        setImageResource(R.drawable.eyes_left);
    }
}
