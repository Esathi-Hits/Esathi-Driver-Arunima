package com.e_sathi.driver;
import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/*import com.e_sathidriver.Direction;
import com.e_sathidriver.R;
import com.e_sathidriver.TransparentProgressDialog;
import com.google.android.material.navigation.NavigationView; */
import com.mapbox.android.core.location.LocationEngine;
import com.mapbox.android.core.location.LocationEngineListener;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.LocationComponentOptions;
import com.mapbox.mapboxsdk.location.modes.CameraMode;
import com.mapbox.mapboxsdk.location.modes.RenderMode;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.style.sources.GeoJsonOptions;
import com.mapmyindia.sdk.plugin.annotation.LineManager;
import com.mapmyindia.sdk.plugin.annotation.LineOptions;
import com.mmi.services.api.autosuggest.model.AutoSuggestAtlasResponse;
import com.mmi.services.api.autosuggest.model.ELocation;
import com.mmi.services.api.directions.DirectionsCriteria;
import com.mmi.services.api.distance.MapmyIndiaDistanceMatrix;
import com.mmi.services.api.distance.models.DistanceResponse;
import com.mmi.services.api.distance.models.DistanceResults;
import com.mmi.services.api.textsearch.MapmyIndiaTextSearch;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity  extends Fragment implements OnMapReadyCallback, LocationEngineListener, TextWatcher, TextView.OnEditorActionListener {
    private static final int MY_PERMISSIONS_REQUEST_FINE_LOCATION = 100;
    private LocationComponent locationComponent;
    private MapboxMap mapmyIndiaMaps;
    private LocationEngine locationEngine;
    private MapView mapView;
    private ImageView iv1;
    private List<LatLng> listOfLatLng;
    private EditText autoSuggestText;
    private EditText pickupLocationText;
    private RecyclerView recyclerView;
    private LineManager lineManager;
    private LinearLayoutManager mLayoutManager;
    private TransparentProgressDialog transparentProgressDialog;
    private Handler handler;
    private int tp=0;
    private String placename1;
    private TextView tvDistance, tvDuration,tvplacename;
    private LinearLayout directionDetailsLayout;
    private double lat1,long1, lat2=0 ,long2=0;
    private int vt,tb=0;

    @SuppressLint("ClickableViewAccessibility")
    /*@Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homedrawer);
        mapView = findViewById(R.id.map_view);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        initReferences();
        initListeners();
        autoSuggestText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                tp = 0;
                return false;
            }
        });
    }

     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_home, container, false);
        mapView = view.findViewById(R.id.map_view1);
        AppCompatImageButton currentlocation=view.findViewById(R.id.current_location);
        AppCompatImageButton locationof=view.findViewById(R.id.location_empty);
        AppCompatButton dir=view.findViewById(R.id.direct);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);


        initReferences(view);
        initListeners();
        autoSuggestText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                tp = 0;
                return false;
            }
        });
        /*pickupLocationText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(
                        lat1, long1)).zoom(16).tilt(0).build();
                mapmyIndiaMaps.setCameraPosition(cameraPosition);
                tp = 0;
                return false;
            }
        });*/
        currentlocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(
                        lat1, long1)).zoom(16).tilt(0).build();
                mapmyIndiaMaps.setCameraPosition(cameraPosition);
            }
        });
        locationof.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mapmyIndiaMaps.clear();
                if(lineManager != null) {
                    lineManager.clearAll();
                    tb=0;
                    directionDetailsLayout.setVisibility(View.GONE);
                }
            }
        });
        mapView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recyclerView.setVisibility(View.INVISIBLE);
            }
        });
        dir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Direction.class);
                Bundle b = new Bundle();
                b.putDouble("lat2", lat2);
                b.putDouble("long2", long2);
                b.putDouble("lat1",lat1);
                b.putDouble("long1",long1);
                b.putString("placename",placename1);
                intent.putExtras(b);
                startActivity(intent);
            }
        });
        return view;
    }

    /*   Toolbar toolbar = findViewById(R.id.toolbar) ;
       setSupportActionBar(toolbar) ;
       DrawerLayout drawer = findViewById(R.id.drawerlayout ) ;
       ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
               this, drawer , toolbar , R.string.navigation_drawer_open ,
               R.string.navigation_drawer_close ) ;
       drawer.addDrawerListener(toggle) ;
       toggle.syncState() ;
       NavigationView navigationView = findViewById(R.id.navview ) ;
       navigationView.setNavigationItemSelectedListener(this) ;
       if(savedInstanceState==null){
           getFragmentManager().popBackStackImmediate();
           finish();
           navigationView.setCheckedItem(R.id.home);
       }
   }

   @Override
   public void onBackPressed () {
       DrawerLayout drawer = findViewById(R.id.drawerlayout ) ;
       if (drawer.isDrawerOpen(GravityCompat. START )) {
           drawer.closeDrawer(GravityCompat. START ) ;
       } else {
           super .onBackPressed() ;
       }
   }
   @Override
   public boolean onNavigationItemSelected ( @NonNull MenuItem item) {
       // Handle navigation view item clicks here.
       int id = item.getItemId() ;
       if (id == R.id.home ) {
           getFragmentManager().popBackStackImmediate();
           finish();
       }
       else if(id == R.id.profile ) {
           getSupportFragmentManager().beginTransaction().replace(R.id.framelayout,new ProfileFragment()).commit();
       }
       else if (id == R.id.currentRide ) {
           getSupportFragmentManager().beginTransaction().replace(R.id.framelayout,new CurrentRideFragment()).commit();
       } else if (id == R.id.recentRides ) {
           getSupportFragmentManager().beginTransaction().replace(R.id.framelayout,new RecentRideFragment()).commit();
       } else if (id == R.id.signOut ) {
           getSupportFragmentManager().beginTransaction().replace(R.id.framelayout,new SignOutFragment()).commit();
       }
       else if (id == R.id.settings ) {
           getSupportFragmentManager().beginTransaction().replace(R.id.framelayout,new SettingsFragment()).commit();
       }
       else if (id == R.id.Rate ) {
           getSupportFragmentManager().beginTransaction().replace(R.id.framelayout,new RateFragment()).commit();
       }
       else if (id == R.id.Communication ) {
           getSupportFragmentManager().beginTransaction().replace(R.id.framelayout,new ContactFragment()).commit();
       }
       else if (id == R.id.aboutus ) {
           getSupportFragmentManager().beginTransaction().replace(R.id.framelayout,new AboutFragment()).commit();
       }
       DrawerLayout drawer = findViewById(R.id. drawerlayout ) ;
       drawer.closeDrawer(GravityCompat. START ) ;
       return true;
   }

     */
    private void initListeners() {
        autoSuggestText.addTextChangedListener(this);
        autoSuggestText.setOnEditorActionListener(this);
        /*pickupLocationText.addTextChangedListener(this);
        pickupLocationText.setOnEditorActionListener(this);*/
    }

    private void initReferences(View view) {
        iv1=(ImageView)view.findViewById(R.id.iv);
        iv1.setVisibility(View.INVISIBLE);
        autoSuggestText = view.findViewById(R.id.auto_suggest);
        recyclerView = view.findViewById(R.id.recyclerview);
        mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setVisibility(View.GONE);
        directionDetailsLayout = view.findViewById(R.id.Distance_direction);
        tvDistance = view.findViewById(R.id.Distance);
        tvDuration = view.findViewById(R.id.duration);
        tvplacename=view.findViewById(R.id.pl_name);
        transparentProgressDialog = new TransparentProgressDialog(getActivity(), R.drawable.circle_loader, "");
        handler = new Handler();
    }

    @Override
    public void onMapReady(MapboxMap mapmyIndiaMaps) {
        this.mapmyIndiaMaps = mapmyIndiaMaps;
        lineManager = new LineManager(mapView, mapmyIndiaMaps, null, new GeoJsonOptions().withLineMetrics(true).withBuffer(2));
        enableLocation();
    }

    @Override
    public void onMapError(int i, String s) {

    }

    private void enableLocation() {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(
                    getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_FINE_LOCATION);
            return;
        }
        LocationComponentOptions options = LocationComponentOptions.builder(getActivity())
                .trackingGesturesManagement(true)
                .accuracyColor(ContextCompat.getColor(getActivity(), R.color.teal_200))
                .foregroundDrawable(R.drawable.location_pointer)
                .build();
        locationComponent = mapmyIndiaMaps.getLocationComponent();
        locationComponent.activateLocationComponent(getActivity(), options);
        locationComponent.setLocationComponentEnabled(true);
        locationEngine = locationComponent.getLocationEngine();
        locationEngine.addLocationEngineListener(this);
        locationComponent.setCameraMode(CameraMode.TRACKING);
        locationComponent.setRenderMode(RenderMode.COMPASS);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_FINE_LOCATION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    enableLocation();

                } else {
                    iv1.setVisibility(View.VISIBLE);
                    mapView.setVisibility(View.INVISIBLE);

                }
                return;
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
        if (locationEngine != null) {
            locationEngine.removeLocationEngineListener(this);
            locationEngine.addLocationEngineListener(this);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
        if (locationEngine != null)
            locationEngine.removeLocationEngineListener(this);
    }


    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
        if (locationEngine != null) {
            locationEngine.removeLocationEngineListener(this);
            locationEngine.removeLocationUpdates();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
        if (locationEngine != null) {
            locationEngine.deactivate();
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onConnected() {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    getActivity(), // Activity
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_FINE_LOCATION);
            return;
        }
        locationEngine.requestLocationUpdates();

    }
    private String getFormattedDistance(double distance) {

        if ((distance / 1000) < 1) {
            return distance + "mtr.";
        }
        DecimalFormat decimalFormat = new DecimalFormat("#.#");
        return decimalFormat.format(distance / 1000) + "Km.";
    }

    private String getFormattedDuration(double duration) {
        long min = (long) (duration % 3600 / 60);
        long hours = (long) (duration % 86400 / 3600);
        long days = (long) (duration / 86400);
        if (days > 0L) {
            return days + " " + (days > 1L ? "Days" : "Day") + " " + hours + " " + "hr" + (min > 0L ? " " + min + " " + "min." : "");
        } else {
            return hours > 0L ? hours + " " + "hr" + (min > 0L ? " " + min + " " + "min" : "") : min + " " + "min.";
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        if(tb!=1)
            mapmyIndiaMaps.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 16));
        lat1=location.getLatitude();
        long1=location.getLongitude();
        vt=1;
        if(lat2!=0)
            polyline();

    }
    public void selectedPlace(ELocation eLocation) {
        String add = "Latitude: " + eLocation.latitude + " longitude: " + eLocation.longitude;
        tp=1;
        autoSuggestText.setText(eLocation.placeName);
        autoSuggestText.clearFocus();
        InputMethodManager in = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        in.hideSoftInputFromWindow(autoSuggestText.getWindowToken(), 0);
        addMarker(Double.parseDouble(eLocation.latitude), Double.parseDouble(eLocation.longitude) , eLocation.placeName);
        showToast(add);
    }



    private void addMarker(double latitude, double longitude , String placename) {
        mapmyIndiaMaps.clear();
        if(lineManager != null) {
            lineManager.clearAll();
        }
        lat2=latitude;
        long2=longitude;
        placename1=placename;
        mapmyIndiaMaps.addMarker(new MarkerOptions().position(new LatLng(
                latitude, longitude)).title(placename));
        CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(
                latitude, longitude)).zoom(8).tilt(0).build();
        mapmyIndiaMaps.setCameraPosition(cameraPosition);
        polyline();

    }
    private void polyline(){
        if(lineManager != null) {
            lineManager.clearAll();
            listOfLatLng = SemiCirclePointsListHelper.showCurvedPolyline(new LatLng(lat2, long2), new LatLng(lat1, long1), 50);
            LineOptions lineOptions = new LineOptions()
                    .points(listOfLatLng)
                    .lineColor("#FF0000")
                    .lineWidth(4f);
            lineManager.setLineDasharray(new Float[]{95f, 05f});
            lineManager.create(lineOptions);
            List<Point> coordinatesPoint = new ArrayList<Point>();
            coordinatesPoint.add(Point.fromLngLat(long2, lat2));
            coordinatesPoint.add(Point.fromLngLat(long1, lat1));
            if (CheckInternet.isNetworkAvailable(getContext())) {
                calculateDistance(coordinatesPoint);
                vt=0;
            } else {
                Toast.makeText(getContext(), getString(R.string.pleaseCheckInternetConnection), Toast.LENGTH_SHORT).show();
            }
        }

    }

    private void calculateDistance(List<Point> pointList) {
        if(vt!=1) {
            show();
        }
        MapmyIndiaDistanceMatrix.builder()
                .coordinates(pointList)
                .profile(DirectionsCriteria.PROFILE_DRIVING)
                .resource(DirectionsCriteria.RESOURCE_DISTANCE_ETA)
                .build()
                .enqueueCall(new Callback<DistanceResponse>() {
                    @Override
                    public void onResponse(Call<DistanceResponse> call, Response<DistanceResponse> response) {
                        hide();
                        if (response.code() == 200) {
                            if (response.body() != null) {
                                DistanceResponse legacyDistanceResponse = response.body();
                                DistanceResults distanceResults = legacyDistanceResponse.results();

                                if (distanceResults != null) {
                                    updateData(distanceResults);
                                } else {
                                    Toast.makeText(getActivity(), "Failed: " + legacyDistanceResponse.responseCode(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<DistanceResponse> call, Throwable t) {
                        hide();
                    }
                });
    }


    private void updateData(DistanceResults distanceResults) {

        directionDetailsLayout.setVisibility(View.VISIBLE);
        tb=1;
        tvDuration.setText(getFormattedDuration(distanceResults.durations().get(0)[1]));
        tvDistance.setText(getFormattedDistance(distanceResults.distances().get(0)[1]));
        tvplacename.setText(placename1);
    }
    private void callTextSearchApi(String searchString){
        MapmyIndiaTextSearch.builder()
                .query(searchString)
                .build().enqueueCall(new Callback<AutoSuggestAtlasResponse>() {
            @Override
            public void onResponse(Call<AutoSuggestAtlasResponse> call, Response<AutoSuggestAtlasResponse> response) {
                if (response.code() == 200) {
                    if (response.body() != null) {
                        ArrayList<ELocation> suggestedList = response.body().getSuggestedLocations();
                        if (suggestedList.size() > 0) {
                            recyclerView.setVisibility(View.VISIBLE);
                            AutoSuggestAdapter autoSuggestAdapter = new AutoSuggestAdapter(suggestedList, eLocation -> {
                                selectedPlace(eLocation);
                                recyclerView.setVisibility(View.GONE);
                            });
                            recyclerView.setAdapter(autoSuggestAdapter);
                        }
                    } else {
                        showToast("Not able to get value, Try again.");
                    }
                }
            }
            @Override
            public void onFailure(Call<AutoSuggestAtlasResponse> call, Throwable t) {
                showToast(t.toString());
            }
        });
    }

    private void show() {
        transparentProgressDialog.show();
    }

    private void hide() {
        transparentProgressDialog.dismiss();
    }

    private void showToast(String msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        handler.postDelayed(() -> {
            recyclerView.setVisibility(View.GONE);
            if (s.length() < 3)
                recyclerView.setAdapter(null);

            if (s != null && s.toString().trim().length() < 2) {
                recyclerView.setAdapter(null);
                return;
            }
            if (s.length() > 3 && tp==1)
                recyclerView.setAdapter(null);

            if (s.length() > 2 && tp==0) {
                if (CheckInternet.isNetworkAvailable(getContext())) {
                    callTextSearchApi(s.toString());
                } else {
                    showToast(getString(R.string.pleaseCheckInternetConnection));
                }
            }
        }, 3);

    }

    @Override
    public void afterTextChanged(Editable s) {
    }


    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId== EditorInfo.IME_ACTION_SEARCH){
            callTextSearchApi(v.getText().toString());
            autoSuggestText.clearFocus();
            InputMethodManager in = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            in.hideSoftInputFromWindow(autoSuggestText.getWindowToken(), 0);
            return true;
        }
        return false;
    }

   /* public void hide1(View view) {
        recyclerView.setVisibility(View.INVISIBLE);
    }

    public void location_off(View view) {
        mapmyIndiaMaps.clear();
        if(lineManager != null) {
            lineManager.clearAll();
            tb=0;
            directionDetailsLayout.setVisibility(View.GONE);
        }
    }

    public void current(View view) {
        CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(
                lat1, long1)).zoom(16).tilt(0).build();
        mapmyIndiaMaps.setCameraPosition(cameraPosition);
    }

    public void Direction(View view) {
        Intent intent = new Intent(getActivity(), Direction.class);
        Bundle b = new Bundle();
        b.putDouble("lat2", lat2);
        b.putDouble("long2", long2);
        b.putDouble("lat1",lat1);
        b.putDouble("long1",long1);
        b.putString("placename",placename1);
        intent.putExtras(b);
        startActivity(intent);
    }

    */
}
