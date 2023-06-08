package com.example.user.navigationdrawersample;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.navigationdrawersample.Auth.ApiServices;
import com.example.user.navigationdrawersample.Model.DataAyam;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AddDataAyamActivity extends AppCompatActivity {

    TextView tv_tanggal,tv_jumlah_masuk, tv_harga_satuan,tv_mati;
    Button reset,save;
    private DatePickerDialog.OnDateSetListener mDate;
    String formattedDate = "";
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_data_ayam);
        tv_tanggal = findViewById(R.id.tanggalmasuk);
        tv_jumlah_masuk = findViewById(R.id.jumlahmasuk);
        tv_harga_satuan = findViewById(R.id.hargasatuan);
        tv_mati = findViewById(R.id.mati);
        save = findViewById(R.id.save);
        reset = findViewById(R.id.reset);

        formattedDate = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault()).format(new Date());
        tv_tanggal.setText(formattedDate);
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_tanggal.setText("");
                tv_jumlah_masuk.setText("");
                tv_harga_satuan.setText("");
                tv_mati.setText("");
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    String new_jumlah = tv_jumlah_masuk.getText().toString().trim();
                    String new_harga = tv_harga_satuan.getText().toString().trim();
                    String new_mati = tv_mati.getText().toString().trim();

                    if (Integer.parseInt(new_mati) > Integer.parseInt(new_jumlah)) {
                        Toast.makeText(AddDataAyamActivity.this, "Jumlah mati tidak boleh lebih besar dari jumlah masuk", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    ApiServices.createDataAyam(getApplicationContext(), new_jumlah, new_harga, new_mati, new ApiServices.CreateDataAyamResponseListener() {
                        @Override
                        public void onSuccess(JSONObject response) {
                            Toast.makeText(AddDataAyamActivity.this, "Berhasil menambah data ayam", Toast.LENGTH_SHORT).show();
                            tv_tanggal.setText("");
                            tv_jumlah_masuk.setText("");
                            tv_harga_satuan.setText("");
                            tv_mati.setText("");
                            ApiServices.readDataAyam(getApplicationContext(), new ApiServices.DataAyamResponseListener() {
                                @Override
                                public void onSuccess(List<DataAyam> dataAyamList) {
                                    onBackPressed();
                                }

                                @Override
                                public void onError(String message) {
                                    Log.e("error", message);
//                                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                                }
                            });

                        }

                        @Override
                        public void onError(String message) {
                            Toast.makeText(AddDataAyamActivity.this, message, Toast.LENGTH_SHORT).show();
                        }
                    });
                }

        });
    }
}