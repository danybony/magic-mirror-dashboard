package net.bonysoft.magicmirror.facerecognition;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;

@RunWith(MockitoJUnitRunner.class)
public class KeyboardFaceSourceTest {

    private static final int CODE_SAD = 1;
    private static final int CODE_HAPPY = 2;

    private KeyboardFaceSource source;
    @Mock
    private FaceTracker.FaceListener mockListener;
    @Mock
    private KeyToFaceMappings mockMappings;

    @Before
    public void setUp() throws Exception {
        source = new KeyboardFaceSource(mockListener, mockMappings);
        when(mockMappings.getFace(CODE_SAD)).thenReturn(FaceExpression.SAD);
        when(mockMappings.getFace(CODE_HAPPY)).thenReturn(FaceExpression.HAPPY);

        when(mockMappings.contains(CODE_HAPPY)).thenReturn(true);
        when(mockMappings.contains(CODE_SAD)).thenReturn(true);
    }

    @Test
    public void givenKeySadIsPressed_thenSadExpressionIsTriggered() {
        source.onKeyDown(CODE_SAD);
        verify(mockListener).onNewFace(FaceExpression.SAD);

    }

    @Test
    public void givenKeySadIsPressedTwice_thenSadExpressionIsTriggeredOnlyOnce() {
        // we are testing when user is keep the button pressed
        source.onKeyDown(CODE_SAD);
        source.onKeyDown(CODE_SAD);
        verify(mockListener, times(1)).onNewFace(FaceExpression.SAD);
    }

    @Test
    public void givenSadKeySadIsPressedAndReleased_thenLookingExpressionIsTriggered() {
        givenKeySadIsPressed_thenSadExpressionIsTriggered();
        source.onKeyUp(CODE_SAD);
        verify(mockListener).onNewFace(FaceExpression.LOOKING);
    }

    @Test
    public void givenUnsupportedKeyIsPressed_thenNoCallbacksAreTriggered() {
        source.onKeyDown(1337);
        verify(mockListener, times(0)).onNewFace(any(FaceExpression.class));
    }

    @Test
    public void givenAnUnsupportedKeyIsPressedAfterASupportedKey_thenLookingExpressionIsTriggeredOnlyOnce() {
        onPressAndReleaseKey(CODE_HAPPY);
        onPressAndReleaseKey(2000);
        verify(mockListener, times(1)).onNewFace(FaceExpression.LOOKING);
    }

    @Test
    public void givenAnTwoUnsupportedKeyIsPressedAfterAUnSupportedKey_thenLookingExpressionIsTriggeredOnlyOnce() {
        onPressAndReleaseKey(CODE_HAPPY);

        onPressAndReleaseKey(2001);
        onPressAndReleaseKey(2002);
        verify(mockListener, times(1)).onNewFace(FaceExpression.LOOKING);
    }

    private void onPressAndReleaseKey(int code) {
        source.onKeyDown(code);
        source.onKeyUp(code);
    }
}
