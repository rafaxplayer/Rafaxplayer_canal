package com.rafaxpalyer.rafaxplayer_canal.Helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Utilities {
    public static String URLPAYLIST="https://www.googleapis.com/youtube/v3/playlists?part=snippet%2CcontentDetails&channelId=UCCNG1OowkXGvbi-6T1J6zBA&key=";
    public static String URLVIDEOS="https://www.googleapis.com/youtube/v3/playlistItems?part=snippet%2CcontentDetails&maxResults=50&playlistId=###&key=";
    public static String URLVIDEOINFO="https://www.googleapis.com/youtube/v3/videos?part=id%2C+snippet%2C+contentDetails%2Cstatistics%2C+status%2CtopicDetails&id=###&maxResults=50&key=";
    public static String URLBASEYOUTUBE="https://www.youtube.com/watch?v=";
    public static String URLCOMMENTS = "https://www.googleapis.com/youtube/v3/commentThreads?part=id%2Creplies%2Csnippet&maxResults=100&videoId=###&key=";
    public static String APIKEY="AIzaSyB0WiO6t2_kWQSIi9riNoQF_sQcHGy1SxM";
    public static String AVATARURL="https://lh3.googleusercontent.com/-Qjb7w-28zAA/AAAAAAAAAAI/AAAAAAAAAEQ/KqCNGUXPIfI/s120-p-rw-no/photo.jpg";
    public static int TYPEPLAYLIST= 1;
    public static int TYPEVIDEOS= 2;
    public static int TYPECOMMENTS= 3;
    public static int TYPEVIDEODETAIL= 4;
    public static int TYPENONE = 0;
    public static int ERRORNOCOMMNTS=0;
    public static int ERRORNOCONNECTION=1;


    public static Boolean TestConnection(Context con) {
        ConnectivityManager connectivityManager;
        NetworkInfo wifiInfo, mobileInfo;
        try {
            connectivityManager = (ConnectivityManager) con
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            wifiInfo = connectivityManager
                    .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            mobileInfo = connectivityManager
                    .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

            if (wifiInfo.isConnected() || mobileInfo.isConnected()) {
                return true;
            }
        } catch (Exception e) {
            return false;
        }

        return false;
    }
    public static String formatDuration(String duration){
        String newDuartion = duration;
        if(!TextUtils.isEmpty(duration)){
            newDuartion = duration.replace("PT","").replace("M","' ").replace("S","''");
        }
        return newDuartion;
    }

    public static String parseUTCDate(String utcdate){
        String date = utcdate;
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            Date parse = simpleDateFormat.parse(utcdate);
            date = new SimpleDateFormat("dd-MM-yyyy , HH:mm:ss").format(parse);
        }catch (Exception ex){
            Log.e("Error parse date",ex.getMessage());
        }
        return date;
    }

    public static SharedPreferences.Editor editSharePrefs(Context con) {

        SharedPreferences.Editor editor = getPrefs(con).edit();

        return editor;
    }

    public static SharedPreferences getPrefs(Context con) {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(con);

        return settings;
    }
}
