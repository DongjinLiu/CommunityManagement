package layout;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.jin.communitymanagement.AssociationActivity;
import com.example.jin.communitymanagement.AssociationActivityAdapter;
import com.example.jin.communitymanagement.HeaderAdapter;
import com.example.jin.communitymanagement.HomeFlag;
import com.example.jin.communitymanagement.HomeFlagAdapter;
import com.example.jin.communitymanagement.MainViewPagerAdapter;
import com.example.jin.communitymanagement.R;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;
import java.util.List;

import info.hoang8f.android.segmented.SegmentedGroup;

import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;


public class HomeFragment extends Fragment {


    private static View view;
    public  static final String GET_THE_VOICE="get the voice";

    private VoiceReceiver voiceReceiver;

    private MaterialSearchView searchView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private List<AssociationActivity> associationActivityList=new ArrayList<>();

    private AssociationActivityAdapter association_ac_adapter;
    private AssociationActivity[] associationActivities={new AssociationActivity("热舞社ABC","hiphop","下午两点",R.drawable.letsdance),new AssociationActivity("热舞社","hiphop","下午两点",R.drawable.letsdance),new AssociationActivity("热舞社","hiphop","下午两点",R.drawable.letsdance),new AssociationActivity("热舞社","hiphop","下午两点",R.drawable.letsdance)};
    private   RecyclerView homeRecyclerView;

    //属性都在这里

    private boolean assiciationOrActivity=false;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

       view= inflater.inflate(R.layout.fragment_home, container, false);

        initHomeFragment();
        initSearchView();
        return view;
    }

    private void initSearchView() {
        searchView=(MaterialSearchView)getActivity().findViewById(R.id.search_view);
        searchView.setEllipsize(true);
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //Do some magic
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                final List<AssociationActivity> filteredModelList=filter(associationActivityList,newText);
                association_ac_adapter.setFilter(filteredModelList);
                association_ac_adapter.animateTo(filteredModelList);
                homeRecyclerView.scrollToPosition(0);
                return true;

            }
        });

        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {
                //Do some magic
            }

            @Override
            public void onSearchViewClosed() {
                association_ac_adapter.setFilter(associationActivityList);
            }


        });
        searchView.setVoiceSearch(false); //or false

    }

    @Override
    public void onResume() {
        super.onResume();
        IntentFilter intentFilter=new IntentFilter();
        intentFilter.addAction("com.example.jin.communitymanagement.GET_THE_VOICE");
        voiceReceiver=new VoiceReceiver();
        getActivity().registerReceiver(voiceReceiver,intentFilter);
    }

    @Override
    public void onPause() {
        super.onPause();
        if(voiceReceiver!=null)
        {
            getActivity().unregisterReceiver(voiceReceiver);
            voiceReceiver=null;
        }
    }

    private void initHomeFragment() {
        initRecyclerView();
        initSwipeRefresh();


    }
    private void initSwipeRefresh()
    {


        swipeRefreshLayout=(SwipeRefreshLayout) view.findViewById(R.id.home_swipe_refresh);
        if(swipeRefreshLayout==null)
        {
            Log.d(TAG, "initSwipeRefresh: swipeRe出问题啦");
        }
        swipeRefreshLayout.setColorSchemeResources(R.color.colorText);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshDisplay();
            }
        });


    }

    private void refreshDisplay()
    {

        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    Thread.sleep(10);
                }catch (InterruptedException e)
                {
                    e.printStackTrace();
                }

               getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        initAssociationActivity();
                        association_ac_adapter.notifyDataSetChanged();
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });
            }

        }).start();
    }
    private void initRecyclerView()
    {
        initAssociationActivity();


            homeRecyclerView=(RecyclerView) view.findViewById(R.id.home_recycler_view) ;


        GridLayoutManager layoutManager=new GridLayoutManager(getContext(),1);

        homeRecyclerView.setLayoutManager(layoutManager);
        association_ac_adapter =new AssociationActivityAdapter(associationActivityList);
        homeRecyclerView.setAdapter(association_ac_adapter);
        HeaderAdapter headerAdapter=new HeaderAdapter(association_ac_adapter);
        LayoutInflater inflater_header=LayoutInflater.from(getContext());
        View view=inflater_header.inflate(R.layout.home_header_cardview,null);
        initRecycHeader(view);
        headerAdapter.addHeaderView(view);
        homeRecyclerView.setAdapter(headerAdapter);

    }

    private void initRecycHeader(View view) {
        initHomeFlagList(view);


        SegmentedGroup segmented= (SegmentedGroup)view.findViewById(R.id.group_home_segmented);
       segmented.setTintColor(Color.parseColor("#FFcc00"));
        segmented.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId)
                {
                    case R.id.btn_home_activity:
                        Toast.makeText(getActivity(), "你选择了活动", Toast.LENGTH_SHORT).show();
                        assiciationOrActivity=false;
                        break;
                    case R.id.btn_home_association:
                        Toast.makeText(getActivity(), "你选择了社团", Toast.LENGTH_SHORT).show();
                        assiciationOrActivity=true;
                        break;
                }
            }
        });

    }

    private void initHomeFlagList(View view) {
        initActivityFlagList();
        initAssociationFlagList();
        RecyclerView recyclerViewFlag=(RecyclerView)view.findViewById(R.id.recycler_header_home);
        LinearLayoutManager layoutManager=new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerViewFlag.setLayoutManager(layoutManager);
        HomeFlagAdapter adapterFlag=new HomeFlagAdapter(activityFlagList);
        recyclerViewFlag.setAdapter(adapterFlag);
    }

    private List<HomeFlag> associationFlagList=new ArrayList<>();
    private void initAssociationFlagList() {

        HomeFlag dance=new HomeFlag(false,"舞蹈");
        associationFlagList.add(dance);
        HomeFlag opera=new HomeFlag(false,"话剧");
        associationFlagList.add(opera);
        HomeFlag song=new HomeFlag(false,"音乐");
        associationFlagList.add(song);
        HomeFlag literal=new HomeFlag(false,"文艺");
        associationFlagList.add(literal);
        HomeFlag outdoor=new HomeFlag(false,"户外");
        associationFlagList.add(outdoor);
        HomeFlag little=new HomeFlag(false,"小众");
        associationFlagList.add(little);

    }

    private List<HomeFlag> activityFlagList=new ArrayList<>();
    private void initActivityFlagList() {
        HomeFlag literal=new HomeFlag(false,"文艺");
        activityFlagList.add(literal);
        HomeFlag quiet=new HomeFlag(false,"恬静");
        activityFlagList.add(quiet);
        HomeFlag crazy=new HomeFlag(false,"疯狂");
        activityFlagList.add(crazy);
        HomeFlag sexy=new HomeFlag(false,"骚气");
        activityFlagList.add(sexy);
        HomeFlag ghost=new HomeFlag(false,"诡异");
        activityFlagList.add(ghost);

    }

    private void initAssociationActivity()
    {
        associationActivityList.clear();
        for (int i=0;i<associationActivities.length;i++)
        {
            associationActivityList.add(associationActivities[i]);
        }
        for (int i=0;i<associationActivities.length;i++)
        {
            associationActivityList.add(associationActivities[i]);
        }
    }
    private List<AssociationActivity> filter(List<AssociationActivity> associationActivities, String query) {
        query = query.toLowerCase();

        final List<AssociationActivity> filteredModelList = new ArrayList<>();
        for (AssociationActivity associationAc : associationActivities) {

            final String nameEn = associationAc.getActivityName().toLowerCase();
            final String AcEn = associationAc.getAssociationName().toLowerCase();
            final String name = associationAc.getActivityName();
            final String Ac = associationAc.getAssociationName();

            if (name.contains(query) || Ac.contains(query) || nameEn.contains(query) || AcEn.contains(query)) {

                filteredModelList.add(associationAc);
            }
        }
        return filteredModelList;
    }

    class VoiceReceiver extends BroadcastReceiver
    {
        @Override
        public void onReceive(Context context, Intent intent) {
          String searchWrd=  intent.getStringExtra(GET_THE_VOICE);
            searchView.setQuery(searchWrd,false);

        }
    }
}
