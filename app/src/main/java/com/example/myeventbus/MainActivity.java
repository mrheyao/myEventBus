package com.example.myeventbus;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.mybusview.MyBus;
import com.example.mybusview.Subscribe;
import com.example.mybusview.ThreadMode;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MyBus.getDefault().regist(this);
    }

    @Subscribe(threadmode = ThreadMode.MAIN)
    public void getMessage(String msg){
        Log.d("testtt","1 mybus is ok:"+msg+" "+Thread.currentThread().getName());
    }

    @Subscribe(threadmode =ThreadMode.BACKEND)
    public void getMessage2(String msg){
        Log.d("testtt","2 mybus is ok:"+msg+" "+Thread.currentThread().getName());
    }

    public void jumpactivity2(View view) {
        Intent intent = new Intent(this,Activity2.class );
        startActivity(intent);
    }
}
