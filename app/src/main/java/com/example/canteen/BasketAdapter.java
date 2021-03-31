package com.example.canteen;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class BasketAdapter extends BaseAdapter {

    private Activity activity;
    private ArrayList<Basket> basketArrayList;

    public BasketAdapter(Activity activity, ArrayList<Basket> basketArrayList) {
        this.activity = activity;
        this.basketArrayList = basketArrayList;
    }

    @Override
    public int getCount() {
        return basketArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return basketArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null) {
            convertView = LayoutInflater.from(activity).inflate(R.layout.basket_item, null);

        }

        TextView mealName = (TextView) convertView.findViewById(R.id.basket_meal_name);
        TextView mealQuantity = (TextView) convertView.findViewById(R.id.basket_meal_quantity);
        TextView mealSubTotal = (TextView) convertView.findViewById(R.id.basket_meal_subtotal);
        TextView mealId = (TextView) convertView.findViewById(R.id.basket_meal_Id);
        TextView mealDescription = (TextView) convertView.findViewById(R.id.basket_meal_description);
        TextView mealImage = (TextView) convertView.findViewById(R.id.basket_meal_image_url);

//        TextView mealNameBasket = (TextView) convertView.findViewById(R.id.mealNameBasket);
//        TextView mealDescriptionBasket = (TextView) convertView.findViewById(R.id.mealDescriptionBasket);
//        TextView mealPriceBasket = (TextView) convertView.findViewById(R.id.mealPriceBasket);
//        TextView mealIDBasket = (TextView) convertView.findViewById(R.id.mealIDBasket);
//



        Basket basket = basketArrayList.get(position);

        mealName.setText(basket.getMealName());
        mealQuantity.setText(basket.getMealQuantity() + "");
        mealSubTotal.setText("£" + (basket.getMealPrice() * basket.getMealQuantity()));
        mealDescription.setText(basket.getMealDescription());
        mealImage.setText(basket.getMealImage());
        mealId.setText(basket.getMealId());




        return convertView;
    }
}
