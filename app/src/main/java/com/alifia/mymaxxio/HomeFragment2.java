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

import com.google.android.material.tabs.TabLayout;

public class HomeFragment2 extends Fragment {

    private ImageButton btnCreatePost;
    private TabLayout tabLayout;
    private ViewPager2 viewPager2;
    private HomeFragmentAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home2, container, false);

        btnCreatePost = rootView.findViewById(R.id.button_create_post);
        btnCreatePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CreatePost.class);
                startActivity(intent);
            }
        });

        tabLayout = rootView.findViewById(R.id.tabLayout);
        viewPager2 = rootView.findViewById(R.id.viewPager2);

        // namain tab
        tabLayout.addTab(tabLayout.newTab().setText("Semua"));
        tabLayout.addTab(tabLayout.newTab().setText("Nasional"));
        tabLayout.addTab(tabLayout.newTab().setText("Regional"));
        tabLayout.addTab(tabLayout.newTab().setText("Chapter"));

        // set adapter HomeFragmentAdapter buat ngeset posisi sekian bakal nampilin fragment apa
        FragmentManager fragmentManager = getChildFragmentManager();
        adapter = new HomeFragmentAdapter(fragmentManager, getLifecycle());
        viewPager2.setAdapter(adapter);

        // biar tab layout pas dipencet, viewpagernya keganti (dgn lihat position si tab-nya)
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager2.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        // biar kalo viewpager2 nya di-slide, tablayoutnya jg keganti
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                tabLayout.selectTab(tabLayout.getTabAt(position));
            }
        });

        // untuk styling tab selected dengan kustomisasi di tab_title
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            if (tab != null) {
                tab.setCustomView(R.layout.tab_title);
            }
        }

        return rootView;
    }
}