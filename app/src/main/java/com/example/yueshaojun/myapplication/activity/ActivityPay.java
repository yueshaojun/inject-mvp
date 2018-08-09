package com.example.yueshaojun.myapplication.activity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.lib.Presenter;
import com.example.lib.PresenterType;
import com.example.yueshaojun.myapplication.BaseActivity;
import com.example.yueshaojun.myapplication.P.LoginPresenter;
import com.example.yueshaojun.myapplication.P.PayPresenter;
import com.example.yueshaojun.myapplication.R;
import com.example.yueshaojun.myapplication.V.ILoginView;
import com.example.yueshaojun.myapplication.V.IPayView;
import com.example.yueshaojun.myapplication.di.DaggerMyComponent;

import javax.inject.Inject;


public class ActivityPay extends BaseActivity implements ILoginView,IPayView{
    private final static String TAG = "ActivityPay_";

    @Inject
    @Presenter(type = PresenterType.ACTIVITY)
    LoginPresenter loginPresenter;

    @Inject
    @Presenter(type = PresenterType.ACTIVITY)
    PayPresenter payPresenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("MyApplication",TAG+"_onCreate");
    }

    @Override
    public void injectMethod() {
        DaggerMyComponent.create().inject(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loginPresenter.login();
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
        Toast.makeText(this,"ActivityPay_onLoginSuccess",Toast.LENGTH_SHORT).show();
        payPresenter.pay();
    }

    @Override
    public void onLoginError(String errorMsg) {

    }

    @Override
    public void paySuccess() {
        Toast.makeText(this,"ActivityPay_paySuccess",Toast.LENGTH_SHORT).show();
    }
}
