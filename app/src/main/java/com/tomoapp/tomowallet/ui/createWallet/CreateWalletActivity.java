package com.tomoapp.tomowallet.ui.createWallet;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.tomoapp.tomowallet.R;
import com.tomoapp.tomowallet.base.BaseActivity;
import com.tomoapp.tomowallet.helper.LogUtil;
import com.tomoapp.tomowallet.helper.ToastUtil;
import com.tomoapp.tomowallet.model.wallet.Wallet;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by macbook on 12/21/17.
 */

public class CreateWalletActivity extends BaseActivity implements CreateWalletContract.View {


    @BindView(R.id.txt_title)
    TextView txtTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.loading_view)
    LinearLayout loadingView;
    @BindView(R.id.txt_mnemonic)
    TextView txtMnemonic;
    @BindView(R.id.txt_address)
    TextView txtAddress;
    @BindView(R.id.txt_private_key)
    TextView txtPrivateKey;
    @BindView(R.id.scroll_view)
    ScrollView scrollView;
    @BindView(R.id.btn_next)
    AppCompatButton btnNext;

    private CreateWalletPresenter mPresenter;
    private boolean isOnCreating;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_wallet);
        ButterKnife.bind(this);
        mPresenter = new CreateWalletPresenter(this);
        mPresenter.init();
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void onCreating() {
        isOnCreating = true;
        btnNext.setEnabled(false);
        loadingView.setVisibility(View.VISIBLE);
        scrollView.setVisibility(View.GONE);
    }

    @Override
    public void onCreated(Wallet wallet) {
        isOnCreating = false;
        btnNext.setEnabled(true);
        loadingView.setVisibility(View.GONE);
        scrollView.setVisibility(View.VISIBLE);
        txtAddress.setText(wallet.getAddress());
        txtMnemonic.setText(wallet.getMnemonic());
        txtPrivateKey.setText(wallet.getPrivateKey());
    }

    @Override
    public void onDone() {
        LogUtil.d("onDONE");
        ToastUtil.show("DONE");
    }

    @Override
    public void onBack() {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        if (isOnCreating) return;
        super.onBackPressed();
    }

    @Override
    public void onFail(String why) {
        isOnCreating = false;
        new MaterialDialog.Builder(this)
                .title(getString(R.string.error))
                .content(why)
                .positiveText(getString(R.string.close))
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        onBack();
                    }
                })
                .cancelable(false)
                .show();
    }

    @OnClick(R.id.btn_next)
    public void onViewClicked() {
        onDone();
    }
}
