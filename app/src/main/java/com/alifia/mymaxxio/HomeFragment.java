package com.alifia.mymaxxio;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.google.android.material.tabs.TabLayout;

public class HomeFragment extends Fragment {

    private String strNamaUser;
    private RelativeLayout btnForum, btnKegiatan, btnUangkas;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        btnForum = rootView.findViewById(R.id.btn_forum);
        btnForum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ForumDiskusi.class);
                startActivity(intent);
            }
        });

        btnKegiatan = rootView.findViewById(R.id.btn_kegiatan);
        btnKegiatan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), EventActivity.class);
                startActivity(intent);
            }
        });

        btnUangkas = rootView.findViewById(R.id.btn_uangkas);
        btnUangkas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), UangKas.class);
                startActivity(intent);
            }
        });

        return rootView;
    }
}