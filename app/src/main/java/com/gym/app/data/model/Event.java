package com.gym.app.data.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.sql.Timestamp;

/**
 * @author Paul
 * @since 2017.08.30
 */

@Entity
public class Event {
    @PrimaryKey
    @ColumnInfo(name = "id")
    @SerializedName("id")
    @NonNull
    public String mId;

    @ColumnInfo(name = "name")
    @SerializedName("name")
    public String mName;

    @ColumnInfo(name = "start_date")
    @SerializedName("start_date")
    public Timestamp mStartDate;

    @ColumnInfo(name = "end_date")
    @SerializedName("end_date")
    public Timestamp mEndDate;

    @ColumnInfo(name = "ticket_count")
    @SerializedName("ticket_count")
    public int mTicketCount;

    @ColumnInfo(name = "remaining_count")
    @SerializedName("remaining_count")
    public int mRemainingCount;

    @ColumnInfo(name = "description")
    @SerializedName("description")
    public String mDescription;

    @ColumnInfo(name = "image_url")
    @SerializedName("image_url")
    public String mImageUrl;

    @ColumnInfo(name = "currency")
    @SerializedName("currency")
    public String mCurrency;

    @ColumnInfo(name = "url")
    @SerializedName("url")
    public String mUrl;

    @ColumnInfo(name = "embed_url")
    @SerializedName("embed_url")
    public String mEmbedUrl;

    @ColumnInfo(name = "price_min")
    @SerializedName("price_min")
    public String mPriceMin;

    @ColumnInfo(name = "price_max")
    @SerializedName("price_max")
    public int mPriceMax;

    @ColumnInfo(name = "location")
    @SerializedName("location")
    public String mLocation;


    @NonNull
    public String getId() {
        return mId;
    }

    public void setId(@NonNull String id) {
        mId = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public Timestamp getStartDate() {
        return mStartDate;
    }

    public void setStartDate(Timestamp startDate) {
        mStartDate = startDate;
    }

    public Timestamp getEndDate() {
        return mEndDate;
    }

    public void setEndDate(Timestamp endDate) {
        mEndDate = endDate;
    }

    public int getTicketCount() {
        return mTicketCount;
    }

    public void setTicketCount(int ticketCount) {
        mTicketCount = ticketCount;
    }

    public int getRemainingCount() {
        return mRemainingCount;
    }

    public void setRemainingCount(int remainingCount) {
        mRemainingCount = remainingCount;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public String getImageUrl() {
        return mImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        mImageUrl = imageUrl;
    }

    public String getCurrency() {
        return mCurrency;
    }

    public void setCurrency(String currency) {
        mCurrency = currency;
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String url) {
        mUrl = url;
    }

    public String getEmbedUrl() {
        return mEmbedUrl;
    }

    public void setEmbedUrl(String embedUrl) {
        mEmbedUrl = embedUrl;
    }

    public String getPriceMin() {
        return mPriceMin;
    }

    public void setPriceMin(String priceMin) {
        mPriceMin = priceMin;
    }

    public int getPriceMax() {
        return mPriceMax;
    }

    public void setPriceMax(int priceMax) {
        mPriceMax = priceMax;
    }

    public String getLocation() {
        return mLocation;
    }

    public void setLocation(String location) {
        mLocation = location;
    }
}
