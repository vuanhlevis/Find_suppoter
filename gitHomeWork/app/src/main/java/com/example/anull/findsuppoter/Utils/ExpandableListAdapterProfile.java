package com.example.anull.findsuppoter.Utils;


import android.content.Context;
import android.graphics.Typeface;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.example.anull.findsuppoter.R;
import com.example.anull.findsuppoter.fragments.ProfileFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by null on 09/12/2017.
 */

public class ExpandableListAdapterProfile extends BaseExpandableListAdapter {
    private Context context;
    private List<String> listDataHeader;
    private HashMap<String, List<String>> listHashMap;

    public SparseArray<SparseBooleanArray> checkedPositions = new SparseArray<>();

    public ExpandableListAdapterProfile(Context context, List<String> listDataHeader,
                                        HashMap<String, List<String>> listHashMap) {
        this.context = context;
        this.listDataHeader = listDataHeader;
        this.listHashMap = listHashMap;

    }

    @Override
    public int getGroupCount() {
        return listDataHeader.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return listHashMap.get(listDataHeader.get(i)).size();
    }

    @Override
    public Object getGroup(int i) {
        return listDataHeader.get(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        return listHashMap.get(listDataHeader.get(i)).get(i1); // i : group, i1: item
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        String headerTitle = (String) getGroup(i);

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list_parent_expandable, null);
        }

        TextView tv_header = view.findViewById(R.id.tv_parentTitle);
        tv_header.setTypeface(null, Typeface.BOLD);
        tv_header.setText(headerTitle);

        return view;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {


        String childText = (String) getChild(i,i1);
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            view = inflater.inflate(android.R.layout.simple_list_item_multiple_choice, viewGroup, false);
        }

        ((CheckedTextView)view).setText(getChild(i, i1).toString());
        checkedPositions = ProfileFragment.checkedPositionsPro;

        if (checkedPositions.get(i) != null) {
            boolean isChecked = checkedPositions.get(i).get(i1);
            ((CheckedTextView)view).setChecked(isChecked);
            // If it does not exist, mark the checkBox as false
        } else {
            ((CheckedTextView)view).setChecked(false);
        }

        return view;

    }

    @Override
    public boolean isChildSelectable(int i, int i1) {

        return true;
    }

    public void setClicked(int grPos, int childPos) {
        SparseBooleanArray checkedChildPositionsMultiple = checkedPositions.get(grPos);
        // if in the group there was not any child checked
        if (checkedChildPositionsMultiple == null) {
            checkedChildPositionsMultiple = new SparseBooleanArray();
            // By default, the status of a child is not checked
            // So a click will enable it
            checkedChildPositionsMultiple.put(childPos, true);
            checkedPositions.put(grPos, checkedChildPositionsMultiple);
        } else {
            boolean oldState = checkedChildPositionsMultiple.get(childPos);
            checkedChildPositionsMultiple.put(childPos, !oldState);
        }
        notifyDataSetChanged();
    }
}