package com.alifia.mymaxxio;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {

    private EditText idMember, nama, email, noHp, nopol;
    private Spinner regional, chapter;
    private TextView tvLogin, fileNameText;
    private Button btnRegis;
    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;
    private FirebaseFirestore db;
    private MaterialToolbar btnKembali;
    private LinearLayout btnUploadFoto;
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        btnKembali = findViewById(R.id.btn_kembali);
        btnKembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        progressDialog = new ProgressDialog(Register.this);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Silakan tunggu...");
        progressDialog.setCancelable(false);

        idMember = findViewById(R.id.idmember);
        nama = findViewById(R.id.nama);
        email = findViewById(R.id.email);
        noHp = findViewById(R.id.no_hp);
        nopol = findViewById(R.id.nopol);
        fileNameText = findViewById(R.id.file_name_text);

        regional = findViewById(R.id.dropdown_regional);
        ArrayAdapter<CharSequence> adapterRegionalReqEvent = ArrayAdapter.createFromResource(
                this,
                R.array.req_event_regional_array,
                R.layout.custom_spinner_item
        );
        adapterRegionalReqEvent.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        regional.setAdapter(adapterRegionalReqEvent);
        regional.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        chapter = findViewById(R.id.dropdown_chapter);
        ArrayAdapter<CharSequence> adapterChapterReqEvent = ArrayAdapter.createFromResource(
                this,
                R.array.req_event_chapter_array,
                R.layout.custom_spinner_item
        );
        adapterChapterReqEvent.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        chapter.setAdapter(adapterChapterReqEvent);
        chapter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        tvLogin = findViewById(R.id.sudah);
        tvLogin.setOnClickListener(v -> {
            Intent intent = new Intent(Register.this, Login.class);
            startActivity(intent);
        });

        btnUploadFoto = findViewById(R.id.btn_upload_kta);
        btnUploadFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });

        btnRegis = findViewById(R.id.button_regis);
        btnRegis.setOnClickListener(v -> {
            if (allFieldsFilled()) {
                if (!isGmailAddress(email.getText().toString())) {
                    Toast.makeText(this, "Alamat email harus Gmail dan ditulis dalam huruf kecil", Toast.LENGTH_SHORT).show();
                } else {
                    progressDialog.show();
                    uploadImageToFirebase();
                }
            } else {
                Toast.makeText(Register.this, "Silakan isi semua data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean allFieldsFilled() {
        return idMember.getText().length() > 0 && nama.getText().length() > 0 && email.getText().length() > 0 &&
                noHp.getText().length() > 0 && nopol.getText().length() > 0 && imageUri != null;
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            fileNameText.setText(imageUri.getLastPathSegment());
        }
    }

    private void uploadImageToFirebase() {
        if (imageUri != null) {
            StorageReference storageRef = FirebaseStorage.getInstance().getReference("foto_kta")
                    .child(System.currentTimeMillis() + ".jpg");

            storageRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String downloadUrl = uri.toString();
                            registerUser(downloadUrl);
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(Register.this, "Gagal mengunggah foto", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    /*private void registerUser(String photoUrl) {
        String emailText = email.getText().toString();

        // validasi alamat email menggunakan domain gmail
        if (!isGmailAddress(emailText)) {
            Toast.makeText(this, "Alamat email harus Gmail dan ditulis dalam huruf kecil", Toast.LENGTH_SHORT).show();
            return;
        }

        String idMemberText = idMember.getText().toString();
        String namaText = nama.getText().toString();
        String noHpText = noHp.getText().toString();
        String nopolText = nopol.getText().toString();
        String regionalText = regional.getSelectedItem().toString();
        String chapterText = chapter.getSelectedItem().toString();

        Map<String, Object> userMap = new HashMap<>();
        userMap.put("idAnggota", idMemberText);
        userMap.put("namaLengkap", namaText);
        userMap.put("email", emailText);
        userMap.put("noHp", noHpText);
        userMap.put("nopol", nopolText);
        userMap.put("namaRegional", regionalText);
        userMap.put("namaChapter", chapterText);
        userMap.put("fotoKTA", photoUrl);
        userMap.put("status", "Belum Ditinjau");

        db.collection("users_non_registered").document(idMemberText)
                .set(userMap, SetOptions.merge())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Registrasi berhasil", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Gagal menyimpan data registrasi", Toast.LENGTH_SHORT).show();
                    }
                });
    }*/

    private void registerUser(String photoUrl) {
        String idMemberText = idMember.getText().toString();
        String namaText = nama.getText().toString();
        String emailText = email.getText().toString();
        String noHpText = noHp.getText().toString();
        String nopolText = nopol.getText().toString();
        String regionalText = regional.getSelectedItem().toString();
        String chapterText = chapter.getSelectedItem().toString();

        Map<String, Object> userMap = new HashMap<>();
        userMap.put("idAnggota", idMemberText);
        userMap.put("namaLengkap", namaText);
        userMap.put("email", emailText);
        userMap.put("noHp", noHpText);
        userMap.put("nopol", nopolText);
        userMap.put("namaRegional", regionalText);
        userMap.put("namaChapter", chapterText);
        userMap.put("fotoKTA", photoUrl);
        userMap.put("status", "Belum Ditinjau");

        db.collection("users_non_registered")
                .add(userMap)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        // Update the document with the auto-generated ID
                        String autoId = documentReference.getId();
                        documentReference.update("id", autoId)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        progressDialog.dismiss();
                                        Toast.makeText(Register.this, "Registrasi berhasil", Toast.LENGTH_SHORT).show();
                                        finish();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        progressDialog.dismiss();
                                        Toast.makeText(Register.this, "Gagal menyimpan data registrasi", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(Register.this, "Gagal menyimpan data registrasi", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private boolean isGmailAddress(String email) {
        String gmailRegex = "^[a-zA-Z0-9_]+@gmail\\.com$";
        return email.matches(gmailRegex);
    }
}