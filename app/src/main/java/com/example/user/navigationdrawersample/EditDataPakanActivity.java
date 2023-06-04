package com.example.user.navigationdrawersample;

import android.app.DatePickerDialog;
import android.content.Intent;
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
import com.example.user.navigationdrawersample.Model.DataPakan;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class EditDataPakanActivity extends AppCompatActivity {

    TextView epembelian,ejenis, estok,eharga;
    Button ebatal,esave;
    private DatePickerDialog.OnDateSetListener mDate;
    String formattedDate = "";
    DataPakan dataPakan;
    String id = "";
    String pembelian = "";
    String jenispakan = "";
    String stokpakan = "";
    String hargakg = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_data_pakan);

        epembelian = findViewById(R.id.pembelian);
        ejenis = findViewById(R.id.jenispakan);
        eharga = findViewById(R.id.hargakg);
        estok = findViewById(R.id.stokpakan);
        esave = findViewById(R.id.save);
        ebatal = findViewById(R.id.batal);


        Intent intent = getIntent();
        if (intent.getExtras() != null) {
            dataPakan = (DataPakan) intent.getSerializableExtra("datapakan");
            id = dataPakan.getId();
            pembelian = dataPakan.getPembelian();
            jenispakan = dataPakan.getJenisPakan();
            stokpakan = dataPakan.getStok();
            hargakg = dataPakan.getHarga();
        }
        epembelian.setText(pembelian);
        ejenis.setText(jenispakan);
        eharga.setText(hargakg);
        estok.setText(stokpakan);
        formattedDate = pembelian;

        epembelian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(EditDataPakanActivity.this,android.R.style.Theme_Material_Light_Dialog_MinWidth,
                        mDate, year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
                dialog.show();
            }
        });
        mDate = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                Calendar selectedDate = Calendar.getInstance();
                selectedDate.set(year, month, dayOfMonth);

                SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());
                formattedDate = dateFormat.format(selectedDate.getTime());

                epembelian.setText(formattedDate);
            }
        };
        ebatal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        esave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());

                try {
                    Date date = dateFormat.parse(formattedDate);
                    SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                    String new_pembelian = outputFormat.format(date);
                    String new_jenis = ejenis.getText().toString().trim();
                    String new_stok = estok.getText().toString().trim();
                    String new_harga = eharga.getText().toString().trim();

                    ApiServices.updateDataPakan(getApplicationContext(), id, new_pembelian, new_jenis, new_stok, new_harga, new ApiServices.CreateDataPakanResponseListener() {
                        @Override
                        public void onSuccess(JSONObject response) {
                            Toast.makeText(EditDataPakanActivity.this, "Berhasil mengupdate data pakan", Toast.LENGTH_SHORT).show();
                            onBackPressed();
                        }

                        @Override
                        public void onError(String message) {
                            Toast.makeText(EditDataPakanActivity.this, "Gagal mengupdate data pakan", Toast.LENGTH_SHORT).show();
                        }
                    });
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }
        });
    }
}