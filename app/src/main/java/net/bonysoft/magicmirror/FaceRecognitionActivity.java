package net.bonysoft.magicmirror;

import android.Manifest;
import android.app.Dialog;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.novoda.notils.logger.simple.Log;

import net.bonysoft.magicmirror.facerecognition.CameraSourcePreview;
import net.bonysoft.magicmirror.facerecognition.FaceCameraSource;
import net.bonysoft.magicmirror.facerecognition.FaceDetectionUnavailableException;
import net.bonysoft.magicmirror.facerecognition.FaceExpression;
import net.bonysoft.magicmirror.facerecognition.FaceReactionSource;
import net.bonysoft.magicmirror.facerecognition.FaceTracker;

public class FaceRecognitionActivity extends AppCompatActivity {

    private static final int CAMERA_PERMISSION_REQUEST = 0;

    private FaceReactionSource faceSource;
    private CameraSourcePreview preview;

    private SystemUIHider systemUIHider;
    private TextView faceStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_face_recognition);

        faceStatus = (TextView) findViewById(R.id.status);
        preview = (CameraSourcePreview) findViewById(R.id.preview);

        systemUIHider = new SystemUIHider(findViewById(android.R.id.content));
        keepScreenOn();

        boolean hasCamera = getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT);
        if (!hasCamera) {
            Toast.makeText(this, R.string.no_camera_available_error, Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.CAMERA},
                    CAMERA_PERMISSION_REQUEST
            );
        } else {
            tryToCreateCameraSource();
        }
        displayErrorIfPlayServicesMissing();
    }

    private void keepScreenOn() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    private void tryToCreateCameraSource() {
        try {
            faceSource = FaceCameraSource.createFrom(this, faceListener, preview);
        } catch (FaceDetectionUnavailableException e) {
            Toast.makeText(this, R.string.face_detection_not_available_error, Toast.LENGTH_LONG).show();
        }
    }

    private void displayErrorIfPlayServicesMissing() {
        int code = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this);
        if (code != ConnectionResult.SUCCESS) {
            Dialog dlg = GoogleApiAvailability.getInstance().getErrorDialog(this, code, 101);
            dlg.show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        systemUIHider.hideSystemUi();

        if (faceSourceHasBeenDefined()) {
            faceSource.start();
        }
    }

    private boolean faceSourceHasBeenDefined() {
        return faceSource != null;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == CAMERA_PERMISSION_REQUEST) {
            if (isPermissionGranted(grantResults)) {
                tryToCreateCameraSource();
            } else {
                Log.e("User denied CAMERA permission");
                finish();
            }
        }
    }

    private boolean isPermissionGranted(@NonNull int[] grantResults) {
        return grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    protected void onPause() {
        super.onPause();
        preview.stop();
        systemUIHider.showSystemUi();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (faceSourceHasBeenDefined()) {
            faceSource.release();
        }
    }

    private final FaceTracker.FaceListener faceListener = new FaceTracker.FaceListener() {
        @Override
        public void onNewFace(final FaceExpression expression) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    faceStatus.setText(expression.toString());
                }
            });
        }
    };
}
