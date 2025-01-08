package com.alifia.mymaxxio;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alifia.mymaxxio.model.Event;
import com.alifia.mymaxxio.model.Post;
import com.alifia.mymaxxio.model.User;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PostDetailActivity extends AppCompatActivity {
    private MaterialToolbar btnKembali;
    ImageView imgUserPhoto, imgPost, imgBikinKomen;
    TextView username, category, isiPost, jmlLike, jmlKomen;
    RecyclerView komentarList;
    KomentarAdapter adapter;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_post_detail);

        btnKembali = findViewById(R.id.btn_kembali);
        btnKembali.setNavigationOnClickListener(v -> {
            finish();
        });

        imgUserPhoto = findViewById(R.id.img_user_photo);


        username = findViewById(R.id.tv_username);
        category = findViewById(R.id.tv_category);
        isiPost = findViewById(R.id.tv_description);
        jmlLike = findViewById(R.id.tv_like_count);
        jmlKomen = findViewById(R.id.tv_comment_count);
        imgPost = findViewById(R.id.postImage);

        // ambil data bundle (id post) dari PostAdapter
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String bundlePostID = bundle.getString("postId");
        Log.d("ISI BUNDLE APA???", bundlePostID);

        db = FirebaseFirestore.getInstance();

        if (bundlePostID != null) {
            db.collection("posts")
                    .document(bundlePostID)
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot != null) {
                            Post posts = documentSnapshot.toObject(Post.class);

                            // ngeset foto profil
                            FirebaseFirestore store = FirebaseFirestore.getInstance();
                            store
                                    .collection("users")
                                    .document(posts.getIdAnggota())
                                    .get()
                                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                        @Override
                                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                                            User user = documentSnapshot.toObject(User.class);
                                            if (user.getPhotoProfile() != null) {
                                                Glide.with(getApplicationContext())
                                                        .load(user.getPhotoProfile())
                                                        .into(imgUserPhoto);
                                            } else {
                                                imgUserPhoto.setImageResource(R.drawable.ic_profile);
                                            }
                                        }
                                    });

                            username.setText(posts.getNama());
                            category.setText(posts.getNamaTingkatan());
                            isiPost.setText(posts.getIsiPost());
                            jmlLike.setText(String.valueOf(posts.getJmlLike()));
                            jmlKomen.setText(String.valueOf(posts.getJmlKomen()));
                            if (posts.getAttachment() != null && posts.getAttachment() != "") {
                                Glide.with(getApplicationContext())
                                        .load(posts.getAttachment())
                                        .into(imgPost);
                                imgPost.setVisibility(View.VISIBLE);
                            }
                        }
                    });
        }

        komentarList = findViewById(R.id.komentarList);
        komentarList.setLayoutManager(new LinearLayoutManager(this));
        if (bundlePostID != null) {
            fetchDataKomentar(bundlePostID);
        }

        imgBikinKomen = findViewById(R.id.img_comment);
        imgBikinKomen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PostDetailActivity.this, CreateKomentar.class);
                Bundle bundle = new Bundle();
                bundle.putString("postId", bundlePostID);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    private void fetchDataKomentar(String postId) {
        db = FirebaseFirestore.getInstance();

        db.collection("posts_komentar").
                whereEqualTo("idPostAsli", postId).
                get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful() && !task.getResult().isEmpty()) {
                            List<DocumentSnapshot> dataKomentarList = task.getResult().getDocuments();
                            adapter = new KomentarAdapter(new ArrayList<>(), getApplicationContext());
                            komentarList.setAdapter(adapter);
                            adapter.setKomentarList(dataKomentarList);
                        } else {
                            Log.d("MAAAFFF ", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }
}