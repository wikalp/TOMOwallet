package com.tomoapp.tomowallet.helper.socket;

import java.math.BigInteger;

/**
 * Created by macbook on 12/22/17.
 */

public interface TOMOSocketListener {

    void onSocketConnected();
    void onSocketDisconnected(Object... args);
    void onRetrieveUserInfo(String userInfoString);
    void onRetrieveReward(String value);
    void onCashedIn(String transactionDetail);
    void onCashedOut(String transactionDetail);
}
