package com.example.howdoufeel.Database;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.howdoufeel.Model.Song;
import com.example.howdoufeel.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.ViewHolder>
{
    Context context;
    ArrayList<Song> baihatArrayList;

    public FavoriteAdapter(Context context, ArrayList<Song> baihatArrayList) {
        this.context = context;
        this.baihatArrayList = baihatArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view=inflater.inflate(R.layout.song_item,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
    Song baihat = baihatArrayList.get(position);
    holder.txtcasi.setText(baihat.getArtists());
    holder.txtten.setText(baihat.getName());
    Picasso.get().load(baihat.getImageUri()).into(holder.imghinh);

    }

    @Override
    public int getItemCount() {
        return baihatArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView txtten,txtcasi;
        ImageView imghinh,imgluotthich;
        public ViewHolder(View itemView)
        {
            super(itemView);
            txtten=itemView.findViewById((R.id.textviewtenbaihathot));
            txtcasi=itemView.findViewById(R.id.textviewcasi);
            imghinh=itemView.findViewById(R.id.imageviewbaihathot);
            imgluotthich=itemView.findViewById(R.id.imageviewluotthich);
        }
    }
}
