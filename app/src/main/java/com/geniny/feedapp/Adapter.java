package com.geniny.feedapp;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Point;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Adapter extends ArrayAdapter<Article>
{
    private LayoutInflater inflater;
    private int layout;
    private ArrayList<Article> articles;
    private Picasso picasso;
    private int width;
    private Display display;
    private WindowManager windowManager;

    Adapter(Context context, int resource, ArrayList<Article> articles) {
        super(context, resource, articles);
        this.articles = articles;
        this.layout = resource;
        this.inflater = LayoutInflater.from(context);
        picasso = Picasso.with(context);
        windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        display = windowManager.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        width = size.x;
    }

    @NonNull
    public View getView(int position, View convertView, @NonNull ViewGroup parent)
    {
        ViewHolder viewHolder;
        if(convertView == null){
            convertView = inflater.inflate(this.layout, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final Article article = articles.get(position);
        viewHolder.title.setText(article.getTitle());
        viewHolder.date.setText(article.getDate());
        viewHolder.preview.setText(article.getPreview());

        if(!article.getImageURL().isEmpty()){
            try {
                picasso.load(article.getImageURL())
                        .placeholder(R.drawable.ic_placeholder)
                        .resize(width,0)
                        .into(viewHolder.image);
                viewHolder.image.setVisibility(View.VISIBLE);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        else
            viewHolder.image.setVisibility(View.GONE);

        return convertView;
    }

    private class ViewHolder {
        final ImageView image;
        final TextView title, preview, date;

        ViewHolder(View view){
            title = view.findViewById(R.id.title);
            preview = view.findViewById(R.id.preview);
            date = view.findViewById(R.id.date);
            image = view.findViewById(R.id.image);
        }
    }

}
