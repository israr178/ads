package com.adglobi.ads;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class AdValidator implements GetNetworkResult, LocationListener{
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    public static String simNetworkCountryCode;
    public static String gpsCountryCode;
    public static String gpsCity;
    public static String gpsRegionName;
    public static String apiCountryCode;
    public static String apiCity;
    public static String apiISP;
    public static String apiRegionName;
    public static String simNetworkISP;
    LocationManager locationManager;

    public static String getSimNetworkCountryCode() {
        if(simNetworkCountryCode == null){
            simNetworkCountryCode = Utilities.getDataSharedPreference(AdGlobi.getApplicationContext(),"simNetworkCountryCode");
        }
        return simNetworkCountryCode;
    }

    public static String getGpsCountryCode() {
        if(gpsCountryCode == null){
            gpsCountryCode = Utilities.getDataSharedPreference(AdGlobi.getApplicationContext(),"gpsCountryCode");
        }
        if(gpsCountryCode.isEmpty()){
            gpsCountryCode = "-1";
        }
        return gpsCountryCode;
    }

    public static String getGpsCity() {
        if(gpsCity == null){
            gpsCity = Utilities.getDataSharedPreference(AdGlobi.getApplicationContext(),"gpsCity");
        }
        return gpsCity;
    }

    public static String getGpsRegionName() {
        return gpsRegionName;
    }

    public static String getApiCountryCode() {
        if(apiCountryCode == null){
            apiCountryCode = Utilities.getDataSharedPreference(AdGlobi.getApplicationContext(),"apiCountryCode");
        }
        return apiCountryCode;
    }

    public static String getApiCity() {
        if(apiCity == null){
            apiCity = Utilities.getDataSharedPreference(AdGlobi.getApplicationContext(),"apiCity");
        }
        return apiCity;
    }

    public static String getApiISP() {
        if(apiISP == null){
            apiISP = Utilities.getDataSharedPreference(AdGlobi.getApplicationContext(),"apiISP");
        }
        return apiISP;
    }

    public static String getApiRegionName() {
        return apiRegionName;
    }

    public static String getSimNetworkISP() {
        return simNetworkISP;
    }

    public static boolean isDebug() {
        if (BuildConfig.DEBUG) {
            // do something for a debug build
            return true;
        }
        return false;
    }

    public boolean isAdAllowed(AdModel ad){
        //1- check allowed country
        if(isThisCountryAllowed(ad)){
            //2- check this device type allowed
            if(isThisDeviceAllowed(ad)){
                //3- check OS allowed
                if(isThisOsAllowed(ad)){
                    //4- check this city allowed
                    if(isThisCityAllowed(ad)){
                        return true;
                    }else{
                        return false;
                    }
                }else{
                    return false;
                }
            }else{
                return false;
            }
        }else{
            return false;
        }
    }

    public boolean isThisCountryAllowed(AdModel ad){
        if(ad.getCountry_allow().isEmpty() && ad.getCountry_block().isEmpty()){
            return true;
        }
        Log.d("israr"," ad :"+ ad.getCountry_allow() + " "+ getApiCountryCode());
        if(ad.getCountry_allow().isEmpty() ||
                ad.getCountry_allow().contains(getGpsCountryCode()) ||
                ad.getCountry_allow().contains(getApiCountryCode())){
            // ad is allowed in this country
            if(!ad.getCountry_block().isEmpty() &&
                    (ad.getCountry_block().equalsIgnoreCase(getGpsCountryCode()) ||
                            ad.getCountry_block().equalsIgnoreCase(getApiCountryCode()))){
                // ad is blocked in this country
                return false;
            }else{
                return true;
            }

        }else{
            //ad is not allowed in this country
            return false;
        }
    }
    public boolean isThisCityAllowed(AdModel ad){
        if(ad.getCity_allow().isEmpty() && ad.getCity_block().isEmpty()){
            return true;
        }
        if(ad.getCity_allow().isEmpty() ||
                ad.getCity_allow().contains(getGpsCity()) ||
                ad.getCity_allow().contains(getApiCity())){
            // ad is allowed in this city
            if(!ad.getCity_block().isEmpty() &&
                    (ad.getCity_block().contains(getGpsCity()) ||
                            ad.getCity_block().contains(getApiCity()))){
                // ad is blocked in this country
                return false;
            }else{
                return true;
            }

        }else{
            //ad is not allowed in this country
            return false;
        }
    }
    public boolean isThisOsAllowed(AdModel ad){
        if(ad.getOs_allow().isEmpty() && ad.getOs_block().isEmpty()){
            return true;
        }

        if((ad.getOs_allow().isEmpty() || ad.getOs_allow().equalsIgnoreCase(getOSType()) && (ad.getOs_block().isEmpty() || !ad.getOs_block().equalsIgnoreCase(getOSType())))){
            return true;
        }else{
            return false;
        }
    }
    public boolean isThisDeviceAllowed(AdModel ad){
        if(ad.getDevice_allow().isEmpty() && ad.getDevice_block().isEmpty()){
            return true;
        }

        if((ad.getDevice_allow().isEmpty() || ad.getDevice_allow().contains(getDeviceType()) && (ad.getDevice_block().isEmpty() || !ad.getDevice_block().equalsIgnoreCase(getDeviceType())))){
            return true;
        }else{
            return false;
        }
    }
    //get country name from SIM
    public static String getDeviceCountryLevel1(Context context) {
        try {
            final TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            simNetworkCountryCode = tm.getSimCountryIso();
            simNetworkISP = tm.getNetworkOperatorName();
            if (simNetworkCountryCode != null && simNetworkCountryCode.length() == 2) { // SIM country code is available
                return simNetworkCountryCode.toLowerCase(Locale.US).toUpperCase();

            }
            else if (tm.getPhoneType() != TelephonyManager.PHONE_TYPE_CDMA) { // device is not 3G (would be unreliable)
                simNetworkCountryCode = tm.getNetworkCountryIso();
                if (simNetworkCountryCode != null && simNetworkCountryCode.length() == 2) { // network country code is available
                    return simNetworkCountryCode.toLowerCase(Locale.US).toUpperCase();
                }
            }
        }
        catch (Exception e) { }
        return null;
    }

    //get device information
    public static String getDeviceManufacturer(){
        String manufacturer = Build.MANUFACTURER;
        return manufacturer;

    }
    public static String getDeviceModel(){
        String model = Build.MODEL;
        return model;
    }
    public static int getDeviceVersion(){
        int version = Build.VERSION.SDK_INT;
        return version;
    }
    public static String getDeviceType(){
        String model = getDeviceModel();
        if(model.contains("phone")){
            return "smartphone";
        }
        if(model.contains("tablet")){
            return "tablet";
        }
        if(model.contains("watch")){
            return "watch";
        }
        return "unknown";
    }
    public static String getOSType(){
        return "android";
    }

    public void getUserLocationInfoFromApiLevel2(){
        NetworkHandler handler = new NetworkHandler(this);
        handler.doGetRequestForJson(Constants.USER_INFO_URL,"");
    }

    @Override
    public void onRequestCompleted(String result) {
        if (result!=null && !result.isEmpty()){
            Log.d("israr",result);
            try {
                JSONObject apiResponse = new JSONObject(result);
                apiCountryCode = apiResponse.getString("country_code");
                apiCity = apiResponse.getString("city");
                apiRegionName = apiResponse.getString("state");
                Utilities.saveDataSharedPreference(AdGlobi.getApplicationContext(),"apiCountryCode",apiCountryCode);
                Utilities.saveDataSharedPreference(AdGlobi.getApplicationContext(),"apiCity",apiCity);
                Utilities.saveDataSharedPreference(AdGlobi.getApplicationContext(),"apiISP","");
                Utilities.saveDataSharedPreference(AdGlobi.getApplicationContext(),"apiRegionName",apiRegionName);

//                String status = apiResponse.getString("status");
//                if(status != null && !status.isEmpty() && status.equals("success")){
//                    apiCountryCode = apiResponse.getString("countryCode");
//                    apiCity = apiResponse.getString("city");
//                    apiISP = apiResponse.getString("isp");
//                    apiRegionName = apiResponse.getString("regionName");
//                    Utilities.saveDataSharedPreference(AdGlobi.getApplicationContext(),"apiCountryCode",apiCountryCode);
//                    Utilities.saveDataSharedPreference(AdGlobi.getApplicationContext(),"apiCity",apiCity);
//                    Utilities.saveDataSharedPreference(AdGlobi.getApplicationContext(),"apiISP",apiISP);
//                    Utilities.saveDataSharedPreference(AdGlobi.getApplicationContext(),"apiRegionName",apiRegionName);
//                }else{
//                    //api failed, get location via gps
//                    AdGlobi.getLocationViaGps();
//                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else{
            //api failed, get location via gps
            AdGlobi.getLocationViaGps();
        }
    }
    public static boolean checkLocationPermission(Activity context) {
        if (ContextCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED ) {
            //request the permission
            return false;
        }else{
            //permission is granted
            return true;
        }
    }
    public void requestLocationPermission(Activity context){
        ActivityCompat.requestPermissions(context,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                MY_PERMISSIONS_REQUEST_LOCATION);
    }
    @SuppressLint("MissingPermission")
    public void getLocationLevel3(Activity context){

        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if(location == null){
            location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        }else{
            saveLocationInfoLevel3(location);
        }
        if(location == null){
            location = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
        }
        if (location == null){
            if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
                Log.d("israr","calling enable GPS_PROVIDER");
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
            }else if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){
                Log.d("israr","calling enable NETWORK_PROVIDER");
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
            }else{
                Log.d("israr","calling enable loc");
              //  context.startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
        }else{
            saveLocationInfoLevel3(location);
        }
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        locationManager.removeUpdates(this);
        saveLocationInfoLevel3(location);
    }

    public void saveLocationInfoLevel3(Location loc){
        Geocoder gcd = new Geocoder(AdGlobi.getApplicationContext(), Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = gcd.getFromLocation(loc.getLatitude(), loc.getLongitude(), 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (addresses.size() > 0) {
           gpsCountryCode = addresses.get(0).getCountryCode();
           gpsCity = addresses.get(0).getLocality();
           gpsRegionName = addresses.get(0).getAdminArea();

           Utilities.saveDataSharedPreference(AdGlobi.getApplicationContext(),"gpsCountryCode",gpsCountryCode);
            Utilities.saveDataSharedPreference(AdGlobi.getApplicationContext(),"gpsCity",gpsCity);
            Utilities.saveDataSharedPreference(AdGlobi.getApplicationContext(),"gpsRegionName",gpsRegionName);

        }
        else {
            // do your stuff
        }
    }
    public static boolean isLocationUpdateRequired(Context con){
        String lastTimeLocationUpdated= Utilities.getDataSharedPreference(con,"lastTimeLocationUpdated");
        if(lastTimeLocationUpdated == null || lastTimeLocationUpdated.isEmpty()){
            Utilities.saveDataSharedPreference(con,"lastTimeLocationUpdated",String.valueOf(System.currentTimeMillis()));
            return true;
        }else{
            Long lastTimeLocationUpdatedC = Long.parseLong(lastTimeLocationUpdated);
            Long currentTimeMilliSec = System.currentTimeMillis();
            Long diff = currentTimeMilliSec - lastTimeLocationUpdatedC;
            if(diff > 900000) {
                Utilities.saveDataSharedPreference(con,"lastTimeLocationUpdated",String.valueOf(currentTimeMilliSec));
                return true;
            }else{
                return false;
            }
        }
    }
}
