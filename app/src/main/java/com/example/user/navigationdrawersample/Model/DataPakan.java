package com.example.user.navigationdrawersample.Model;

import java.io.Serializable;

public class DataPakan  implements Serializable {
    private String id;
    private String pembelian;
    private String jenisPakan;
    private String stok;
    private String harga;
    private String total;

    public DataPakan(String id, String pembelian, String jenisPakan, String stok, String harga, String total) {
        this.id = id;
        this.pembelian = pembelian;
        this.jenisPakan = jenisPakan;
        this.stok = stok;
        this.harga = harga;
        this.total = total;
    }

    public String getId() {
        return id;
    }

    public String getPembelian() {
        return pembelian;
    }

    public String getJenisPakan() {
        return jenisPakan;
    }

    public String getStok() {
        return stok;
    }

    public String getHarga() {
        return harga;
    }

    public String getTotal() {
        return total;
    }
}
