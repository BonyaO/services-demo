package com.example.servicesdemo;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import androidx.annotation.Nullable;

public class MyBoundedService extends Service {

    public int add(int a, int b){
        return a + b;
    }
    public int subtract(int a, int b){
        return a - b;
    }
    public int multiply(int a, int b){
        return a * b;
    }
    public float divide(int a, int b){
        return (float) a / (float) b;
    }

    private MyLocalBinder myLocalBinder = new MyLocalBinder();


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return myLocalBinder;
    }

    public class MyLocalBinder extends Binder{
        MyBoundedService getService(){
            return MyBoundedService.this;
        }
    }

}
