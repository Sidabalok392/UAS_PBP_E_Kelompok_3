package com.example.easy_learning.model;

public class Course {
    private Long id;
    private String nama_modul;
    private String kode;
    private String desc;
    private String url;

    public Course(String nama_modul, String kode, String desc, String url) {
        this.nama_modul = nama_modul;
        this.kode = kode;
        this.desc = desc;
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

    public String getKode() {
        return kode;
    }

    public void setKode(String kode) {
        this.kode = kode;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}