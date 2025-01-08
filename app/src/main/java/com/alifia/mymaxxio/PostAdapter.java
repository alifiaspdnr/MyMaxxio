package com.alifia.mymaxxio;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.alifia.mymaxxio.model.Post;
import com.alifia.mymaxxio.model.User;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {
    private List<Post> posts;
    private User user;
    private FirebaseFirestore store;

    public class PostViewHolder extends RecyclerView.ViewHolder {
        public ImageView userPhoto;
        public TextView userName;
        public TextView description;
        public TextView category;
        public ImageView imgLike;
        public TextView likeCount;
        public ImageView imgComment;
        public TextView commentCount;
        public ImageView postImage;

        public PostViewHolder(View v) {
            super(v);
            userPhoto = v.findViewById(R.id.img_user_photo);
            userName = v.findViewById(R.id.tv_username);
            description = v.findViewById(R.id.tv_description);
            category = v.findViewById(R.id.tv_category);
            imgLike = v.findViewById(R.id.img_like);
            likeCount = v.findViewById(R.id.tv_like_count);
            imgComment = v.findViewById(R.id.img_comment);
            commentCount = v.findViewById(R.id.tv_comment_count);
            postImage = v.findViewById(R.id.postImage);

            // kalo postingan dipencet, intent ke PostDetailActivity
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Post data_post = posts.get(getAdapterPosition());
                    Log.d("ID POST CLICK DI CARD", data_post.getId());
                    Intent intent = new Intent(v.getContext(), PostDetailActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("postId", data_post.getId());
                    intent.putExtras(bundle);
                    v.getContext().startActivity(intent);
                }
            });

            // kalo icon komen dipencet, ngarah ke buat komen
            imgComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Post data_post = posts.get(getAdapterPosition());
                    Intent intent = new Intent(v.getContext(), CreateKomentar.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("postId", data_post.getId());
                    intent.putExtras(bundle);
                    v.getContext().startActivity(intent);
                }
            });
        }
    }

    public PostAdapter(List<Post> posts) {
        Log.d("TAG", posts.toString());
        this.posts= posts;
        this.user = new User();
        store = FirebaseFirestore.getInstance();
    }

    public PostAdapter(List<Post> posts, User user) {
        this.posts = posts;
        this.user = user;
        store = FirebaseFirestore.getInstance();
    }

    @Override
    public PostViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_post_row, parent, false);
        return new PostViewHolder(v);
    }

    @Override
    public void onBindViewHolder(PostViewHolder holder, int position) {
        // ngeset foto profil
        store
                .collection("users")
                .document(posts.get(position).getIdAnggota())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        user = documentSnapshot.toObject(User.class);
                        if (user.getPhotoProfile() != null) {
                            Glide.with(holder.itemView.getContext())
                                    .load(user.getPhotoProfile())
                                    .into(holder.userPhoto);
                        } else {
                            holder.userPhoto.setImageResource(R.drawable.ic_profile);
                        }
                    }
                });

        holder.userName.setText(posts.get(position).getNama());
        holder.description.setText(posts.get(position).getIsiPost());
        holder.category.setText(posts.get(position).getNamaTingkatan());

        if (user.getLikes().contains(posts.get(position).getId())) {
            holder.imgLike.setImageResource(R.drawable.ic_like);
        } else {
            holder.imgLike.setImageResource(R.drawable.baseline_favorite_border_24);
        }

        holder.likeCount.setText(String.valueOf(posts.get(position).getJmlLike()));

        // like (dari cgpt tapi ttp gabisa)
        /*holder.imgLike.setOnClickListener(new View.OnClickListener() {
            final int currentPosition = holder.getAdapterPosition();
            @Override
            public void onClick(View v) {
                Post post = posts.get(currentPosition);
                String userId = user.getUserId();

                // Cek apakah user sudah menyukai post ini dengan mengambil data user dari Firestore
                store.collection("users").document(userId)
                        .get()
                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                User currentUser = documentSnapshot.toObject(User.class);
                                List<String> likes = currentUser.getLikes();

                                if (likes.contains(post.getId())) {
                                    // Jika sudah menyukai, kurangi jumlah like, hapus id post dari array likes, dan ubah gambar like menjadi tidak aktif
                                    store.collection("posts").document(post.getId())
                                            .update("jmlLike", post.getJmlLike() - 1);
                                    likes.remove(post.getId());
                                    store.collection("users").document(userId)
                                            .update("likes", likes);
                                    post.setJmlLike(post.getJmlLike() - 1);
                                    holder.imgLike.setImageResource(R.drawable.baseline_favorite_border_24);
                                } else {
                                    // Jika belum menyukai, tambah jumlah like, tambahkan id post ke array likes, dan ubah gambar like menjadi aktif
                                    store.collection("posts").document(post.getId())
                                            .update("jmlLike", post.getJmlLike() + 1);
                                    likes.add(post.getId());
                                    store.collection("users").document(userId)
                                            .update("likes", likes);
                                    post.setJmlLike(post.getJmlLike() + 1);
                                    holder.imgLike.setImageResource(R.drawable.ic_like);
                                }

                                // Memperbarui teks jumlah like
                                holder.likeCount.setText(String.valueOf(post.getJmlLike()));
                            }
                        });
            }
        });*/

        // like lama
        /*holder.imgLike.setOnClickListener(new View.OnClickListener() {
            final int currentPosition = holder.getAdapterPosition();
            @Override
            public void onClick(View v) {
                List<String> likes = user.getLikes();
                Post post = posts.get(currentPosition);
                if (user.getLikes().contains(post.getId())) {
                    store.collection("posts").document(post.getId())
                            .update("jmlLike", post.getJmlLike() - 1);
                    likes.remove(post.getId());
                    store.collection("users").document(user.getUserId())
                            .update("likes", likes);
                    post.setJmlLike(post.getJmlLike() - 1);
                    holder.imgLike.setImageResource(R.drawable.baseline_favorite_border_24);
                } else {
                    store.collection("posts").document(post.getId())
                            .update("jmlLike", post.getJmlLike() + 1);
                    likes.add(post.getId());
                    store.collection("users").document(user.getUserId())
                            .update("likes", likes);
                    post.setJmlLike(post.getJmlLike() + 1);
                    holder.imgLike.setImageResource(R.drawable.ic_like);
                }
                holder.likeCount.setText(String.valueOf(post.getJmlLike()));
            }
        });*/

        holder.commentCount.setText(String.valueOf(posts.get(position).getJmlKomen()));
        holder.imgComment.setImageResource(R.drawable.ic_comment);

        if (posts.get(position).getAttachment() != null && posts.get(position).getAttachment() != "") {
            Glide.with(holder.itemView.getContext())
                    .load(posts.get(position).getAttachment())
                    .into(holder.postImage);
            holder.postImage.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }
}
