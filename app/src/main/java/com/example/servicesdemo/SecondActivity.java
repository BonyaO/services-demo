package com.example.servicesdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class SecondActivity extends AppCompatActivity {

    Button btnAdd, btnSubtract, btnMultiply, btnDivide;
    TextView resultTV;
    EditText firstVariable, secondVariable;

    boolean isBounded = false;
    private MyBoundedService myBoundedService;

    @Override
    protected void onStart() {
        super.onStart();

        Intent intent = new Intent(this, MyBoundedService.class);
        bindService(intent, mConnection, BIND_AUTO_CREATE);
    }

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MyBoundedService.MyLocalBinder myLocalBinder = (MyBoundedService.MyLocalBinder) service;
            myBoundedService = myLocalBinder.getService();
            isBounded = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isBounded = false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        btnAdd = findViewById(R.id.btnAdd);
        btnDivide = findViewById(R.id.btnDivide);
        btnMultiply = findViewById(R.id.btnMultiply);
        btnSubtract = findViewById(R.id.btnSubtract);

    }

    @Override
    protected void onStop() {
        super.onStop();
        if(isBounded){
            unbindService(mConnection);
            isBounded = false;
        }
    }

    public void onClickEvent(View view) {
        resultTV = findViewById(R.id.resultTV);
        firstVariable = findViewById(R.id.firstVariable);
        secondVariable = findViewById(R.id.secondVariable);

        int numOne = Integer.valueOf(firstVariable.getText().toString());
        int numTwo = Integer.valueOf(secondVariable.getText().toString());

        String resultString = "";


        if(isBounded){
            switch (view.getId()){
                case R.id.btnAdd:
                    resultString = String.valueOf(myBoundedService.add(numOne, numTwo));
                    break;
                case R.id.btnDivide:
                    resultString = String.valueOf(myBoundedService.divide(numOne, numTwo));
                    break;
                case R.id.btnMultiply:
                    resultString = String.valueOf(myBoundedService.multiply(numOne, numTwo));
                    break;
                case R.id.btnSubtract:
                    resultString = String.valueOf(myBoundedService.subtract(numOne, numTwo));
                    break;
            }
            resultTV.setText(resultString);
        }
    }

}
