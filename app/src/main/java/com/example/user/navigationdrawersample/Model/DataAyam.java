package com.example.user.navigationdrawersample.Model;

import java.io.Serializable;

public class DataAyam implements Serializable {
    private String id;
    private String tanggalMasuk;
    private String jumlahMasuk;
    private String hargaSatuan;
    private String mati;
    private String totalHarga;
    private String totalAyam;

    public DataAyam(String id,String tanggalMasuk, String jumlahMasuk, String hargaSatuan, String mati, String totalHarga, String totalAyam) {
        this.id = id;
        this.tanggalMasuk = tanggalMasuk;
        this.jumlahMasuk = jumlahMasuk;
        this.hargaSatuan = hargaSatuan;
        this.mati = mati;
        this.totalHarga = totalHarga;
        this.totalAyam = totalAyam;
    }

    public String getId(){ return id;}

    public String getTanggalMasuk() {
        return tanggalMasuk;
    }

    public String getJumlahMasuk() {
        return jumlahMasuk;
    }

    public String getHargaSatuan() {
        return hargaSatuan;
    }

    public String getMati() {
        return mati;
    }

    public String getTotalHarga() {
        return totalHarga;
    }

    public String getTotalAyam() {
        return totalAyam;
    }
}
