package net.bonysoft.magicmirror.sfx;

import java.util.HashMap;
import java.util.Map;

import net.bonysoft.magicmirror.facerecognition.FaceExpression;

public final class SfxMappings {

    private final Map<FaceExpression, FacialExpressionEffects> mappings;

    public static SfxMappings newInstance() {
        Map<FaceExpression, FacialExpressionEffects> mappings = new HashMap<>();
        mappings.put(FaceExpression.SAD, FacialExpressionEffects.SAD);
        mappings.put(FaceExpression.NEUTRAL, FacialExpressionEffects.NEUTRAL);
        mappings.put(FaceExpression.HAPPY, FacialExpressionEffects.HAPPY);
        mappings.put(FaceExpression.JOYFUL, FacialExpressionEffects.JOYFUL);
        mappings.put(FaceExpression.LOOKING, FacialExpressionEffects.LOOKING);
        return new SfxMappings(mappings);
    }

    private SfxMappings(Map<FaceExpression, FacialExpressionEffects> mappings) {
        this.mappings = mappings;
    }

    public FacialExpressionEffects forExpression(FaceExpression expression) {
        return mappings.get(expression);
    }
}
