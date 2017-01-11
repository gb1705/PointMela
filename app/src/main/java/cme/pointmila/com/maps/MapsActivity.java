package cme.pointmila.com.maps;

import android.content.Intent;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;

import cme.pointmila.com.election.models.ElectionCenter;

public class MapsActivity extends SupportMapFragment implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        GoogleMap.OnInfoWindowClickListener,
        GoogleMap.OnMapLongClickListener,
        GoogleMap.OnMapClickListener,
        GoogleMap.OnMarkerClickListener {

    /*double lat = 19.1136;
    double lng = 72.8697;

    double lat1 = 19.1551;
    double lng1 = 72.8679;

    double lat2 = 19.0213;
    double lng2 = 72.8424;*/


    private GoogleApiClient mGoogleApiClient;
    private Location mCurrentLocation;

    private final int[] MAP_TYPES = {GoogleMap.MAP_TYPE_SATELLITE,
            GoogleMap.MAP_TYPE_NORMAL,
            GoogleMap.MAP_TYPE_HYBRID,
            GoogleMap.MAP_TYPE_TERRAIN,
            GoogleMap.MAP_TYPE_NONE};
    private int curMapTypeIndex = 1;

    GoogleMap mMap;


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setHasOptionsMenu(true);
//        mMap = ((SupportMapFragment) getActivity().getSupportFragmentManager().findFragmentById(map))
//                .getMap();

        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        initListeners();
        getArgumentsDataFromBundle();
    }

    private void initListeners() {
        getMap().setOnMarkerClickListener(this);
        getMap().setOnMapLongClickListener(this);
        getMap().setOnInfoWindowClickListener(this);
        getMap().setOnMapClickListener(this);
        getMap().getUiSettings().setMapToolbarEnabled(true);

    }

    private void getArgumentsDataFromBundle() {
        ArrayList<ElectionCenter> electionCenters = getArguments().getParcelableArrayList("electionCenters");
        if (electionCenters != null)
            getMarkeroption(electionCenters);
    }

    @Override
    public void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        mCurrentLocation = LocationServices
                .FusedLocationApi
                .getLastLocation(mGoogleApiClient);
        try {
            initCamera(mCurrentLocation);
        } catch (Exception e) {
            e.printStackTrace();
            ArrayList<ElectionCenter> electionCenters = getArguments().getParcelableArrayList("electionCenters");
            if (electionCenters != null) {
                LatLng latLng = new LatLng(Double.parseDouble(electionCenters.get(0).getCenterLatitude()), Double.parseDouble(electionCenters.get(0).getCenterLongitude()));
                initCamera(latLng);
            }
        }
    }

    private void initCamera(LatLng latLng) {

        try {


            CameraPosition position = CameraPosition.builder()
                    .target(new LatLng(latLng.latitude,
                            latLng.longitude))
                    .zoom(10f)
                    .bearing(0.0f)
                    .tilt(0.0f)
                    .build();

            getMap().animateCamera(CameraUpdateFactory
                    .newCameraPosition(position), null);

            getMap().setMapType(MAP_TYPES[curMapTypeIndex]);
            getMap().setTrafficEnabled(true);
            getMap().setMyLocationEnabled(true);
        } catch (Exception c) {

        }
    }

    private void initCamera(Location location) {
        try {


            CameraPosition position = CameraPosition.builder()
                    .target(new LatLng(location.getLatitude(),
                            location.getLongitude()))
                    .zoom(10f)
                    .bearing(0.0f)
                    .tilt(0.0f)
                    .build();

            getMap().animateCamera(CameraUpdateFactory
                    .newCameraPosition(position), null);

            getMap().setMapType(MAP_TYPES[curMapTypeIndex]);
            getMap().setTrafficEnabled(true);
            getMap().setMyLocationEnabled(true);
        } catch (Exception e) {

        }
    }

    @Override
    public void onMapClick(LatLng latLng) {

//        MarkerOptions options = new MarkerOptions().position(latLng);
//        options.title(getAddressFromLatLng(latLng));
//
//        options.icon(BitmapDescriptorFactory.defaultMarker());
//        getMap().addMarker(options);
    }


    private String getAddressFromLatLng(LatLng latLng) {
        Geocoder geocoder = new Geocoder(getActivity());

        String address = "";
        try {
            address = geocoder
                    .getFromLocation(latLng.latitude, latLng.longitude, 1)
                    .get(0).getAddressLine(0);
        } catch (IOException e) {
        }

        return address;
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        marker.showInfoWindow();
        getMap().getUiSettings().setMapToolbarEnabled(true);
        String uriString = "google.navigation:q=" + marker.getPosition().latitude + "," + marker.getPosition().longitude + "";
        Uri gmmIntentUri = Uri.parse(uriString);
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        if (mapIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivity(mapIntent);
        }

        return true;
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onInfoWindowClick(Marker marker) {

    }


    @Override
    public void onMapLongClick(LatLng latLng) {

    }

    public void getMarkeroption(ArrayList<ElectionCenter> electionCenters) {

        for (int i = 0; i < electionCenters.size(); i++) {
            ElectionCenter electionCenter = electionCenters.get(i);
            getMap().addMarker(new MarkerOptions()
                    .position(new LatLng(Double.parseDouble(electionCenter.getCenterLatitude()), Double.parseDouble(electionCenter.getCenterLongitude())))
                    .snippet("MMC 2016")
                    .title(electionCenter.getCenterDescription())).showInfoWindow();
        }
    }


}