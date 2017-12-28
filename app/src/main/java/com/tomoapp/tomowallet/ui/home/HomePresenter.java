package com.tomoapp.tomowallet.ui.home;

import com.tomoapp.tomowallet.base.MainApplication;
import com.tomoapp.tomowallet.helper.LogUtil;
import com.tomoapp.tomowallet.helper.ToastUtil;
import com.tomoapp.tomowallet.model.userInfo.pojo.UserInfo;
import com.tomoapp.tomowallet.model.wallet.WalletDataSource;
import com.tomoapp.tomowallet.model.wallet.WalletRepository;
import com.tomoapp.tomowallet.model.walletAction.WalletActionDataSource;
import com.tomoapp.tomowallet.model.walletAction.WalletActionRepository;

/**
 * Created by macbook on 12/25/17.
 */

public class HomePresenter implements HomeContract.Presenter{
    private HomeContract.View mView;
    private WalletDataSource mWallet;
    private UserInfo userInfo;
    private WalletActionDataSource mWalletAction;
    public HomePresenter(HomeContract.View mView) {
        this.mView = mView;
        this.mWallet = new WalletRepository(mView.getContext());
        this.mWalletAction = new WalletActionRepository(mView.getContext());

    }

    @Override
    public void init() {

    }

    @Override
    public void destroy() {

    }

    @Override
    public void onMine() {
        try {
            mView.onRewarding();
            mWalletAction.performReward(new WalletActionDataSource.ActionExecuteListener() {
                @Override
                public void onFail(int code, String why) {
                    ToastUtil.show(why);
                    mView.onRewarded(null);
                }
            });
        }catch (Exception e){
            LogUtil.e(e);
        }
    }


    @Override
    public void onCashIn(double value) {
        mWalletAction.cashIn(value, new WalletActionDataSource.ActionExecuteListener() {
            @Override
            public void onFail(int code, String why) {
                mView.onCashFail(why);
            }
        });
    }

    @Override
    public void onCashOut(double value) {
        mWalletAction.cashOut(value, new WalletActionDataSource.ActionExecuteListener() {
            @Override
            public void onFail(int code, String why) {
                mView.onCashFail(why);
            }
        });
    }

    @Override
    public void deleteWallet() {
        mWallet.deleteWallet();
        mView.onWalletDeleted();
    }

    @Override
    public void refreshUserInfo() {
        MainApplication.get().emitUser(mWallet.getAddress());
    }

    @Override
    public String getWalletAddress() {
        return mWallet == null ? "" : mWallet.getAddress();
    }

    @Override
    public String getMnemonic() {
        return mWallet == null ? "" : mWallet.getMnemonic();
    }


    @Override
    public UserInfo getUserInfo() {
        return userInfo;
    }
}
