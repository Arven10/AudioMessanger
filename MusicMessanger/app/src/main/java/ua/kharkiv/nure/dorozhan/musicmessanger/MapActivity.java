package ua.kharkiv.nure.dorozhan.musicmessanger;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.InputStream;

import ua.kharkiv.nure.dorozhan.musicmessanger.models.User;

public class MapActivity extends FragmentActivity implements LocationListener{
    private GoogleMap mGoogleMap;
    private User user;
    private ImageView image;
    private TextView username;
    private TextView emailLabel;
    private TextView locationTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!isGooglePlayServicesAvailable()) {
            finish();
        }
        setContentView(R.layout.activity_map);
        String userName = getIntent().getStringExtra("userName");
        String userId = getIntent().getStringExtra("userId");
        String userEmail = getIntent().getStringExtra("userEmail");
        String userPhotoUrl = getIntent().getStringExtra("userPhotoUrl");
        user = new User(userId, userName, userEmail, userPhotoUrl);
        image = (ImageView) findViewById(R.id.image);
        username = (TextView) findViewById(R.id.username);
        emailLabel = (TextView) findViewById(R.id.email);
        username.setText(user.getUsername());
        emailLabel.setText(user.getEmail());
        new LoadProfileImage(image).execute(user.getPhotoUrl());
        SupportMapFragment supportMapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.googleMap);
        mGoogleMap = supportMapFragment.getMap();
        mGoogleMap.setMyLocationEnabled(true);
        LocationManager mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        Criteria mCriteria = new Criteria();
        String mBestProvider = mLocationManager.getBestProvider(mCriteria, true);
        Location mLocation = mLocationManager.getLastKnownLocation(mBestProvider);
        if (mLocation != null) {
            onLocationChanged(mLocation);
        }
        mLocationManager.requestLocationUpdates(mBestProvider, 20, 0, this);
    }

    @Override
    public void onLocationChanged(Location location) {
        locationTextView = (TextView) findViewById(R.id.latlongLocation);
        double mLatitude = location.getLatitude();
        double mLongitude = location.getLongitude();
        LatLng mPoint = new LatLng(mLatitude, mLongitude);
        mGoogleMap.addMarker(new MarkerOptions().position(mPoint));
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(mPoint));
        mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(15));
        locationTextView.setText("Latitude:" + mLatitude + ", Longitude:" + mLongitude);
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    private boolean isGooglePlayServicesAvailable() {
        int mStatus = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (ConnectionResult.SUCCESS == mStatus) {
            return true;
        } else {
            GooglePlayServicesUtil.getErrorDialog(mStatus, this, 0).show();
            return false;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private class LoadProfileImage extends AsyncTask {
        ImageView downloadedImage;

        public LoadProfileImage(ImageView image) {
            this.downloadedImage = image;
        }

        protected void onPostExecute(Object result) {
            downloadedImage.setImageBitmap((Bitmap)result);
        }

        @Override
        protected Object doInBackground(Object[] params) {
            String url = (String)params[0];
            Bitmap icon = null;
            try {
                InputStream in = new java.net.URL(url).openStream();
                icon = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return icon;
        }
    }
}