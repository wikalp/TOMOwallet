package com.tomoapp.tomowallet.ui.home;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.tomoapp.tomowallet.R;
import com.tomoapp.tomowallet.base.BaseActivity;
import com.tomoapp.tomowallet.helper.LogUtil;
import com.tomoapp.tomowallet.model.wallet.WalletDataSource;
import com.tomoapp.tomowallet.model.wallet.WalletRepository;

/**
 * Created by macbook on 12/21/17.
 */

public class HomeActivity extends BaseActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        WalletDataSource wallet = new WalletRepository(this);
        LogUtil.d("HomeActivity: " + wallet.getAddress());
        LogUtil.d("HomeActivity: " + wallet.getPrivateKey());
    }
}
