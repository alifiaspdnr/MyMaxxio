package com.alifia.mymaxxio;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class HomeFragmentAdapter extends FragmentStateAdapter {
    public HomeFragmentAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 1) {
            return new HomeNasionalFragment();
        } else if (position == 2) {
            return new HomeRegionalFragment();
        } else if (position == 3) {
            return new HomeChapterFragment();
        }
        return new HomeSemuaFragment();
    }

    @Override
    public int getItemCount() {
        return 4;
    }
}
