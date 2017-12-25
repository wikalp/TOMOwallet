package com.tomoapp.tomowallet.ui.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.tomoapp.tomowallet.R;
import com.tomoapp.tomowallet.base.BaseActivity;
import com.tomoapp.tomowallet.helper.LogUtil;
import com.tomoapp.tomowallet.helper.ToastUtil;
import com.tomoapp.tomowallet.model.userInfo.pojo.UserInfo;
import com.tomoapp.tomowallet.model.wallet.WalletDataSource;
import com.tomoapp.tomowallet.model.wallet.WalletRepository;
import com.tomoapp.tomowallet.ui.splash.SplashActivity;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by macbook on 12/21/17.
 */

public class HomeActivity extends BaseActivity implements HomeContract.View , HomeMenuFragment.OnMenuClickListener{


    @BindView(R.id.txt_label)
    TextView txtLabel;
    @BindView(R.id.btn_menu)
    ImageView btnMenu;
    @BindView(R.id.txt_mine_label)
    TextView txtMineLabel;
    @BindView(R.id.progress_mining)
    ProgressBar progressMining;
    @BindView(R.id.btn_mine)
    LinearLayout btnMine;
    @BindView(R.id.label_2)
    TextView label2;
    @BindView(R.id.txt_num_of_total_tmc)
    TextView txtNumOfTotalTmc;
    @BindView(R.id.layout_header)
    ConstraintLayout layoutHeader;
    @BindView(R.id.txt_wallet_address)
    TextView txtWalletAddress;
    private HomeContract.Presenter mPresenter;
    private WalletDataSource wallet = new WalletRepository(this);
    private HomeMenuFragment homeMenuFragment;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        wallet = new WalletRepository(this);
        LogUtil.d("HomeActivity: " + wallet.getMnemonic());
        LogUtil.d("HomeActivity: " + wallet.getAddress());
        LogUtil.d("HomeActivity: " + wallet.getPrivateKey());
        txtWalletAddress.setText(wallet.getAddress());
        mPresenter = new HomePresenter(this);
        mPresenter.init();
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (mPresenter == null)
            mPresenter = new HomePresenter(this);
        mPresenter.refreshUserInfo();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null)
            mPresenter.destroy();
    }


    @Override
    public void setUserInfo(final UserInfo userInfo) {
        try {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    txtWalletAddress.setText(mPresenter.getWalletAddress());
                    Double value = userInfo.getTmcMainchain() + userInfo.getTmcSidechain();
                    txtNumOfTotalTmc.setText(String.format(Locale.ENGLISH, "%.4f",
                            value));
                }
            });

        } catch (Exception e) {
            LogUtil.e(e);
        }
    }

    @Override
    public void onRewarded(final String value) {
        try {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        txtMineLabel.setText(getString(R.string.mine_tmc));
                        progressMining.setVisibility(View.GONE);
                        btnMine.setEnabled(true);
                        if (value == null) return;
                        Double doubleValue = Double.valueOf(value);
                        Double realValue = doubleValue / Math.pow(10, 18);
                        txtNumOfTotalTmc.setText(String.format(Locale.ENGLISH, "%.4f",
                                realValue));
                    } catch (NumberFormatException e) {
                        LogUtil.e(e);
                    }
                    /**/
                }
            });

        } catch (Exception e) {
            LogUtil.e(e);
        }
    }

    @Override
    public void onRewarding() {
        try {
            progressMining.setVisibility(View.VISIBLE);
            txtMineLabel.setText(getString(R.string.mining));
            btnMine.setEnabled(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onShowAddress() {
        try {
            new MaterialDialog.Builder(this)
                    .title(getString(R.string.your_address))
                    .content(mPresenter.getWalletAddress())
                    .positiveText(getString(R.string.ok))
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            dialog.dismiss();
                        }
                    })
                    .cancelable(true)
                    .show();
        }catch (Exception e){
            LogUtil.e(e);
        }
    }

    @Override
    public void onShowMenu() {
        try {
            if (homeMenuFragment == null){
                homeMenuFragment = HomeMenuFragment.newInstance("menu", this);
            }
            homeMenuFragment.show(getSupportFragmentManager(),"menu");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onMenuClicked(int code) {
        homeMenuFragment.dismissAllowingStateLoss();
        switch (code){
            case HomeMenuFragment.CODE_VIEW_ADDRESS:
                onShowAddress();
                break;
            case HomeMenuFragment.CODE_VIEW_MNEMONIC:
                onViewMnemonic();
                break;
            case HomeMenuFragment.CODE_DELETE_ADDRESS:
                promptDeleteWallet();
                break;
        }
    }

    @Override
    public Context getContext() {
        return this;
    }


    @Override
    public void onWalletDeleted() {
        ToastUtil.show(getString(R.string.wallet_deleted));
        startActivity(new Intent(this, SplashActivity.class));
        finish();
    }

    private void onViewMnemonic(){
        try {
            new MaterialDialog.Builder(this)
                    .title(getString(R.string.your_mnemonic))
                    .content(mPresenter.getMnemonic())
                    .positiveText(getString(R.string.ok))
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            dialog.dismiss();
                        }
                    })
                    .cancelable(true)
                    .show();
        }catch (Exception e){
            LogUtil.e(e);
        }
    }

    private void promptDeleteWallet(){
        try {
            new MaterialDialog.Builder(this)
                    .title(getString(R.string.be_careful))
                    .content(getString(R.string.delete_wallet_mess))
                    .positiveText(getString(R.string.delete))
                    .negativeText(getString(R.string.close))
                    .positiveColor(getResources().getColor(R.color.color_50))
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            mPresenter.deleteWallet();
                        }
                    })
                    .cancelable(true)
                    .show();
        }catch (Exception e){
            LogUtil.e(e);
        }
    }


    @OnClick({R.id.btn_menu, R.id.btn_mine, R.id.txt_wallet_address})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_menu:
                onShowMenu();
                break;
            case R.id.btn_mine:
                mPresenter.onMine();
                break;
            case R.id.txt_wallet_address:
                onShowAddress();
                break;
        }
    }




}
