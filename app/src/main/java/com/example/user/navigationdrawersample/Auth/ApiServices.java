package com.example.user.navigationdrawersample.Auth;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.user.navigationdrawersample.Model.DataAyam;
import com.example.user.navigationdrawersample.Model.DataOvk;
import com.example.user.navigationdrawersample.Model.DataPakan;

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
    private static String HOST = "http://172.20.10.9:8000";
    private static String API = HOST + "/api";
    private static String LOGIN = API + "/login";

    public static String getDataAyam() {
        return DATA_AYAM;
    }

    private static String DATA_AYAM = API +"/data-ayam-counts";

    public static String getStokPakan() {
        return STOK_PAKAN;
    }

    private static String STOK_PAKAN = API +"/data-pakan-stok";

    public static String getMingguIni() {
        return MINGGU_INI;
    }

    private static String MINGGU_INI = API +"/data-pakan-weeks";

    public static String getBulanIni() {
        return BULAN_INI;
    }

    private static String BULAN_INI = API +"/data-pakan-month";

    public static String getDataOvk() {
        return DATA_OVK;
    }

    private static String DATA_OVK = API +"/data-ovk-last";

    public static String getLOGOUT() {
        return LOGOUT;
    }

    private static String LOGOUT = API+"/logout";


    public static String getApiLogin() {
        return LOGIN;
    }

    public interface LoginResponseListener {
        void onSuccess(String response);
        void onError(String message);
    }

    public static void login(Context context, String email, String pass, LoginResponseListener listener) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, getApiLogin(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Response", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String message = jsonObject.getString("message");
                    if (message.equals("success")){
                        String token = jsonObject.getString("token");
                        SharedPreferences.Editor editor = context.getSharedPreferences("PHITIX", MODE_PRIVATE).edit();
                        editor.putString("token", token);
                        editor.putBoolean("isLogin", true);
                        editor.apply();
                        listener.onSuccess("Berhasil Login");
                    }
                } catch (JSONException e){
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
                                if (message.equals("Email belum terdaftar")) {
                                    listener.onError("Email Anda Belum Terdaftar");
                                } else if (message.equals("incorrect password")) {
                                    listener.onError("Password Anda Salah");
                                }
                            } catch (JSONException | UnsupportedEncodingException e) {
                                e.printStackTrace();
                                listener.onError("Gagal Login: " + e.getMessage());
                            }
                        }
                        else{
                            listener.onError("Gagal Login: network response is null");
                            Log.e("AuthServices", "Error: " + error.getMessage());
                        }
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("email", email);
                params.put("password", pass);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

    public interface LogoutResponseListener {
        void onSuccess(String message);
        void onError(String message);
    }

    public static void logOut(Context context, String token, LogoutResponseListener listener) {
        StringRequest request = new StringRequest(Request.Method.POST, getLOGOUT(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                listener.onSuccess(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                listener.onError(error.getMessage());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + token);
                return headers;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(request);
    }



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
    public static void createDataAyam(Context context, String jumlah_masuk, String harga_satuan, String mati, CreateDataAyamResponseListener listener) {
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
    public static void updateDataAyam(Context context, String id, String jumlah_masuk, String harga_satuan,String mati, CreateDataAyamResponseListener listener) {
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


    public interface CreateDataPakanResponseListener {
        void onSuccess(JSONObject response);
        void onError(String message);
    }
    public interface DataPakanResponseListener {
        void onSuccess(List<DataPakan> dataPakanList);
        void onError(String message);
    }
    public interface DeleteDataPakanResponseListener {
        void onSuccess(String response);
        void onError(String message);
    }


    //create data pakan
    public static void createDataPakan(Context context, String pembelian, String jenis_pakan, String stok, String harga, CreateDataPakanResponseListener listener) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, API + "data-pakan", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String message = jsonObject.getString("message");
                    if (message.equals("success")) {
                        JSONObject data = jsonObject.getJSONObject("datapakan");
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
                params.put("pembelian", pembelian);
                params.put("jenis_pakan", jenis_pakan);
                params.put("stok_pakan", stok);
                params.put("harga_kg", harga);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

    //read data pakan
    public static void readDataPakan(Context context, final DataPakanResponseListener listener){
        StringRequest stringRequest = new StringRequest(Request.Method.GET, API + "data-pakan", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String message = jsonObject.getString("message");
                    List<DataPakan> dataPakans = new ArrayList<>();
                    if (message.equals("success")) {
                        JSONArray dataArray = jsonObject.getJSONArray("datapakan");
                        for (int i = 0; i < dataArray.length(); i++) {
                            JSONObject dataPakanObj = dataArray.getJSONObject(i);
                            // Retrieve data details from the nested object
                            String id = dataPakanObj.getString("id");
                            String pembelian = dataPakanObj.getString("pembelian");
                            String jenis = dataPakanObj.getString("jenis_pakan");
                            String stok = dataPakanObj.getString("stok_pakan");
                            String harga = dataPakanObj.getString("harga_kg");
                            String totalharga = dataPakanObj.getString("total_harga");
                            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
                            Date parsedDate = inputFormat.parse(pembelian);

                            // Format tanggal ke dalam format dd-MMMM-yyyy
                            SimpleDateFormat outputFormat = new SimpleDateFormat("dd MMMM yyyy");
                            String formattedPembelian = outputFormat.format(parsedDate);

                            DataPakan dataPakan = new DataPakan(id,formattedPembelian,jenis,stok,harga,totalharga);
                            dataPakans.add(dataPakan);
                        }
                        listener.onSuccess(dataPakans);
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

    //update data pakan
    public static void updateDataPakan(Context context, String id, String pembelian, String jenis_pakan, String stok,String harga, CreateDataPakanResponseListener listener) {
        StringRequest stringRequest = new StringRequest(Request.Method.PUT, API + "data-pakan/" + id, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String message = jsonObject.getString("message");
                    if (message.equals("success")) {
                        JSONObject data = jsonObject.getJSONObject("datapakan");
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
                                listener.onError("Gagal update data: " + e.getMessage());
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
                params.put("pembelian", pembelian);
                params.put("jenis_pakan", jenis_pakan);
                params.put("stok_pakan", stok);
                params.put("harga_kg", harga);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

    //delete data pakan
    public static void deleteDataPakan(Context context, String id, DeleteDataPakanResponseListener listener) {
        StringRequest stringRequest = new StringRequest(Request.Method.DELETE, API + "data-pakan/" + id, new Response.Listener<String>() {
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

    public interface CreateDataOvkResponseListener {
        void onSuccess(JSONObject response);
        void onError(String message);
    }
    public interface DataOvkResponseListener {
        void onSuccess(List<DataOvk> dataOvkList);
        void onError(String message);
    }
    public interface DeleteDataOvkResponseListener {
        void onSuccess(String response);
        void onError(String message);
    }

    public static void createDataOvk(Context context, String tanggalOvk, String jenisOvk, String jumlahAyam, String nextOvk, String biayaOvk, CreateDataOvkResponseListener listener) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, API + "data-ovk", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String message = jsonObject.getString("message");
                    if (message.equals("success")) {
                        JSONObject data = jsonObject.getJSONObject("ovk");
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
                params.put("tanggal_ovk", tanggalOvk);
                params.put("jenis_ovk", jenisOvk);
                params.put("jumlah_ayam", jumlahAyam);
                params.put("next_ovk", nextOvk);
                params.put("biaya_ovk", biayaOvk);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

    public static void readDataOvk(Context context, final DataOvkResponseListener listener){
        StringRequest stringRequest = new StringRequest(Request.Method.GET, API + "data-ovk", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String message = jsonObject.getString("message");
                    List<DataOvk> dataOvks = new ArrayList<>();
                    if (message.equals("success")) {
                        JSONArray dataArray = jsonObject.getJSONArray("ovk");
                        for (int i = 0; i < dataArray.length(); i++) {
                            JSONObject dataOvkObj = dataArray.getJSONObject(i);
                            // Retrieve data details from the nested object
                            String id = dataOvkObj.getString("id");
                            String tanggal_ovk = dataOvkObj.getString("tanggal_ovk");
                            String jenis_ovk = dataOvkObj.getString("jenis_ovk");
                            String jumlah_ayam = dataOvkObj.getString("jumlah_ayam");
                            String next_ovk = dataOvkObj.getString("next_ovk");
                            String biaya_ovk = dataOvkObj.getString("biaya_ovk");
                            String total_biaya = dataOvkObj.getString("total_biaya");
                            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
                            Date parsedDate = inputFormat.parse(tanggal_ovk);
                            Date parsedDate2 = inputFormat.parse(next_ovk);

                            // Format tanggal ke dalam format dd-MMMM-yyyy
                            SimpleDateFormat outputFormat = new SimpleDateFormat("dd MMMM yyyy");
                            String formatted = outputFormat.format(parsedDate);
                            String formatted2 = outputFormat.format(parsedDate2);

                            DataOvk dataOvk = new DataOvk(id,formatted,jenis_ovk,jumlah_ayam,formatted2,biaya_ovk, total_biaya);
                            dataOvks.add(dataOvk);
                        }
                        listener.onSuccess(dataOvks);
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

    public static void updateDataOvk(Context context, String id, String tanggalOvk, String jenisOvk, String jumlahAyam, String nextOvk, String biayaOvk, CreateDataOvkResponseListener listener) {
        StringRequest stringRequest = new StringRequest(Request.Method.PUT, API + "data-ovk/" + id, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String message = jsonObject.getString("message");
                    if (message.equals("success")) {
                        JSONObject data = jsonObject.getJSONObject("ovk");
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
                params.put("tanggal_ovk", tanggalOvk);
                params.put("jenis_ovk", jenisOvk);
                params.put("jumlah_ayam", jumlahAyam);
                params.put("next_ovk", nextOvk);
                params.put("biaya_ovk", biayaOvk);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

    public static void deleteDataOvk(Context context, String id, DeleteDataOvkResponseListener listener) {
        StringRequest stringRequest = new StringRequest(Request.Method.DELETE, API + "data-ovk/" + id, new Response.Listener<String>() {
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
