package com.example.jin.communitymanagement;

import android.content.Context;
import android.media.Image;
import android.support.v7.widget.CardView;
import android.support.v7.widget.ListPopupWindow;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by summe on 2017/8/15.
 */

public class AssociationActivityAdapter extends RecyclerView.Adapter<AssociationActivityAdapter.ViewHolder> {


    private List<AssociationActivity> m_association_ac_List;
    private Context mContest;

    static class ViewHolder extends RecyclerView.ViewHolder
    {
        CardView cardView;
        ImageView activityImage;
        TextView activityName;
        TextView associationName;
        TextView activityTime;

        public ViewHolder(View view)
        {
            super(view);
            cardView=(CardView)view;
            activityImage=(ImageView)view.findViewById(R.id.img_activity);
            activityName=(TextView)view.findViewById(R.id.text_ac_Name);
            associationName=(TextView)view.findViewById(R.id.text_ac_associationName);
            activityTime=(TextView)view.findViewById(R.id.text_ac_time);

        }
    }

    public AssociationActivityAdapter(List<AssociationActivity> association_ac_List)
    {
        m_association_ac_List=association_ac_List;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(mContest==null)
        {
            mContest=parent.getContext();
        }
        View view= LayoutInflater.from(mContest).inflate(R.layout.association_activity_item,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        AssociationActivity associationActivity=m_association_ac_List.get(position);
        holder.activityName.setText(associationActivity.getActivityName());
        holder.associationName.setText(associationActivity.getAssociationName());
        holder.activityTime.setText(associationActivity.getMyTime());
        Glide.with(mContest).load(associationActivity.getImageId()).into(holder.activityImage);
    }



    @Override
    public int getItemCount() {
        return m_association_ac_List.size();
    }

    public void setFilter(List<AssociationActivity> association_ac_List)
    {
        m_association_ac_List=new ArrayList<>();
        m_association_ac_List.addAll(association_ac_List);
        notifyDataSetChanged();
    }


    public void addItem(int position,AssociationActivity associationActivity)
    {
        m_association_ac_List.add(position,associationActivity);
        notifyItemInserted(position);
    }
    public AssociationActivity removeItem(int position)
    {
        final  AssociationActivity associationActivity=m_association_ac_List.remove(position);
        notifyItemRemoved(position);
        return associationActivity;
    }
    public void moveItem(int fromPosition,int toPosition)
    {
        final AssociationActivity associationActivity=m_association_ac_List.remove(fromPosition);
        m_association_ac_List.add(toPosition,associationActivity);
        notifyItemMoved(fromPosition,toPosition);
    }
    private void applyAndAnimateMovedItems(List<AssociationActivity> association_ac_List)
    {
        for (int toPosition = association_ac_List.size() - 1; toPosition >= 0; toPosition--) {
            final AssociationActivity associationActivity = association_ac_List.get(toPosition);
            final int fromPosition = m_association_ac_List.indexOf(associationActivity);
            if (fromPosition >= 0 && fromPosition != toPosition) {
                moveItem(fromPosition, toPosition);
            }
        }

    }
    private void applyAndAnimateAdditions(List<AssociationActivity> association_ac_List) {
        for (int i = 0, count = association_ac_List.size(); i < count; i++) {
            final AssociationActivity associationActivity = association_ac_List.get(i);
            if (!m_association_ac_List.contains(associationActivity)) {
                addItem(i, associationActivity);
            }
        }
    }
    private void applyAndAnimateRemovals(List<AssociationActivity> association_ac_List) {
        for (int i = m_association_ac_List.size() - 1; i >= 0; i--) {
            final AssociationActivity associationActivity = m_association_ac_List.get(i);
            if (!association_ac_List.contains(associationActivity)) {
                removeItem(i);
            }
        }
    }

    public void animateTo(List<AssociationActivity> association_ac_List) {
        applyAndAnimateRemovals(association_ac_List);
        applyAndAnimateAdditions(association_ac_List);
        applyAndAnimateMovedItems(association_ac_List);
    }


}
