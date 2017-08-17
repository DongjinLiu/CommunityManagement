package com.example.jin.communitymanagement;

import android.content.Intent;
import android.graphics.Color;
import android.speech.RecognizerIntent;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Toast;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;

import layout.BorrowFragment;
import layout.DateFragment;
import layout.HomeFragment;
import layout.MineFragment;
import layout.MoneyFragment;

public class MainActivity extends AppCompatActivity {
    private ViewPager viewPager;

    private MainViewPagerAdapter adapter;

    private AHBottomNavigation bottomNavigation;

    private Toolbar toolbar;


    private MaterialSearchView searchView;

    //做标签，说明现在是属于哪一个fragment
    public static int MARK = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_main);
        operateToolBar();
        initBottomNavigation();

        initViewPager();

        initSearchView();

    }

    //当主页背景被按下时候，关闭搜索框

    @Override
    public void onBackPressed() {
        if (searchView.isSearchOpen()) {
            searchView.closeSearch();
        } else {
            super.onBackPressed();
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);


    }

    private void operateToolBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }


    //初始化搜索框
    private void initSearchView()
    {
        searchView = (MaterialSearchView) findViewById(R.id.search_view);
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //Do some magic
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //Do some magic
                return false;
            }
        });

        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {
                //Do some magic
            }

            @Override
            public void onSearchViewClosed() {
                //Do some magic
            }


        });
        searchView.setVoiceSearch(true); //or false
    }
//语音识别反馈
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == MaterialSearchView.REQUEST_VOICE && resultCode == RESULT_OK) {
            ArrayList<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (matches != null && matches.size() > 0) {
                String searchWrd = matches.get(0);
                if (!TextUtils.isEmpty(searchWrd)) {
                    searchView.setQuery(searchWrd, false);
                }
            }
            return;
        }


        super.onActivityResult(requestCode, resultCode, data);
    }

    private void initViewPager()
    {
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        adapter=new MainViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new HomeFragment());
        adapter.addFragment(new BorrowFragment());
        adapter.addFragment(new DateFragment());
        adapter.addFragment(new MoneyFragment());
        adapter.addFragment(new MineFragment());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                bottomNavigation.setCurrentItem(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void initBottomNavigation()
    {
        bottomNavigation=(AHBottomNavigation)findViewById(R.id.bottom_navigation);
        AHBottomNavigationItem item1=new AHBottomNavigationItem("主页",R.drawable.home,R.color.colorAccent);
        AHBottomNavigationItem item2=new AHBottomNavigationItem("租赁",R.drawable.borrow,R.color.colorPrimaryDark);
        AHBottomNavigationItem item3=new AHBottomNavigationItem("日程", R.drawable.date,R.color.colorPrimary);
        AHBottomNavigationItem item4=new AHBottomNavigationItem("赞助",R.drawable.money,R.color.colorPrimary);
        AHBottomNavigationItem item5=new AHBottomNavigationItem("我的",R.drawable.mine,R.color.colorPrimary);
        bottomNavigation.addItem(item1);
        bottomNavigation.addItem(item2);
        bottomNavigation.addItem(item3);
        bottomNavigation.addItem(item4);
        bottomNavigation.addItem(item5);
        //设置背景颜色
        bottomNavigation.setDefaultBackgroundColor(Color.parseColor("#333333"));

// Disable the translation inside the CoordinatorLayout
        bottomNavigation.setBehaviorTranslationEnabled(false);



// Change colors
        //选中的颜色
        bottomNavigation.setAccentColor(Color.parseColor("#FFFF00"));
        //非active的颜色
        bottomNavigation.setInactiveColor(Color.parseColor("#999999"));

// Force to tint the drawable (useful for font with icon for example)
        bottomNavigation.setForceTint(true);

// Display color under navigation bar (API 21+)
// Don't forget these lines in your style-v21
// <item name="android:windowTranslucentNavigation">true</item>
// <item name="android:fitsSystemWindows">true</item>
        bottomNavigation.setTranslucentNavigationEnabled(true);

// Manage titles
        bottomNavigation.setTitleState(AHBottomNavigation.TitleState.SHOW_WHEN_ACTIVE);


        bottomNavigation.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
            @Override
            public boolean onTabSelected(int position, boolean wasSelected) {

                viewPager.setCurrentItem(position);
                MARK=position;
                return true;
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar,menu);

        MenuItem item = menu.findItem(R.id.action_search);
        searchView.setMenuItem(item);


        return true;

    }




}
