package com.example.xinyichen.reflect;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

/**
 * Created by xinyichen on 10/7/17.
 */

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.CustomViewHolder> {
    private Context context;
    private ArrayList<DaysData> data;

    ListAdapter(Context context, ArrayList<DaysData> data) {
        this.context = context;
        this.data = data;
    }


    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_view, parent, false);
        return new CustomViewHolder(view);
    }


    @Override
    public void onBindViewHolder(final CustomViewHolder holder, int position) {
        final DaysData m = data.get(position);
        holder.title.setText(m.title);
        holder.date.setText(m.date);
        holder.numInterest.setText(context.getResources().getString(R.string.pplInt, m.numInterested + ""));
        holder.email.setText(m.hostEmail);

        StorageReference pathReference = Utils.storageReference.child("/" + m.key + ".png");

        Glide.with(context)
                .using(new FirebaseImageLoader())
                .load(pathReference)
                .into(holder.eventImg);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), DataDetails.class);
                intent.putExtra("social", m);
                v.getContext().startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() { return data.size(); }

    /**
     * A card displayed in the RecyclerView
     */
    class CustomViewHolder extends RecyclerView.ViewHolder {
        TextView dateView;
        ImageView pieImg;
        TextView sentiment;

        CustomViewHolder(View view) {
            super(view);
            this.dateView = (TextView) view.findViewById(R.id.dateView);
            this.pieImg = (ImageView) view.findViewById(R.id.pieImg);
            this.sentiment = (TextView) view.findViewById(R.id.sentimentView);
        }
    }
}
