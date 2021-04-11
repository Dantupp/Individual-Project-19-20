package danieltupper2.com.mobilelanguagelearner;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, ActivityCompat.OnRequestPermissionsResultCallback {

    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationClient;
    private final int ACCESS_FINE_LOCATION_REQUEST_CODE = 100;
    private final LatLngBounds campusMapBounds = new LatLngBounds(new LatLng(50.933433, -1.400798),new LatLng(50.938666, -1.392537));
    private AppDatabase db;
    private AppSettings settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, ACCESS_FINE_LOCATION_REQUEST_CODE);
        }
        else{
            init();
        }
    }

    private void init(){
        setContentView(R.layout.activity_maps);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults){
        switch(requestCode){
            case ACCESS_FINE_LOCATION_REQUEST_CODE:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    init();
                } else{
                    Toast locationNeedsAcceptingMessage = Toast.makeText(getApplicationContext(),"Location must be enabled",Toast.LENGTH_LONG);
                    locationNeedsAcceptingMessage.show();
                }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.mMap = googleMap;
        db = AppDatabase.getInstance(getApplicationContext());
        settings = AppSettings.getInstance();

        mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(getApplicationContext(),R.raw.map_style));
        mMap.setMyLocationEnabled(true);
        mMap.setLatLngBoundsForCameraTarget(campusMapBounds);
        mMap.setMinZoomPreference(16);
        mMap.setMaxZoomPreference(19);
        mMap.moveCamera(CameraUpdateFactory.zoomTo(19));
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(com.google.android.gms.maps.model.Marker marker) {
                Intent intent = new Intent(getApplicationContext(),VocabActivity.class);
                Marker clickedMarker = (Marker) marker.getTag();
                intent.putExtra("ID",clickedMarker.getLocationID());
                getApplicationContext().startActivity(intent);
                return false;
            }
        });

        ImageButton menuImageButton = findViewById(R.id.menuImageButton);
        menuImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),MenuActivity.class);
                getApplicationContext().startActivity(intent);
            }
        });

        fusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if(location!=null){
                    LatLng userLocation = new LatLng(location.getLatitude(),location.getLongitude());

                    if(!campusMapBounds.contains(userLocation)) {
                        Toast notOnCampusMessage = Toast.makeText(getApplicationContext(),"Move Closer to Highfield Campus",Toast.LENGTH_LONG);
                        notOnCampusMessage.show();
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(campusMapBounds.getCenter()));
                    }
                }
            }
        });

        List<Marker> markers = db.MarkerDao().getAll();
        for(Marker m : markers){
            danieltupper2.com.mobilelanguagelearner.Location l = db.LocationDao().getLocationFromID(m.getLocationID());
            com.google.android.gms.maps.model.Marker mapMarker = mMap.addMarker(new MarkerOptions().position(new LatLng(l.getLat(),l.getLng())).title(m.getPlaceName()));
            mapMarker.setTag(m);
        }
        startLocationUpdates();
    }

    public void startLocationUpdates(){
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setFastestInterval(1500);
        locationRequest.setInterval(2000);

        fusedLocationClient.requestLocationUpdates(locationRequest,new LocationCallback(){
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    LatLng userLocation = new LatLng(location.getLatitude(),location.getLongitude());
                    if(campusMapBounds.contains(userLocation)) {
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(userLocation));
                    }
                }
            };
        },getMainLooper());
    }
}
