package com.example.servicesdemo;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.util.Log;

import androidx.annotation.Nullable;

public class MyIntentService extends IntentService {
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    private static final String TAG = MyIntentService.class.getSimpleName();
    public MyIntentService() {
        super("MyWorkerThread");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Log.i(TAG, "onHandleIntent: Thread Name: " + Thread.currentThread().getName());

        int sleepTime = intent.getIntExtra("sleepTime", 1);
        ResultReceiver resultReceiver = intent.getParcelableExtra("receiver");
        int ctr = 1;
        while (ctr <= sleepTime){
            Log.i(TAG, "Counter is now "  + ctr);

            try{
                Thread.sleep(1000);
            }catch (Exception e){
                e.printStackTrace();
            }
            ctr++;
        }

        Bundle bundle = new Bundle();
        bundle.putString("resultIntentService", "Counter stopped at " + ctr + " seconds");
        resultReceiver.send(18, bundle);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "onCreate: Thread Name: " + Thread.currentThread().getName());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy: Thread Name: " + Thread.currentThread().getName());
    }
}
