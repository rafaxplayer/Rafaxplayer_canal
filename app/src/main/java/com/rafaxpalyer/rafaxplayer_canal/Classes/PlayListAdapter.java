package com.rafaxpalyer.rafaxplayer_canal.Classes;

import android.animation.Animator;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.rafaxpalyer.rafaxplayer_canal.Details_Activity;
import com.rafaxpalyer.rafaxplayer_canal.Helper.Utilities;
import com.rafaxpalyer.rafaxplayer_canal.Models.Video;
import com.rafaxpalyer.rafaxplayer_canal.PlayList_Activity;
import com.rafaxpalyer.rafaxplayer_canal.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class PlayListAdapter extends RecyclerView.Adapter<PlayListAdapter.ViewHolder> {
    private ArrayList<Video> list;
    private Context con;
    private Activity act;
    private FrameLayout progress;

    public PlayListAdapter(Context con, Activity act, ArrayList<Video> lst) {
        this.con = con;
        this.act = act;
        this.list = lst;

    }

    @Override
    public PlayListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_vdeo_list, parent, false);

        ViewHolder holder = new ViewHolder(v);
        return holder;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.title.setText(((Video) list.get(position)).getTitle());

        holder.date.setText("Publicado el : "+((Video) list.get(position)).getDate());
        Picasso.with(con).load(((Video) list.get(position)).getThumbnail()).resize(200, 150).centerCrop().into(holder.thumbnail);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView title;
        public TextView date;
        public ImageView thumbnail;

        public ViewHolder(View v) {
            super(v);
            title = (TextView) v.findViewById(R.id.textTitle);
            date = (TextView) v.findViewById(R.id.textDate);
            thumbnail = (ImageView) v.findViewById(R.id.imageVideo);
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(con, Details_Activity.class);
            intent.putExtra("video", (Video) list.get(ViewHolder.this.getLayoutPosition()));

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                ActivityOptions transitionActivityOptions = ActivityOptions.makeSceneTransitionAnimation(
                        act, Pair.create(v.findViewById(R.id.imageVideo), thumbnail.getTransitionName()));
                con.startActivity(intent, transitionActivityOptions.toBundle());
            } else {
                con.startActivity(intent);
            }

        }
    }
}
