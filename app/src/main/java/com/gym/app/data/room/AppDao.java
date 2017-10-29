package com.gym.app.data.room;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.gym.app.data.model.Product;

import java.util.List;

import io.reactivex.Single;

/**
 * @author Paul
 * @since 2017.09.25
 */

@Dao
public interface AppDao {

    @Query("select * from product")
    Single<List<Product>> getProducts();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertProducts(List<Product> products);

}
