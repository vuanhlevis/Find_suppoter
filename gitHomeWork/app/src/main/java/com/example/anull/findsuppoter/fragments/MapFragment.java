package com.example.anull.findsuppoter.fragments;


import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.anull.findsuppoter.R;
import com.example.anull.findsuppoter.Utils.ExpandableListAdapter;
import com.example.anull.findsuppoter.Utils.MylocationListener;
import com.example.anull.findsuppoter.model.User;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.greenrobot.eventbus.EventBus;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.content.Context.LOCATION_SERVICE;

/**
 * A simple {@link Fragment} subclass.
 */
public class MapFragment extends Fragment implements OnMapReadyCallback {
    private CardView cv_choose;
    private TextView tv_chooseOption;
    private String findSupport = null;
    private Context context;

    private List<User> userList;

    // location
    private LatLng mlocation = null;
    private String mylocation = null;

    //
    private ExpandableListView listView;
    private ExpandableListAdapter listAdapter;

    private List<String> listDataheader;
    private HashMap<String, List<String>> listHash;
    private List<MarkerOptions> markerOptionsList;


    private GoogleMap gg_map;

    MapView mvmap;
    View mview;

    // database

    FirebaseAuth firebaseAuth;
    FirebaseDatabase database;
    DatabaseReference databaseReference;
    private String TAG = "map";

    public MapFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        userList = new ArrayList<>();
        markerOptionsList = new ArrayList<>();

        context = getContext();
        firebaseAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("users");
        setupMap();

    }

    private void setupMap() {
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);
        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Log.d(TAG, "onLocationChanged: " + location.getLatitude() + ", " + location.getLongitude());
                mlocation = new LatLng(location.getLatitude(), location.getLongitude());
                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(mlocation, 15);
                gg_map.animateCamera(cameraUpdate);

                mylocation = location.getLatitude() + "," + location.getLongitude();
                Log.d(TAG, "onLocationChanged: " + mlocation.toString());

                getUser();

            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 1000, locationListener);

    }


    private void setupInput() {
        cv_choose = mview.findViewById(R.id.cv_choose);
        tv_chooseOption = mview.findViewById(R.id.tv_chooseOption);

    }

    private void getUser() {
        databaseReference.orderByChild("email").equalTo(firebaseAuth.getCurrentUser().getEmail())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Log.d(TAG, "onDataChange: " + dataSnapshot);
                        if (dataSnapshot.getChildrenCount() > 0) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                Log.d(TAG, "map: snap" + snapshot);
                                User user = snapshot.getValue(User.class);

                                user.setLocation(mylocation);

                                databaseReference.child(snapshot.getKey()).setValue(user);
                            }
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mview = inflater.inflate(R.layout.fragment_map, container, false);
        setupInput();

        cv_choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                final AlertDialog.Builder dialog =
                        new AlertDialog.Builder(getContext());
                dialog.setTitle("CHOOSE OPTION");
                dialog.setMessage("You can choose only 1 option!!");

                LayoutInflater inflater = LayoutInflater.from(getContext());

                View chuyennganh_layout = inflater.inflate(R.layout.fragment_choose, null);
                listView = chuyennganh_layout.findViewById(R.id.lv_expand);
                initData();

                listAdapter = new ExpandableListAdapter(chuyennganh_layout.getContext(), listDataheader, listHash);

                listView.setAdapter(listAdapter);


                listView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
                    @Override
                    public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i1, long l) {
                        TextView tv_chooseChuyennganh = view.findViewById(R.id.tv_child);
                        findSupport = tv_chooseChuyennganh.getText().toString();

                        return true;
                    }
                });


                dialog.setPositiveButton("SELECT", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (findSupport == null) {
                            Snackbar.make(view, "You Must Be Choose An Option", Snackbar.LENGTH_LONG)
                                    .show();
                        } else
                            tv_chooseOption.setText(findSupport);

                        databaseReference.orderByChild("available").equalTo("1")
                                .addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        userList.clear();
                                        Log.d(TAG, "onDataChange: " + dataSnapshot);
                                        if (dataSnapshot.getChildrenCount() > 0) {
                                            for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {

                                                Log.d(TAG, "onDataChange: snap" + userSnapshot);
                                                User user = userSnapshot.getValue(User.class);

                                                if (user.getEmail().equals(firebaseAuth.getCurrentUser().getEmail())) {

                                                } else {
                                                    userList.add(user);
                                                }

                                            }
                                        }
                                        if (markerOptionsList.size() > 0) {
                                            for (int i = 0; i < markerOptionsList.size(); i++) {

                                            }
                                        }
                                        gg_map.clear();

                                        markerOptionsList.clear();
                                        if (userList.size() > 0) {
                                            for (int i = 0; i < userList.size(); i++) {
                                                String[] parseLocation = userList.get(i).getLocation().split(",");
                                                double lat = Double.parseDouble(parseLocation[0]);
                                                double lng = Double.parseDouble(parseLocation[1]);
                                                LatLng latLng = new LatLng(lat, lng);

                                                MarkerOptions markerOptions = new MarkerOptions()
                                                        .position(latLng);
                                                gg_map.addMarker(markerOptions);

                                                markerOptionsList.add(markerOptions);
                                            }

                                        } else {
                                            Toast.makeText(getContext(), "There is no people available your option", Toast.LENGTH_SHORT).show();
                                        }

//                                        gg_map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
//                                            @Override
//                                            public boolean onMarkerClick(Marker marker) {
//                                                if (markerOptionsList.size() > 0) {
//                                                    for (int i = 0; i < markerOptionsList.size(); i++) {
//
//                                                    }
//                                                }
//
//                                                return true;
//                                            }
//                                        });


                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });

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
        });

        return mview;
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

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mvmap = mview.findViewById(R.id.mv_map);
        if (mvmap != null) {
            mvmap.onCreate(null);
            mvmap.onResume();
            mvmap.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        MapsInitializer.initialize(getContext());
        gg_map = googleMap;

        if (ActivityCompat.checkSelfPermission(getContext()
                , Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        gg_map.setMyLocationEnabled(true);

    }


}
