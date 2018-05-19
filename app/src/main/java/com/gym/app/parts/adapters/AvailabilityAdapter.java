package com.gym.app.parts.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gym.app.R;
import com.gym.app.data.model.Availability;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AvailabilityAdapter extends RecyclerView.Adapter<AvailabilityAdapter.ViewHolder> {
    private List<Availability> mAvailabilities = new ArrayList<>();
    private final static SimpleDateFormat sFormat = new SimpleDateFormat("EEE, d MMM yyyy HH:mm");
    private final AvailabilityListener mAvailabilityListener;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case

        @BindView(R.id.item_availability_title)
        TextView mTitle;
        @BindView(R.id.item_availability_add)
        View mAdd;
        @BindView(R.id.item_availability_remove)
        View mRemove;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public AvailabilityAdapter(AvailabilityListener availabilityListener) {


        mAvailabilityListener = availabilityListener;
    }

    public void setAvailabilities(List<Availability> mCarsList) {
        this.mAvailabilities = mCarsList;
        notifyDataSetChanged();
    }

    // Create new views (invoked by the layout manager)
    @Override
    public AvailabilityAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_availability, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Availability availability = mAvailabilities.get(position);
        holder.mTitle.setText(availability.mDescription + " from " + availability.mStartDatetime + " to " + availability.mEndDatetime);
        holder.mAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAvailabilityListener.onEdit(availability.mId);
            }
        });
        holder.mRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAvailabilityListener.onDelete(availability.mId);
            }
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mAvailabilities.size();
    }

    public interface AvailabilityListener {
        void onDelete(int id);

        void onEdit(int id);
    }
}