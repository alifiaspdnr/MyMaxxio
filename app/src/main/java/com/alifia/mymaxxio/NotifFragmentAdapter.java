package com.alifia.mymaxxio;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class NotifFragmentAdapter extends FragmentStateAdapter {

    public NotifFragmentAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 1) {
            return new NotifNasionalFragment();
        } else if (position == 2) {
            return new NotifRegionalFragment();
        } else if (position == 3) {
            return new NotifChapterFragment();
        }
        return new NotifSemuaFragment();
    }

    @Override
    public int getItemCount() {
        return 4;
    }
}
