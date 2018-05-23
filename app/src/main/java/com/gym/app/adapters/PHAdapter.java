package com.gym.app.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;

import com.gym.app.R;
import com.gym.app.data.model.ParkingHistory;
import com.gym.app.data.model.ParkingHistoryBody;

import java.util.ArrayList;
import java.util.Locale;

import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class PHAdapter extends RecyclerView.Adapter<PHAdapter.PHViewHolder> {

    private ArrayList<ParkingHistory> mPHs;
    private AdapterView.OnItemClickListener listener;

    @BindView(R.id.ph_list_item_name)
    EditText mName;

    @BindView(R.id.ph_list_item_address)
    EditText mAddress;

    @BindView(R.id.ph_list_item_price)
    EditText mPrice;

    @BindView(R.id.ph_list_item_start_date)
    EditText mStartDate;

    @BindView(R.id.ph_list_item_end_date)
    EditText mEndDate;

    public PHAdapter(ArrayList<ParkingHistory> mParkingHistoriesList) {
        mPHs = mParkingHistoriesList;
    }

    public class PHViewHolder extends RecyclerView.ViewHolder {
        TextView spot;
        TextView address;
        TextView start_date;
        TextView end_date;
        TextView price;

        PHViewHolder(View itemView) {
            super(itemView);
            spot = (TextView) itemView.findViewById(R.id.ph_list_item_name);
            address = (TextView) itemView.findViewById(R.id.ph_list_item_address);
            price = (TextView) itemView.findViewById(R.id.ph_list_item_price);
            start_date = (TextView) itemView.findViewById(R.id.ph_list_item_start_date);
            end_date = (TextView) itemView.findViewById(R.id.ph_list_item_end_date);
        }


    }

    public void setmPHs(ArrayList<ParkingHistory> ph){
        mPHs = ph;
    }

    @Override
    public int getItemCount() {
        return mPHs.size();
    }

    @Override
    public PHViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.parking_history_line, parent, false);
        return new PHViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PHViewHolder holder, int position) {
        holder.spot.setText(String.format(Locale.getDefault(), "%s", mPHs.get(position).getParkSpot().mName));
        holder.address.setText(String.format(Locale.getDefault(), "%s", mPHs.get(position).getParkSpot().mAddress));
        holder.price.setText(String.format(Locale.getDefault(), "%s", mPHs.get(position).getParkSpot().mPricePerHour));
        holder.start_date.setText(String.format(Locale.getDefault(), "%s", mPHs.get(position).getStart_date()));
        holder.end_date.setText(String.format(Locale.getDefault(), "%s", mPHs.get(position).getEnd_date()));
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

}