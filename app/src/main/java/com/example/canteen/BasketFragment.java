package com.example.canteen;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

public class BasketFragment extends Fragment {

    private AppDatabase db;
    private ArrayList<Basket> basketArrayList;
    private BasketAdapter adapter;
    private String mealName;
    private String mealDescription;
    private String mealPrice;
    private String mealId;
    private float mealPriceFloat;
    Double total = 0.00;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_basket, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //Initialise DB
        db = AppDatabase.getAppDatabase(getContext());

        showBasket();

        basketArrayList = new ArrayList<Basket>();
        adapter = new BasketAdapter(this.getActivity(), basketArrayList);
        ListView listView = (ListView) getActivity().findViewById(R.id.basket_list);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                listView.getChildAt(position).findViewById(R.id.basket_meal_Id);

                TextView name = listView.getChildAt(position).findViewById(R.id.basket_meal_name);
                TextView quantity = (TextView) getActivity().findViewById(R.id.basket_meal_quantity);
                TextView subtotal = (TextView) getActivity().findViewById(R.id.basket_meal_subtotal);
                TextView trayMealId = listView.getChildAt(position).findViewById(R.id.basket_meal_Id);
                TextView trayMealDescription = listView.getChildAt(position).findViewById(R.id.basket_meal_description);
                TextView trayMealImage = listView.getChildAt(position).findViewById(R.id.basket_meal_image_url);

                String mealName = name.getText().toString();
                String mealId = trayMealId.getText().toString();
                String mealDescription = trayMealDescription.getText().toString();
                String mealImage = trayMealImage.getText().toString();


                Intent intent = new Intent(getContext(), UpdateMealActivity.class);
                intent.putExtra("mealId", mealId);
                intent.putExtra("mealName", mealName);
                intent.putExtra("mealDescription", mealDescription);
                intent.putExtra("mealImage", mealImage);
                startActivity(intent);


            }
        });


        Button buttonAddPayment = (Button) getActivity().findViewById(R.id.button_add_payment);
        buttonAddPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), CheckoutActivityJava.class);
                intent.putExtra("mealPrice", "12");
                startActivity(intent);
            }
        });


        Button clearBasketButton = (Button) getActivity().findViewById(R.id.clear_basket);
        clearBasketButton.setBackgroundColor(Color.rgb(246, 31, 65));
        clearBasketButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteBasket();
                Intent intent = new Intent(getActivity().getApplicationContext(), MenuActivity.class);
                intent.putExtra("stopbackpressed", "stop");
                Toast.makeText(getActivity().getApplicationContext(), "Basket cleared", Toast.LENGTH_SHORT).show();
                startActivity(intent);
            }
        });

        Button BackToMenu = (Button) getActivity().findViewById(R.id.BackToMenu);
        BackToMenu.setBackgroundColor(Color.rgb(187, 134, 252));
        BackToMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity().getApplicationContext(), MenuActivity.class);
                startActivity(intent);
            }
        });

    }

    @SuppressLint("StaticFieldLeak")
    private void showBasket() {
        new AsyncTask<Void, Void, List<Basket>>() {

            @Override
            protected List<Basket> doInBackground(Void... voids) {
                return db.basketDAO().getBasket();
            }

            @Override
            protected void onPostExecute(List<Basket> baskets) {
                super.onPostExecute(baskets);
                if (!baskets.isEmpty()) {
                    //Refresh the list view
                    basketArrayList.clear();
                    basketArrayList.addAll(baskets);
                    adapter.notifyDataSetChanged();

                    // Calculate the total
                    float total = 0;
                    for (Basket basket : baskets) {
                        total = total + basket.getMealQuantity() * basket.getMealPrice();
                    }

                    TextView totalView = (TextView) getActivity().findViewById(R.id.basket_total);
                    totalView.setText("£" + total);

                } else {
                    TextView alertText = new TextView(getActivity());
                    alertText.setText("Your basket is empty. " + "\n" + "Please click on this link to go back to the menu and order a meal");

                    alertText.setTextSize(22);
                    alertText.setGravity(Gravity.CENTER);

                    ImageView img = new ImageView(getActivity());
                    img.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_shopping_basket));
                    img.setForegroundGravity(Gravity.BOTTOM);


                    LinearLayout linearLayout = (LinearLayout) getActivity().findViewById(R.id.basket_layout);
                    linearLayout.removeAllViews();
                    linearLayout.addView(alertText);
                    linearLayout.addView(img);


                    alertText.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(getActivity().getApplicationContext(), MenuActivity.class);
                            startActivity(intent);
                        }
                    });

                }
            }
        }.execute();
    }


    @SuppressLint("StaticFieldLeak")
    public void deleteBasket() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                db.basketDAO().deleteAll();
                return null;
            }
        }.execute();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    public void callParentMethod() {
        getActivity().onBackPressed();
    }


}





































//    // TODO: Rename parameter arguments, choose names that match
//    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";
//
//    // TODO: Rename and change types of parameters
//    private String mParam1;
//    private String mParam2;
//
//    public BasketFragment() {
//        // Required empty public constructor
//    }
//
//    /**
//     * Use this factory method to create a new instance of
//     * this fragment using the provided parameters.
//     *
//     * @param param1 Parameter 1.
//     * @param param2 Parameter 2.
//     * @return A new instance of fragment BasketFragment.
//     */
//    // TODO: Rename and change types and number of parameters
//    public static BasketFragment newInstance(String param1, String param2) {
//        BasketFragment fragment = new BasketFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
//        return fragment;
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_basket, container, false);
//    }
