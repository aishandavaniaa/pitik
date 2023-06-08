package com.example.user.navigationdrawersample;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.user.navigationdrawersample.Auth.ApiServices;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MessageFragment extends Fragment {

    TextView tv_ayam_masuk,tv_ayam_mati,tv_jumlah_ayam,tv_bulan_ini,tv_minggu_ini,tv_stok_pakan,tv_tanggal_ovk,tv_next_ovk;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_message_layout,container,false);



        tv_ayam_masuk = view.findViewById(R.id.tv_ayam_masuk);
        tv_ayam_mati = view.findViewById(R.id.tv_ayam_mati);
        tv_jumlah_ayam= view.findViewById(R.id.tv_jumlah_ayam);
        tv_bulan_ini = view.findViewById(R.id.tv_bulan_ini);
        tv_minggu_ini = view.findViewById(R.id.tv_minggu_ini);
        tv_stok_pakan = view.findViewById(R.id.tv_stok_pakan);
        tv_tanggal_ovk = view.findViewById(R.id.tv_tanggal_ovk);
        tv_next_ovk = view.findViewById(R.id.tv_next_ovk);

        showDataAyam(getContext());
        showDataPakanMingguIni(getContext());
        showDataPakanBulanIni(getContext());
        showDataPakanStock(getContext());
        showTanggalOvk(getContext());
        return view;
    }

    private void showTanggalOvk(Context context) {
        RequestQueue queue = Volley.newRequestQueue(context);
        JSONObject jsonObject = new JSONObject();
        final String requestBody = jsonObject.toString();

        StringRequest stringRequest = new StringRequest(
                Request.Method.GET, ApiServices.getDataOvk(), new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jo = new JSONObject(response.toString());
                    String tanggal_ovk = jo.getJSONObject("ovk").getString("tanggal_ovk");
                    String next_ovk= jo.getJSONObject("ovk").getString("next_ovk");

                    tv_tanggal_ovk.setText(tanggal_ovk);
                    tv_next_ovk.setText(next_ovk);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(context, "Gagal mengabil data API"+error, Toast.LENGTH_SHORT).show();
                    }
                }
        );
        queue.add(stringRequest);

    }

    private void showDataPakanMingguIni(Context context) {

        RequestQueue queue = Volley.newRequestQueue(context);
        JSONObject jsonObject = new JSONObject();
        final String requestBody = jsonObject.toString();

        StringRequest stringRequest = new StringRequest(
                Request.Method.GET, ApiServices.getMingguIni(), new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jo = new JSONObject(response.toString());
                    String minggu_ini = jo.getString("jumlah_pakan_minggu_ini");

                    tv_minggu_ini.setText(minggu_ini);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(context, "Gagal mengabil data API"+error, Toast.LENGTH_SHORT).show();
                    }
                }
        );
        queue.add(stringRequest);
    }

    private void showDataPakanBulanIni(Context context) {
        RequestQueue queue = Volley.newRequestQueue(context);
        JSONObject jsonObject = new JSONObject();
        final String requestBody = jsonObject.toString();

        StringRequest stringRequest = new StringRequest(
                Request.Method.GET, ApiServices.getBulanIni(), new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jo = new JSONObject(response.toString());
                    String bulan_ini = jo.getString("jumlah_pakan_bulan_ini");

                    tv_bulan_ini.setText(bulan_ini);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(context, "Gagal mengabil data API"+error, Toast.LENGTH_SHORT).show();
                    }
                }
        );
        queue.add(stringRequest);

    }

    private void showDataPakanStock(Context context) {
        RequestQueue queue = Volley.newRequestQueue(context);
        JSONObject jsonObject = new JSONObject();
        final String requestBody = jsonObject.toString();

        StringRequest stringRequest = new StringRequest(
                Request.Method.GET, ApiServices.getStokPakan(), new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jo = new JSONObject(response.toString());
                    String stok_pakan = jo.getString("stock_pakan");

                    tv_stok_pakan.setText(stok_pakan);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(context, "Gagal mengabil data API"+error, Toast.LENGTH_SHORT).show();
                    }
                }
        );
        queue.add(stringRequest);
    }

    private void showDataAyam(Context context) {
        RequestQueue queue = Volley.newRequestQueue(context);
        JSONObject jsonObject = new JSONObject();
        final String requestBody = jsonObject.toString();

        StringRequest stringRequest = new StringRequest(
                Request.Method.GET, ApiServices.getDataAyam(), new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jo = new JSONObject(response.toString());
                    String ayam_masuk  = jo.getString("jumlah_masuk");
                    String ayam_mati = jo.getString("total_mati");
                    String total_ayam = jo.getString("total_ayam");

                    tv_ayam_masuk.setText(ayam_masuk);
                    tv_ayam_mati.setText(ayam_mati);
                    tv_jumlah_ayam.setText(total_ayam);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(context, "Gagal mengabil data API"+error, Toast.LENGTH_SHORT).show();
                    }
                }
        );
        queue.add(stringRequest);
    }
}

