package com.example.myeventbus;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

import com.example.mybusview.MyBus;

public class Activity2 extends Activity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity2);
    }



    public void post(View view) {
        Log.d("testtt","post yao");
        MyBus.getDefault().post("yao");
    }
}
