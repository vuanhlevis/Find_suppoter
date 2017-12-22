package com.example.anull.findsuppoter.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.anull.findsuppoter.R;
import com.example.anull.findsuppoter.model.User;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class ChatFragment extends Fragment {

    private static final String TAG = "chat";
    FirebaseAuth firebaseAuth;
    FirebaseDatabase database;
    DatabaseReference reference;

    String crrUserphone = null;
    String detailPhone = null;

    LinearLayout layout;
    RelativeLayout layout_2;
    ImageView sendButton;
    EditText messageArea;
    ScrollView scrollView;
    Firebase reference1, reference2;
    View view;


    public ChatFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_chat, container, false);


        setupInput();

        getUser();

        Bundle bundle = this.getArguments();
        if (bundle!= null) {

            detailPhone = bundle.getString("phone");
            crrUserphone = bundle.getString("userphone");

            Log.d(TAG, "onCreateView: bundle + " + bundle.toString());
            Log.d(TAG, "onCreateView: bundle + " + detailPhone.toString());
        }


        Firebase.setAndroidContext(getContext());
        reference1 = new Firebase("https://grab-f88b4.firebaseio.com/messages/"
                + crrUserphone + "_" + detailPhone);
        reference2 = new Firebase("https://grab-f88b4.firebaseio.com/messages/" + detailPhone + "_" + crrUserphone);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messageText = messageArea.getText().toString();

                if (!messageText.equals("")) {
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("message", messageText);
                    map.put("user", crrUserphone);
                    reference1.push().setValue(map);
                    reference2.push().setValue(map);
                    messageArea.setText("");
                }
            }
        });

        reference1.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Map map = dataSnapshot.getValue(Map.class);
                String message = map.get("message").toString();
                String userName = map.get("user").toString();

                if (userName.equals(detailPhone)) {
                    addMessageBox("You:-\n" + message, 1);
                } else {
                    addMessageBox(detailPhone + ":-\n" + message, 2);
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        return view;
    }


    private void getUser() {
        reference.orderByChild("email").equalTo(firebaseAuth.getCurrentUser().getEmail())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(com.google.firebase.database.DataSnapshot dataSnapshot) {
                        Log.d(TAG, "onDataChange: " + dataSnapshot);
                        if (dataSnapshot.getChildrenCount() > 0) {
                            for (com.google.firebase.database.DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                Log.d(TAG, "map: snap" + snapshot);
                                User user = snapshot.getValue(User.class);

                                crrUserphone = user.getPhone();
                            }
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    private void setupInput() {
        firebaseAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        reference = database.getReference("users");
        layout = view.findViewById(R.id.layout1);
        layout_2 = view.findViewById(R.id.layout2);
        sendButton = view.findViewById(R.id.sendButton);
        messageArea = view.findViewById(R.id.messageArea);
        scrollView = view.findViewById(R.id.scrollView);

    }

    public void addMessageBox(String message, int type) {
        TextView textView = new TextView(getContext());
        textView.setText(message);

        LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp2.weight = 1.0f;

        if (type == 1) {
            lp2.gravity = Gravity.LEFT;
            textView.setBackgroundResource(R.drawable.bubble_in);
            textView.setTextColor(getResources().getColor(android.R.color.black));
        } else {
            lp2.gravity = Gravity.RIGHT;
            textView.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
            textView.setBackgroundResource(R.drawable.bubble_out);
        }
        textView.setLayoutParams(lp2);
        layout.addView(textView);
        scrollView.fullScroll(View.FOCUS_DOWN);
    }

}
