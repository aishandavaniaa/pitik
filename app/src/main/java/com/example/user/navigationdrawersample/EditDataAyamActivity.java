package com.example.user.navigationdrawersample;

import android.annotation.SuppressLint;
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
import com.example.user.navigationdrawersample.Model.DataAyam;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class EditDataAyamActivity extends AppCompatActivity {

    TextView etanggal,ejumlahmasuk, ehargasatuan,emati;
    Button ebatal,esave;
    private DatePickerDialog.OnDateSetListener mDate;
    String formattedDate = "";
    DataAyam dataAyam;
    String id = "";
    String tanggal = "";
    String jumlah = "";
    String harga = "";
    String mati = "";
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_data_ayam);
        etanggal = findViewById(R.id.tanggalmasuk);
        ejumlahmasuk = findViewById(R.id.jumlahmasuk);
        ehargasatuan = findViewById(R.id.hargasatuan);
        emati = findViewById(R.id.mati);
        esave = findViewById(R.id.save);
        ebatal = findViewById(R.id.batal);

        Intent intent = getIntent();
        if (intent.getExtras() != null) {
            dataAyam = (DataAyam) intent.getSerializableExtra("dataayam");
            id = dataAyam.getId();
            tanggal = dataAyam.getTanggalMasuk();
            jumlah = dataAyam.getJumlahMasuk();
            harga = dataAyam.getHargaSatuan();
            mati = dataAyam.getMati();
        }
        etanggal.setText(tanggal);
        ejumlahmasuk.setText(jumlah);
        ehargasatuan.setText(harga);
        emati.setText(mati);
        formattedDate = tanggal;
        Log.e("format",formattedDate);
        etanggal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(EditDataAyamActivity.this,android.R.style.Theme_Material_Light_Dialog_MinWidth,
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

                etanggal.setText(formattedDate);
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
                    String new_tanggal = outputFormat.format(date);
                    String new_jumlah = ejumlahmasuk.getText().toString().trim();
                    String new_harga = ehargasatuan.getText().toString().trim();
                    String new_mati = emati.getText().toString().trim();

                    if (Integer.parseInt(new_mati) > Integer.parseInt(new_jumlah)) {
                        Toast.makeText(EditDataAyamActivity.this, "Jumlah mati tidak boleh lebih besar dari jumlah masuk", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    ApiServices.updateDataAyam(getApplicationContext(), id, new_tanggal, new_jumlah, new_harga, new_mati, new ApiServices.CreateDataAyamResponseListener() {
                        @Override
                        public void onSuccess(JSONObject response) {
                            Toast.makeText(EditDataAyamActivity.this, "Berhasil mengupdate data ayam", Toast.LENGTH_SHORT).show();
                            ApiServices.readDataAyam(getApplicationContext(), new ApiServices.DataAyamResponseListener() {
                                @Override
                                public void onSuccess(List<DataAyam> dataAyamList) {
                                    onBackPressed();
                                }

                                @Override
                                public void onError(String message) {

                                }
                            });

                        }

                        @Override
                        public void onError(String message) {
                            Toast.makeText(EditDataAyamActivity.this, "Gagal mengupdate data ayam", Toast.LENGTH_SHORT).show();
                        }
                    });
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }
        });
    }
}