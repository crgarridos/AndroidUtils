package com.cgarrido.android.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

public class LocationUtils implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    private static Activity mActivityContext;
    private GoogleApiClient mLocationClient;

    private LocationUtils() {
        mLocationClient = new GoogleApiClient.Builder(mActivityContext)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        mLocationClient.connect();
    }

    public static void setActivityContext(Activity mContext) {
        LocationUtils.mActivityContext = mContext;
    }

    public GoogleApiClient getLocation() {

        //On check si la localisation est activ� a chaque requete
        LocationManager locManager;
        locManager = (LocationManager) mActivityContext.getSystemService(Context.LOCATION_SERVICE);
        try {
            /** Test si le gps est activ� ou non */
            if (!locManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                AlertDialog.Builder localBuilder = new AlertDialog.Builder(mActivityContext);
                localBuilder
                        .setMessage("Le GPS est inactif, voulez-vous l'activer ?")
                        .setCancelable(false)
                        .setPositiveButton("Activer GPS ",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                                        mActivityContext.startActivity(new Intent("android.settings.LOCATION_SOURCE_SETTINGS"));
                                    }
                                }
                        )
                        .setNegativeButton("Ne pas l'activer ",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                                        paramDialogInterface.cancel();
                                    }
                                }).show();
            } else {
                return this.mLocationClient;
            }
        } catch (Exception e) {

        }
        return this.mLocationClient;
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(LocationUtils.mActivityContext,
                        CONNECTION_FAILURE_RESOLUTION_REQUEST);
                /*
				 * Thrown if Google Play services canceled the original
				 * PendingIntent
				 */
            } catch (IntentSender.SendIntentException e) {
                // Log the error
                e.printStackTrace();
            }
        } else {
			/*
			 * If no resolution is available, display a dialog to the user with
			 * the error.
			 */
            Log.e("Location", Integer.toString(connectionResult.getErrorCode()));
            if (connectionResult.getErrorCode() == 2) {
                try {
                    GooglePlayServicesUtil.getErrorDialog(2, LocationUtils.mActivityContext, 0).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onConnected(Bundle arg0) {
        Log.d("Location Utils", "Connect�");
    }

    @Override
    public void onConnectionSuspended(int arg0) {
        // TODO Auto-generated method stub

    }
}
