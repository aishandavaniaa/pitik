package com.example.user.navigationdrawersample.Model;

import java.io.Serializable;

public class DataOvk implements Serializable {
    private String id;
    private String tanggalOvk;
    private String jenisOvk;
    private String jumlahAyam;
    private String nextOvk;
    private String biayaOvk;
    private String totalBiaya;

    public DataOvk(String id, String tanggalOvk, String jenisOvk, String jumlahAyam, String nextOvk, String biayaOvk, String totalBiaya) {
        this.id = id;
        this.tanggalOvk = tanggalOvk;
        this.jenisOvk = jenisOvk;
        this.jumlahAyam = jumlahAyam;
        this.nextOvk = nextOvk;
        this.biayaOvk = biayaOvk;
        this.totalBiaya = totalBiaya;
    }

    public String getId() {
        return id;
    }

    public String getTanggalOvk() {
        return tanggalOvk;
    }

    public String getJenisOvk() {
        return jenisOvk;
    }

    public String getNextOvk() {
        return nextOvk;
    }

    public String getJumlahAyam() {
        return jumlahAyam;
    }

    public String getBiayaOvk() {
        return biayaOvk;
    }

    public String getTotalBiaya() {
        return totalBiaya;
    }
}
