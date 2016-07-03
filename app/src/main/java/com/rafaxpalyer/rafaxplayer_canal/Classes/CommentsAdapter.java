package com.rafaxpalyer.rafaxplayer_canal.Classes;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.rafaxpalyer.rafaxplayer_canal.Helper.Utilities;
import com.rafaxpalyer.rafaxplayer_canal.Models.Comment;
import com.rafaxpalyer.rafaxplayer_canal.R;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.ViewHolder> {
    private ArrayList<Comment> list;
    private Context con;

    public CommentsAdapter(Context con, ArrayList<Comment> lst) {
        this.con = con;
        this.list = lst;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public CommentsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_comment, parent, false);

        ViewHolder holder = new ViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.nick.setText(((Comment) list.get(position)).getNick());
        holder.comment.setText(Html.fromHtml(((Comment) list.get(position)).getCommnet()));

        holder.published.setText("Publicado el : "+((Comment) list.get(position)).getPlublishedAt());
        Picasso.with(con).load(((Comment) list.get(position)).getImageuser()).into(holder.image);

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView nick;
        public TextView comment;
        private TextView published;
        public ImageView image;

        public ViewHolder(View v) {
            super(v);
            nick = (TextView) v.findViewById(R.id.textNick);
            published=(TextView) v.findViewById(R.id.textPlublished);
            comment = (TextView) v.findViewById(R.id.textComment);
            comment.setTextSize(TypedValue.COMPLEX_UNIT_SP,Float.valueOf(Utilities.getPrefs(con).getString("textsize","16")));
            image = (ImageView) v.findViewById(R.id.imageUser);

        }


    }
}
