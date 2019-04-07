package com.a952000243.ingwilson.nosliwsys.tema_test;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;

public class OkHttpRequest {
    private String url;
    private OkHttpClient client = new OkHttpClient();
    // Constructor
    public OkHttpRequest(String url) {
// Se asigna la URL en la instancia
        this.url = url;
    }
    // Metodo para enviar una peticion GET sin parametro
    public Call getNotParams(Callback callback) {
// Se crea un request y la url del
// pedido
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();
// Se ejecuta el pedido
        Call call = client.newCall(request);
        call.enqueue(callback);
        return call;
    }
}
