package com.rafaxpalyer.rafaxplayer_canal;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;

import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.google.android.youtube.player.YouTubePlayerSupportFragment;
import com.rafaxpalyer.rafaxplayer_canal.Classes.AsyncJSON;
import com.rafaxpalyer.rafaxplayer_canal.Classes.CircleTransform;
import com.rafaxpalyer.rafaxplayer_canal.Classes.PlayListAdapter;

import com.rafaxpalyer.rafaxplayer_canal.Helper.AsyncResponseJSON;
import com.rafaxpalyer.rafaxplayer_canal.Helper.Utilities;
import com.rafaxpalyer.rafaxplayer_canal.Models.PlayList;
import com.rafaxpalyer.rafaxplayer_canal.Models.Video;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.util.ArrayList;



public class PlayList_Activity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, AsyncResponseJSON {


    private ImageView imagePlayList;
    private ImageView imageAvatar;
    private CoordinatorLayout coordinatorLayout;
    private YouTubePlayerSupportFragment frag;
    private NavigationView navigationView;
    private ArrayList<PlayList> playlists;

    private RecyclerView listVideos;
    private CollapsingToolbarLayout collapsingView;

    private PlayList currentPlayList;
    private View headerLayout;
    private FrameLayout progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_drawner);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }
        progress=(FrameLayout)findViewById(R.id.progress);
        progress.setVisibility(View.GONE);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        headerLayout = navigationView.getHeaderView(0);
        imagePlayList = (ImageView) findViewById(R.id.thumbnailPlaylist);
        imageAvatar = (ImageView) headerLayout.findViewById(R.id.imageAvatar);
        listVideos = (RecyclerView) findViewById(R.id.list);
        listVideos.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        listVideos.setItemAnimator(new DefaultItemAnimator());
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinated);
        collapsingView = (CollapsingToolbarLayout) findViewById(R.id.collapser);

        playlists = new ArrayList<PlayList>();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        if (!Utilities.TestConnection(getApplicationContext())) {
            new AlertDialog.Builder(this)
                    .setTitle("Ups! No se detecto ninguna red activa")
                    .setMessage("Deseas salir de la aplicación?")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    })
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // do nothing
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();


        }else{
            AsyncJSON asyncjson = new AsyncJSON(PlayList_Activity.this,null, Utilities.URLPAYLIST + Utilities.APIKEY, Utilities.TYPEPLAYLIST);
            asyncjson.delegate = this;
            asyncjson.execute();
        }

        

    }

    @Override
    protected void onResume() {
        super.onResume();


    }
    public void closeProgress(View v){
        v.setVisibility(View.GONE);
    }
    private ArrayList<PlayList> loadPlayLists(JSONObject json) {
        ArrayList<PlayList> pllist = new ArrayList<>();
        try {
            JSONArray jsonArray = json.getJSONArray("items");

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject root = jsonArray.getJSONObject(i);
                String id = root.getString("id");
                String title = root.optJSONObject("snippet").getString("title");
                String description = root.optJSONObject("snippet").getString("description");
                String thumbnail = root.getJSONObject("snippet").getJSONObject("thumbnails").getJSONObject("high").getString("url");
                PlayList playlist = new PlayList(title, id, description, thumbnail);
                pllist.add(playlist);

            }
        } catch (JSONException e) {
            Log.e("Error 117:", e.getMessage());
        } catch (Exception ex) {
            Log.e("Error 119:", ex.getMessage());
        }
        return pllist;

    }
    private void loadVideos(PlayList playlist) {
        String url = Utilities.URLVIDEOS + Utilities.APIKEY;
        url = url.replace("###", playlist.getPlaylistid());
        collapsingView.setTitle(playlist.getTitle().toString());
        Picasso.with(this).load(playlist.getThumbnail()).into(imagePlayList);

        AsyncJSON asyncjson = new AsyncJSON(PlayList_Activity.this,progress, url, Utilities.TYPEVIDEOS);
        asyncjson.delegate = this;
        asyncjson.execute();
    }

    private void displayVideoList(Context con,JSONObject obj, RecyclerView recyclerList) {
        ArrayList<Video> list = null;
        PlayListAdapter adapter = null;
        try {
            list = new ArrayList<Video>();
            JSONArray jsonArray = obj.getJSONArray("items");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject root = jsonArray.getJSONObject(i);
                String id = root.optJSONObject("contentDetails").getString("videoId");
                String title = root.optJSONObject("snippet").getString("title");
                String playListId = root.optJSONObject("snippet").getString("playlistId");
                String description = root.optJSONObject("snippet").getString("description");
                String date = root.optJSONObject("snippet").getString("publishedAt");
                String thumbnail = root.getJSONObject("snippet").getJSONObject("thumbnails").getJSONObject("high").getString("url");
                Video vid = new Video();
                vid.videoid = id;
                vid.title = title;
                vid.description = description;
                vid.thumbnail = thumbnail;
                vid.playlistId = playListId;
                vid.date = Utilities.parseUTCDate(date);
                list.add(vid);
            }
        } catch (JSONException e) {
            Log.e("Error 160:", e.getMessage());
        } catch (Exception ex) {
            Log.e("Error 162:", ex.getMessage());
        }

        if (list.size() > 0) {
            adapter = new PlayListAdapter(con,PlayList_Activity.this, list);
            recyclerList.setAdapter(adapter);
        }
    }



    private void loadMenuPlaylist(ArrayList<PlayList> list) {
        Picasso.with(PlayList_Activity.this).load(Utilities.AVATARURL).transform(new CircleTransform()).into(imageAvatar);
        MenuItem menu = navigationView.getMenu().getItem(0);
        SubMenu submenu = menu.getSubMenu();
        submenu.clear();
        for (int i = 0; i < list.size(); i++) {
            submenu.add(R.id.group_playlist, i, Menu.NONE, ((PlayList) list.get(i)).getTitle());
        }

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_reload) {
            loadVideos(currentPlayList);
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        if (item.getGroupId() == R.id.group_playlist) {
            int id = item.getItemId();
            currentPlayList = (PlayList) playlists.get(id);
            loadVideos(currentPlayList);
            Picasso.with(getApplicationContext()).load(currentPlayList.getThumbnail()).into(imagePlayList);
            collapsingView.setTitle(currentPlayList.getTitle());
        }else if(item.getItemId()==R.id.settings){
            startActivity(new Intent(this,SettingsActivity.class));
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void asyncFinish(JSONObject json, int type) {

        if (type == Utilities.TYPEPLAYLIST) {

            this.playlists = loadPlayLists(json);

            if (playlists.size() > 0) {

                loadMenuPlaylist(playlists);

                currentPlayList = (PlayList) playlists.get(0);

                loadVideos(currentPlayList);
            }

        }else if (type == Utilities.TYPEVIDEOS) {
            displayVideoList(PlayList_Activity.this, json, listVideos);

        }

    }
}
