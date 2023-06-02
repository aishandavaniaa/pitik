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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ApiServices {
    private static String HOST = "http://192.168.43.199:8000";
    private static String API = HOST + "/api/";


    public interface CreateDataAyamResponseListener {
        void onSuccess(JSONObject response);
        void onError(String message);
    }
    public interface DataAyamResponseListener {
        void onSuccess(List<DataAyam> dataAyamList);
        void onError(String message);
    }
    public interface DeleteDataAyamResponseListener {
        void onSuccess(String response);
        void onError(String message);
    }

    //create data ayam
    public static void createDataAyam(Context context, String tanggal_masuk, String jumlah_masuk, String harga_satuan, String mati, CreateDataAyamResponseListener listener) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, API + "data-ayam", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String message = jsonObject.getString("message");
                    if (message.equals("success")) {
                        JSONObject data = jsonObject.getJSONObject("dataayam");
                        listener.onSuccess(data);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener(){
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
                                listener.onError("Gagal tambah data: " + e.getMessage());
                            }
                        } else {
                            listener.onError("Gagal register: network response is null");
                        }
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("tanggal_masuk", tanggal_masuk);
                params.put("jumlah_masuk", jumlah_masuk);
                params.put("harga_satuan", harga_satuan);
                params.put("mati", mati);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

    //read data ayam
    public static void readDataAyam(Context context, final DataAyamResponseListener listener){
        StringRequest stringRequest = new StringRequest(Request.Method.GET, API + "data-ayam", new Response.Listener<String>() {
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
                            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
                            Date parsedDate = inputFormat.parse(tanggalmasuk);

                            // Format tanggal ke dalam format dd-MMMM-yyyy
                            SimpleDateFormat outputFormat = new SimpleDateFormat("dd MMMM yyyy");
                            String formattedTanggalMasuk = outputFormat.format(parsedDate);

                            DataAyam dataAyam = new DataAyam(id,formattedTanggalMasuk,jumlahmasuk,hargasatuan,mati,totalharga,totalayam);
                            dataAyams.add(dataAyam);
                        }
                        listener.onSuccess(dataAyams);
                    }
                } catch (JSONException | ParseException e) {
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

    //update data ayam
    public static void updateDataAyam(Context context, String id, String tanggal_masuk, String jumlah_masuk, String harga_satuan,String mati, CreateDataAyamResponseListener listener) {
        StringRequest stringRequest = new StringRequest(Request.Method.PUT, API + "data-ayam/" + id, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String message = jsonObject.getString("message");
                    if (message.equals("success")) {
                        JSONObject data = jsonObject.getJSONObject("dataayam");
                        listener.onSuccess(data);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener(){
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
                                listener.onError("Gagal tambah data: " + e.getMessage());
                            }
                        } else {
                            listener.onError("Gagal register: network response is null");
                        }
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("tanggal_masuk", tanggal_masuk);
                params.put("jumlah_masuk", jumlah_masuk);
                params.put("harga_satuan", harga_satuan);
                params.put("mati", mati);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

    //delete data ayam
    public static void deleteDataAyam(Context context, String id, DeleteDataAyamResponseListener listener) {
        StringRequest stringRequest = new StringRequest(Request.Method.DELETE, API + "data-ayam/" + id, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String message = jsonObject.getString("message");
                    if (message.equals("success")) {
                        listener.onSuccess("Berhasil menghapus data");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error.networkResponse != null && error.networkResponse.data != null) {
                            try {
                                String responseBody = new String(error.networkResponse.data, "utf-8");
                                JSONObject jsonObject = new JSONObject(responseBody);
//                                String message = jsonObject.getString("message");
//                                listener.onError(message);
                            } catch (JSONException | UnsupportedEncodingException e) {
                                e.printStackTrace();
                                listener.onError("Gagal hapus data: " + e.getMessage());
                            }
                        } else {
                            listener.onError("Gagal hapus: network response is null");
                        }
                    }
                })
        {
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

}
