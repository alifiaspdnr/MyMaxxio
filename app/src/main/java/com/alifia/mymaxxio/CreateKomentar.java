package com.alifia.mymaxxio;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.alifia.mymaxxio.model.Post;
import com.alifia.mymaxxio.model.PostKomentar;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class CreateKomentar extends AppCompatActivity {

    private TextView btnKembali;
    private RelativeLayout btnPost;
    private EditText isiPost;
    private static final int MAX_CHAR_COUNT = 200;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private String strNamaLengkap = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_create_komentar);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        btnKembali = findViewById(R.id.kembali);
        btnKembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isiPost.getText().toString().isEmpty()) {
                    finish();
                } else {
                    showConfirmationDialog();
                }
            }
        });

        isiPost = findViewById(R.id.edittext_isipost);
        isiPost.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // No action needed here
            }

            // kalo udah mencapai 200 karakter tapi user masih ngetik, ada toast peringatan
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() >= MAX_CHAR_COUNT) {
                    Toast.makeText(CreateKomentar.this, "Anda telah mencapai maksimal karakter (200)", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                // kalo user masih ngetik tapi udah melebihi max char, akan dihapus
                if (s.length() > MAX_CHAR_COUNT) {
                    s.delete(MAX_CHAR_COUNT, s.length());
                }
            }
        });

        // data postId hasil bundle dari PostAdapter (dan dari PostDetailActivity?)
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String bundlePostID = bundle.getString("postId");
        Log.d("TAG", bundlePostID);

        btnPost = findViewById(R.id.button_publikasikan);
        btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*// ambil uid dari authentication trus simpen ke variabel
                String uid = mAuth.getCurrentUser().getUid();
                Log.d("UID adalah : ", uid);*/

                // ambil nama lengkap dari firestore trus simpen ke variabel strNamaLengkap
                String email = mAuth.getCurrentUser().getEmail();
                Log.d("EMAILnya : ", email);
                db.collection("member").whereEqualTo("email", email).get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful() && !task.getResult().isEmpty()) {
                                    DocumentSnapshot document = task.getResult().getDocuments().get(0);

                                    // dapet nama lengkap, trus simpen ke variabel
                                    strNamaLengkap = document.getString("namaLengkap");
                                    Log.d("NAMA LENGKAP : ", strNamaLengkap);

                                    if (bundlePostID != null) {
                                        savePost(bundlePostID, strNamaLengkap);
                                    }
                                } else {
                                    Log.d("nama gagal", "Error getting documents: ", task.getException());
                                }
                            }
                        });
            }
        });
    }

    private void savePost(String bundlePostID, String namaLengkap) {
        Log.d("SAVE POST id post : ", bundlePostID);
        Log.d("SAVE POST NAMAAAA : ", namaLengkap);

        // get isi post trus masukin ke variabel
        String postContent = isiPost.getText().toString();

        // tampilin toast kalau gaada tulisan
        if (postContent.isEmpty()) {
            Toast.makeText(this, "Isi post tidak boleh kosong!", Toast.LENGTH_SHORT).show();
            return;
        }

        // fungsi untuk nge-get info user yang lagi login trus panggil instance firestore
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference newKomentarRef = db.collection("posts_komentar").document();

        PostKomentar postKomentar = new PostKomentar(
                newKomentarRef.getId(),
                bundlePostID,
                currentUser.getUid(),
                namaLengkap,
                currentUser.getPhotoUrl() != null ? currentUser.getPhotoUrl().toString() : "",
                postContent,
                System.currentTimeMillis()
        );

        newKomentarRef.set(postKomentar.toMap()).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(CreateKomentar.this, "Post berhasil terkirim", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(CreateKomentar.this, "Post gagal terkirim", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showConfirmationDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Konfirmasi Post")
                .setMessage("Apakah Anda yakin ingin kembali?")
                .setNegativeButton("Hapus", (dialog, which) -> finish())
                .setNeutralButton("Batal", null)
                .show();
    }
}