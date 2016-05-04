package com.base.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.base.R;

import java.util.ArrayList;
import java.util.List;

public class NavigationDrawerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private LayoutInflater layoutInflater;
    private ArrayList<String> itemList;
    private Activity context;
    private ItmClicked itmClicked;

    public NavigationDrawerAdapter(Activity context) {
        this.context = context;
        itmClicked = (ItmClicked) context;
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        itemList = new ArrayList<>();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ItemViewHolder(layoutInflater.inflate(R.layout.navigation_drawer_items, parent, false));
    }

    public void addAll(List<String> itemList) {
        this.itemList.addAll(itemList);
        notifyDataSetChanged();
    }

    public void clear() {
        itemList.clear();
        notifyDataSetChanged();
    }

    public boolean isEmpty() {
        return itemList.isEmpty();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {

        final ItemViewHolder holder = (ItemViewHolder) viewHolder;
        holder.txtCities.setText(itemList.get(position));

        holder.ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itmClicked.onItemClick(position);
            }
        });

    }


    @Override
    public int getItemCount() {
        return itemList.size();
    }


    public class ItemViewHolder extends RecyclerView.ViewHolder {
        public TextView txtCities;
        public RelativeLayout ll;

        public ItemViewHolder(View view) {
            super(view);
            LinkViews(view);
        }

        private void LinkViews(View view) {
            txtCities = (TextView) view.findViewById(R.id.txt_cities_name);
            ll = (RelativeLayout) view.findViewById(R.id.ll_item_cites);
        }
    }

    public interface ItmClicked {
        void onItemClick(int position);
    }
}