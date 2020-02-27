package com.example.smedicine;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.smedicine.fragment.DayHealthAnalyseFragment;
import com.example.smedicine.fragment.MonthHealthAnalyseFragment;
import com.example.smedicine.fragment.WeekHealthAnalyseFragment;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class HealthAnalyseActivity extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager viewPager;

    List<Fragment> fragments = new ArrayList<>();
    List<String> titles = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_analyse);

        //设置导航栏
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_health_analysis);
        setSupportActionBar(toolbar);
//        ActionBar actionBar = getSupportActionBar();
//        if (actionBar != null) {
//            actionBar.hide();
//            actionBar.setDisplayHomeAsUpEnabled(true);
//            //菜单图标
//            actionBar.setHomeAsUpIndicator(R.drawable.menu);
//        }

        tabLayout = findViewById(R.id.tl_tabs);
        viewPager = findViewById(R.id.vp_content);

        fragments.add(new DayHealthAnalyseFragment());
        fragments.add(new WeekHealthAnalyseFragment());
        fragments.add(new MonthHealthAnalyseFragment());

        titles.add("日");
        titles.add("周");
        titles.add("月");

        viewPager.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragments.get(position);
            }

            @Override
            public int getCount() {
                return fragments.size();
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                super.destroyItem(container, position, object);
            }

            @Nullable
            @Override
            public CharSequence getPageTitle(int position) {

                return titles.get(position);
            }
        });

        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.history, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.history_item:
                Intent intent = new Intent(HealthAnalyseActivity.this,MedicineHistoryActivity.class);
                startActivity(intent);
                break;

        }
        return true;
    }

}
