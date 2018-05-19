package com.gym.app.parts.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gym.app.R;
import com.gym.app.data.model.Car;

import java.util.List;

public class CarsAdapter extends RecyclerView.Adapter<CarsAdapter.ViewHolder> {
    private List<Car> mCarsList;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case

        public TextView carPlateRowText;
        public TextView carSizeRowText;
        public TextView carModelRowText;

        public ViewHolder(View view) {
            super(view);
            carPlateRowText = (TextView) view.findViewById(R.id.carPlateRowText);
            carSizeRowText = (TextView) view.findViewById(R.id.carSizeRowText);
            carModelRowText = (TextView) view.findViewById(R.id.carModelRowText);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public CarsAdapter(List<Car> cars) {
        mCarsList = cars;
    }

    public void setmCarsList(List<Car> mCarsList) {
        this.mCarsList = mCarsList;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public CarsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v =  LayoutInflater.from(parent.getContext()).inflate(R.layout.cars_list_row, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.carModelRowText.setText(mCarsList.get(position).getModel());
        holder.carSizeRowText.setText(mCarsList.get(position).getSize());
        holder.carPlateRowText.setText(mCarsList.get(position).getPlate());

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mCarsList.size();
    }
}