package com.alifia.mymaxxio;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.tabs.TabLayout;

public class ForumDiskusi extends AppCompatActivity {

    private ImageButton btnCreatePost;
    private TabLayout tabLayout;
    private ViewPager2 viewPager2;
    private HomeFragmentAdapter adapter;
    private MaterialToolbar btnKembali;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_forum_diskusi);

        btnKembali = findViewById(R.id.btn_kembali);
        btnKembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnCreatePost = findViewById(R.id.button_create_post);
        btnCreatePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ForumDiskusi.this, CreatePost.class);
                startActivity(intent);
            }
        });

        tabLayout = findViewById(R.id.tabLayout);
        viewPager2 = findViewById(R.id.viewPager2);

        // namain tab
        tabLayout.addTab(tabLayout.newTab().setText("Semua"));
        tabLayout.addTab(tabLayout.newTab().setText("Nasional"));
        tabLayout.addTab(tabLayout.newTab().setText("Regional"));
        tabLayout.addTab(tabLayout.newTab().setText("Chapter"));

        // set adapter HomeFragmentAdapter buat ngeset posisi sekian bakal nampilin fragment apa
        FragmentManager fragmentManager = getSupportFragmentManager();
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
    }
}