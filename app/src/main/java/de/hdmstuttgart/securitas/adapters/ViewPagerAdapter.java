package de.hdmstuttgart.securitas.adapters;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import de.hdmstuttgart.securitas.fragments.PwFormFragment;
import de.hdmstuttgart.securitas.fragments.PwGeneratorFragment;


public class ViewPagerAdapter extends FragmentStateAdapter {

    public static final int NUM_PAGES = 2;

    public ViewPagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }


    //creates Fragment depending on the position
    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 1) {
            return new PwGeneratorFragment();
        }
        return new PwFormFragment();
    }


    @Override
    public int getItemCount() {
        return NUM_PAGES;
    }
}