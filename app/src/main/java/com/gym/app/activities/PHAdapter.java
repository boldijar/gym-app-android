package com.gym.app.activities;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gym.app.R;
import com.gym.app.data.model.ParkingHistory;

import java.util.ArrayList;

import javax.security.auth.Subject;

public class PHAdapter extends RecyclerView.Adapter<PHAdapter.PHViewHolder> {

    public class PHViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        RecyclerView rv;

        PHViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            rv = (RecyclerView) itemView.findViewById(R.id.parking_histories);
        }

        @Override
        public void onClick(View v) {
            if (mOnEntryClickListener != null) {
                mOnEntryClickListener.onEntryClick(v, getLayoutPosition());
            }
        }
    }

    private Context mContext;
    private ArrayList<ParkingHistory> mPHs;

    public PHAdapter(Context context, ArrayList<ParkingHistory> arrayPHs) {
        mContext = context;
        mPHs = arrayPHs;

    }

    @Override
    public int getItemCount() {
        return mPHs.size();
    }

    @Override
    public PHViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_parking_history, parent, false);
        return new PHViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PHViewHolder holder, int position) {
        ParkingHistory ph = mPHs.get(position);
        String subject = ph.getSpot(mContext);

    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }


    private OnEntryClickListener mOnEntryClickListener;

    public interface OnEntryClickListener {
        void onEntryClick(View view, int position);
    }

    public void setOnEntryClickListener(OnEntryClickListener onEntryClickListener) {
        mOnEntryClickListener = onEntryClickListener;
    }

}