package com.alifia.mymaxxio;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

import java.util.List;

public class DetailKomentarAdapter extends RecyclerView.Adapter<DetailKomentarAdapter.DetailKomentarViewHolder> {
    private List<DocumentSnapshot> komentarList;
    private FirebaseFirestore db;
    private Context context;

    public DetailKomentarAdapter(List<DocumentSnapshot> komentarList, Context context) {
        this.komentarList = komentarList;
        this.context = context;
        this.db = FirebaseFirestore.getInstance();
    }

    @NonNull
    @Override
    public DetailKomentarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_komentar_row, parent, false);
        return new DetailKomentarViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DetailKomentarAdapter.DetailKomentarViewHolder holder, int position) {
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

    public void setDetailKomentarList(List<DocumentSnapshot> komentarList) {
        this.komentarList = komentarList;
        notifyDataSetChanged();
    }

    class DetailKomentarViewHolder extends RecyclerView.ViewHolder {
        ImageView fotoProfil;
        TextView nama, isiPost;

        public DetailKomentarViewHolder(@NonNull View itemView) {
            super(itemView);
            fotoProfil = itemView.findViewById(R.id.img_user_photo);
            nama = itemView.findViewById(R.id.tv_username);
            isiPost = itemView.findViewById(R.id.tv_description);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DocumentSnapshot komentar = komentarList.get(getAdapterPosition());
                    Log.d("ID POST PADA KOMENTAR??? ", komentar.getString("idPostAsli"));
                    Intent intent = new Intent(v.getContext(), PostDetailActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("postId", komentar.getString("idPostAsli"));
                    intent.putExtras(bundle);
                    v.getContext().startActivity(intent);
                }
            });
        }
    }
}
