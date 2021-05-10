package com.example.canteen;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.IngredientViewHolder> {
    private ArrayList<String> mealIDList;
    private ArrayList<String> nameList;
    private ArrayList<String> descriptionList;
    private ArrayList<String> ingredientList;
    private ArrayList<String> priceList;
    private ArrayList<String> urlList;
    Context mContext;
    String universityInitials;
    String email;

    public IngredientAdapter(ArrayList<String> mealIDList, ArrayList<String> nameList, ArrayList<String> descriptionList, ArrayList<String> ingredientList,
                             ArrayList<String> priceList, ArrayList<String> urlList, Context mContext, String universityInitials, String email) {
        this.mealIDList = mealIDList;
        this.nameList = nameList;
        this.descriptionList = descriptionList;
        this.ingredientList = ingredientList;
        this.priceList = priceList;
        this.urlList = urlList;
        this.mContext = mContext;
        this.universityInitials = universityInitials;
        this.email = email;
    }

    @NonNull
    @Override
    public IngredientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Inflating the meal_item
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.meal_item, parent, false);
        IngredientViewHolder ingredientViewHolder = new IngredientViewHolder(view);
        return ingredientViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientViewHolder holder, int position) {
        holder.mealName.setText(nameList.get(position));
        holder.mealDescription.setText(descriptionList.get(position));
        holder.mealIngredients.setText(ingredientList.get(position));
        holder.mealPrice.setText(priceList.get(position));
        holder.mealID.setText(mealIDList.get(position));
        Glide.with(holder.mealImage.getContext()).load(urlList.get(position)).into(holder.mealImage);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, MealDetailActivity.class);
                intent.putExtra("mealId", mealIDList.get(position));
                intent.putExtra("mealName", nameList.get(position));
                intent.putExtra("mealDescription", descriptionList.get(position));
                intent.putExtra("mealPrice", priceList.get(position));
                intent.putExtra("mealImage", urlList.get(position));
                intent.putExtra("mealIngredients", ingredientList.get(position));
                intent.putExtra("universityInitials", universityInitials);
                intent.putExtra("email", email);
                Log.d("TAG", "onClick: " + email);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mealIDList.size();
    }

    public class IngredientViewHolder extends RecyclerView.ViewHolder {
        private TextView mealID, mealName, mealDescription, mealIngredients, mealPrice;
        private ImageView mealImage;

        public IngredientViewHolder(@NonNull View parent) {
            super(parent);
            mealID = itemView.findViewById(R.id.mealID);
            mealName = itemView.findViewById(R.id.mealName);
            mealDescription = itemView.findViewById(R.id.mealDescription);
            mealIngredients = itemView.findViewById(R.id.mealIngredients);
            mealPrice = itemView.findViewById(R.id.mealPrice);
            mealImage = itemView.findViewById(R.id.mealImage);
        }
    }
}
