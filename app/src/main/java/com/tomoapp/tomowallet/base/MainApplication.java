package com.tomoapp.tomowallet.base;

import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.tomoapp.tomowallet.BuildConfig;
import com.tomoapp.tomowallet.helper.LogUtil;
import com.tomoapp.tomowallet.helper.socket.TOMOSocketListener;

import org.json.JSONObject;

import java.math.BigInteger;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;


/**
 * Created by macbook on 12/21/17.
 */

public class MainApplication extends MultiDexApplication {


    private static MainApplication instance;
    private Socket socket;
    @Override
    public void onCreate() {
        super.onCreate();
        MultiDex.install(this);
        instance = this;

    }

    private void initSocket(final TOMOSocketListener callback){
        try {
            socket = IO.socket(BuildConfig.API_ENDPOINT);
            socket.on(io.socket.client.Socket.EVENT_CONNECT, new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    callback.onSocketConnected();
                }

            }).on("user", new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    for (Object object : args) {
                        LogUtil.d("onUser: " + object.toString());
                    }
                    callback.onRetrieveUserInfo(args[0].toString());
                }
            }).on("reward", new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    for (Object object : args) {
                        LogUtil.d("onReward: " + object.toString());
                    }
                    callback.onRetrieveReward(args[0].toString());
                }
            }).on("cashIn", new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    for (Object object : args) {
                        LogUtil.d("onCashIn: " + object.toString());
                    }
                    callback.onCashedIn(args[0].toString());
                }
            }).on("cashOut", new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    for (Object object : args) {
                        LogUtil.d("onCashOut: " + object.toString());
                    }
                    callback.onCashedOut(args[0].toString());
                }
            }).on(io.socket.client.Socket.EVENT_DISCONNECT, new Emitter.Listener() {

                @Override
                public void call(Object... args) {
                    for (Object object : args) {
                        LogUtil.d("onDisconnect: " + object);
                    }
                    callback.onCashedOut(args[0].toString());
                }

            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void connectSocket(TOMOSocketListener callback){
        try {
            if (socket == null)
                initSocket(callback);
            if (!socket.connected())
                socket.connect();
        } catch (Exception e) {
            LogUtil.e(e);
        }
    }

    public void disconnectSocket(TOMOSocketListener callback){
        try {
            if (socket == null)
                initSocket(callback);
            if (socket.connected())
                socket.disconnect();
        } catch (Exception e) {
            LogUtil.e(e);
        }
    }

    public void emitUser(String walletAddress){
        try {
            if (socket == null || !socket.connected())
                return;
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("address", walletAddress);
            socket.emit("user", jsonObject);
        } catch (Exception e) {
            LogUtil.e(e);
        }
    }



    public static MainApplication get(){
        return instance;
    }
}
