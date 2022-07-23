package com.example.chatsdk_android;


import android.content.Context;
import android.util.Log;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {


    private static Retrofit retrofitInstance , retrofitToken;
    private static String BASE_URL="https://staging.360scrm.com/api/WebChat/";
    private static String BASE_URL_TOKEN="https://jomo.360scrm.com/";
    private WebSocket webSocket;
    String SERVER_PATH ,token;
    Context context;
    CallBack callBack;

    public RetrofitClient(Context context, CallBack callBack) {
        this.context = context;
        this.callBack = callBack;
    }

//   private String SERVER_PATH = "wss://demo.piesocket.com/v3/channel_1?api_key=VCXCEuvhGcBDP7XhiJJUDvR1e1D3eiVjgZ9VRiaV&notify_self";

    public static Retrofit getRetrofitInstance(){
        if(retrofitInstance == null){
            retrofitInstance = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return  retrofitInstance;
    }
    public static Retrofit retrofitTokenInstance(){
        if(retrofitToken == null){
            retrofitToken = new Retrofit.Builder()
                    .baseUrl(BASE_URL_TOKEN)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return  retrofitToken;
    }

    ApiMethods apiMethods = RetrofitClient.getRetrofitInstance().create(ApiMethods.class);
    ApiMethods tokenMethod = RetrofitClient.retrofitTokenInstance().create(ApiMethods.class);

    // Public Methods
    //
    public void GetChannelToken(Context context ){
        Call<TokenResponse> call = tokenMethod.getToken("password","admin@ibex.co","Jomoadmin@123");
        call.enqueue(new Callback<TokenResponse>() {
            @Override
            public void onResponse(Call<TokenResponse> call, Response<TokenResponse> response) {
                TokenResponse tokenResponse = response.body();
                token = tokenResponse.getAccessToken();
                Log.d("responseApi", "onResponse: " + response.code());
                Log.d("responseApi", "onResponse: " + tokenResponse.getAccessToken());
                initiateSocketConnection(token);
            }

            @Override
            public void onFailure(Call<TokenResponse> call, Throwable t) {
                Log.d("responseApi", "onFailure: " + t.getMessage());
            }
        });
    }
    public void initiateSocketConnection(String channelToken ) {
        token = "VCXCEuvhGcBDP7XhiJJUDvR1e1D3eiVjgZ9VRiaV&notify_self";
        SERVER_PATH = "wss://demo.piesocket.com/v3/channel_1?api_key=" + token;
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(SERVER_PATH).build();
        webSocket = client.newWebSocket(request, new SocketChannel());
    }




    class SocketChannel extends WebSocketListener {
        @Override
        public void onOpen(WebSocket webSocket, okhttp3.Response response) {
            super.onOpen(webSocket, response);
            callBack.messageCallBack(webSocket, response.message());
            Log.d("responsedata", "message: " + response.message());



        }

        @Override
        public void onMessage(WebSocket webSocket, String text) {
            super.onMessage(webSocket, text);
            callBack.messageCallBack(webSocket,text);
        //    Log.d("responsedata", "message: " + text);



//        runOnUiThread(() -> {
//            //PopulateAdapterClass()
//            try {
//                JSONObject jsonObject = new JSONObject(text);
//                jsonObject.put("isSent", false);
//                Log.d("message", "onMessage: " + text);
//                if(jsonObject.getString("name").equals(name)
//                        || jsonObject.getString("message").isEmpty()){
//
//                }else {
//     //               messageAdapter.addItem(jsonObject);
//     //               recyclerView.smoothScrollToPosition(messageAdapter.getItemCount() - 1);
//                }
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//
//        });

        }

    }

}

