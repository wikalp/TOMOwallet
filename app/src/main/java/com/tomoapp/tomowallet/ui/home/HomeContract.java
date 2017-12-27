package com.tomoapp.tomowallet.ui.home;

import android.content.Context;

import com.tomoapp.tomowallet.model.userInfo.pojo.UserInfo;
import com.tomoapp.tomowallet.model.wallet.Wallet;
import com.tomoapp.tomowallet.model.walletActionResponse.CashActionResponse;
import com.tomoapp.tomowallet.model.walletActionResponse.RewardResponse;


/**
 * Created by macbook on 12/22/17.
 */

public interface HomeContract {

    interface View{
        Context getContext();

        void setUserInfo(UserInfo userInfo);
        void onRewarded(RewardResponse rewardResponse);
        void onCashing();
        void onCashed(CashActionResponse cashActionResponse);
        void onCashFail(String reason);
        void onRewarding();
        void onShowAddress();
        void onShowMenu();
        void onWalletDeleted();

    }


    interface Presenter{
        void init();
        void destroy();
        void onMine();
        void deleteWallet();
        void refreshUserInfo();
        String getWalletAddress();
        String getMnemonic();
        void onCashIn(double value);
        void onCashOut(double value);
        UserInfo getUserInfo();

    }
}
