package com.tomoapp.tomowallet.ui.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.airbnb.lottie.LottieAnimationView;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.gson.Gson;
import com.jaychang.srv.SimpleRecyclerView;
import com.tomoapp.tomowallet.R;
import com.tomoapp.tomowallet.base.BaseActivity;
import com.tomoapp.tomowallet.base.BaseSocketActivity;
import com.tomoapp.tomowallet.base.MainApplication;
import com.tomoapp.tomowallet.helper.LogUtil;
import com.tomoapp.tomowallet.helper.ToastUtil;
import com.tomoapp.tomowallet.model.userInfo.pojo.Log;
import com.tomoapp.tomowallet.model.userInfo.pojo.UserInfo;
import com.tomoapp.tomowallet.model.wallet.WalletDataSource;
import com.tomoapp.tomowallet.model.wallet.WalletRepository;
import com.tomoapp.tomowallet.model.walletActionResponse.CashActionResponse;
import com.tomoapp.tomowallet.model.walletActionResponse.RewardResponse;
import com.tomoapp.tomowallet.ui.ethInfo.ETHInfoActivity;
import com.tomoapp.tomowallet.ui.home.cell.HeaderCell;
import com.tomoapp.tomowallet.ui.home.cell.LogCell;
import com.tomoapp.tomowallet.ui.receive_tmc.ReceiveTMCActivity;
import com.tomoapp.tomowallet.ui.send_tmc.SendTMCActivity;
import com.tomoapp.tomowallet.ui.splash.SplashActivity;
import com.tomoapp.tomowallet.ui.tmcInfo.TMCInfoActivity;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by macbook on 12/21/17.
 */

public class HomeActivity extends BaseSocketActivity implements HomeContract.View, HomeMenuFragment.OnMenuClickListener, HeaderCell.HeaderCellItemClickListener {


    @BindView(R.id.txt_label)
    TextView txtLabel;
    @BindView(R.id.btn_menu)
    ImageView btnMenu;
    @BindView(R.id.txt_mine_label)
    TextView txtMineLabel;
    @BindView(R.id.progress_mining)
    LottieAnimationView progressMining;
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
    @BindView(R.id.recycler_view)
    SimpleRecyclerView recyclerView;
    @BindView(R.id.btn_send_tmc)
    FloatingActionButton btnSendTmc;
    @BindView(R.id.btn_receive_tmc)
    FloatingActionButton btnReceiveTmc;
    @BindView(R.id.fab)
    FloatingActionMenu fab;
    private HomeContract.Presenter mPresenter;
    private WalletDataSource wallet = new WalletRepository(this);
    private HomeMenuFragment homeMenuFragment;
    private HeaderCell headerCell;

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
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {
                    // Scrolling up
                    fab.hideMenu(true);
                } else {
                    // Scrolling down
                    fab.showMenu(true);
                }
            }
        });
        mPresenter = new HomePresenter(this);
        mPresenter.init();
    }


    @Override
    protected void onResume() {
        super.onResume();
        MainApplication.setCurrentActivity(this);
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
                    if (recyclerView.getItemCount() > 0 && recyclerView.getCell(0) instanceof HeaderCell) {
                        if (userInfo.getLogs().size() > 0){
                            recyclerView.updateCell(0, userInfo.getLogs().get(0));
                        }
                        recyclerView.removeCells(1, recyclerView.getItemCount() - 1);
                        for (Log log : userInfo.getLogs())
                            recyclerView.addCell(new LogCell(log, HomeActivity.this));
                    } else {
                        headerCell = new HeaderCell(userInfo, HomeActivity.this, HomeActivity.this);
                        recyclerView.addCell(headerCell);
                        for (Log log : userInfo.getLogs())
                            recyclerView.addCell(new LogCell(log, HomeActivity.this));
                    }
                }
            });

        } catch (Exception e) {
            LogUtil.e(e);
        }
    }

    @Override
    public void onRewarded(final RewardResponse rewardResponse) {
        try {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {

                        txtMineLabel.setText(getString(R.string.mine_tmc));
                        progressMining.setVisibility(View.GONE);
                        btnMine.setEnabled(true);
                        if (rewardResponse == null) return;
                        Double doubleValue = rewardResponse.getValue();
                        Double realValue = doubleValue / Math.pow(10, 18);
                        txtNumOfTotalTmc.setText(String.format(Locale.ENGLISH, "%.4f",
                                realValue));
                        if (recyclerView.getItemCount() > 0 && recyclerView.getCell(0) instanceof HeaderCell) {
                            recyclerView.updateCell(0, rewardResponse.getLog());
                            recyclerView.addCell(1, new LogCell(rewardResponse.getLog(), HomeActivity.this));
                        }

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
        } catch (Exception e) {
            LogUtil.e(e);
        }
    }

    @Override
    public void onShowMenu() {
        try {
            if (homeMenuFragment == null) {
                homeMenuFragment = HomeMenuFragment.newInstance("menu", this);
            }
            homeMenuFragment.show(getSupportFragmentManager(), "menu");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onMenuClicked(int code) {
        homeMenuFragment.dismissAllowingStateLoss();
        switch (code) {
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
    public BaseSocketActivity getContext() {
        return this;
    }


    @Override
    public void onWalletDeleted() {
        ToastUtil.show(getString(R.string.wallet_deleted));
        startActivity(new Intent(this, SplashActivity.class));
        finish();
    }

    private void onViewMnemonic() {
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
        } catch (Exception e) {
            LogUtil.e(e);
        }
    }

    private void promptDeleteWallet() {
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
        } catch (Exception e) {
            LogUtil.e(e);
        }
    }


    @OnClick({R.id.btn_menu, R.id.btn_mine, R.id.txt_wallet_address, R.id.btn_send_tmc, R.id.btn_receive_tmc})
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
            case R.id.btn_send_tmc:
                fab.close(true);
                startActivity(new Intent(this, SendTMCActivity.class));
                break;
            case R.id.btn_receive_tmc:
                fab.close(true);
                startActivity(new Intent(this, ReceiveTMCActivity.class));
                break;
        }
    }


    @Override
    public void onMainChainInfoClicked() {
        try {
            String data = new Gson().toJson(mPresenter.getUserInfo());
            startActivity(new Intent(this, ETHInfoActivity.class).putExtra(ETHInfoActivity.ARGS_USER_INFO, data));
        } catch (Exception e) {
            LogUtil.e(e);
        }
    }

    @Override
    public void onSideChainInfoClicked() {
        try {
            String data = new Gson().toJson(mPresenter.getUserInfo());
            startActivity(new Intent(this, TMCInfoActivity.class).putExtra(TMCInfoActivity.ARGS_USER_INFO, data));
        } catch (Exception e) {
            LogUtil.e(e);
        }
    }

    @Override
    public void onCashInBtnClicked(Double cashInValue) {
        onCashing();
        mPresenter.onCashIn(cashInValue);
    }

    @Override
    public void onCashOutBtnClicked(Double cashOutValue) {
        onCashing();
        mPresenter.onCashOut(cashOutValue);
    }


    @Override
    public void onCashing() {
        btnMine.setEnabled(false);
    }

    @Override
    public void onCashed(final CashActionResponse cashActionResponse) {
        try {

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        btnMine.setEnabled(true);
                        txtMineLabel.setText(getString(R.string.mine_tmc));
                        progressMining.setVisibility(View.GONE);
                        btnMine.setEnabled(true);
                        if (cashActionResponse == null) return;
                        Double doubleValue = cashActionResponse.getMainchain() + cashActionResponse.getSidechain();
                        Double realValue = doubleValue / Math.pow(10, 18);
                        txtNumOfTotalTmc.setText(String.format(Locale.ENGLISH, "%.4f",
                                realValue));
                        if (recyclerView.getItemCount() > 0 && recyclerView.getCell(0) instanceof HeaderCell)
                            recyclerView.updateCell(0, cashActionResponse.getLog());
                        recyclerView.addCell(1, new LogCell(cashActionResponse.getLog(), HomeActivity.this));
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
    public void onCashFail(String reason) {
        ToastUtil.show(reason);
    }


    @Override
    public void onSocketConnected() {
        super.onSocketConnected();
        emitUser();
    }

    @Override
    public void onSocketDisconnected(Object... args) {
        super.onSocketDisconnected(args);
        LogUtil.d("onSocketDisconnected: " + MainApplication.getCurrentActivity().getLocalClassName());
    }

    @Override
    public void onRetrieveUserInfo(UserInfo userInfo) {
        super.onRetrieveUserInfo(userInfo);
        setUserInfo(userInfo);
    }

    @Override
    public void onRetrieveReward(RewardResponse reward) {
        super.onRetrieveReward(reward);
        onRewarded(reward);
    }

    @Override
    public void onCashedIn(CashActionResponse cashInDetail) {
        super.onCashedIn(cashInDetail);
        onCashed(cashInDetail);
    }

    @Override
    public void onCashedOut(CashActionResponse cashOutDetail) {
        super.onCashedOut(cashOutDetail);
        onCashed(cashOutDetail);
    }

    @Override
    public void onTMCSent(CashActionResponse transactionDetail) {
        super.onTMCSent(transactionDetail);
        onCashed(transactionDetail);
    }

    @Override
    public void onTMCReceived(CashActionResponse transactionDetail) {
        super.onTMCReceived(transactionDetail);
        onCashed(transactionDetail);
    }
}
