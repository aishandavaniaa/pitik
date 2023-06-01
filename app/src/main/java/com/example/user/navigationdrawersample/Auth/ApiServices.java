package com.example.user.navigationdrawersample.Auth;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.user.navigationdrawersample.Model.DataAyam;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class ApiServices {
    private static String HOST = "http://192.168.43.199:8000";
    private static String API = HOST + "/api/";

    public interface DataAyamResponseListener {
        void onSuccess(List<DataAyam> dataAyamList);
        void onError(String message);
    }
    //read data ayam
    public static void readDataAyam(Context context, final DataAyamResponseListener listener){
        StringRequest stringRequest = new StringRequest(Request.Method.GET, API + "data-ayam-get", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String message = jsonObject.getString("message");
                    List<DataAyam> dataAyams = new ArrayList<>();
                    if (message.equals("success")) {
                        JSONArray dataArray = jsonObject.getJSONArray("data");
                        for (int i = 0; i < dataArray.length(); i++) {
                            JSONObject dataAyamObj = dataArray.getJSONObject(i);
                            // Retrieve data details from the nested object
                            String id = dataAyamObj.getString("id");
                            String tanggalmasuk = dataAyamObj.getString("tanggal_masuk");
                            String jumlahmasuk = dataAyamObj.getString("jumlah_masuk");
                            String hargasatuan = dataAyamObj.getString("harga_satuan");
                            String mati = dataAyamObj.getString("mati");
                            String totalharga = dataAyamObj.getString("total_harga");
                            String totalayam = dataAyamObj.getString("total_ayam");

                            DataAyam dataAyam = new DataAyam(id,tanggalmasuk,jumlahmasuk,hargasatuan,mati,totalharga,totalayam);
                            dataAyams.add(dataAyam);
                        }
                        listener.onSuccess(dataAyams);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error.networkResponse != null && error.networkResponse.data != null) {
                    try {
                        String responseBody = new String(error.networkResponse.data, "utf-8");
                        JSONObject jsonObject = new JSONObject(responseBody);
                        String message = jsonObject.getString("message");
                        listener.onError(message);
                    } catch (JSONException | UnsupportedEncodingException e) {
                        e.printStackTrace();
                        listener.onError("Gagal mendapatkan data: " + e.getMessage());
                    }
                } else {
                    listener.onError("Gagal mendapatkan data: network response is null");
                }
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

}
