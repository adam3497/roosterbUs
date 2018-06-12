package org.roosterbus.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.roosterbus.R;

import java.util.ArrayList;

public class RoutesAdapter extends RecyclerView.Adapter<RoutesAdapter.ViewHolder> {

    private Context context;
    private ArrayList<CardRoutes> routesArray;

    public RoutesAdapter(Context context, ArrayList<CardRoutes> routesArray){
        this.context = context;
        this.routesArray = routesArray;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_routes, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.txtTitle.setText(routesArray.get(position).getTitle());
        holder.txtInitialName.setText(Character.toString(routesArray.get(position).getTitle().charAt(0)));
        holder.txtRate.setText(routesArray.get(position).getRate());
    }

    @Override
    public int getItemCount() {
        if(routesArray.isEmpty()){
            return 0;
        }
        else {
            return routesArray.size();
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        private TextView txtInitialName, txtTitle, txtRate;

        public ViewHolder(View view) {
            super(view);
            txtInitialName = (TextView) view.findViewById(R.id.txt_initial_routes_name);
            txtTitle = (TextView) view.findViewById(R.id.txt_routes_name);
            txtRate = (TextView) view.findViewById(R.id.txt_rate);
        }
    }
}
