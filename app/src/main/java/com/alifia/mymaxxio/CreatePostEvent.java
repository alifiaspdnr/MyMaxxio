package com.alifia.mymaxxio;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.alifia.mymaxxio.model.Post;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class CreatePostEvent extends AppCompatActivity {

    private TextView btnKembali;
    private RelativeLayout btnPost;
    private ImageView postImage;
    private EditText isiPost;
    private ImageButton btnMedia;
    private static final int PICK_MEDIA_REQUEST = 1;
    private Uri mediaUri;
    private static final int MAX_CHAR_COUNT = 200;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private String strNamaLengkap = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_create_post_event);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // button kembali dan kalo diklik akan kembali (kalo isiPost kosong)
        // kalau tidak kosong, tampilin alert dialog yang terdapat pilihan hapus atau batal
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

        // fungsi untuk bikin user gabisa ngetik lagi kalo max char udh tercapai
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
                    Toast.makeText(CreatePostEvent.this, "Anda telah mencapai maksimal karakter (200)", Toast.LENGTH_SHORT).show();
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

        // ambil data bundle dari UnggahanKegiatan
        Bundle strEventID = getIntent().getExtras();
        String bundleEventID = strEventID.getString("event");
        Log.d("TAG", bundleEventID);

        // button post dan untuk ngepost
        btnPost = findViewById(R.id.button_publikasikan);
        btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bundleEventID != null) {
                    // ambil nama lengkap dari firestore trus simpen ke variabel strNamaLengkap
                    String email = mAuth.getCurrentUser().getEmail();
                    Log.d("EMAILnya : ", email);
                    db.collection("member").whereEqualTo("email", email).get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful() && !task.getResult().isEmpty()) {
                                        DocumentSnapshot document = task.getResult().getDocuments().get(0);
                                        strNamaLengkap = document.getString("namaLengkap");
                                        Log.d("NAMA LENGKAP : ", strNamaLengkap);
                                        savePost(false, strNamaLengkap, bundleEventID);
                                    } else {
                                        Log.d("nama gagal", "Error getting documents: ", task.getException());
                                    }
                                }
                            });

                    //savePost(false, bundleEventID);
                }
            }
        });

        // button untuk memilih media, bisa foto atau video
        // maximal 4 media dan untuk video maximal 2 menit
        btnMedia = findViewById(R.id.button_media);
        btnMedia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMediaPicker();
            }
        });
        postImage = findViewById(R.id.postImage);
    }

    private void openMediaPicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/* video/*");
        startActivityForResult(intent, PICK_MEDIA_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_MEDIA_REQUEST && resultCode == RESULT_OK && data != null) {
            mediaUri = data.getData();
            Log.d("TAG", "onActivityResult: " + mediaUri);
            postImage.setImageURI(mediaUri);
            postImage.setVisibility(View.VISIBLE);

            // Do something with the mediaUri if needed
        }
    }

    private void savePost(boolean isPosted, String namaLengkap, String eventId) {
        Log.d("SAVE POST NAMA LENGKAP : ", namaLengkap);

        // get isi post trus masukin ke variabel
        String postContent = isiPost.getText().toString();

        // tampilin toast kalau gaada tulisan & gaada media
        if (postContent.isEmpty() && mediaUri == null) {
            Toast.makeText(this, "Isi post tidak boleh kosong!", Toast.LENGTH_SHORT).show();
            return;
        }

        // fungsi untuk nge-get info user yang lagi login trus panggil instance firestore
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference newPostRef = db.collection("posts").document();

        // kalo isi post ada media
        if (mediaUri != null) {
            // fungsi untuk panggil instance storage (untuk nyimpen media)
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReference();

            StorageReference imageRef = storageRef.child("post" + "/" + newPostRef.getId());
            UploadTask uploadTask = imageRef.putFile(mediaUri);

            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    imageRef.getDownloadUrl().addOnCompleteListener(imageTask -> {
                        if (imageTask.isSuccessful()) {
                            String url = imageTask.getResult().toString();
                            Post post = new Post(
                                    newPostRef.getId(),
                                    currentUser.getUid(),
                                    namaLengkap,
                                    currentUser.getPhotoUrl() != null ? currentUser.getPhotoUrl().toString() : "",
                                    "",
                                    "",
                                    "",
                                    "",
                                    eventId,
                                    postContent,
                                    url,
                                    0,
                                    0,
                                    0,
                                    System.currentTimeMillis()
                            );
                            newPostRef.set(post.toMap()).addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    Toast.makeText(CreatePostEvent.this, "Post berhasil terkirim", Toast.LENGTH_SHORT).show();
                                    finish();
                                } else {
                                    Toast.makeText(CreatePostEvent.this, "Post gagal terkirim", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    });
                }
            });
        } else { // kalo isi post gaada media (cuma teks aja)
            Post post = new Post(
                    newPostRef.getId(),
                    currentUser.getUid(),
                    namaLengkap,
                    currentUser.getPhotoUrl() != null ? currentUser.getPhotoUrl().toString() : "",
                    "",
                    "",
                    "",
                    "",
                    eventId,
                    postContent,
                    "",
                    0,
                    0,
                    0,
                    System.currentTimeMillis()
            );

            newPostRef.set(post.toMap()).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(CreatePostEvent.this, "Post berhasil terkirim", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(CreatePostEvent.this, "Post gagal terkirim", Toast.LENGTH_SHORT).show();
                }
            });
        }
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