package com.alifia.mymaxxio;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alifia.mymaxxio.model.Post;
import com.alifia.mymaxxio.model.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class UnggahanKegiatan extends AppCompatActivity {

    private ImageButton btnCreatePost;
    private MaterialToolbar btnKembali;
    private RecyclerView unggahanKegiatanList;
    private PostAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_unggahan_kegiatan);

        btnKembali = findViewById(R.id.btn_kembali);
        btnKembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // untuk menampilkan list unggahan kegiatan, digunakan recyclerview
        unggahanKegiatanList = findViewById(R.id.unggahanKegiatanList);
        unggahanKegiatanList.setLayoutManager(new LinearLayoutManager(this));
        //adapter = new PostAdapter(new ArrayList<>());

        // ambil data bundle dari EventDetailActivity
        Bundle bundleEventID = getIntent().getExtras();
        String strEventID = bundleEventID.getString("event");
        Log.d("UNGGAHAN KEGIATAN ADA ID KEGIATAN??? ", strEventID);

        // set adapter dan nge-load data collection post where isi field idKegiatan == bundle event ID
        FirebaseFirestore store = FirebaseFirestore.getInstance();
        store.collection("posts")
                .whereEqualTo("idKegiatan", strEventID)
                .whereEqualTo("isPosted", 1)
                .orderBy("created", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot snapshot) {
                        if (snapshot != null) {
                            FirebaseAuth auth = FirebaseAuth.getInstance();
                            String uid = auth.getUid();
                            assert uid != null;
                            store.collection("users")
                                    .document(uid)
                                    .get()
                                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                        @Override
                                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                                            User user = documentSnapshot.toObject(User.class);
                                            adapter = new PostAdapter(snapshot.toObjects(Post.class), user);
                                            unggahanKegiatanList.setAdapter(adapter);
                                        }
                                    });
                        } else {
                            adapter = new PostAdapter(new ArrayList<>());
                            unggahanKegiatanList.setAdapter(adapter);
                        }
                    }
                });

        btnCreatePost = findViewById(R.id.button_create_post);
        btnCreatePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (strEventID != null) {
                    Intent intent = new Intent(UnggahanKegiatan.this, CreatePostEvent.class);
                    intent.putExtra("event", strEventID);
                    startActivity(intent);
                }
            }
        });
    }
}