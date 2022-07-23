package com.example.chatsdk_android;

import okhttp3.WebSocket;

public interface CallBack {
    public void messageCallBack(WebSocket webSocket, String message);

}
