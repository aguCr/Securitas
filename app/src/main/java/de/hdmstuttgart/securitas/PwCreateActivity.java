package de.hdmstuttgart.securitas;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;

import de.hdmstuttgart.securitas.adapters.ViewPagerAdapter;

public class PwCreateActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager2 viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pw_create);


        tabLayout = findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.view_pager);

        //set text from string resources
        tabLayout.addTab(tabLayout.newTab().setText(getResources().getString(R.string.password_title)));
        tabLayout.addTab(tabLayout.newTab().setText(getResources().getString(R.string.generator_title)));


        FragmentManager fragmentManager = getSupportFragmentManager();
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(fragmentManager, getLifecycle());
        viewPager.setAdapter(viewPagerAdapter);

        //set correct Fragment on click
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                //nothing
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                //nothing
            }
        });

        //set correct Fragment on swipe
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                tabLayout.selectTab(tabLayout.getTabAt(position));
            }
        });

    }

    //otherwise the title set in the PwFormFragment will be ellipsized: Passwo...
    @Override
    public void setTitle(final CharSequence title) {
        new Handler(Looper.getMainLooper()).post(() -> PwCreateActivity.super.setTitle(title));
    }
}