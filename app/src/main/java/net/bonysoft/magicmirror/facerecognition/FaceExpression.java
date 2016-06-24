package net.bonysoft.magicmirror.facerecognition;

import com.novoda.notils.exception.DeveloperError;

public enum FaceExpression {
    SAD(0.05f),
    NEUTRAL(0.25f),
    HAPPY(0.7f),
    JOYFUL(1.0f),
    LOOKING(Float.MAX_VALUE);

    private final float threshold;

    FaceExpression(float threshold) {
        this.threshold = threshold;
    }

    public static FaceExpression fromSmilingProbability(float smilingProbability) {
        for (FaceExpression faceExpression : values()) {
            if (smilingProbability <= faceExpression.threshold) {
                return faceExpression;
            }
        }
        throw new DeveloperError("FaceExpression not found with smiling probability: " + smilingProbability);
    }

    public boolean isMissing() {
        return this == LOOKING;
    }
}
