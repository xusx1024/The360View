package com.shengxingg.opengleslib;

import android.annotation.SuppressLint;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import com.shengxingg.opengleslib.glutils.Ball;
import com.shengxingg.opengleslib.glutils.IViews;

/**
 * Fun:
 * Created by sxx.xu on 5/31/2018.
 */

public class GLPanorama extends RelativeLayout implements SensorEventListener {

  private static final float NS2S = 1.0f / 1000000000.0f;// 纳秒2秒
  int yy = 0;
  private Context mContext;
  private IViews mGlSurfaceView;
  private ImageView img;
  private float mPreviousY, mPreviousYs;
  private float mPreviousX, mPreviousXs;
  private float predegrees = 0;
  private Ball mBall;
  @SuppressLint("HandlerLeak") Handler mHandler = new Handler() {
    @Override public void handleMessage(Message msg) {
      switch (msg.what) {
        case 101:
          Sensordt info = (Sensordt) msg.obj;
          float y = info.getSensorY();
          float x = info.getSensorX();
          float dy = y - mPreviousY;
          float dx = x - mPreviousX;
          mBall.yAngle += dx * 2.0f;
          mBall.xAngle += dy * 0.5f;
          if (mBall.xAngle < -50f) {
            mBall.xAngle = -50f;
          } else if (mBall.xAngle > 50f) {
            mBall.xAngle = 50f;
          }
          mPreviousX = x;
          mPreviousY = y;
          rotate();
          break;
        default:
          break;
      }
    }
  };
  private SensorManager mSensorManager;
  private Sensor mGyroscopeSensor;
  private float timestamp;
  private float angle[] = new float[3];
  private Handler mHandlers = new Handler();

  public GLPanorama(Context context) {
    super(context);
    this.mContext = context;
    init();
  }

  public GLPanorama(Context context, AttributeSet attrs) {
    super(context, attrs);
    this.mContext = context;
    init();
  }

  public GLPanorama(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    this.mContext = context;
    init();
  }

  @Override public void onSensorChanged(SensorEvent event) {
    if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE) {
      if (timestamp != 0) {
        final float dT = (event.timestamp - timestamp) * NS2S;
        angle[0] += event.values[0] * dT;
        angle[1] += event.values[1] * dT;
        angle[2] += event.values[2] * dT;

        float anglex = (float) Math.toDegrees(angle[0]);
        float angley = (float) Math.toDegrees(angle[1]);
        float anglez = (float) Math.toDegrees(angle[2]);

        Sensordt info = new Sensordt();
        info.setSensorX(angley);
        info.setSensorY(anglex);
        info.setSensorZ(anglez);

        Message msg = new Message();
        msg.what = 101;
        msg.obj = info;
        mHandler.sendMessage(msg);
      }
      timestamp = event.timestamp;
    }
  }

  @Override public void onAccuracyChanged(Sensor sensor, int accuracy) {

  }

  @Override public boolean onTouchEvent(MotionEvent event) {
    mSensorManager.unregisterListener(this);
    float y = event.getY();
    float x = event.getX();

    switch (event.getAction()) {
      case MotionEvent.ACTION_MOVE:
        float dy = y - mPreviousYs;
        float dx = x - mPreviousXs;

        mBall.yAngle += dx * 0.3f;
        mBall.xAngle += dy * 0.3f;

        if (mBall.xAngle < -50f) {
          mBall.xAngle = -50f;
        } else if (mBall.xAngle > 50f) {
          mBall.xAngle = 50f;
        }
        rotate();
        break;
      case MotionEvent.ACTION_UP:
        mSensorManager.registerListener(this, mGyroscopeSensor,
            SensorManager.SENSOR_DELAY_FASTEST);
        break;
    }

    mPreviousXs = x;
    mPreviousYs = y;
    return true;
  }

  private void init() {
    initView();
  }

  private void initView() {
    LayoutInflater.from(mContext).inflate(R.layout.panoramalayout, this);
    mGlSurfaceView = findViewById(R.id.mIViews);
    img = findViewById(R.id.img);
    img.setOnClickListener(new OnClickListener() {
      @Override public void onClick(View v) {
        zero();
      }
    });
  }

  private void rotate() {
    RotateAnimation anim =
        new RotateAnimation(predegrees, -mBall.yAngle, Animation.RELATIVE_TO_SELF, 0.5f,
            Animation.RELATIVE_TO_SELF, 0.5f);
    anim.setDuration(200);
    img.startAnimation(anim);
    predegrees = -mBall.yAngle;
  }

  private void zero() {
    yy = (int) ((mBall.yAngle - 90f) / 10f);
    mHandlers.post(new Runnable() {
      @Override public void run() {
        if (yy != 0) {
          if (yy > 0) {
            mBall.yAngle = mBall.yAngle - 10f;
            mHandlers.postDelayed(this, 16);
            yy--;
          }
          if (yy < 0) {
            mBall.yAngle = mBall.yAngle + 10f;
            mHandlers.postDelayed(this, 16);
            yy++;
          }
        } else {
          mBall.yAngle = 90f;
        }
        mBall.xAngle = 0f;
      }
    });
  }

  private void initSensor() {
    mSensorManager = (SensorManager) mContext.getSystemService(Context.SENSOR_SERVICE);
    mGyroscopeSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
    mSensorManager.registerListener(this, mGyroscopeSensor, SensorManager.SENSOR_DELAY_FASTEST);
  }

  /**
   * 传入图片路径
   */
  public void setGLPanorama(int pimgid) {
    mGlSurfaceView.setEGLContextClientVersion(2);
    mBall = new Ball(mContext, pimgid);
    mGlSurfaceView.setRenderer(mBall);
    initSensor();
  }

  class Sensordt {
    float sensorX;
    float sensorY;
    float sensorZ;

    public float getSensorX() {
      return sensorX;
    }

    public void setSensorX(float sensorX) {
      this.sensorX = sensorX;
    }

    public float getSensorY() {
      return sensorY;
    }

    public void setSensorY(float sensorY) {
      this.sensorY = sensorY;
    }

    public float getSensorZ() {
      return sensorZ;
    }

    public void setSensorZ(float sensorZ) {
      this.sensorZ = sensorZ;
    }
  }
}
