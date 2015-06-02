package ua.kharkiv.nure.dorozhan.musicmessanger.ui.tabs;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;

import ua.kharkiv.nure.dorozhan.musicmessanger.R;
import ua.kharkiv.nure.dorozhan.musicmessanger.serversOperations.ServerData;
import ua.kharkiv.nure.dorozhan.musicmessanger.models.User;

/**
 * Created by Dorozhan on 01.06.2015.
 */
public class UserInformationTab extends Fragment implements LocationListener {
    private GoogleMap mGoogleMap;
    private User user;
    private ImageView image;
    private TextView username;
    private TextView emailLabel;
    private TextView locationTextView;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View viewUserInfo = inflater.inflate(R.layout.tab_user_information,container,false);
        if (!isGooglePlayServicesAvailable()) {
            getActivity().finish();
        }
        String userName = getActivity().getIntent().getStringExtra("userName");
        String userId =  getActivity().getIntent().getStringExtra("userId");
        String userEmail =  getActivity().getIntent().getStringExtra("userEmail");
        String userPhotoUrl =  getActivity().getIntent().getStringExtra("userPhotoUrl");
        user = new User(userId, userName, userEmail, userPhotoUrl);
        image = (ImageView) viewUserInfo.findViewById(R.id.image);
        username = (TextView) viewUserInfo.findViewById(R.id.username);
        emailLabel = (TextView) viewUserInfo.findViewById(R.id.email);
        locationTextView = (TextView) viewUserInfo.findViewById(R.id.latlongLocation);
        username.setText(user.getUsername());
        emailLabel.setText(user.getEmail());
        new LoadProfileImage(image).execute(user.getPhotoUrl());
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.googleMap);
        mGoogleMap = mapFragment.getMap();
        mGoogleMap.setMyLocationEnabled(true);
        LocationManager mLocationManager = (LocationManager) getActivity()
                .getSystemService(Context.LOCATION_SERVICE);
        Criteria mCriteria = new Criteria();
        String mBestProvider = mLocationManager.getBestProvider(mCriteria, true);
        Location mLocation = mLocationManager.getLastKnownLocation(mBestProvider);
        if (mLocation != null) {
            onLocationChanged(mLocation);
        }
        //mLocationManager.requestLocationUpdates(mBestProvider, 20, 0, this);
        return viewUserInfo;
    }

    @Override
    public void onLocationChanged(Location location) {
        double mLatitude = location.getLatitude();
        double mLongitude = location.getLongitude();
        user.setLatitude(mLatitude);
        user.setLongitude(mLongitude);

        LatLng mPoint = new LatLng(mLatitude, mLongitude);
        mGoogleMap.addMarker(new MarkerOptions().position(mPoint));
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(mPoint));
        mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(15));
        locationTextView.setText("Latitude:" + mLatitude + ", Longitude:" + mLongitude);

        new SendUserData().execute();
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
        int mStatus = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getActivity());
        if (ConnectionResult.SUCCESS == mStatus) {
            return true;
        } else {
            GooglePlayServicesUtil.getErrorDialog(mStatus, getActivity(), 0).show();
            return false;
        }
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

    private class SendUserData extends AsyncTask<Void, Integer, String> {

        @Override
        protected String doInBackground(Void... params) {
            try {
                ServerData.sendUserData(user);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return "User data send";
        }

        protected void onPostExecute(String result) {
            Log.e("User data send", result); // udalit'
        }
    }
}
