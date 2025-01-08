package com.alifia.mymaxxio;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class HomeBeforeLoginFragment extends Fragment {

    private TextView tvLogin;
    private RelativeLayout btnForumDiskusi, btnKegiatan, btnUangkas;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home_before_login, container, false);

        // untuk ke halaman login
        tvLogin = rootView.findViewById(R.id.ingin);
        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Login.class);
                startActivity(intent);
            }
        });

        // button forum diskusi ////////////////////
        btnForumDiskusi = rootView.findViewById(R.id.btn_forum);
        btnForumDiskusi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(getActivity(), ForumDiskusiBeforeLogin.class); //
//                startActivity(intent);
                showConfirmationDialog();
            }
        });

        // mau ke kegiatan: menampilkan alert harus login dulu
        btnKegiatan = rootView.findViewById(R.id.btn_kegiatan);
        btnKegiatan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showConfirmationDialog();
            }
        });

        // mau ke uang kas: menampilkan alert harus login dulu
        btnUangkas = rootView.findViewById(R.id.btn_uangkas);
        btnUangkas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showConfirmationDialog();
            }
        });

        return rootView;
    }

    private void showConfirmationDialog() {
        new AlertDialog.Builder(getContext())
                .setMessage("Anda harus login terlebih dahulu!")
                .setPositiveButton("Login", (dialog, which) -> {
                    Intent intent = new Intent(getContext(), Login.class);
                    startActivity(intent);
                })
                .setNeutralButton("Batal", null)
                .show();
    }
}