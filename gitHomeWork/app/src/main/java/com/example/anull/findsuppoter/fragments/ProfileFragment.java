package com.example.anull.findsuppoter.fragments;


import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Switch;

import com.example.anull.findsuppoter.R;
import com.example.anull.findsuppoter.Utils.ExpandableListAdapter;
import com.example.anull.findsuppoter.Utils.ExpandableListAdapterProfile;
import com.example.anull.findsuppoter.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import dmax.dialog.SpotsDialog;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    private static final String TAG = "tttx";
    private String pass = "";
    EditText et_nameProfile;
    EditText et_mailProfile;
    EditText et_phoneProfile;
    EditText et_available;
    EditText et_pass;
    ImageView iv_edit;
    Switch sw_status;

    String mlocation = "";

    EditText et_chuyennganh;
    String rating = "0";

    RatingBar rt_profile;

    View view;

    // expanlistview
    private ExpandableListView listView;
    private ExpandableListAdapterProfile listAdapter;

    private List<String> listDataheader;
    private HashMap<String, List<String>> listHash;

    //

    private FirebaseAuth firebaseAuth;
    private DatabaseReference users;
    private FirebaseDatabase database;

    public ProfileFragment() {

        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Arkhip_font.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build());
        super.onCreate(savedInstanceState);
        firebaseAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        users = database.getReference("users");
    }

    @Subscribe(sticky = true)
    public void onReceiveUserEvent(User user) {
        et_nameProfile.setText(user.getName());
//        tv_name_title .setText(user.getName());
        et_mailProfile.setText(user.getEmail());
        et_phoneProfile.setText(user.getPhone());
        et_pass.setText(user.getPassword());

        if (user.getAvailable().equals("0")) {
            et_available.setTextColor(getResources().getColor((R.color.ballPressColor)));
            et_available.setText("Dissable");
        } else {
            et_available.setTextColor(getResources().getColor(R.color.bt_signIncolor));
            et_available.setText("Enable");
        }

        mlocation = user.getLocation();

        rating = user.getRating();
        rt_profile.setRating(Float.parseFloat(rating));
        pass = user.getPassword();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Arkhip_font.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build());
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_profile, container, false);
        setUpInput();

        if (EventBus.getDefault().isRegistered(this)) {

        } else {
            EventBus.getDefault().register(this);
        }

//        Log.d(TAG, "onCreateView:  user" + users);
//        Log.d(TAG, "onCreateView:  data" + database.toString());
//        Log.d(TAG, "onCreateView:  email" + firebaseAuth.getCurrentUser().getEmail());

        iv_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogEditProfile();

                Log.d(TAG, "onClick: " + rating);

            }
        });

        et_chuyennganh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setOnclickchuyennganh();
            }
        });


        return view;


    }

    private void setOnclickchuyennganh() {
        android.app.AlertDialog.Builder dialog =
                new android.app.AlertDialog.Builder(getContext());
        dialog.setTitle("CHOOSE OPTION");
        dialog.setMessage("You can choose only 1 option!!");

        LayoutInflater inflater = LayoutInflater.from(getContext());

        View chuyennganh_layout = inflater.inflate(R.layout.fragment_choose, null);
        listView = chuyennganh_layout.findViewById(R.id.lv_expand);
        initData();

        listAdapter = new ExpandableListAdapterProfile(chuyennganh_layout.getContext(), listDataheader, listHash);

        listView.setAdapter(listAdapter);

        dialog.setPositiveButton("SAVE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                listView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
                    @Override
                    public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i1, long l) {
//                        int position = listView.getFlatListPosition(ExpandableListView.getPackedPositionForChild(i,i1));
//
//                        listView.setItemChecked(position, true);

                        return false;
                    }
                });


                dialogInterface.dismiss();
            }
        });

        dialog.setView(chuyennganh_layout);

        dialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        dialog.show();
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

        listHash.put(listDataheader.get(0), cntt);
        listHash.put(listDataheader.get(1), vienthong);
        listHash.put(listDataheader.get(2), dientu);
        listHash.put(listDataheader.get(3), khac);

    }

    private void setUpInput() {
        rt_profile = view.findViewById(R.id.rt_profile);
        et_nameProfile = view.findViewById(R.id.et_name_profile);
//        tv_name_title = view.findViewById(R.id.tv_name_profile_title);
        et_mailProfile = view.findViewById(R.id.et_mail_profile);
        et_phoneProfile = view.findViewById(R.id.et_phone_profile);
        et_available = view.findViewById(R.id.et_available);
        iv_edit = view.findViewById(R.id.iv_edit);
        et_chuyennganh = view.findViewById(R.id.et_chuyennganh);

        et_pass = view.findViewById(R.id.et_password_profile);


    }

    private void showDialogEditProfile() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
        dialog.setTitle("EDIT YOUR PROFILE");
        dialog.setMessage("Change your profile here!");

        LayoutInflater inflater = LayoutInflater.from(getContext());
        final View editLayout = inflater.inflate(R.layout.layout_profile, null);

        final MaterialEditText mt_name = editLayout.findViewById(R.id.et_name_editprofile);
        final MaterialEditText mt_pass = editLayout.findViewById(R.id.et_password_editprofile);
        final MaterialEditText mt_phone = editLayout.findViewById(R.id.et_phone_editprofile);

        sw_status = editLayout.findViewById(R.id.sw_status);

        mt_phone.setText(et_phoneProfile.getText());
        mt_name.setText(et_nameProfile.getText());
        mt_pass.setText(pass);

        Log.d(TAG, "onClick:  + user " + firebaseAuth.getCurrentUser().getEmail());

        if (et_available.getText().toString().equals("Dissable")) {
            sw_status.setChecked(false);
        } else {
            sw_status.setChecked(true);
        }

        dialog.setView(editLayout);
        dialog.setPositiveButton("SAVE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
//                dialogInterface.dismiss();

                if (TextUtils.isEmpty(mt_name.getText().toString())) {
                    Snackbar.make(view, "Enter Your Name Please", Snackbar.LENGTH_SHORT)
                            .show();
                    return;
                }

                if (TextUtils.isEmpty(mt_pass.getText().toString())) {
                    Snackbar.make(view, "Enter Your Password", Snackbar.LENGTH_SHORT)
                            .show();
                    return;
                }


                if ((mt_pass.getText().toString().length() < 6)) {
                    Snackbar.make(view, "Password must be more 6 character", Snackbar.LENGTH_SHORT)
                            .show();
                    return;
                }

                final SpotsDialog waitingDialog = new SpotsDialog(getContext());
                waitingDialog.show();

                users.orderByChild("email").equalTo(firebaseAuth.getCurrentUser().getEmail())
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                Log.d(TAG, "onDataChange: " + dataSnapshot);
                                if (dataSnapshot.getChildrenCount() > 0) {
                                    for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {

                                        Log.d(TAG, "onDataChange: snap" + userSnapshot);
                                        User user = userSnapshot.getValue(User.class);
                                        user.setRating(rating);
                                        if (sw_status.isChecked()) {
                                            user.setAvailable("1");

                                        } else {
                                            user.setAvailable("0");
                                        }

                                        user.setName(mt_name.getText().toString());
                                        user.setEmail(firebaseAuth.getCurrentUser().getEmail());
                                        user.setPhone(mt_phone.getText().toString());
                                        user.setPassword(mt_pass.getText().toString());
                                        user.setLocation(mlocation);

                                        firebaseAuth.getCurrentUser().updatePassword(mt_pass.getText().toString());

                                        users.child(userSnapshot.getKey()).setValue(user);

                                        updateProfile(user);

                                    }

                                }

                                waitingDialog.dismiss();
                                Snackbar.make(editLayout, "Success !", Snackbar.LENGTH_SHORT)
                                        .show();
                                return;

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

            }
        });

        dialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        dialog.show();

    }

    private void updateProfile(User user) {
        et_phoneProfile.setText(user.getPhone());
        et_mailProfile.setText(user.getEmail());
        et_nameProfile.setText(user.getName());
        et_pass.setText(user.getPassword());

        if (user.getAvailable().equals("0")) {
            et_available.setTextColor(getResources().getColor((R.color.ballPressColor)));
            et_available.setText("Dissable");
        } else {
            et_available.setTextColor(getResources().getColor(R.color.bt_signIncolor));
            et_available.setText("Enable");
        }

    }

}
