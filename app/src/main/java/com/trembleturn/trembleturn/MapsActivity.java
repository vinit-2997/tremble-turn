package com.trembleturn.trembleturn;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Point;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
//import android.location.LocationListener;
import android.os.Build;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.location.LocationListener;

import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;
import com.trembleturn.trembleturn.POJO.Routes;
import com.trembleturn.trembleturn.POJO.Steps;
import com.trembleturn.trembleturn.webservice.ApiRouter;
import com.trembleturn.trembleturn.webservice.ApiRoutes;
import com.trembleturn.trembleturn.webservice.ErrorType;
import com.trembleturn.trembleturn.webservice.OnResponseListener;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener, OnResponseListener
{
    public static String TAG = MainActivity.class.getSimpleName();

    public GoogleMap mMap;
    private GoogleApiClient client;
    private LocationRequest locationRequest;
    private Location lastLocation;
    private Marker currentLocationMarker;
    public static final int REQUEST_LOCATION_CODE = 99;
    public LatLng latlng;
    public static String instructions = new String();


    String[] latlong =  "-34.8799074,174.7565664".split(",");
    double latitude = Double.parseDouble(latlong[0]);
    double longitude = Double.parseDouble(latlong[1]);

    LatLng location = new LatLng(latitude, longitude);

    TextView directions = (TextView)findViewById(R.id.directions);


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        if(Build.VERSION.SDK_INT >=  Build.VERSION_CODES.M)
        {
            checkLocationPermission();
        }
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

//        animateMarker(currentLocationMarker, location, false);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch(requestCode)
        {
            case REQUEST_LOCATION_CODE:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
                    {
                        if(client == null)
                        {
                            buildGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled(true);
                    }
                }

                else // permission denied
                {
                    Toast.makeText(this, "Permission Denied !!", Toast.LENGTH_LONG).show();

                }
                return;
        }
    }




    public static void setAnimation(GoogleMap myMap, final List<LatLng> directionPoint, final List<String> man, final List<String> instr)
    {
        Marker marker = myMap.addMarker(new MarkerOptions()
                .position(directionPoint.get(0))
                .flat(true));

        myMap.animateCamera(CameraUpdateFactory.newLatLngZoom(directionPoint.get(0), 10));

        animateMarker(myMap, marker, directionPoint, man, instr, false);
    }


    private static void animateMarker(final GoogleMap myMap, final Marker marker, final List<LatLng> directionPoint,final List<String> man,final List<String> instr, final boolean hideMarker)
    {
        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        Projection proj = myMap.getProjection();
        final long duration = 30000;

        final Interpolator interpolator = new LinearInterpolator();

        handler.post(new Runnable()
        {
            int i = 0;


            @Override
            public void run()
            {
                long elapsed = SystemClock.uptimeMillis() - start;
                float t = interpolator.getInterpolation((float) elapsed / duration);
                if (i < directionPoint.size())
                {

                    marker.setPosition(directionPoint.get(i));
                    myMap.animateCamera(CameraUpdateFactory.newLatLng(directionPoint.get(i)));

                    instructions= instr.get(i);

                    if(man.get(i).contains("right")){

                    }
                    else if(man.get(i).contains("left")){

                    }

                    else{

                    }
                     Log.d("maneuver", man.get(i) );



                }
                //myMap.animateCamera(CameraUpdateFactory.zoomTo(2.0f));

                i++;



                if (t < 1.0) {
                    // Post again 16ms later.
                    handler.postDelayed(this, 1000);
                } else {
                    if (hideMarker) {
                        marker.setVisible(false);
                    } else {
                        marker.setVisible(true);
                    }
                }
            }
        });
    }

    protected synchronized void buildGoogleApiClient()
    {
        client = new GoogleApiClient.Builder(this).
                addConnectionCallbacks(this).
                addOnConnectionFailedListener(this).
                addApi(LocationServices.API).
                build();

        client.connect();
    }

    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        mMap = googleMap;

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED )
        {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }
    }


    @Override
    public void onLocationChanged(Location location)
    {
        lastLocation = location;

        if(currentLocationMarker!=null)
        {
            currentLocationMarker.remove();
        }

        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Current Location");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));

        currentLocationMarker = mMap.addMarker(markerOptions);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomBy(10));

        if(client!=null)
        {
            LocationServices.FusedLocationApi.removeLocationUpdates(client, this);
        }


    }

    public void OnClick(View v)
    {
        if(v.getId() == R.id.B_search)
        {
            EditText tf_location = (EditText)findViewById(R.id.TF_tocation);
            String location = tf_location.getText().toString();
            List<Address> addressList = null;
            MarkerOptions mo = new MarkerOptions();

            if(!location.equals(""))
            {
                Geocoder geocoder = new Geocoder(this);
                try {
                    addressList=geocoder.getFromLocationName(location, 5);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                for(int i=0; i<addressList.size(); i++)
                {
                    Address myAddress = addressList.get(i);
//                    LatLng latlng = new LatLng(myAddress.getLatitude(), myAddress.getLongitude());
                      latlng = new LatLng(myAddress.getLatitude(), myAddress.getLongitude());

                    mo.position(latlng);
                    mo.title("Your Search Result !");
                    mMap.addMarker(mo);
                    mMap.animateCamera(CameraUpdateFactory.newLatLng(latlng));

                }
            }
        }
    }

    public void setPoints(View v)
    {
        if(v.getId() == R.id.setPoints)
        {
            MarkerOptions mo = new MarkerOptions();

            List<LatLng> zz = new ArrayList<LatLng>();


                try
                {
                    LatLng source = new LatLng(19.0269, 72.8553);
                    new ApiRouter(this, this, ApiRoutes.RC_A2B_STEPS, TAG)
                            .makeStringGetRequest(ApiRoutes.getA2BRequestUrl(source, latlng));
                }

                catch (Exception e)
                {
                    e.printStackTrace();
                }


//
//            Intent intent = getIntent();
//            Bundle b = intent.getExtras();
//
//            String message = intent.getStringExtra("dataFromMain");
//
//            Bundle extras = getIntent().getExtras();
//            if(extras != null)
//            {
//                //List<LatLng> j =  (List<LatLng>)extras.get("name");
//                Routes routes = (Routes)extras.get("name");
//                int sizeOfRoute = routes.legs.get(0).steps.size();
//
//                int i;
//                List<LatLng> latlnglist = new ArrayList<LatLng>();
//
//                for(i=0; i<sizeOfRoute; i++)
//                {
//                    double lat1 = routes.legs.get(0).steps.get(0).end_location.lat;
//                    double lng1 = routes.legs.get(0).steps.get(0).end_location.lng;
//                    latlnglist.add(new LatLng(lat1, lng1));
//                }
//                setAnimation(mMap,latlnglist);
//
//            }
        }
    }

    public void onSuccess(int requestCode, JSONObject response)
    {
        switch (requestCode)
        {
            case ApiRoutes.RC_A2B_STEPS:
                try
                {
                    Routes routes = new Gson().fromJson(response.getJSONArray("routes").get(0).toString(), Routes.class);
                    String []pathdisplay=getPaths(routes);
                    directiondisplay(pathdisplay);

                    int sizeOfRoute = routes.legs.get(0).steps.size();



                    int i;
                    List<LatLng> latlnglist = new ArrayList<LatLng>();
                    List<String> manevuer = new ArrayList<String>();
                    List<String> instr = new ArrayList<String>();



                    for(i=0; i<sizeOfRoute; i++)
                    {
                        double lat1 = routes.legs.get(0).steps.get(i).end_location.lat;
                        double lng1 = routes.legs.get(0).steps.get(i).end_location.lng;
                        latlnglist.add(new LatLng(lat1, lng1));

                        String m2 = routes.legs.get(0).steps.get(i).htmlinstructions;
                        instr.add(m2);


                        String m1 = routes.legs.get(0).steps.get(i).maneuver;
                        if(m1== null)
                            manevuer.add("Keep going");
                        else
                            manevuer.add(m1);

                    }

                    Log.i(TAG,latlnglist.toString());

                    setAnimation(mMap,latlnglist, manevuer, instr);
                }

                catch (Exception e)
                {
                    e.printStackTrace();
                }
                break;

            case ApiRoutes.RC_BAND_LEFT_HALF:
                Log.i(TAG, "Vibrate left band successful");
                break;
            case ApiRoutes.RC_BAND_RIGHT_HALF:
                Log.i(TAG, "Vibrate right band successful");
                break;
        }
    }

    public void onError(int requestCode, ErrorType errorType, JSONObject response) {

    }

    @Override
    public void onConnected(@Nullable Bundle bundle)
    {
        locationRequest = new LocationRequest();

        locationRequest.setInterval(1000);
        locationRequest.setFastestInterval(1000);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
        {
            LocationServices.FusedLocationApi.requestLocationUpdates(client, locationRequest, this);
        }
    }

    public boolean checkLocationPermission()
    {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION))
            {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_CODE);

            }

            else
            {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_CODE);


            }
            return false;
        }

        else
        {
            return true;
        }

    }

    @Override
    public void onConnectionSuspended(int i)
    {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult)
    {

    }

    public void showtoast(String show)
    {
        Toast.makeText(this, show, Toast.LENGTH_SHORT).show();
    }


    private void vibrateLeft()
    {
        try
        {
            new ApiRouter(this, this, ApiRoutes.RC_BAND_LEFT_HALF, TAG)
                    .makeStringGetRequest(ApiRoutes.BAND_LEFT_HALF);
        }

        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void vibrateRight()
    {
        try
        {
            new ApiRouter(this, this, ApiRoutes.RC_BAND_RIGHT_HALF, TAG)
                    .makeStringGetRequest(ApiRoutes.BAND_RIGHT_HALF);
        }

        catch (Exception e)
        {
            e.printStackTrace();
        }
    }


    public String[] getPaths(Routes route){

        List<Steps> step = route.legs.get(0).steps;
        String routes[] = new String[step.size()];
        for(int i=0;i<step.size();i++){

            routes[i]=getPath(step.get(i));
        }

        return routes;
    }

    public String getPath(Steps step){

        String polyline = step.polyline.points;
        return polyline;
    }

    public void directiondisplay(String[] path){


        for(int i =0 ; i<path.length ; i++){



            PolylineOptions options = new PolylineOptions();
            options.color(Color.RED);
            options.width(10);
            options.addAll(decode(path[i]));

            mMap.addPolyline(options);

        }

    }


    public static List<LatLng> decode(final String encodedPath) {
        int len = encodedPath.length();

        // For speed we preallocate to an upper bound on the final length, then
        // truncate the array before returning.
        final List<LatLng> path = new ArrayList<LatLng>();
        int index = 0;
        int lat = 0;
        int lng = 0;

        while (index < len) {
            int result = 1;
            int shift = 0;
            int b;
            do {
                b = encodedPath.charAt(index++) - 63 - 1;
                result += b << shift;
                shift += 5;
            } while (b >= 0x1f);
            lat += (result & 1) != 0 ? ~(result >> 1) : (result >> 1);

            result = 1;
            shift = 0;
            do {
                b = encodedPath.charAt(index++) - 63 - 1;
                result += b << shift;
                shift += 5;
            } while (b >= 0x1f);
            lng += (result & 1) != 0 ? ~(result >> 1) : (result >> 1);

            path.add(new LatLng(lat * 1e-5, lng * 1e-5));
        }

        return path;
    }

}































//
//package com.example.Maps;
//
//import android.content.DialogInterface;
//import android.content.pm.PackageManager;
//import android.location.Location;
//import android.os.Bundle;
//import android.support.annotation.NonNull;
//import android.support.v4.app.ActivityCompat;
//import android.support.v4.content.ContextCompat;
//import android.support.v7.app.AlertDialog;
//import android.support.v7.app.AppCompatActivity;
//import android.util.Log;
//import android.view.Menu;
//import android.view.MenuItem;
//import android.view.View;
//import android.widget.FrameLayout;
//import android.widget.TextView;
//
//import com.google.android.gms.location.FusedLocationProviderClient;
//import com.google.android.gms.location.LocationServices;
//import com.google.android.gms.location.places.GeoDataClient;
//import com.google.android.gms.location.places.PlaceDetectionClient;
//import com.google.android.gms.location.places.PlaceLikelihood;
//import com.google.android.gms.location.places.PlaceLikelihoodBufferResponse;
//import com.google.android.gms.location.places.Places;
//import com.google.android.gms.maps.CameraUpdateFactory;
//import com.google.android.gms.maps.GoogleMap;
//import com.google.android.gms.maps.OnMapReadyCallback;
//import com.google.android.gms.maps.SupportMapFragment;
//import com.google.android.gms.maps.model.CameraPosition;
//import com.google.android.gms.maps.model.LatLng;
//import com.google.android.gms.maps.model.Marker;
//import com.google.android.gms.maps.model.MarkerOptions;
//import com.google.android.gms.tasks.OnCompleteListener;
//import com.google.android.gms.tasks.Task;
//
///**
// * An activity that displays a map showing the place at the device's current location.
// */
//public class MapsActivityCurrentPlace extends AppCompatActivity
//        implements OnMapReadyCallback {
//
//    private static final String TAG = MapsActivityCurrentPlace.class.getSimpleName();
//    private GoogleMap mMap;
//    private CameraPosition mCameraPosition;
//
//    // The entry points to the Places API.
//    private GeoDataClient mGeoDataClient;
//    private PlaceDetectionClient mPlaceDetectionClient;
//
//    // The entry point to the Fused Location Provider.
//    private FusedLocationProviderClient mFusedLocationProviderClient;
//
//    // A default location (Sydney, Australia) and default zoom to use when location permission is
//    // not granted.
//    private final LatLng mDefaultLocation = new LatLng(-33.8523341, 151.2106085);
//    private static final int DEFAULT_ZOOM = 15;
//    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
//    private boolean mLocationPermissionGranted;
//
//    // The geographical location where the device is currently located. That is, the last-known
//    // location retrieved by the Fused Location Provider.
//    private Location mLastKnownLocation;
//
//    // Keys for storing activity state.
//    private static final String KEY_CAMERA_POSITION = "camera_position";
//    private static final String KEY_LOCATION = "location";
//
//    // Used for selecting the current place.
//    private static final int M_MAX_ENTRIES = 5;
//    private String[] mLikelyPlaceNames;
//    private String[] mLikelyPlaceAddresses;
//    private String[] mLikelyPlaceAttributions;
//    private LatLng[] mLikelyPlaceLatLngs;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        // Retrieve location and camera position from saved instance state.
//        if (savedInstanceState != null) {
//            mLastKnownLocation = savedInstanceState.getParcelable(KEY_LOCATION);
//            mCameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION);
//        }
//
//        // Retrieve the content view that renders the map.
//        setContentView(R.layout.activity_maps);
//
//        // Construct a GeoDataClient.
//        mGeoDataClient = Places.getGeoDataClient(this, null);
//
//        // Construct a PlaceDetectionClient.
//        mPlaceDetectionClient = Places.getPlaceDetectionClient(this, null);
//
//        // Construct a FusedLocationProviderClient.
//        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
//
//        // Build the map.
//        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
//                .findFragmentById(R.id.map);
//        mapFragment.getMapAsync(this);
//
//    }
//
//    /**
//     * Saves the state of the map when the activity is paused.
//     */
//    @Override
//    protected void onSaveInstanceState(Bundle outState) {
//        if (mMap != null) {
//            outState.putParcelable(KEY_CAMERA_POSITION, mMap.getCameraPosition());
//            outState.putParcelable(KEY_LOCATION, mLastKnownLocation);
//            super.onSaveInstanceState(outState);
//        }
//    }
//
//    /**
//     * Sets up the options menu.
//     * @param menu The options menu.
//     * @return Boolean.
//     */
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.current_place_menu, menu);
//        return true;
//    }
//
//    /**
//     * Handles a click on the menu option to get a place.
//     * @param item The menu item to handle.
//     * @return Boolean.
//     */
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        if (item.getItemId() == R.id.option_get_place) {
//            showCurrentPlace();
//        }
//        return true;
//    }
//
//    /**
//     * Manipulates the map when it's available.
//     * This callback is triggered when the map is ready to be used.
//     */
//    @Override
//    public void onMapReady(GoogleMap map) {
//        mMap = map;
//
//        // Use a custom info window adapter to handle multiple lines of text in the
//        // info window contents.
//        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
//
//            @Override
//            // Return null here, so that getInfoContents() is called next.
//            public View getInfoWindow(Marker arg0) {
//                return null;
//            }
//
//            @Override
//            public View getInfoContents(Marker marker) {
//                // Inflate the layouts for the info window, title and snippet.
//                View infoWindow = getLayoutInflater().inflate(R.layout.custom_info_contents,
//                        (FrameLayout) findViewById(R.id.map), false);
//
//                TextView title = ((TextView) infoWindow.findViewById(R.id.title));
//                title.setText(marker.getTitle());
//
//                TextView snippet = ((TextView) infoWindow.findViewById(R.id.snippet));
//                snippet.setText(marker.getSnippet());
//
//                return infoWindow;
//            }
//        });
//
//        // Prompt the user for permission.
//        getLocationPermission();
//
//        // Turn on the My Location layer and the related control on the map.
//        updateLocationUI();
//
//        // Get the current location of the device and set the position of the map.
//        getDeviceLocation();
//    }
//
//    /**
//     * Gets the current location of the device, and positions the map's camera.
//     */
//    private void getDeviceLocation() {
//        /*
//         * Get the best and most recent location of the device, which may be null in rare
//         * cases when a location is not available.
//         */
//        try {
//            if (mLocationPermissionGranted) {
//                Task<Location> locationResult = mFusedLocationProviderClient.getLastLocation();
//                locationResult.addOnCompleteListener(this, new OnCompleteListener<Location>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Location> task) {
//                        if (task.isSuccessful()) {
//                            // Set the map's camera position to the current location of the device.
//                            mLastKnownLocation = task.getResult();
//                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
//                                    new LatLng(mLastKnownLocation.getLatitude(),
//                                            mLastKnownLocation.getLongitude()), DEFAULT_ZOOM));
//                        } else {
//                            Log.d(TAG, "Current location is null. Using defaults.");
//                            Log.e(TAG, "Exception: %s", task.getException());
//                            mMap.moveCamera(CameraUpdateFactory
//                                    .newLatLngZoom(mDefaultLocation, DEFAULT_ZOOM));
//                            mMap.getUiSettings().setMyLocationButtonEnabled(false);
//                        }
//                    }
//                });
//            }
//        } catch (SecurityException e)  {
//            Log.e("Exception: %s", e.getMessage());
//        }
//    }
//
//
//    /**
//     * Prompts the user for permission to use the device location.
//     */
//    private void getLocationPermission() {
//        /*
//         * Request location permission, so that we can get the location of the
//         * device. The result of the permission request is handled by a callback,
//         * onRequestPermissionsResult.
//         */
//        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
//                android.Manifest.permission.ACCESS_FINE_LOCATION)
//                == PackageManager.PERMISSION_GRANTED) {
//            mLocationPermissionGranted = true;
//        } else {
//            ActivityCompat.requestPermissions(this,
//                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
//                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
//        }
//    }
//
//    /**
//     * Handles the result of the request for location permissions.
//     */
//    @Override
//    public void onRequestPermissionsResult(int requestCode,
//                                           @NonNull String permissions[],
//                                           @NonNull int[] grantResults) {
//        mLocationPermissionGranted = false;
//        switch (requestCode) {
//            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
//                // If request is cancelled, the result arrays are empty.
//                if (grantResults.length > 0
//                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    mLocationPermissionGranted = true;
//                }
//            }
//        }
//        updateLocationUI();
//    }
//
//    /**
//     * Prompts the user to select the current place from a list of likely places, and shows the
//     * current place on the map - provided the user has granted location permission.
//     */
//    private void showCurrentPlace() {
//        if (mMap == null) {
//            return;
//        }
//
//        if (mLocationPermissionGranted) {
//            // Get the likely places - that is, the businesses and other points of interest that
//            // are the best match for the device's current location.
//            @SuppressWarnings("MissingPermission") final
//            Task<PlaceLikelihoodBufferResponse> placeResult =
//                    mPlaceDetectionClient.getCurrentPlace(null);
//            placeResult.addOnCompleteListener
//                    (new OnCompleteListener<PlaceLikelihoodBufferResponse>() {
//                        @Override
//                        public void onComplete(@NonNull Task<PlaceLikelihoodBufferResponse> task) {
//                            if (task.isSuccessful() && task.getResult() != null) {
//                                PlaceLikelihoodBufferResponse likelyPlaces = task.getResult();
//
//                                // Set the count, handling cases where less than 5 entries are returned.
//                                int count;
//                                if (likelyPlaces.getCount() < M_MAX_ENTRIES) {
//                                    count = likelyPlaces.getCount();
//                                } else {
//                                    count = M_MAX_ENTRIES;
//                                }
//
//                                int i = 0;
//                                mLikelyPlaceNames = new String[count];
//                                mLikelyPlaceAddresses = new String[count];
//                                mLikelyPlaceAttributions = new String[count];
//                                mLikelyPlaceLatLngs = new LatLng[count];
//
//                                for (PlaceLikelihood placeLikelihood : likelyPlaces) {
//                                    // Build a list of likely places to show the user.
//                                    mLikelyPlaceNames[i] = (String) placeLikelihood.getPlace().getName();
//                                    mLikelyPlaceAddresses[i] = (String) placeLikelihood.getPlace()
//                                            .getAddress();
//                                    mLikelyPlaceAttributions[i] = (String) placeLikelihood.getPlace()
//                                            .getAttributions();
//                                    mLikelyPlaceLatLngs[i] = placeLikelihood.getPlace().getLatLng();
//
//                                    i++;
//                                    if (i > (count - 1)) {
//                                        break;
//                                    }
//                                }
//
//                                // Release the place likelihood buffer, to avoid memory leaks.
//                                likelyPlaces.release();
//
//                                // Show a dialog offering the user the list of likely places, and add a
//                                // marker at the selected place.
//                                openPlacesDialog();
//
//                            } else {
//                                Log.e(TAG, "Exception: %s", task.getException());
//                            }
//                        }
//                    });
//        } else {
//            // The user has not granted permission.
//            Log.i(TAG, "The user did not grant location permission.");
//
//            // Add a default marker, because the user hasn't selected a place.
//            mMap.addMarker(new MarkerOptions()
//                    .title(getString(R.string.default_info_title))
//                    .position(mDefaultLocation)
//                    .snippet(getString(R.string.default_info_snippet)));
//
//            // Prompt the user for permission.
//            getLocationPermission();
//        }
//    }
//
//    /**
//     * Displays a form allowing the user to select a place from a list of likely places.
//     */
//    private void openPlacesDialog() {
//        // Ask the user to choose the place where they are now.
//        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                // The "which" argument contains the position of the selected item.
//                LatLng markerLatLng = mLikelyPlaceLatLngs[which];
//                String markerSnippet = mLikelyPlaceAddresses[which];
//                if (mLikelyPlaceAttributions[which] != null) {
//                    markerSnippet = markerSnippet + "\n" + mLikelyPlaceAttributions[which];
//                }
//
//                // Add a marker for the selected place, with an info window
//                // showing information about that place.
//                mMap.addMarker(new MarkerOptions()
//                        .title(mLikelyPlaceNames[which])
//                        .position(markerLatLng)
//                        .snippet(markerSnippet));
//
//                // Position the map's camera at the location of the marker.
//                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(markerLatLng,
//                        DEFAULT_ZOOM));
//            }
//        };
//
//        // Display the dialog.
//        AlertDialog dialog = new AlertDialog.Builder(this)
//                .setTitle(R.string.pick_place)
//                .setItems(mLikelyPlaceNames, listener)
//                .show();
//    }
//
//    /**
//     * Updates the map's UI settings based on whether the user has granted location permission.
//     */
//    private void updateLocationUI() {
//        if (mMap == null) {
//            return;
//        }
//        try {
//            if (mLocationPermissionGranted) {
//                mMap.setMyLocationEnabled(true);
//                mMap.getUiSettings().setMyLocationButtonEnabled(true);
//            } else {
//                mMap.setMyLocationEnabled(false);
//                mMap.getUiSettings().setMyLocationButtonEnabled(false);
//                mLastKnownLocation = null;
//                getLocationPermission();
//            }
//        } catch (SecurityException e)  {
//            Log.e("Exception: %s", e.getMessage());
//        }
//    }
//}
//


//            zz.add(new LatLng(19.0269, 72.8553));
//            zz.add(new LatLng(73.015137, 19.447169));
//            zz.add(new LatLng(18.87705190456764, 432.9396057128907));
//            zz.add(new LatLng(18.87900103610622, 433.06320190429693));
//            zz.add(new LatLng(18.79386786395132, 433.0625152587891));

//            zz.add(new LatLng(43.6533096, -79.3827656));
//            zz.add(new LatLng(43.6557259, -79.38373369999999));
//            zz.add(new LatLng(43.6557259, -79.38373369999999));
//            zz.add(new LatLng(43.6618361, -79.35452389999999));
//            zz.add(new LatLng(43.6557259, -79.38373369999999));
//            zz.add(new LatLng(43.66366379999999, -79.3555052));
//            zz.add(new LatLng(43.6618361, -79.35452389999999));
//            zz.add(new LatLng(43.7628257, -79.33669689999999));
//            zz.add(new LatLng(43.66366379999999, -79.3555052));
//            zz.add(new LatLng(43.7680179, -79.3292728));
//            zz.add(new LatLng(43.7628257, -79.33669689999999));
//            zz.add(new LatLng(43.7901516, -79.2235381));
//            zz.add(new LatLng(43.7680179, -79.3292728));
//            zz.add(new LatLng(43.79311999999999, -79.2162862));
//            zz.add(new LatLng(43.7901516, -79.2235381));
//            zz.add(new LatLng(43.7901516, -79.2235381));


//
//            zz.add(new LatLng(-35.27801,149.12958));
//            zz.add(new LatLng(-35.28032,149.12907));
//            zz.add(new LatLng(-35.28099,149.12929));
//            zz.add(new LatLng(-35.28144,149.12984));
//            zz.add(new LatLng(-35.28194,149.13003));
//            zz.add(new LatLng(-35.28282,149.12956));
//            zz.add(new LatLng(-35.28302,149.12881));
//            zz.add(new LatLng(-35.28473,149.12836));






//            String[] latlong =  "19.0269, 72.8553".split(",");
//            double latitude = Double.parseDouble(latlong[0]);
//            double longitude = Double.parseDouble(latlong[1]);
//
//            LatLng location = new LatLng(latitude, longitude);
//            mo.position(location);
//            mo.title("Your Search Result !");
//            Marker aa=mMap.addMarker(mo);
//            mMap.animateCamera(CameraUpdateFactory.newLatLng(location));
//            public void getAtoBSteps(LatLng source, LatLng dest)
//            {



//    public void animateMarker(final Marker marker, final LatLng toPosition, final boolean hideMarker)
//    {

//        final Handler handler = new Handler();
//        final long start = SystemClock.uptimeMillis();
//        Projection proj = mMap.getProjection();
//        Point startPoint = proj.toScreenLocation(marker.getPosition());
//        final LatLng startLatLng = proj.fromScreenLocation(startPoint);
//        final long duration = 500;
//
//        final Interpolator interpolator = new LinearInterpolator();
//
//        handler.post(new Runnable()
//        {
//            @Override
//            public void run()
//            {
//                long elapsed = SystemClock.uptimeMillis() - start;
//                float t = interpolator.getInterpolation((float) elapsed / duration);
//                double lng = t * toPosition.longitude + (1 - t) * startLatLng.longitude;
//                double lat = t * toPosition.latitude + (1 - t) * startLatLng.latitude;
//                marker.setPosition(new LatLng(lat, lng));
//
//                if (t < 1.0)
//                {
//                    // Post again 16ms later.
//                    handler.postDelayed(this, 16);
//                }
//
//                else
//                {
//                    if (hideMarker)
//                    {
//                        marker.setVisible(false);
//                    }
//                    else
//                    {
//                        marker.setVisible(true);
//                    }
//                }
//            }
//        });
//   }
