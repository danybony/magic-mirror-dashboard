package net.bonysoft.magicmirror.facerecognition;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

import net.bonysoft.magicmirror.R;

public class LookingEyes extends ImageView {

    private static final long LOOKING_DELAY_MILLIS = 500;

    private LookingDirection lookingDirection;

    public LookingEyes(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LookingEyes(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        updateLookingDirection(LookingDirection.LEFT);
    }

    private void updateLookingDirection(LookingDirection direction) {
        lookingDirection = direction;
        if (direction == LookingDirection.LEFT) {
            setImageResource(R.drawable.eyes_left);
        } else {
            setImageResource(R.drawable.eyes_right);
        }
    }

    public void show() {
        setVisibility(VISIBLE);
        getHandler().postDelayed(swapDirection, LOOKING_DELAY_MILLIS);
    }

    public void hide() {
        getHandler().removeCallbacks(swapDirection);
        setVisibility(GONE);
    }

    private final Runnable swapDirection = new Runnable() {
        @Override
        public void run() {
            updateLookingDirection(lookingDirection.next());
            getHandler().postDelayed(this, LOOKING_DELAY_MILLIS);
        }
    };

    private enum LookingDirection {
        LEFT,
        RIGHT;

        public LookingDirection next() {
            if (this == LEFT) {
                return RIGHT;
            } else {
                return LEFT;
            }
        }
    }
}
