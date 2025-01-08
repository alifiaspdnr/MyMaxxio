package com.alifia.mymaxxio.model;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

public class Notif {
    public String id;
    public String isiPengumuman;
    public String judulPengumuman;
    public String namaChapter;
    public String namaRegional;
    public String namaTingkatan;

    public Notif() {}

    public Notif(String id, String isiPengumuman, String judulPengumuman, String namaChapter, String namaRegional, String namaTingkatan) {
        this.id = id;
        this.isiPengumuman = isiPengumuman;
        this.judulPengumuman = judulPengumuman;
        this.namaChapter = namaChapter;
        this.namaRegional = namaRegional;
        this.namaTingkatan = namaTingkatan;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> notif = new HashMap<>();
        notif.put("id", id);
        notif.put("isiPengumuman", isiPengumuman);
        notif.put("judulPengumuman", judulPengumuman);
        notif.put("namaChapter", namaChapter);
        notif.put("namaRegional", namaRegional);
        notif.put("namaTingkatan", namaTingkatan);
        return notif;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIsiPengumuman() {
        return isiPengumuman;
    }

    public void setIsiPengumuman(String isiPengumuman) {
        this.isiPengumuman = isiPengumuman;
    }

    public String getJudulPengumuman() {
        return judulPengumuman;
    }

    public void setJudulPengumuman(String judulPengumuman) {
        this.judulPengumuman = judulPengumuman;
    }

    public String getNamaChapter() {
        return namaChapter;
    }

    public void setNamaChapter(String namaChapter) {
        this.namaChapter = namaChapter;
    }

    public String getNamaRegional() {
        return namaRegional;
    }

    public void setNamaRegional(String namaRegional) {
        this.namaRegional = namaRegional;
    }

    public String getNamaTingkatan() {
        return namaTingkatan;
    }

    public void setNamaTingkatan(String namaTingkatan) {
        this.namaTingkatan = namaTingkatan;
    }
}
