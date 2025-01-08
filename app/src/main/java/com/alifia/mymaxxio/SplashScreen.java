package com.alifia.mymaxxio;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashScreen extends AppCompatActivity {

    private static final int SPLASH_SCREEN = 5000;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash_screen);

        // mAuth = FirebaseAuth.getInstance();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // cek status login pengguna
                mAuth = FirebaseAuth.getInstance();
                FirebaseUser currentUser = mAuth.getCurrentUser();

                // percabangan untuk mengecek apakah pengguna masih login atau tidak
                if (currentUser != null) {
                    // pengguna sudah login, maka masuk ke halaman home
                    Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                    startActivity(intent);
                } else {
                    // pengguna belum login, maka masuk ke halaman home versi before login
                    Intent intent = new Intent(SplashScreen.this, BeforeLogin.class);
                    startActivity(intent);
                }
                finish();
            }
        }, SPLASH_SCREEN);

    }
}