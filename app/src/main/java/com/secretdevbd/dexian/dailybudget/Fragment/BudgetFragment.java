package com.secretdevbd.dexian.dailybudget.Fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
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

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

public class BudgetFragment extends Fragment {
    String TAG = "XIAN";

    RecyclerView RV_budgets;
    FloatingActionButton fab_addBudget;

    Button btn_budgetBack, btn_budgetNext;
    TextView TV_budgetTitle;

    RecyclerView.LayoutManager mLayoutManager;
    RecyclerView.Adapter mRecycleAdapter;


    String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
    int curr_month, curr_year;

    int curr_nav = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_budget, container, false);

        RV_budgets = view.findViewById(R.id.RV_budgets);
        fab_addBudget = view.findViewById(R.id.fab_addBudget);
        btn_budgetBack = view.findViewById(R.id.btn_budgetBack);
        btn_budgetNext = view.findViewById(R.id.btn_budgetNext);
        TV_budgetTitle = view.findViewById(R.id.TV_budgetTitle);

        curr_month = Calendar.getInstance().get(Calendar.MONTH);
        curr_year = Calendar.getInstance().get(Calendar.YEAR);

        generateBudgets(curr_month, curr_year);

        fab_addBudget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addBudget();
            }
        });

        btn_budgetBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                curr_nav--;
                if(curr_month+curr_nav<0){
                    curr_month=11;
                    curr_year--;
                    curr_nav=0;
                    generateBudgets(curr_month, curr_year);
                }else {
                    generateBudgets(curr_month+curr_nav, curr_year);
                }
            }
        });
        btn_budgetNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                curr_nav++;
                if(curr_month+curr_nav>11){
                    curr_month=0;
                    curr_year++;
                    curr_nav=0;
                    generateBudgets(curr_month, curr_year);
                }else {
                    generateBudgets(curr_month+curr_nav, curr_year);
                }

            }
        });


        return view;
    }

    public void generateBudgets(final int month, final int year){
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                TV_budgetTitle.setText(months[month]+"-"+year);
            }
        });

        Log.i(TAG, "MONTH : "+month+" YEAR : "+year);
        ArrayList<Budget> budgets = new DBhandler(getContext()).getAllBudgetsbyMonthYear(month+1, year);
        Collections.reverse(budgets);

        mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        RV_budgets.setLayoutManager(mLayoutManager);
        mRecycleAdapter = new RecycleViewAdapterForBudgets(getContext(), budgets);
        RV_budgets.setAdapter(mRecycleAdapter);
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
            viewHolder.TV_catType.setText(""+budgets.get(i).getBamount());

            if(budgets.get(i).getCtype().equals("Income")){
                viewHolder.TV_catType.setTextColor(getResources().getColor(R.color.dark_green));
            }
            viewHolder.IV_cat.setImageDrawable(getResources().getDrawable(R.drawable.ic_check_green));

            viewHolder.setClickListener(new ItemClickListener() {
                @Override
                public void onClick(View view, final int position, boolean isLongClick) {
                    if (isLongClick) {

                        AlertDialog.Builder builder;
                        builder = new AlertDialog.Builder(getContext());
                        //Uncomment the below code to Set the message and title from the strings.xml file
                        builder.setMessage("Do you want to delete this Budget ?").setTitle("Delete Budget");

                        //Setting message manually and performing action on button click
                        builder.setMessage("Do you want to delete this Budget ?")
                                .setCancelable(false)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        DBhandler DBH = new DBhandler(getContext());
                                        DBH.deleteBudget(budgets.get(position).getBid());
                                        dialog.cancel();
                                        Toast.makeText(getContext(),"Budget Deleted",
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
                        alert.setTitle("Delete Budget !");
                        alert.show();
                        reloadFragment();

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
        dialog.getWindow().setLayout(((getWidth(getActivity()) / 100) * 100), ((getHeight(getActivity()) / 100) * 80));
        dialog.getWindow().setGravity(Gravity.CENTER);


        final EditText ET_budgetAmount;
        final Spinner SP_budgetCategory, SP_month, SP_year;
        Button btn_addBudget;

        ET_budgetAmount = dialog.findViewById(R.id.ET_budgetAmount);
        SP_budgetCategory = dialog.findViewById(R.id.SP_budgetCategory);
        btn_addBudget = dialog.findViewById(R.id.btn_addBudget);
        SP_month = dialog.findViewById(R.id.SP_month);
        SP_year = dialog.findViewById(R.id.SP_year);

        final ArrayList<Category> categories = new DBhandler(getContext()).getAllCategories();
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

        ArrayList<String> years = new ArrayList<String>();
        int year_limit = curr_year+3;
        for(int i=curr_year; i<year_limit; i++){
            years.add(""+i);
        }

        final ArrayList<String> months_ = new ArrayList<String>();
        for(int i=0; i<12; i++){
            months_.add(months[(curr_month+i)%12]);
        }

        ArrayAdapter<String> yearAdapter = new ArrayAdapter<String>(getActivity().getApplicationContext(), android.R.layout.simple_spinner_item, years);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        SP_year.setAdapter(yearAdapter);

        ArrayAdapter<String> monthAdapter = new ArrayAdapter<String>(getActivity().getApplicationContext(), android.R.layout.simple_spinner_item, months_);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        SP_month.setAdapter(monthAdapter);

        dialog.show();

        if(categories.size()==0){
            Toast.makeText(getContext(), "Please add Category First !!", Toast.LENGTH_LONG).show();
            dialog.cancel();
        }

        btn_addBudget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int amount = 0;
                if(!ET_budgetAmount.getText().toString().equals("")){
                    amount = Integer.parseInt(ET_budgetAmount.getText().toString());
                }
                int cat_id = SP_budgetCategory.getSelectedItemPosition();
                String sel_month = SP_month.getSelectedItem().toString();
                int sel_year = Integer.parseInt(SP_year.getSelectedItem().toString());
                int int_month = -1;
                for(int i=0; i<12; i++){
                    if(sel_month.equals(months[i])){
                        Log.i(TAG, i+" sel_month.equals(months[i]) "+  months[i]);
                        int_month = i+1;
                    }
                }
                Log.i(TAG, "MONTH : "+int_month+" YEAR : "+sel_year);

                if(amount>0){
                    DBhandler DBH = new DBhandler(getActivity().getApplicationContext());
                    DBH.addBudget(categories.get(cat_id).getCid(), amount, int_month, sel_year);

                    Toast.makeText(getActivity().getApplicationContext(), "Budget Added", Toast.LENGTH_SHORT).show();
                    dialog.cancel();

                    reloadFragment();
                }else{
                    Toast.makeText(getActivity().getApplicationContext(), "Please Enter Correct Amount.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
