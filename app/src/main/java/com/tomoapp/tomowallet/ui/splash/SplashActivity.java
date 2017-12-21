package com.tomoapp.tomowallet.ui.splash;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.tomoapp.tomowallet.base.BaseActivity;
import com.tomoapp.tomowallet.ui.createWallet.CreateWalletActivity;
import com.tomoapp.tomowallet.ui.home.HomeActivity;

/**
 * Created by macbook on 12/21/17.
 */

public class SplashActivity extends BaseActivity implements SplashContract.View {
    private SplashContract.Presenter mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mPresenter = new SplashPresenter(this);
        mPresenter.init();
    }


    @Override
    protected void onDestroy() {
        if (mPresenter != null)
            mPresenter.destroy();
        super.onDestroy();
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void goToCreateWallet() {
        startActivity(new Intent(this, CreateWalletActivity.class));
        finish();
    }

    @Override
    public void goToMain() {
        startActivity(new Intent(this, HomeActivity.class));
        finish();
    }
}
