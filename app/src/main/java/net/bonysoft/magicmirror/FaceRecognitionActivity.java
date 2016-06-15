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
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.MultiProcessor;
import com.google.android.gms.vision.Tracker;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;
import com.novoda.notils.logger.simple.Log;

import java.io.IOException;

import net.bonysoft.magicmirror.facerecognition.CameraSourcePreview;

public class FaceRecognitionActivity extends AppCompatActivity {

    private static final int CAMERA_PERMISSION_REQUEST = 0;
    private static final float CAMERA_SOURCE_REQUESTED_FPS = 30.0f;
    private static final int CAMERA_SOURCE_WIDTH = 640;
    private static final int CAMERA_SOURCE_HEIGHT = 360;

    private CameraSource cameraSource = null;
    private CameraSourcePreview preview;

    private SystemUIHider systemUIHider;
    private TextView textViewStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_face_recognition);

        textViewStatus = (TextView) findViewById(R.id.status);
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
            createCameraSource();
        }

        checkForPlayServices();
    }

    private void keepScreenOn() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    private void createCameraSource() {
        FaceDetector detector = new FaceDetector.Builder(this)
                .setClassificationType(FaceDetector.ALL_CLASSIFICATIONS)
                .build();

        detector.setProcessor(
                new MultiProcessor.Builder<>(new GraphicFaceTrackerFactory())
                        .build()
        );

        if (!detector.isOperational()) {
            textViewStatus.setText(R.string.face_detection_not_available_error);
            return;
        }

        cameraSource = new CameraSource.Builder(this, detector)
                .setRequestedPreviewSize(CAMERA_SOURCE_WIDTH, CAMERA_SOURCE_HEIGHT)
                .setFacing(CameraSource.CAMERA_FACING_FRONT)
                .setRequestedFps(CAMERA_SOURCE_REQUESTED_FPS)
                .build();
    }

    private void checkForPlayServices() {
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

        startCameraSource();
    }

    private void startCameraSource() {
        if (cameraSource == null) {
            return;
        }
        try {
            preview.start(cameraSource);
        } catch (IOException e) {
            Log.e("Unable to start camera source.", e);
            cameraSource.release();
            cameraSource = null;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == CAMERA_PERMISSION_REQUEST) {
            if (isPermissionGranted(grantResults)) {
                createCameraSource();
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
        if (cameraSource != null) {
            cameraSource.release();
            cameraSource = null;
        }
        systemUIHider.showSystemUi();
    }

    private class GraphicFaceTrackerFactory implements MultiProcessor.Factory<Face> {
        @Override
        public Tracker<Face> create(Face face) {
            return new GraphicFaceTracker();
        }
    }

    private class GraphicFaceTracker extends Tracker<Face> {

        @Override
        public void onUpdate(FaceDetector.Detections<Face> detectionResults, Face face) {
            float smiling = face.getIsSmilingProbability();
            String currentFeeling;
            if (smiling <= 0.1) {
                currentFeeling = ":-(";
            } else if (smiling <= 0.3) {
                currentFeeling = ":-/";
            } else if (smiling <= 0.5) {
                currentFeeling = ":-)";
            } else {
                currentFeeling = "boh :-D ?";
            }
            updateMessage(String.valueOf(smiling) + " " + currentFeeling);
        }

        @Override
        public void onMissing(FaceDetector.Detections<Face> detectionResults) {
            updateMessage("O_O");
        }

        @Override
        public void onDone() {
            updateMessage("O_O");
        }

        private void updateMessage(final String currentFeeling) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    textViewStatus.setText(currentFeeling);
                }
            });
        }
    }

}
