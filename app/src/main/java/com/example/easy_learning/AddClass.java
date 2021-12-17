package com.example.easy_learning;

public class AddClass
{
    private String nama_class;
    private String kode_class;
    private String desc_class;
    private String url_class;

    public AddClass(String nama_class, String kode_class, String desc_class, String url_class) {
        this.nama_class = nama_class;
        this.kode_class = kode_class;
        this.desc_class = desc_class;
        this.url_class = url_class;
    }

    public String getNama_class() {
        return nama_class;
    }

    public void setNama_class(String nama_class) {
        this.nama_class = nama_class;
    }

    public String getKode_class() {
        return kode_class;
    }

    public void setKode_class(String kode_class) {
        this.kode_class = kode_class;
    }

    public String getDesc_class() {
        return desc_class;
    }

    public void setDesc_class(String desc_class) {
        this.desc_class = desc_class;
    }

    public String getUrl_class() {
        return url_class;
    }

    public void setUrl_class(String url_class) {
        this.url_class = url_class;
    }
}
