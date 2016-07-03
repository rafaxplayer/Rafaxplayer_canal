package com.rafaxpalyer.rafaxplayer_canal.Classes;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.FrameLayout;


import com.rafaxpalyer.rafaxplayer_canal.Helper.AsyncResponseJSON;

import org.json.JSONObject;


/**
 * Created by rafax on 07/06/2016.
 */
public class AsyncJSON extends AsyncTask<Void, Void, JSONObject> {

    private String url;
    private Context context;
    public AsyncResponseJSON delegate = null;
    private int type;
    private FrameLayout progress;

    public AsyncJSON(Context context, FrameLayout pro, String url, int type) {
        this.url = url;
        this.type = type;
        this.context = context;
        this.progress = pro;

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        if (progress != null) {
            progress.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected JSONObject doInBackground(Void... params) {

        ConectorHttpJSON conector = new ConectorHttpJSON(url);
        JSONObject obj = null;
        try {

            obj = conector.execute();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return obj;
    }

    @Override
    protected void onPostExecute(JSONObject result) {

        delegate.asyncFinish(result, type);

        if (progress != null) {
            progress.setVisibility(View.GONE);
        }

        super.onPostExecute(result);
    }
}
