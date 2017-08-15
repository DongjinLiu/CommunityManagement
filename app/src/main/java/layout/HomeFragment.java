package layout;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.jin.communitymanagement.AssociationActivity;
import com.example.jin.communitymanagement.AssociationActivityAdapter;
import com.example.jin.communitymanagement.R;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment {

    private AssociationActivity[] associationActivities={new AssociationActivity("热舞社","hiphop","下午两点",R.drawable.letsdance),new AssociationActivity("热舞社","hiphop","下午两点",R.drawable.letsdance),new AssociationActivity("热舞社","hiphop","下午两点",R.drawable.letsdance),new AssociationActivity("热舞社","hiphop","下午两点",R.drawable.letsdance)};

    private List<AssociationActivity> associationActivityList=new ArrayList<>();

    private AssociationActivityAdapter adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        initAssociationActivity();
       View view= inflater.inflate(R.layout.fragment_home, container, false);
        RecyclerView recyclerView=(RecyclerView)view.findViewById(R.id.home_recycler_view);
        GridLayoutManager layoutManager=new GridLayoutManager(getContext(),2);
        recyclerView.setLayoutManager(layoutManager);
        adapter=new AssociationActivityAdapter(associationActivityList);
        recyclerView.setAdapter(adapter);
        return view;
    }



    private void initAssociationActivity()
    {
        associationActivityList.clear();
        for (int i=0;i<associationActivities.length;i++)
        {
            associationActivityList.add(associationActivities[i]);
        }
    }
}
