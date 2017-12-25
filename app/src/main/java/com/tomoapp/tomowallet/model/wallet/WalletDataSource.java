package com.tomoapp.tomowallet.model.wallet;

import android.content.Context;

import com.evgenii.jsevaluator.interfaces.JsCallback;
import com.tomoapp.tomowallet.helper.wallet.WalletHelper;

/**
 * Created by macbook on 12/21/17.
 */

public interface WalletDataSource {


    void createMnemonic(JsCallback callback);
    void createWallet(String mnemonic, WalletHelper.CreateWalletCallback callback);
    void createWallet(WalletHelper.CreateWalletCallback callback);
    void saveWallet(Wallet wallet);
    String getPrivateKey();
    String getAddress();
    String getMnemonic();
    void deleteWallet();
}
