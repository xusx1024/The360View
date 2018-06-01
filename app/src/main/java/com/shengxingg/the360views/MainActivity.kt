package com.shengxingg.the360views

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.shengxingg.opengleslib.GLPanorama

class MainActivity : AppCompatActivity() {

  var mGLPanorama: GLPanorama? = null
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    mGLPanorama = findViewById(R.id.mgl)
    //传入全景图片
    mGLPanorama!!.setGLPanorama(R.drawable.imggugong)
  }
}
