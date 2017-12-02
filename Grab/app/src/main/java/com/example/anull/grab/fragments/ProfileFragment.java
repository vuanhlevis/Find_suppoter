package com.example.anull.grab.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.anull.grab.R;
import com.example.anull.grab.activities.LoginActivity;
import com.example.anull.grab.database.DatabaseUserFirebase;
import com.example.anull.grab.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import dmax.dialog.SpotsDialog;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


public class ProfileFragment extends Fragment {
    private static final String TAG = "tttx";
    TextView tv_name;
    TextView tv_name_title;
    TextView tv_email;
    TextView tv_phone;
    TextView tv_chuyennganh;
    ImageView iv_edit;
    String rating = "0";

    RatingBar rt_profile;

    View view;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference users;
    private FirebaseDatabase database;


    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        users = database.getReference("users");

    }

    @Subscribe(sticky = true)
    public void onReceiveUserEvent(User user) {
        tv_name.setText(user.getName());
        tv_name_title .setText(user.getName());
        tv_email.setText(user.getEmail());
        tv_phone.setText(user.getPhone());
        tv_chuyennganh.setText("Java");
        rating = user.getRating();
        rt_profile.setRating(Float.parseFloat(rating));
    }

    private void setUpInput() {
        rt_profile = view.findViewById(R.id.rt_profile);
        tv_name = view.findViewById(R.id.tv_name_profile);
        tv_name_title = view.findViewById(R.id.tv_name_profile_title);
        tv_email = view.findViewById(R.id.tv_email_profile);
        tv_phone = view.findViewById(R.id.tv_phone_profile);
        tv_chuyennganh = view.findViewById(R.id.tv_chuyennganh_profile);
        iv_edit = view.findViewById(R.id.iv_edit_profile);
        rt_profile = view.findViewById(R.id.rt_profile);


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

        EventBus.getDefault().register(this);

        Log.d(TAG, "onCreateView:  user" + users);
        Log.d(TAG, "onCreateView:  data" + database.toString());
        Log.d(TAG, "onCreateView:  email" + firebaseAuth.getCurrentUser().getEmail());

        iv_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogEditProfile();

                Log.d(TAG, "onClick: " + rating);

            }
        });


        return view;
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
        final MaterialEditText mt_mail = editLayout.findViewById(R.id.et_email_editprofile);

        mt_mail.setText(tv_email.getText());

        mt_phone.setText(tv_phone.getText());
        mt_name.setText(tv_name.getText());



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
                if (TextUtils.isEmpty(mt_mail.getText().toString())) {
                    Snackbar.make(view, "Enter Your Email", Snackbar.LENGTH_SHORT)
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
                                        User user = userSnapshot.getValue(User.class);
                                        user.setRating(rating);
                                        user.setAvailable("1");
                                        user.setName(mt_name.getText().toString());
                                        user.setPhone(mt_phone.getText().toString());
                                        user.setEmail(mt_mail.getText().toString());
                                        user.setPassword(mt_pass.getText().toString());

                                        firebaseAuth.getCurrentUser().updatePassword(mt_pass.getText().toString());
                                        firebaseAuth.getCurrentUser().updateEmail(mt_mail.getText().toString());

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
        tv_phone.setText(user.getPhone());
        tv_email.setText(user.getEmail());
        tv_name.setText(user.getName());
        tv_name_title.setText(user.getName());


    }

}
