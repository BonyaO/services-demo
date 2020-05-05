package com.example.servicesdemo;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class MyStartedService extends Service {
    private static final String TAG = MyStartedService.class.getSimpleName();
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.i(TAG, "onBind: Thread Name: " + Thread.currentThread().getName());
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "onCreate: Thread Name: " + Thread.currentThread().getName());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "onStartCommand: Thread Name: " + Thread.currentThread().getName());
        int sleepTime = intent.getIntExtra("Sleep Time",1);

       new MyAsyncTask().execute(sleepTime);

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy: Thread Name: " + Thread.currentThread().getName());
    }
     class MyAsyncTask extends AsyncTask<Integer, String, String>{

        @Override
        protected String doInBackground(Integer... params) {
            Log.i(TAG, "doInBackground: Thread Name: " + Thread.currentThread().getName());

            int sleepTime = params[0];
            int ctr = 1;
            while (ctr<= sleepTime){
                publishProgress("Counter is now "  + ctr);

                try{
                    Thread.sleep(1000);
                }catch (Exception e){
                    e.printStackTrace();
                }
                ctr++;
            }
            return "Counter Stopped at " + ctr + " seconds";
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.i(TAG, "onPreExecute: Thread Name: " + Thread.currentThread().getName());
        }

        @Override
        protected void onPostExecute(String str) {
            super.onPostExecute(str);
            stopSelf();
            Log.i(TAG, "onPostExecute: Thread Name: " + Thread.currentThread().getName());

            Intent intent = new Intent("action.service.to.activity");
            intent.putExtra("startServiceResult", str);
            sendBroadcast(intent);
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            Toast.makeText(MyStartedService.this, values[0], Toast.LENGTH_SHORT).show();
            Log.i(TAG, "onProgressUpdate: Thread Name: " + Thread.currentThread().getName());
        }
    }
}
