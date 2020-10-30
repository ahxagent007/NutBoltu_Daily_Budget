package com.secretdevbd.dexian.dailybudget.Fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
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
import com.secretdevbd.dexian.dailybudget.DB.Budget;
import com.secretdevbd.dexian.dailybudget.DB.Category;
import com.secretdevbd.dexian.dailybudget.DB.DBhandler;
import com.secretdevbd.dexian.dailybudget.ItemClickListener;
import com.secretdevbd.dexian.dailybudget.R;

import java.util.ArrayList;

public class BudgetFragment extends Fragment {

    RecyclerView RV_budgets;
    FloatingActionButton fab_addBudget;

    Button btn_budgetBack, btn_budgetNext;
    TextView TV_budgetTitle;

    RecyclerView.LayoutManager mLayoutManager;
    RecyclerView.Adapter mRecycleAdapter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_budget, container, false);

        RV_budgets = view.findViewById(R.id.RV_budgets);
        fab_addBudget = view.findViewById(R.id.fab_addBudget);
        btn_budgetBack = view.findViewById(R.id.btn_budgetBack);
        btn_budgetNext = view.findViewById(R.id.btn_budgetNext);
        TV_budgetTitle = view.findViewById(R.id.TV_budgetTitle);

        ArrayList<Budget> budgets = new DBhandler(getContext()).getAllBudgets();

        mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        RV_budgets.setLayoutManager(mLayoutManager);
        mRecycleAdapter = new RecycleViewAdapterForBudgets(getContext(), budgets);
        RV_budgets.setAdapter(mRecycleAdapter);

        fab_addBudget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        return view;
    }


    public class RecycleViewAdapterForBudgets extends RecyclerView.Adapter<RecycleViewAdapterForBudgets.ViewHolder> {

        String TAG = "XIAN";

        ArrayList<Budget> budgets;
        Context context;

        public RecycleViewAdapterForBudgets(Context context, ArrayList<Budget> budgets) {
            super();
            this.context = context;
            this.budgets = budgets;
            //Log.i(TAG,"RECYCLE VIEW Constructor");
        }

        @Override
        public RecycleViewAdapterForBudgets.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View v = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.single_category, viewGroup, false);
            RecycleViewAdapterForBudgets.ViewHolder viewHolder = new RecycleViewAdapterForBudgets.ViewHolder(v);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(RecycleViewAdapterForBudgets.ViewHolder viewHolder, final int i) {

            viewHolder.TV_catName.setText(budgets.get(i).getCname());
            viewHolder.TV_catType.setText(budgets.get(i).getCtype());

            if(budgets.get(i).getCtype().equals("Income")){
                viewHolder.TV_catType.setTextColor(getResources().getColor(R.color.dark_green));
            }
            viewHolder.IV_cat.setImageDrawable(getResources().getDrawable(R.drawable.ic_check_green));

            viewHolder.setClickListener(new ItemClickListener() {
                @Override
                public void onClick(View view, final int position, boolean isLongClick) {
                    if (isLongClick) {

                    } else {
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return budgets.size();
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

    public void reloadFragment(){
        // Reload current fragment
        Fragment frg = getActivity().getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        final FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.detach(frg);
        ft.attach(frg);
        ft.commit();
    }

    public static int getWidth(Context context) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager windowmanager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        windowmanager.getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }

    public static int getHeight(Context context) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager windowmanager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        windowmanager.getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }

    public void deleteBudget(){
        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(getContext());
        //Uncomment the below code to Set the message and title from the strings.xml file
        builder.setMessage("Do you want to delete this Category ?").setTitle("Delete Category");

        //Setting message manually and performing action on button click
        builder.setMessage("Do you want to delete this Category ?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        DBhandler DBH = new DBhandler(getContext());
                        //DBH.deleteCategory(categories.get(position).getCid());
                        dialog.cancel();
                        Toast.makeText(getContext(),"Category Deleted",
                                Toast.LENGTH_SHORT).show();
                        reloadFragment();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //  Action for 'NO' Button
                        dialog.cancel();
                    }
                });
        //Creating dialog box
        AlertDialog alert = builder.create();
        //Setting the title manually
        alert.setTitle("Delete Category !");
        alert.show();
    }

    private void addBudget(){
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.add_budget);

        dialog.setCancelable(true);
        dialog.getWindow().setLayout(((getWidth(getActivity()) / 100) * 100), ((getHeight(getActivity()) / 100) * 60));
        dialog.getWindow().setGravity(Gravity.CENTER);


        final EditText ET_budgetAmount;
        final Spinner SP_budgetCategory;
        Button btn_addBudget;

        ET_budgetAmount = dialog.findViewById(R.id.ET_budgetAmount);
        SP_budgetCategory = dialog.findViewById(R.id.SP_budgetCategory);
        btn_addBudget = dialog.findViewById(R.id.btn_addBudget);

        ArrayList<Category> categories = new DBhandler(getContext()).getAllCategories();
        ArrayList<String> catnames = new ArrayList<String>();
        for (Category c:categories){
            catnames.add(c.getCname());
        }
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity().getApplicationContext(), android.R.layout.simple_spinner_item, catnames);
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        SP_budgetCategory.setAdapter(dataAdapter);

        dialog.show();


        btn_addBudget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int amount = Integer.parseInt(ET_budgetAmount.getText().toString());
                int id = SP_budgetCategory.getSelectedItemPosition();

                if(amount>0){
                    DBhandler DBH = new DBhandler(getActivity().getApplicationContext());
                    //DBH.addBudget();

                    Toast.makeText(getActivity().getApplicationContext(), "Budget Added", Toast.LENGTH_SHORT).show();
                    dialog.cancel();

                    reloadFragment();
                }else{
                    Toast.makeText(getActivity().getApplicationContext(), "Please enter amount.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
