package com.alifia.mymaxxio;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;

public class Login extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private TextView tvForgotPass, tvRegis;
    private Button btnLogin;
    private MaterialToolbar btnKembali;
    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(Login.this);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Silakan tunggu...");
        progressDialog.setCancelable(false);

        btnKembali = findViewById(R.id.btn_kembali);
        btnKembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        etEmail = findViewById(R.id.email);
        etPassword = findViewById(R.id.password);

        tvForgotPass = findViewById(R.id.lupa);
        tvForgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, ForgotPassword.class);
                startActivity(intent);
            }
        });

        tvRegis = findViewById(R.id.belum);
        tvRegis.setOnClickListener(v ->{
            Intent intent = new Intent(Login.this, Register.class);
            startActivity(intent);
        });

        btnLogin = findViewById(R.id.button_masuk);
        btnLogin.setOnClickListener(v -> {
            if (etEmail.getText().length()>0 && etPassword.getText().length()>0){
                login(etEmail.getText().toString(), etPassword.getText().toString());
            } else if (etEmail.getText().length()==0) {
                etEmail.setError("Email tidak boleh kosong!");
            } else if (etPassword.getText().length()==0) {
                etPassword.setError("Password tidak boleh kosong!");
            }
        });
    }

    private void login(String email, String password) {
        // validasi alamat email menggunakan domain gmail
        if (!isGmailAddress(email)) {
            Toast.makeText(Login.this, "Alamat email harus Gmail dan ditulis dalam huruf kecil", Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog.show();
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful() && task.getResult()!=null){
                    if(task.getResult().getUser()!=null){
                        reload();
                    }else{
                        Toast.makeText(Login.this, "Email atau password salah!", Toast.LENGTH_SHORT).show();
                        progressDialog.hide();
                    }
                }else{
                    // mengecek alasan login gagal
                    Exception e = task.getException();
                    if (e instanceof FirebaseAuthInvalidCredentialsException){
                        // untuk password salah
                        Toast.makeText(Login.this, "Password Anda salah", Toast.LENGTH_SHORT).show();
                        progressDialog.hide();
                    }
                }
            }
        });
    }

    private boolean isGmailAddress(String email) {
        String gmailRegex = "^[a-zA-Z0-9_]+@gmail\\.com$";
        return email.matches(gmailRegex);
    }

    private void reload() {
        Intent intent = new Intent(Login.this, MainActivity.class);
        startActivity(intent);
    }
}