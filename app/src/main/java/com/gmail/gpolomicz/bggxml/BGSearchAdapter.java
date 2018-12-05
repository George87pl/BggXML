package com.gmail.gpolomicz.bggxml;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import java.util.List;

public class BGSearchAdapter extends ArrayAdapter {
    private static final String TAG = "GPDEB";

    private final int layoutResource;
    private final LayoutInflater layoutInflater;
    private List<BGSearchEntry> bgEntry;

    public BGSearchAdapter(Context context, int resource, List<BGSearchEntry> bgEntry) {
        super(context, resource);
        this.layoutResource = resource;
        this.layoutInflater = LayoutInflater.from(context);
        this.bgEntry = bgEntry;
    }

    @Override
    public int getCount() {
        return bgEntry.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = layoutInflater.inflate(layoutResource, parent, false);

            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        BGSearchEntry currentInfo = bgEntry.get(position);

        viewHolder.title.setText(currentInfo.getTitle());
        viewHolder.date.setText(currentInfo.getPubDate());

        Picasso.get().load(currentInfo.getImage()).into(viewHolder.image);

        return convertView;
    }

    private class ViewHolder {
        final TextView title;
        final TextView date;
        final ImageView image;

        ViewHolder(View v) {
            this.title = v.findViewById(R.id.bg_title_search);
            this.date = v.findViewById(R.id.bg_date_search);
            this.image = v.findViewById(R.id.bg_image_search);
        }
    }

}
