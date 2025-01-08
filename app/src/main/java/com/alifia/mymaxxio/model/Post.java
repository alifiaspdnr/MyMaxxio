package com.alifia.mymaxxio.model;

import com.google.firebase.Timestamp;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

public class Post {
    public String id;
    public String idAnggota;
    public String nama;
    public String fotoProfil;
    public String namaTingkatan;
    public String idRegional;
    public String idChapter;
    public String chapter;
    public String idKegiatan;
    public String isiPost;
    @Nullable public String attachment;
    public int jmlLike;
    public int jmlKomen;
    public int isPosted;
    public long created;

    public Post() {}

    public Post(String id, String idAnggota, String nama, String fotoProfil, String namaTingkatan, String idRegional, String idChapter, String chapter, String idKegiatan, String isiPost, @Nullable String attachment, int jmlLike, int jmlKomen, int isPosted, long created) {
        this.id = id;
        this.idAnggota = idAnggota;
        this.nama = nama;
        this.fotoProfil = fotoProfil;
        this.namaTingkatan = namaTingkatan;
        this.idRegional = idRegional;
        this.idChapter = idChapter;
        this.chapter = chapter;
        this.idKegiatan = idKegiatan;
        this.isiPost = isiPost;
        this.attachment = attachment;
        this.jmlLike = jmlLike;
        this.jmlKomen = jmlKomen;
        this.isPosted = isPosted;
        this.created = created;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> post = new HashMap<>();
        post.put("id", id);
        post.put("idAnggota", idAnggota);
        post.put("nama", nama);
        post.put("fotoProfil", fotoProfil);
        post.put("namaTingkatan", namaTingkatan);
        post.put("idRegional", idRegional);
        post.put("idChapter", idChapter);
        post.put("chapter", chapter);
        post.put("idKegiatan", idKegiatan);
        post.put("isiPost", isiPost);
        post.put("attachment", attachment);
        post.put("jmlLike", jmlLike);
        post.put("jmlKomen", jmlKomen);
        post.put("isPosted", isPosted);
        post.put("created", created);
        return post;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdAnggota() {
        return idAnggota;
    }

    public void setIdAnggota(String idAnggota) {
        this.idAnggota = idAnggota;
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

    public String getIdChapter() {
        return idChapter;
    }

    public void setIdChapter(String idChapter) {
        this.idChapter = idChapter;
    }

    public String getIdKegiatan() {
        return idKegiatan;
    }

    public void setIdKegiatan(String idKegiatan) {
        this.idKegiatan = idKegiatan;
    }

    public String getIsiPost() {
        return isiPost;
    }

    public void setIsiPost(String isiPost) {
        this.isiPost = isiPost;
    }

    public String getAttachment() {
        return attachment;
    }

    public void setAttachment(String attachment) {
        this.attachment = attachment;
    }

    public int getJmlLike() {
        return jmlLike;
    }

    public void setJmlLike(int jmlLike) {
        this.jmlLike = jmlLike;
    }

    public int getJmlKomen() {
        return jmlKomen;
    }

    public void setJmlKomen(int jmlKomen) {
        this.jmlKomen = jmlKomen;
    }

    public int getIsPosted() {
        return isPosted;
    }

    public void setIsPosted(int isPosted) {
        this.isPosted = isPosted;
    }

    public long getCreated() {
        return created;
    }

    public void setCreated(long created) {
        this.created = created;
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

    public String getChapter() {
        return chapter;
    }

    public void setChapter(String chapter) {
        this.chapter = chapter;
    }
}
