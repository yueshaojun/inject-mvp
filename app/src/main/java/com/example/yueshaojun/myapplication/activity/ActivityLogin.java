package com.example.yueshaojun.myapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.yueshaojun.myapplication.BaseActivity;
import com.example.yueshaojun.myapplication.R;
import com.example.yueshaojun.myapplication.V.ILoginView;
import com.example.yueshaojun.myapplication.di.DaggerMyComponent;

/**
 * 例子：登陆页面
 * @author yueshaojun
 */
public class ActivityLogin extends BaseActivity implements ILoginView {

    private static final String TAG = "ActivityLogin_";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        findViewById(R.id.click).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ActivityLogin.this,ActivityPay.class));
            }
        });
    }

    @Override
    public void injectMethod() {
        DaggerMyComponent.create().inject(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("MyApplication",TAG+"onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("MyApplication",TAG+"onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("MyApplication",TAG+"onStop");
    }


    @Override
    public void onLoginSuccess() {
        Log.i("MyModule", TAG + "LoginSuccess_");
        Toast.makeText(this, "ActivityLogin onLoginSuccess", Toast.LENGTH_LONG).show();
        startActivity(new Intent(this,ActivityPay.class));

    }

    @Override
    public void onLoginError(String errorMsg) {

    }
}
