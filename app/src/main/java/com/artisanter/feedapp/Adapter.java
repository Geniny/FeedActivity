package com.artisanter.feedapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

// Адаптер для представления Статьи
public class Adapter extends ArrayAdapter<Record>
{
    private LayoutInflater inflater;
    private int layout;
    private ArrayList<Record> records;
    private Picasso picasso;
    private int width;

    Adapter(Context context, int resource, ArrayList<Record> records, int width) {
        super(context, resource, records);
        this.records = records;
        this.layout = resource;
        this.width = width;
        this.inflater = LayoutInflater.from(context);
        picasso = Picasso.with(context);
    }
    @NonNull
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        View view = convertView == null?
                inflater.inflate(this.layout, parent, false)
                :convertView;
        if(convertView == null){
            view.setTag(new ViewHolder(view));
        }
        final ViewHolder holder = (ViewHolder)view.getTag();

        final Record record = records.get(position);

        holder.title.setText(record.getTitle());
        holder.date.setText(record.getDate());
        holder.preview.setText(record.getPreview());

        // Востановление кэшированного изображения
        if(!record.getImageURL().isEmpty()){
            try {
                picasso.load(record.getImageURL())
                        .placeholder(R.drawable.ic_placeholder)
                        .resize(width, 0)
                        .into(holder.image);
                holder.image.setVisibility(View.VISIBLE);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        else
            holder.image.setVisibility(View.GONE);

        return view;
    }

    private class ViewHolder
    {
        ViewHolder(View view) {
            this.view = view;
            title = view.findViewById(R.id.title);
            preview = view.findViewById(R.id.preview);
            date = view.findViewById(R.id.date);
            image = view.findViewById(R.id.image);
        }
        View view;
        TextView title;
        TextView preview;
        TextView date;
        ImageView image;
    }
}
