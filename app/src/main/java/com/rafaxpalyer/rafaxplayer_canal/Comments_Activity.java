package com.rafaxpalyer.rafaxplayer_canal;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import com.rafaxpalyer.rafaxplayer_canal.Classes.AsyncJSON;
import com.rafaxpalyer.rafaxplayer_canal.Classes.CommentsAdapter;
import com.rafaxpalyer.rafaxplayer_canal.Helper.AsyncResponseJSON;
import com.rafaxpalyer.rafaxplayer_canal.Helper.Utilities;
import com.rafaxpalyer.rafaxplayer_canal.Models.Comment;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Comments_Activity extends AppCompatActivity implements AsyncResponseJSON {
    private RecyclerView listComments;
    private LinearLayout emptyview;
    private FrameLayout progress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Comentarios");
        toolbar.setBackgroundColor(getResources().getColor(R.color.primary));
        progress=(FrameLayout)findViewById(R.id.progress);
        emptyview = (LinearLayout) findViewById(R.id.empty);
        emptyview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        listComments = (RecyclerView) findViewById(R.id.listComments);
        listComments.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        listComments.setItemAnimator(new DefaultItemAnimator());

    }

    @Override
    protected void onResume() {
        super.onResume();
        String id = getIntent().getExtras().getString("videoid");

        if (!TextUtils.isEmpty(id)) {
            loadComments(id);
        }

    }
    public void closeProgress(View v){
        v.setVisibility(View.GONE);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }

    private void setEmptyView(Boolean visibility) {
        if (visibility) {
            listComments.setVisibility(View.VISIBLE);
            emptyview.setVisibility(View.GONE);
        } else {
            listComments.setVisibility(View.GONE);
            emptyview.setVisibility(View.VISIBLE);
        }

    }

    private void loadComments(String videoid) {
        String url = Utilities.URLCOMMENTS+Utilities.APIKEY;
        url=url.replace("###",videoid);
        AsyncJSON asyncjson = new AsyncJSON(Comments_Activity.this,progress, url, Utilities.TYPECOMMENTS);
        asyncjson.delegate = this;
        asyncjson.execute();
    }

    private ArrayList<Comment> loadListComments(JSONObject json) {
        ArrayList<Comment> list = new ArrayList<Comment>();
        try {
            JSONArray jsonArray = json.getJSONArray("items");
            if (jsonArray.length() > 0) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject root = jsonArray.getJSONObject(i);
                    String nick = root.optJSONObject("snippet").getJSONObject("topLevelComment").getJSONObject("snippet").getString("authorDisplayName");
                    String comment = root.optJSONObject("snippet").getJSONObject("topLevelComment").getJSONObject("snippet").getString("textDisplay");
                    String plublishedAt = root.optJSONObject("snippet").getJSONObject("topLevelComment").getJSONObject("snippet").getString("publishedAt");
                    String imageuser = root.getJSONObject("snippet").getJSONObject("topLevelComment").getJSONObject("snippet").getString("authorProfileImageUrl");

                    Comment playlist = new Comment(nick, comment,Utilities.parseUTCDate(plublishedAt), imageuser);
                    list.add(playlist);

                }
            } else {
                Log.e(getApplication().getPackageName(), "No Comments");
            }

        } catch (JSONException e) {
            Log.e("Error :", e.getMessage());
        } catch (Exception ex) {
            Log.e("Error :", ex.getMessage());
        }
        return list;
    }

    @Override
    public void asyncFinish(JSONObject output, int type) {

        if (type == Utilities.TYPECOMMENTS) {

            ArrayList<Comment> listCom = loadListComments(output);
            Boolean bValue = (listCom.size() > 0);
            setEmptyView(bValue);
            if (bValue) {
                CommentsAdapter adapter = new CommentsAdapter(Comments_Activity.this, listCom);
                listComments.setAdapter(adapter);

            }

        }
    }
}
