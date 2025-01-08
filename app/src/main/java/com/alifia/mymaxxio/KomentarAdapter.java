package com.alifia.mymaxxio;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alifia.mymaxxio.model.Post;
import com.alifia.mymaxxio.model.User;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class KomentarAdapter extends RecyclerView.Adapter<KomentarAdapter.KomentarViewHolder> {
    private List<DocumentSnapshot> komentarList;
    private FirebaseFirestore db;
    private Context context;

    public KomentarAdapter(List<DocumentSnapshot> komentarList, Context context) {
        this.komentarList = komentarList;
        this.context = context;
        this.db = FirebaseFirestore.getInstance();
    }

    @NonNull
    @Override
    public KomentarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_komentar_row, parent, false);
        return new KomentarViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull KomentarViewHolder holder, int position) {
        DocumentSnapshot komentar = komentarList.get(position);

        // foto
        String userId = komentar.getString("idAnggota");
        db.collection("users").document(userId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    User user = documentSnapshot.toObject(User.class);
                    if (user != null && user.getPhotoProfile() != null) {
                        Glide.with(context)
                                .load(user.getPhotoProfile())
                                .into(holder.fotoProfil);
                    }
                }
            }
        });

        holder.nama.setText(komentar.getString("nama"));
        holder.isiPost.setText(komentar.getString("isiPost"));
    }

    @Override
    public int getItemCount() {
        return komentarList.size();
    }

    public void setKomentarList(List<DocumentSnapshot> komentarList) {
        this.komentarList = komentarList;
        notifyDataSetChanged();
    }

    static class KomentarViewHolder extends RecyclerView.ViewHolder {
        ImageView fotoProfil;
        TextView nama, isiPost;

        public KomentarViewHolder(@NonNull View itemView) {
            super(itemView);
            fotoProfil = itemView.findViewById(R.id.img_user_photo);
            nama = itemView.findViewById(R.id.tv_username);
            isiPost = itemView.findViewById(R.id.tv_description);
        }
    }
}
