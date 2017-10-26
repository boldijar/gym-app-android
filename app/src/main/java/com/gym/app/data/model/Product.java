package com.gym.app.data.model;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Paul
 * @since 2017.10.25
 */

public class Product {

    public String mName;
    public String mImage;
    public String mDescription;
    public String mPrice;


    public static List<Product> getProducts() {
        ArrayList<Product> products = new ArrayList<>();
        for (int i = 0; i <= 130; i++) {
            Product product = randomProduct();
            products.add(product);
        }
        return products;
    }

    public static Product randomProduct() {
        Product product = new Product();
        product.mDescription = "Very good for bulking, great piece, cheap bla bla";
        product.mName = "Whey Protein Impact";
        product.mPrice = "199 RON";
        if (Math.random() < .5) {
            product.mDescription += " very good worth it ";
        }
        if (Math.random() < .25) {
            product.mDescription += " is very cheap but must go yes bla bla very long description ";
        }
        if (Math.random() < .3) {
            product.mDescription += " very good worth  i like lalalala good it ";
        }
        if (Math.random() < .3) {
            product.mImage = "https://upload.wikimedia.org/wikipedia/commons/thumb/3/31/Ice_Cream_dessert_02.jpg/220px-Ice_Cream_dessert_02.jpg";
        } else if (Math.random() < .3) {
            product.mImage = "https://www.toppers.com/Portals/0/house-pizza-toppers-classic.jpg";
        } else {
            product.mImage = "http://i.imgur.com/cJo1HjM.jpg";
        }
        return product;
    }
}
