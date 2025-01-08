package com.alifia.mymaxxio.model;

import com.google.firebase.Timestamp;

import java.util.HashMap;
import java.util.Map;

public class Event {
    private String id;
    private String namaTingkatan;
    private String idRegional;
    private String namaKegiatan;
    private String deskripsiSingkat;
    private String deskripsi;
    private String imageUrl;
    private Timestamp tgl;
    private String alasan;
    private int isApproved;
    public Event() {}

    public Event(String id, String namaTingkatan, String idRegional, String namaKegiatan, String deskripsiSingkat, String deskripsi, Timestamp tgl, String imageUrl) {
        this.id = id;
        this.namaTingkatan = namaTingkatan;
        this.idRegional = idRegional;
        this.namaKegiatan = namaKegiatan;
        this.deskripsiSingkat = deskripsiSingkat;
        this.deskripsi = deskripsi;
        this.tgl = tgl;
        this.imageUrl = imageUrl;
    }

    public Event(String id, String namaTingkatan, String idRegional, String namaKegiatan, String deskripsiSingkat, String deskripsi, Timestamp tgl, String imageUrl, String alasan) {
        this.id = id;
        this.namaTingkatan = namaTingkatan;
        this.idRegional = idRegional;
        this.namaKegiatan = namaKegiatan;
        this.deskripsiSingkat = deskripsiSingkat;
        this.deskripsi = deskripsi;
        this.tgl = tgl;
        this.imageUrl = imageUrl;
        this.alasan = alasan;
    }

    public Event(String id, String namaTingkatan, String idRegional, String namaKegiatan, String deskripsiSingkat, String deskripsi, Timestamp tgl, String imageUrl, String alasan, int isApproved) {
        this.id = id;
        this.namaTingkatan = namaTingkatan;
        this.idRegional = idRegional;
        this.namaKegiatan = namaKegiatan;
        this.deskripsiSingkat = deskripsiSingkat;
        this.deskripsi = deskripsi;
        this.tgl = tgl;
        this.imageUrl = imageUrl;
        this.alasan = alasan;
        this.isApproved = isApproved;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> kegiatan = new HashMap<>();
        kegiatan.put("id", id);
        kegiatan.put("namaTingkatan", namaTingkatan);
        kegiatan.put("idRegional", idRegional);
        kegiatan.put("namaKegiatan", namaKegiatan);
        kegiatan.put("deskripsiKegiatan", deskripsiSingkat);
        kegiatan.put("deskripsi", deskripsi);
        kegiatan.put("tgl", tgl);
        return kegiatan;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNamaTingkatan() {
        return namaTingkatan;
    }

    public void setNamaTingkatan(String namaTingkatan) {
        this.namaTingkatan = namaTingkatan;
    }

    public String getIdRegional() {
        return idRegional;
    }

    public void setIdRegional(String idRegional) {
        this.idRegional = idRegional;
    }

    public String getNamaKegiatan() {
        return namaKegiatan;
    }

    public void setNamaKegiatan(String namaKegiatan) {
        this.namaKegiatan = namaKegiatan;
    }

    public String getDeskripsiSingkat() {
        return deskripsiSingkat;
    }

    public void setDeskripsiSingkat(String deskripsiSingkat) {
        this.deskripsiSingkat = deskripsiSingkat;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public Timestamp getTgl() {
        return tgl;
    }

    public void setTgl(Timestamp tgl) {
        this.tgl = tgl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getAlasan() {
        return alasan;
    }

    public void setAlasan(String alasan) {
        this.alasan = alasan;
    }

    public int getIsApproved() {
        return isApproved;
    }

    public void setIsApproved(int isApproved) {
        this.isApproved = isApproved;
    }
}
