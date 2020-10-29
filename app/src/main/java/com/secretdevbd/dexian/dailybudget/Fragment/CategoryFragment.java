package com.secretdevbd.dexian.dailybudget.Fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.secretdevbd.dexian.dailybudget.DB.Category;
import com.secretdevbd.dexian.dailybudget.DB.DBhandler;
import com.secretdevbd.dexian.dailybudget.ItemClickListener;
import com.secretdevbd.dexian.dailybudget.R;

import java.util.ArrayList;

public class CategoryFragment extends Fragment {

    String TAG = "XIAN";

    FloatingActionButton fab_addCat;
    RecyclerView RV_categories;

    RecyclerView.Adapter mRecycleAdapter;
    RecyclerView.LayoutManager mLayoutManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_category, container, false);

        fab_addCat = view.findViewById(R.id.fab_addCat);
        RV_categories = view.findViewById(R.id.RV_categories);

        ArrayList<Category> categories = new DBhandler(getContext()).getAllCategories();
        Log.i(TAG,"DB cat Size : "+categories.size());

        mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        RV_categories.setLayoutManager(mLayoutManager);
        mRecycleAdapter = new RecycleViewAdapterForCategories(getContext(), categories);
        RV_categories.setAdapter(mRecycleAdapter);

        fab_addCat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addCategory();
            }
        });

        return view;
    }
    private void addCategory(){

        AlertDialog.Builder myBuilder = new AlertDialog.Builder(getContext());
        View myView = getLayoutInflater().inflate(R.layout.add_category, null);

        final EditText ET_categoryName;
        final Spinner SP_categoryType;
        Button btn_addCategory;



        ET_categoryName = myView.findViewById(R.id.ET_categoryName);
        SP_categoryType = myView.findViewById(R.id.SP_categoryType);
        btn_addCategory = myView.findViewById(R.id.btn_addCategory);

        String[] types = {"Income", "Expense"};
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity().getApplicationContext(), android.R.layout.simple_spinner_item, types);
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        SP_categoryType.setAdapter(dataAdapter);

        myBuilder.setView(myView);
        final AlertDialog dialog = myBuilder.create();

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        dialog.getWindow().setLayout(width, height); //Controlling width and height.

        dialog.show();


        btn_addCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Name = ET_categoryName.getText().toString();
                String type = SP_categoryType.getSelectedItem().toString();

                if(Name.length()>0 && type.length()>0){
                    DBhandler DBH = new DBhandler(getActivity().getApplicationContext());

                    DBH.addCategory(type, Name);

                    Toast.makeText(getActivity().getApplicationContext(), "Category Added", Toast.LENGTH_SHORT).show();
                    dialog.cancel();

                    // Reload current fragment
                    Fragment frg = getActivity().getSupportFragmentManager().findFragmentById(R.id.fragment_container);
                    final FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                    ft.detach(frg);
                    ft.attach(frg);
                    ft.commit();
                }else{
                    Toast.makeText(getActivity().getApplicationContext(), "Please write a name of this category", Toast.LENGTH_SHORT).show();
                }



            }
        });


    }

    public class RecycleViewAdapterForCategories extends RecyclerView.Adapter<RecycleViewAdapterForCategories.ViewHolder> {

        String TAG = "XIAN";

        ArrayList<Category> categories;
        Context context;

        public RecycleViewAdapterForCategories(Context context, ArrayList<Category> categories) {
            super();
            this.context = context;
            this.categories = categories;
            //Log.i(TAG,"RECYCLE VIEW Constructor");
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View v = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.single_category, viewGroup, false);
            ViewHolder viewHolder = new ViewHolder(v);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(ViewHolder viewHolder, final int i) {

            //Log.i(TAG,i+" RECYCLE VIEW "+(productList.get(i).getProductName()));

            /*if(!PigeonList.get(i).getPigeonPicture().equalsIgnoreCase("Null")){
                viewHolder.IV_pigeonPicture.setImageBitmap(decodeBase64Image(PigeonList.get(i).getPigeonPicture()));


            }else{

            }*/
            viewHolder.TV_catName.setText(categories.get(i).getCname());
            viewHolder.TV_catType.setText(categories.get(i).getCtype());

            if(categories.get(i).getCtype().equals("Income")){
                viewHolder.IV_cat.setImageDrawable(getResources().getDrawable(R.drawable.ic_add_green));
                viewHolder.TV_catType.setTextColor(getResources().getColor(R.color.dark_green));
            }else{
                viewHolder.IV_cat.setImageDrawable(getResources().getDrawable(R.drawable.ic_remove_red));
            }

            viewHolder.setClickListener(new ItemClickListener() {
                @Override
                public void onClick(View view, int position, boolean isLongClick) {
                    if (isLongClick) {

                    } else {
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return categories.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

            private ImageView IV_cat;
            private TextView TV_catName, TV_catType;

            private ItemClickListener clickListener;

            public ViewHolder(View itemView) {
                super(itemView);

                IV_cat = itemView.findViewById(R.id.IV_cat);
                TV_catName = itemView.findViewById(R.id.TV_catName);
                TV_catType = itemView.findViewById(R.id.TV_catType);

                itemView.setOnClickListener(this);
                itemView.setOnLongClickListener(this);
            }

            public void setClickListener(ItemClickListener itemClickListener) {
                this.clickListener = itemClickListener;
            }

            @Override
            public void onClick(View view) {
                clickListener.onClick(view, getPosition(), false);
            }

            @Override
            public boolean onLongClick(View view) {
                clickListener.onClick(view, getPosition(), true);
                return true;
            }
        }

    }
}


