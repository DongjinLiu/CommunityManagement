package com.example.jin.communitymanagement;

import android.graphics.Color;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Toast;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;

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



    //做标签，说明现在是属于哪一个fragment
    public static int MARK=0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_main);
        operateToolBar();
        initBottomNavigation();

        initViewPager();






    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);


    }

    private  void operateToolBar()
    {
        toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
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
    //下面这个函数主要是操作顶部搜索框
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar,menu);
        MenuItem item = menu.findItem(R.id.searchIt);

        SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        //设置搜索栏的默认提示
        searchView.setQueryHint("请输入社团名，活动名");
        //默认刚进去就打开搜索栏
        searchView.setIconified(true);
        //设置输入文本的EditText
        searchView.setBackgroundColor(Color.parseColor("#444444"));
        SearchView.SearchAutoComplete et = (SearchView.SearchAutoComplete)searchView.findViewById(R.id.search_src_text);
        //设置搜索栏的默认提示，作用和setQueryHint相同
        et.setHint("请输入社团名，活动名");
        et.setTextSize(15.0f);
        //设置提示文本的颜色

        et.setHintTextColor(Color.parseColor("#777777"));
        //设置输入文本的颜色

        et.setTextColor(Color.YELLOW);
        //设置提交按钮是否可见
        //searchView.setSubmitButtonEnabled(true);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Toast.makeText(MainActivity.this, "您输入的文本为" + query, Toast.LENGTH_SHORT).show();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });



        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.search_filter:
                Toast.makeText(this, "You clicked the search filter", Toast.LENGTH_SHORT).show();
                break;
            default:
        }
        return true;
    }


}
