package com.tomoapp.tomowallet.helper.alert;

import android.view.View;

import com.tapadoo.alerter.Alert;
import com.tapadoo.alerter.Alerter;
import com.tomoapp.tomowallet.base.BaseActivity;
import com.tomoapp.tomowallet.helper.LogUtil;
import com.tomoapp.tomowallet.model.walletActionResponse.CashActionResponse;
import com.tomoapp.tomowallet.model.walletActionResponse.RewardResponse;

import java.util.Locale;

/**
 * Created by macbook on 12/28/17.
 */

public class AlertHelper {
    private BaseActivity activity;

    private Alert alert;
    public AlertHelper(BaseActivity activity) {
        this.activity = activity;

    }


    public Alert getAlert() {
        return alert;
    }


    public boolean isAlertShowing(){
        return alert != null && alert.isShown();
    }


    public void showRewarded(final RewardResponse rewardResponse){
        try {
            if (activity == null) return;
            alert = Alerter.create(activity)
                    .setTitle("REWARD")
                    .setText(rewardResponse.getLog().getMessage())
                    .setDuration(3000)
                    .enableSwipeToDismiss()
                    .setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            onItemClicked(rewardResponse);
                        }
                    })
                    .show();
        } catch (Exception e) {
            LogUtil.e(e);
        }
    }

    public void showCashedIn(final CashActionResponse cashActionResponse){
        try {
            if (activity == null) return;
            alert = Alerter.create(activity)
                    .setTitle("CASHED IN")
                    .setText(cashActionResponse.getLog().getMessage())
                    .setDuration(3000)
                    .enableSwipeToDismiss()
                    .setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            onItemClicked(cashActionResponse);
                        }
                    })
                    .show();
        } catch (Exception e) {
            LogUtil.e(e);
        }
    }

    public void showCashedOut(final CashActionResponse cashActionResponse){
        try {
            if (activity == null) return;
            alert = Alerter.create(activity)
                    .setTitle("CASHED OUT")
                    .setText(cashActionResponse.getLog().getMessage())
                    .setDuration(3000)
                    .enableSwipeToDismiss()
                    .setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            onItemClicked(cashActionResponse);
                        }
                    })
                    .show();
        } catch (Exception e) {
            LogUtil.e(e);
        }
    }

    public void showSentTMC(final CashActionResponse cashActionResponse){
        try {
            if (activity == null) return;
            alert = Alerter.create(activity)
                    .setTitle("TMC SENT!")
                    .setText(cashActionResponse.getLog().getMessage())
                    .setDuration(3000)
                    .enableSwipeToDismiss()
                    .setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            onItemClicked(cashActionResponse);
                        }
                    })
                    .show();
        } catch (Exception e) {
            LogUtil.e(e);
        }
    }

    public void showReceivedTMC(final CashActionResponse cashActionResponse){
        try {
            if (activity == null) return;
            alert = Alerter.create(activity)
                    .setTitle("TMC RECEIVED!")
                    .setText(cashActionResponse.getLog().getMessage())
                    .setDuration(3000)
                    .enableSwipeToDismiss()
                    .setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            onItemClicked(cashActionResponse);
                        }
                    })
                    .show();
        } catch (Exception e) {
            LogUtil.e(e);
        }
    }


    void onItemClicked(CashActionResponse cashActionResponse){
        LogUtil.d("onItemClicked in Alert: " + cashActionResponse.toString());
    }

    void onItemClicked(RewardResponse rewardResponse){
        LogUtil.d("onItemClicked in Alert: " + rewardResponse.toString());
    }
}
