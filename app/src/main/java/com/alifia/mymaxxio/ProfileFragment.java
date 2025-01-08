package com.alifia.mymaxxio;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class ProfileFragment extends Fragment {

    private RelativeLayout btnUnggahanSaya, btnUnggahanFavoritSaya, btnKomentarSaya, btnGantiPassword, btnLogout;
    private TextView tvUsername, tvNopol, tvChapter;
    private FirebaseAuth mAuth;
    private ImageView imgProfile;
    private FirebaseFirestore db;
    //private String idChapter = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // submenu 1. unggahan saya
        btnUnggahanSaya = rootView.findViewById(R.id.btn_unggahan_saya);
        btnUnggahanSaya.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), DetailUnggahanSaya.class);
                startActivity(intent);
            }
        });

        // submenu 2. unggahan favorit saya (blm)
        btnUnggahanFavoritSaya = rootView.findViewById(R.id.btn_unggahan_fav);
        btnUnggahanFavoritSaya.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), DetailUnggahanFavoritSaya.class);
                startActivity(intent);
            }
        });

        // submenu 3. komentar saya
        btnKomentarSaya = rootView.findViewById(R.id.btn_komentar_saya);
        btnKomentarSaya.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), DetailKomentarSaya.class);
                startActivity(intent);
            }
        });

        // submenu 3. ganti pass
        btnGantiPassword = rootView.findViewById(R.id.btn_ganti_pass);
        btnGantiPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), GantiPassword.class);
                startActivity(intent);
            }
        });

        btnLogout = rootView.findViewById(R.id.btn_logout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // menampilkan alert konfirmasi apakah mau logout?
                new AlertDialog.Builder(getContext())
                        .setMessage("Apakah Anda yakin ingin logout?")
                        .setPositiveButton("Logout", (dialog, which) -> {
                            mAuth.signOut();
                            Intent intent = new Intent(getActivity(), BeforeLogin.class);
                            startActivity(intent);
                        })
                        .setNeutralButton("Batal", null)
                        .show();
            }
        });

        tvUsername = rootView.findViewById(R.id.nama_user);
        tvNopol = rootView.findViewById(R.id.nopol_user);

        // ambil nama lengkap dari firestore dan set text di tvUsername, tvNopol
        String email = mAuth.getCurrentUser().getEmail();
        Log.d("EMAILnya : ", email);
        db.collection("member").whereEqualTo("email", email).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful() && !task.getResult().isEmpty()) {
                            DocumentSnapshot document = task.getResult().getDocuments().get(0);
                            String strNamaLengkap = document.getString("namaLengkap");
                            tvUsername.setText(strNamaLengkap);
                            String strNopol = document.getString("nopol");
                            tvNopol.setText(strNopol);
                        } else {
                            Log.d("ProfileFragment", "Error getting documents: ", task.getException());
                        }
                    }
                });


        tvChapter = rootView.findViewById(R.id.chapter_user);
        db.collection("member").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful() && !task.getResult().isEmpty()) {
                            DocumentSnapshot document = task.getResult().getDocuments().get(0);
                            String namaChapter = document.getString("namaChapter");
                            Log.d("namaChapter : ", namaChapter);
                            tvChapter.setText(namaChapter);
                        } else {
                            Log.d("ProfileFragment", "Error getting documents: ", task.getException());
                        }
                    }
                });



        imgProfile = rootView.findViewById(R.id.imageViewProfile);
        if (mAuth.getCurrentUser().getPhotoUrl() != null) {
            Uri photoUrl = mAuth.getCurrentUser().getPhotoUrl();
            Glide.with(getActivity()).load(photoUrl).into(imgProfile);
        }
        ActivityResultLauncher<PickVisualMediaRequest> pickMedia =
                registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
                    if (uri != null) {
                        imgProfile.setImageURI(uri);
                        FirebaseUser user = mAuth.getCurrentUser();
                        FirebaseStorage storage = FirebaseStorage.getInstance();
                        StorageReference imageRef = storage.getReference().child("users/" + user.getUid() + "/profile.jpg");
                        UploadTask uploadTask = imageRef.putFile(uri);
                        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                imageRef.getDownloadUrl().addOnCompleteListener(imageTask -> {
                                    if (imageTask.isSuccessful()) {
                                        String url = imageTask.getResult().toString();
                                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                                .setPhotoUri(Uri.parse(url))
                                                .build();

                                        user.updateProfile(profileUpdates)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            FirebaseFirestore store = FirebaseFirestore.getInstance();
                                                            store.collection("users")
                                                                    .document(user.getUid()).update("photoProfile", url);
                                                            Toast.makeText(getActivity(), "User photo profile updated", Toast.LENGTH_SHORT).show();
                                                            Toast.makeText(getActivity(), "User photo profile updated", Toast.LENGTH_SHORT).show();
                                                        } else {
                                                            Toast.makeText(getActivity(), "Failed to update user photo profile", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });
                                        Toast.makeText(getActivity(), "User photo profile updated", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(getActivity(), "Failed to update user photo profile", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        });
                    }
                });
        imgProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickMedia.launch(new PickVisualMediaRequest.Builder()
                        .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                        .build());
            }
        });

        return rootView;
    }
}