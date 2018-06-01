package com.shengxing.googlevr;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import com.google.vr.sdk.widgets.pano.VrPanoramaEventListener;
import com.google.vr.sdk.widgets.pano.VrPanoramaView;

public class MainActivity extends AppCompatActivity {

  private VrPanoramaView mVrPanoramaView;
  private VrPanoramaView.Options paNormalOptions;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    initVrPaNormalView();
  }

  @Override protected void onResume() {
    super.onResume();
    mVrPanoramaView.resumeRendering();
  }

  @Override protected void onDestroy() {
    // Destroy the widget and free memory.
    super.onDestroy();
    mVrPanoramaView.shutdown();
  }

  private void initVrPaNormalView() {
    mVrPanoramaView = findViewById(R.id.mVrPanoramaView);
    paNormalOptions = new VrPanoramaView.Options();
    paNormalOptions.inputType = VrPanoramaView.Options.TYPE_STEREO_OVER_UNDER;
    mVrPanoramaView.setInfoButtonEnabled(true);
    mVrPanoramaView.setStereoModeButtonEnabled(true);
    mVrPanoramaView.setEventListener(new VrPanoramaEventListener() );
    mVrPanoramaView.loadImageFromBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.andes),paNormalOptions);
  }
}
