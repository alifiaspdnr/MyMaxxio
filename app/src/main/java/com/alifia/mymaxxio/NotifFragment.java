package com.alifia.mymaxxio;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.tabs.TabLayout;

public class NotifFragment extends Fragment {

    private TabLayout tabLayout;
    private ViewPager2 viewPager2;
    private NotifFragmentAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_notif, container, false);

        tabLayout = rootView.findViewById(R.id.tabLayout);
        viewPager2 = rootView.findViewById(R.id.viewPager2);

        // namain tab
        tabLayout.addTab(tabLayout.newTab().setText("Semua"));
        tabLayout.addTab(tabLayout.newTab().setText("Nasional"));
        tabLayout.addTab(tabLayout.newTab().setText("Regional"));
        tabLayout.addTab(tabLayout.newTab().setText("Chapter"));

        // set adapter HomeFragmentAdapter buat ngeset posisi sekian bakal nampilin fragment apa
        FragmentManager fragmentManager = getChildFragmentManager();
        adapter = new NotifFragmentAdapter(fragmentManager, getLifecycle());
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