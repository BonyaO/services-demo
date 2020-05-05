package com.example.servicesdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.CursorJoiner;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    TextView txtvIntentServiceResult, txtvStartedServiceResult;
    Button btnNextActivity;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtvIntentServiceResult = findViewById(R.id.txtvIntentServiceResult);
        txtvStartedServiceResult = findViewById(R.id.txtvStartedServiceResult);
        btnNextActivity = findViewById(R.id.btnMessengerActivity);

        btnNextActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MyMessengerActivity.class);
                startActivity(intent);
            }
        });
    }

    public void stopStartedService(View view) {
        Intent intent = new Intent(MainActivity.this, MyStartedService.class);
        stopService(intent);

    }

    public void startStartedService(View view) {
        Intent intent = new Intent(MainActivity.this, MyStartedService.class);
        intent.putExtra("Sleep Time", 10);
        startService(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("action.service.to.activity");
        registerReceiver(myStartedServiceResultReceiver, intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();

        unregisterReceiver(myStartedServiceResultReceiver);
    }

    private BroadcastReceiver myStartedServiceResultReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String result = intent.getStringExtra("startServiceResult");
            txtvStartedServiceResult.setText(result);
        }
    };

    public void startIntentService(View view) {

        ResultReceiver myResultReceiver = new MyResultReceiver(null);

        Intent intent = new Intent(this, MyIntentService.class);
        intent.putExtra("sleepTime", 10);
        intent.putExtra("receiver", myResultReceiver);
        startService(intent);
    }

    public void startSecondActivity(View view) {
        Intent intent = new Intent(this, SecondActivity.class);
        startActivity(intent);
    }

    private class MyResultReceiver extends ResultReceiver{

        /**
         * Create a new ResultReceive to receive results.  Your
         * {@link #onReceiveResult} method will be called from the thread running
         * <var>handler</var> if given, or from an arbitrary thread if null.
         *
         * @param handler
         */
        public MyResultReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            super.onReceiveResult(resultCode, resultData);

            Log.i("MyResultReceiver", "onReceiveResult: " + Thread.currentThread().getName());

            if(resultCode == 18 && resultData != null){
              final  String result = resultData.getString("resultIntentService");
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Log.i("MyHandler", "run: " + Thread.currentThread().getName());
                        txtvIntentServiceResult.setText(result);
                    }
                });

            }
        }
    }
}
