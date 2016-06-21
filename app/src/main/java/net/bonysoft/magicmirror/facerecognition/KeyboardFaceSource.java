package net.bonysoft.magicmirror.facerecognition;

public class KeyboardFaceSource implements FaceReactionSource {

    private static final FaceExpression NEUTRAL_EXPRESSION = FaceExpression.LOOKING;
    private static final int KEY_NEUTRAL = -1;

    private final FaceTracker.FaceListener faceListener;
    private final KeyToFaceMappings mappings;

    private int currentPress = KEY_NEUTRAL;

    public KeyboardFaceSource(FaceTracker.FaceListener faceListener, KeyToFaceMappings mappings) {
        this.faceListener = faceListener;
        this.mappings = mappings;
    }

    public boolean onKeyDown(int keyCode) {
        if (sameKeyCodeIsTriggered(keyCode) || isStillUnsupportedCode(keyCode)) {
            return false;
        }
        FaceExpression faceExpression = mappings.getFace(keyCode);
        if (faceExpression != null) {
            currentPress = keyCode;
            faceListener.onNewFace(faceExpression);
            return true;
        }
        currentPress = KEY_NEUTRAL;
        return false;
    }

    private boolean sameKeyCodeIsTriggered(int keyCode) {
        return currentPress == keyCode;
    }

    private boolean isStillUnsupportedCode(int keyCode) {
        return currentPress == KEY_NEUTRAL && !mappings.contains(keyCode);
    }

    public boolean onKeyUp(int keyCode) {
        if (sameKeyCodeIsTriggered(keyCode)) {
            faceListener.onNewFace(NEUTRAL_EXPRESSION);
            currentPress = KEY_NEUTRAL;
        }
        return true;
    }

    @Override
    public void start() {
        // no-op
    }

    @Override
    public void release() {
        // no-op
    }
}
