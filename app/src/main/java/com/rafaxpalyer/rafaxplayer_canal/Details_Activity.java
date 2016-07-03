package com.rafaxpalyer.rafaxplayer_canal;

import android.content.Intent;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.util.SortedList;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;


import com.rafaxpalyer.rafaxplayer_canal.Classes.AsyncJSON;
import com.rafaxpalyer.rafaxplayer_canal.Helper.AsyncResponseJSON;
import com.rafaxpalyer.rafaxplayer_canal.Helper.Utilities;
import com.rafaxpalyer.rafaxplayer_canal.Models.Video;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;




public class Details_Activity extends AppCompatActivity implements AsyncResponseJSON {
    private ImageView thumbnailVideo;
    private CollapsingToolbarLayout collapserView;
    private TextView textTitle;
    private TextView textDate;
    private TextView textDescription;
    private TextView textDuration;
    private TextView textLikes;
    private FloatingActionButton fab;
    private Video currentVideo;
    private FrameLayout progress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        progress=(FrameLayout)findViewById(R.id.progress);
        thumbnailVideo=(ImageView)findViewById(R.id.thumbnailDetail);
        collapserView=(CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        // ocultamos titulo
        collapserView.setExpandedTitleColor(getResources().getColor(R.color.transparent));
        collapserView.setCollapsedTitleTextColor(getResources().getColor(R.color.transparent));

        textTitle=(TextView) findViewById(R.id.textDetailsTitle);
        textDuration=(TextView) findViewById(R.id.textDuration);
        textLikes=(TextView) findViewById(R.id.textLikes);
        textDescription=(TextView) findViewById(R.id.textDescription);
        textDate=(TextView) findViewById(R.id.textDetailsDate);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        Animation show_fab_1 = AnimationUtils.loadAnimation(getApplication(), R.anim.zoom_in);
        fab.startAnimation(show_fab_1);
    }

    @Override
    protected void onResume() {
        super.onResume();

        Video vid =getIntent().getParcelableExtra("video");
        if(vid != null){
            currentVideo = vid;

            String url = Utilities.URLVIDEOINFO + Utilities.APIKEY;
            url = url.replace("###", vid.getVideoid());
            AsyncJSON asyncjson = new AsyncJSON(Details_Activity.this,progress, url, Utilities.TYPEVIDEOS);
            asyncjson.delegate = this;
            asyncjson.execute();
        }

    }
    public void closeProgress(View v){
        v.setVisibility(View.GONE);
    }
    private Video createVideoInfo(Video vid,JSONObject obj){

        try {

            JSONArray jsonArray = obj.getJSONArray("items");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject root = jsonArray.getJSONObject(i);
                vid.setDate(Utilities.formatDuration(root.optJSONObject("snippet").getString("publishedAt")));
                vid.setDuration(Utilities.formatDuration(root.optJSONObject("contentDetails").getString("duration")));
                vid.setLikes(root.optJSONObject("statistics").getString("likeCount"));
                vid.setViewcount(root.optJSONObject("statistics").getString("viewCount"));
            }
        } catch (JSONException e) {
            Log.e("Error 104:", e.getMessage());
        } catch (Exception ex) {
            Log.e("Error 106:", ex.getMessage());
        }
        return vid;
    }

    private void displayVideoInfo(Video vid){
        Picasso.with(this).load(vid.getThumbnail()).into(thumbnailVideo);
        textTitle.setText(vid.getTitle());
        textDate.setText("Subido el : " + vid.getDate());
        textDescription.setText(vid.getDescription());
        textDescription.setTextSize(TypedValue.COMPLEX_UNIT_SP,Float.valueOf(Utilities.getPrefs(this).getString("textsize","16")));
        textDuration.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.time,0,0,0);
        textDuration.setText(" "+vid.getDuration());
        textLikes.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.like,0,0,0);
        textLikes.setText(" "+vid.getLikes());

    }
    public void OnClick(View v){

        if(v.getId()== R.id.buttonComments){
            Intent in = new Intent(this,Comments_Activity.class);
            in.putExtra("videoid",currentVideo.getVideoid());
            startActivity(in);

        }else if(v.getId()==R.id.fab){
            Intent intent = new Intent(this,Player_Activity.class);
            intent.putExtra("videoid",currentVideo.getVideoid());
            startActivity(intent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_details_, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }else if(item.getItemId() == R.id.action_share){

            ShareAction(currentVideo.getVideoid());
        }

        return super.onOptionsItemSelected(item);
    }


    private void ShareAction(String videoid) {
        String url = String.format(Utilities.URLBASEYOUTUBE,videoid);
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, url);
        startActivity(Intent.createChooser(shareIntent,"Compartir con :"));
    }

    @Override
    public void asyncFinish(JSONObject output, int type) {
        if(type == Utilities.TYPEVIDEOS){
            Video vid = createVideoInfo(currentVideo,output);
            if(vid != null){
                displayVideoInfo(vid);
            }
        }
    }
}
