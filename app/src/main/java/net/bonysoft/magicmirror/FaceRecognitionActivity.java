package net.bonysoft.magicmirror;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

public class FaceRecognitionActivity extends AppCompatActivity {

    private SystemUIHider systemUIHider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_face_recognition);

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
            // TODO: init camera source
        }

    }

    private void keepScreenOn() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    @Override
    protected void onResume() {
        super.onResume();
        systemUIHider.hideSystemUi();

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == CAMERA_PERMISSION_REQUEST) {
            if (isPermissionGranted(grantResults)) {
                // TODO: init camera source
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
        systemUIHider.showSystemUi();
    }

}
