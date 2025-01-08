package com.alifia.mymaxxio;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Register2 extends AppCompatActivity {


    private EditText etIdMember, etEmail, etPassword;
    private TextView tvLogin;
    private Button btnRegis, btnGeneratePass;
    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register2);

        mAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(Register2.this);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Silakan tunggu...");
        progressDialog.setCancelable(false);

        etIdMember = findViewById(R.id.idmember);
        etEmail = findViewById(R.id.email);
        etPassword = findViewById(R.id.password);

        tvLogin = findViewById(R.id.sudah);
        tvLogin.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), Login.class);
            startActivity(intent);
        });

//        btnGeneratePass = findViewById(R.id.generatePass);
//        btnGeneratePass.setOnClickListener(v -> {
//            String generatedPassword = generatePassword(8); // Misalnya, panjang password 8 karakter
//            etPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
//            etPassword.setText(generatedPassword);
//        });

        btnRegis = findViewById(R.id.button_regis);
        btnRegis.setOnClickListener(v -> {
            if (etIdMember.getText().length()>0 && etEmail.getText().length()>0 && etPassword.getText().length()>0){
                regis(etIdMember.getText().toString(), etEmail.getText().toString(), etPassword.getText().toString());
            }else{
                Toast.makeText(getApplicationContext(), "Silakan isi semua data", Toast.LENGTH_SHORT).show();
            }
        });

    }


//    private String generatePassword(int length) {
//        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
//
//        StringBuilder password = new StringBuilder();
//        Random random = new Random();
//
//        for (int i = 0; i < length; i++) {
//            int index = random.nextInt(characters.length());
//            password.append(characters.charAt(index));
//        }
//        return password.toString();
//    }

    private void regis(String idMember, String email, String password) {
        // validasi alamat email menggunakan domain gmail
        if (!isGmailAddress(email)) {
            Toast.makeText(this, "Alamat email harus Gmail dan ditulis dalam huruf kecil", Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog.show();
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful() && task.getResult() != null) {
                    FirebaseUser firebaseUser = task.getResult().getUser();
                    if (firebaseUser != null) {
                        UserProfileChangeRequest request = new UserProfileChangeRequest.Builder()
                                .setDisplayName(idMember)
                                .build();
                        firebaseUser.updateProfile(request).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                String userID = firebaseUser.getUid();
                                String idMemberUser = firebaseUser.getDisplayName();
                                String emailUser = firebaseUser.getEmail();

                                Map<String, Object> userMap = new HashMap<>();
                                userMap.put("userID", userID);
                                userMap.put("idMemberUser", idMemberUser);
                                userMap.put("emailUser", emailUser);

                                db = FirebaseFirestore.getInstance();
                                CollectionReference usersCollectionRef = db.collection("users");

                                usersCollectionRef.document(firebaseUser.getUid()).set(userMap)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                reload();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(getApplicationContext(), "Gagal menyimpan data pengguna", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                //reload();
                            }
                        });
                    } else {
                        Toast.makeText(getApplicationContext(), "Register gagal!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean isGmailAddress(String email) {
        String gmailRegex = "^[a-zA-Z0-9_]+@gmail\\.com$";
        return email.matches(gmailRegex);
    }

    private void reload() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }
}