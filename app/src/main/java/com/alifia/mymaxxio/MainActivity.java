package com.alifia.mymaxxio;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // ngeset tampilan fragment (default) jadi HomeFragment
        // yang mana di dalam HomeFragment itu layoutnya yang fragment_home.xml
        loadFragment(new HomeFragment());

        // naro bottom navigation view untuk navbar
        BottomNavigationView navigationView = findViewById(R.id.navbar);
        // pakein listener untuk penanganan gimana kalo salah satu item diklik
        navigationView.setOnNavigationItemSelectedListener(this);

        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;

        int itemId = item.getItemId();

        if (itemId == R.id.fr_home) {
            fragment = new HomeFragment();
        } else if (itemId == R.id.fr_notif) {
            fragment = new NotifFragment();
        } else if (itemId == R.id.fr_profile) {
            fragment = new ProfileFragment();
        }
        return loadFragment(fragment);
    }

    private boolean loadFragment(Fragment fragment) {
        if(fragment != null){
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }
}