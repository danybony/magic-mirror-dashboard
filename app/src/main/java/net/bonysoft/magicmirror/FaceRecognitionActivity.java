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
    }

    private void keepScreenOn() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    @Override
    protected void onResume() {
        super.onResume();
        systemUIHider.hideSystemUi();
    }

    @Override
    protected void onPause() {
        super.onPause();
        systemUIHider.showSystemUi();
    }

}
