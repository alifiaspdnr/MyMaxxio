package com.alifia.mymaxxio.model;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

public class PostKomentar {
    public String id;
    public String idPostAsli;
    public String idAnggota;
    public String nama;
    public String fotoProfil;
    public String isiPost;
    public long created;

    public PostKomentar() {}

    public PostKomentar(String id, String idPostAsli, String idAnggota, String nama, String fotoProfil, String isiPost, long created) {
        this.id = id;
        this.idPostAsli = idPostAsli;
        this.idAnggota = idAnggota;
        this.nama = nama;
        this.fotoProfil = fotoProfil;
        this.isiPost = isiPost;
        this.created = created;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> post = new HashMap<>();
        post.put("id", id);
        post.put("idPostAsli", idPostAsli);
        post.put("idAnggota", idAnggota);
        post.put("nama", nama);
        post.put("fotoProfil", fotoProfil);
        post.put("isiPost", isiPost);
        post.put("created", created);
        return post;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdPostAsli() {
        return idPostAsli;
    }

    public void setIdPostAsli(String idPostAsli) {
        this.idPostAsli = idPostAsli;
    }

    public String getIdAnggota() {
        return idAnggota;
    }

    public void setIdAnggota(String idAnggota) {
        this.idAnggota = idAnggota;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getFotoProfil() {
        return fotoProfil;
    }

    public void setFotoProfil(String fotoProfil) {
        this.fotoProfil = fotoProfil;
    }

    public String getIsiPost() {
        return isiPost;
    }

    public void setIsiPost(String isiPost) {
        this.isiPost = isiPost;
    }

    public long getCreated() {
        return created;
    }

    public void setCreated(long created) {
        this.created = created;
    }
}
