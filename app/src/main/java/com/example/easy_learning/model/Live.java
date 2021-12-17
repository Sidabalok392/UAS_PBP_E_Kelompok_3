package com.example.easy_learning.model;

public class Live {
    private Long id;
    private String nama_modul;
    private String sesi;
    private String tanggal;
    private String url;

    public Live(String nama_modul, String sesi, String tanggal, String url) {
        this.nama_modul = nama_modul;
        this.sesi = sesi;
        this.tanggal = tanggal;
        this.url = url;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNama_modul() {
        return nama_modul;
    }

    public void setNama_modul(String nama_modul) {
        this.nama_modul = nama_modul;
    }

    public String getSesi() {
        return sesi;
    }

    public void setSesi(String sesi) {
        this.sesi = sesi;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
