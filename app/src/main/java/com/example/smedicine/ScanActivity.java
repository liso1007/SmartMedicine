package com.example.smedicine;

import android.os.Bundle;

import com.journeyapps.barcodescanner.CaptureActivity;

//扫描条形码需要打开摄像头
//再新建一个Activity，这个Activity是用来打开摄像头的。我这里的名字叫ScanActivity，主要代码：
public class ScanActivity extends CaptureActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
