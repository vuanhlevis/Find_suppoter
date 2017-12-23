package com.example.anull.findsuppoter.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.example.anull.findsuppoter.R;
import com.example.anull.findsuppoter.Utils.ExpandableListAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChooseFragment extends Fragment {

    private ExpandableListView listView;
    private ExpandableListAdapter listAdapter;

    private List<String> listDataheader;
    private HashMap<String, List<String>> listHash;

    public View view;

    public ChooseFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/VnSouthern.TTF")
                .setFontAttrId(R.attr.fontPath)
                .build());
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_choose, container, false);

        setupInput();

        return view;
    }

    private void setupInput() {
        listView = view.findViewById(R.id.lv_expand);

        initData();

        listAdapter = new ExpandableListAdapter(getContext(),listDataheader, listHash);

        listView.setAdapter(listAdapter);
    }

    private void initData() {
        listDataheader = new ArrayList<>();
        listHash = new HashMap<>();

        listDataheader.add("Information Technology");
        listDataheader.add("Telecommunication");
        listDataheader.add("Electronic");
        listDataheader.add("Different");


        List<String> cntt = new ArrayList<>();
        List<String> vienthong = new ArrayList<>();
        List<String> dientu = new ArrayList<>();
        List<String> khac = new ArrayList<>();

        cntt.add("Java");
        cntt.add("C");
        cntt.add("C++");
        cntt.add("Android");
        cntt.add("PHP");
        cntt.add("Ruby");
        cntt.add("Assembly");

        vienthong.add("Ly Thuyet Thong Tin");
        vienthong.add("Dien Tu Truyen Thong");
        vienthong.add("Keo cap");
        vienthong.add("Mang May Tinh");

        dientu.add("Assembly");
        dientu.add("IOT");
        dientu.add("Robot");

        khac.add("Giai Tich");
        khac.add("Dai So");
        khac.add("Nhung Nguyen Ly Co Ban Cua Chu Nghia Mac-Lenin");
        khac.add("Giao Duc The Chat");

        listHash.put(listDataheader.get(0),cntt);
        listHash.put(listDataheader.get(1),vienthong);
        listHash.put(listDataheader.get(2),dientu);
        listHash.put(listDataheader.get(3),khac);

    }

}
