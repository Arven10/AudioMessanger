package ua.kharkiv.nure.dorozhan.musicmessanger;

import java.io.InputStream;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;

import ua.kharkiv.nure.dorozhan.musicmessanger.models.User;


public class MainActivity extends Activity implements OnClickListener, ConnectionCallbacks,
        OnConnectionFailedListener {

    private static final int RC_SIGN_IN = 0;
    private GoogleApiClient mGoogleApiClient;

    private boolean mIntentInProgress;
    private boolean signedInUser;
    private ConnectionResult mConnectionResult;
    private SignInButton signInButton;
//    private ImageView image;
//    private TextView username, emailLabel;
//    private TextView idTextView, locationTextView;
//    private LinearLayout profileFrame, signInFrame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        signInButton = (SignInButton) findViewById(R.id.signin);
        signInButton.setOnClickListener(this);
//        image = (ImageView) findViewById(R.id.image);
//        username = (TextView) findViewById(R.id.username);
//        emailLabel = (TextView) findViewById(R.id.email);
//
//        idTextView = (TextView) findViewById(R.id.id);
//        locationTextView = (TextView) findViewById(R.id.location);
//
//        profileFrame = (LinearLayout) findViewById(R.id.profileFrame);
//        signInFrame = (LinearLayout) findViewById(R.id.signinFrame);
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .addApi(Plus.API, Plus.PlusOptions.builder().build())
                .addScope(Plus.SCOPE_PLUS_LOGIN)
                .build();
    }


    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    private void resolveSignInError() {
        if (mConnectionResult.hasResolution()) {
            try {
                mIntentInProgress = true;
                mConnectionResult.startResolutionForResult(this, RC_SIGN_IN);
            } catch (SendIntentException e) {
                mIntentInProgress = false;
                mGoogleApiClient.connect();
            }
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        if (!result.hasResolution()) {
            GooglePlayServicesUtil.getErrorDialog(result.getErrorCode(), this, 0).show();
            return;
        }
        if (!mIntentInProgress) {
            mConnectionResult = result;
            if (signedInUser) {
                resolveSignInError();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int responseCode, Intent intent) {
        switch (requestCode) {
            case RC_SIGN_IN:
                if (responseCode == RESULT_OK) {
                    signedInUser = false;
                }
                mIntentInProgress = false;
                if (!mGoogleApiClient.isConnecting()) {
                    mGoogleApiClient.connect();
                }
                break;
        }
    }

    @Override
    public void onConnected(Bundle arg0) {
        signedInUser = false;
        Toast.makeText(this, "Connected", Toast.LENGTH_LONG).show();

        User user = getProfileInformation();
        Intent intent = new Intent(this, MapActivity.class);
        intent.putExtra("userId", user.getUserId());
        intent.putExtra("userEmail", user.getEmail());
        intent.putExtra("userName", user.getUsername());
        intent.putExtra("userPhotoUrl", user.getPhotoUrl());
        startActivity(intent);
//        idTextView.setText(user.getUserId());
//        username.setText(user.getUsername());
//        emailLabel.setText(user.getEmail());
//        new LoadProfileImage(image).execute(user.getPhotoUrl());
//        updateProfile(true);

    }

//    private void updateProfile(boolean isSignedIn) {
//        if (isSignedIn) {
//            signInFrame.setVisibility(View.GONE);
//            profileFrame.setVisibility(View.VISIBLE);
//        } else {
//            signInFrame.setVisibility(View.VISIBLE);
//            profileFrame.setVisibility(View.GONE);
//        }
//    }

    private User getProfileInformation() {
        try {
            if (Plus.PeopleApi.getCurrentPerson(mGoogleApiClient) != null) {
                Person currentPerson = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);
                String personName = currentPerson.getDisplayName();
                String personPhotoUrl = currentPerson.getImage().getUrl();
                String personEmail = Plus.AccountApi.getAccountName(mGoogleApiClient);
                String personId = currentPerson.getId();
                User currentUser = new User(personId, personName, personEmail, personPhotoUrl);
                return currentUser;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void onConnectionSuspended(int cause) {
        mGoogleApiClient.connect();
        //updateProfile(false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.signin:
                googlePlusLogin();
                break;
        }
    }

    public void signIn(View v) {
        googlePlusLogin();
    }

//    public void logout(View v) {
//        googlePlusLogout();
//    }

    private void googlePlusLogin() {
        if (!mGoogleApiClient.isConnecting()) {
            signedInUser = true;
            resolveSignInError();
        }
    }

//    private void googlePlusLogout() {
//        if (mGoogleApiClient.isConnected()) {
//            Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
//            mGoogleApiClient.disconnect();
//            mGoogleApiClient.connect();
//            updateProfile(false);
//        }
//    }

}
