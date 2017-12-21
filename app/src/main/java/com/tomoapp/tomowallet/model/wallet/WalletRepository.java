package com.tomoapp.tomowallet.model.wallet;

import android.content.Context;
import android.content.SharedPreferences;

import com.evgenii.jsevaluator.interfaces.JsCallback;
import com.scottyab.aescrypt.AESCrypt;
import com.tomoapp.tomowallet.base.MainApplication;
import com.tomoapp.tomowallet.helper.LogUtil;
import com.tomoapp.tomowallet.helper.wallet.WalletHelper;

import net.idik.lib.cipher.so.CipherClient;

/**
 * Created by macbook on 12/21/17.
 */

public class WalletRepository implements WalletDataSource {
    WalletHelper walletHelper;

    public WalletRepository(Context context) {
        this.walletHelper = new WalletHelper(context);
    }

    @Override
    public void createMnemonic(JsCallback callback) {
        try {
            walletHelper.createMnemonic(callback);
        }catch (Exception e){
            LogUtil.e(e);
        }
    }

    @Override
    public void createWallet(String mnemonic, WalletHelper.CreateWalletCallback callback) {
        try {
            walletHelper.createWallet(mnemonic, callback);
        }catch (Exception e){
            LogUtil.e(e);
        }
    }

    @Override
    public void createWallet(WalletHelper.CreateWalletCallback callback) {
        try {
            walletHelper.createWallet(callback);
        }catch (Exception e){
            LogUtil.e(e);
        }
    }

    @Override
    public void saveWallet(Wallet wallet) {
        try{
            String privateKey = wallet.getPrivateKey();
            String address = wallet.getAddress();
            String cipher = CipherClient.wallet();
            String encryptedPrivateKey = AESCrypt.encrypt(cipher, privateKey);
            String encryptedAddress = AESCrypt.encrypt(cipher, address);
            String finalEncrypted = AESCrypt.encrypt(cipher, encryptedAddress + cipher + encryptedPrivateKey);
            SharedPreferences preferences
                    = MainApplication.get().getSharedPreferences("data", Context.MODE_PRIVATE);
            preferences.edit().putString("storage-data",finalEncrypted).apply();
        }catch (Exception e){
            LogUtil.e(e);
        }

    }

    @Override
    public String getPrivateKey() {
        try {
            String encryptedString = MainApplication.get().getSharedPreferences("data", Context.MODE_PRIVATE)
                    .getString("storage-data","");
            if (encryptedString.isEmpty())
                return "";
            String cipher = CipherClient.wallet();
            String tmp1 = AESCrypt.decrypt(cipher, encryptedString);
            String tmp2 = tmp1.split(cipher)[1];
            return AESCrypt.decrypt(cipher, tmp2);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    @Override
    public String getAddress() {
        try {
            String encryptedString = MainApplication.get().getSharedPreferences("data", Context.MODE_PRIVATE)
                    .getString("storage-data","");
            if (encryptedString.isEmpty())
                return "";
            String cipher = CipherClient.wallet();
            String tmp1 = AESCrypt.decrypt(cipher, encryptedString);
            String tmp2 = tmp1.split(cipher)[0];
            return AESCrypt.decrypt(cipher, tmp2);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}
