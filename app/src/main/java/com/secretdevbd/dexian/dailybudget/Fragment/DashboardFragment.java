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

import com.secretdevbd.dexian.dailybudget.DB.BudgetSatus;
import com.secretdevbd.dexian.dailybudget.DB.Category;
import com.secretdevbd.dexian.dailybudget.DB.DBhandler;
import com.secretdevbd.dexian.dailybudget.DB.Transaction;
import com.secretdevbd.dexian.dailybudget.ItemClickListener;
import com.secretdevbd.dexian.dailybudget.R;

import java.util.ArrayList;
import java.util.Calendar;

public class DashboardFragment extends Fragment {

    String TAG = "XIAN";

    TextView TV_income, TV_expense, TV_balance, TV_monthYear;
    RecyclerView RV_budgetStatus;
    Button btn_back, btn_forward;

    RecyclerView.LayoutManager mLayoutManager;
    RecyclerView.Adapter mRecycleAdapter;

    String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
    int curr_day, curr_month, curr_year;

    int curr_nav = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_dashboard, container, false);

        TV_income = view.findViewById(R.id.TV_income);
        TV_expense = view.findViewById(R.id.TV_expense);
        TV_balance = view.findViewById(R.id.TV_balance);
        TV_monthYear = view.findViewById(R.id.TV_monthYear);
        RV_budgetStatus = view.findViewById(R.id.RV_budgetStatus);
        btn_back = view.findViewById(R.id.btn_back);
        btn_forward = view.findViewById(R.id.btn_forward);

        curr_day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        curr_month = Calendar.getInstance().get(Calendar.MONTH);
        curr_year = Calendar.getInstance().get(Calendar.YEAR);

        generateTransactions(curr_month, curr_year);

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                curr_nav--;
                if(curr_month+curr_nav<0){
                    curr_month=11;
                    curr_year--;
                    curr_nav=0;
                    generateTransactions(curr_month, curr_year);
                }else {
                    generateTransactions(curr_month+curr_nav, curr_year);
                }
            }
        });
        btn_forward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                curr_nav++;
                if(curr_month+curr_nav>11){
                    curr_month=0;
                    curr_year++;
                    curr_nav=0;
                    generateTransactions(curr_month, curr_year);
                }else {
                    generateTransactions(curr_month+curr_nav, curr_year);
                }
            }
        });

        return view;

    }

    public void generateTransactions(final int month, final int year){
        Log.i(TAG, "MONTH : "+month+" YEAR : "+year);

        DBhandler DBH = new DBhandler(getContext());

        final int income = DBH.getTotalIncome(month+1, year);
        final int expense = DBH.getTotalExpense(month+1, year);
        final int balance = income-expense;

        new Handler().post(new Runnable() {
            @Override
            public void run() {
                TV_monthYear.setText(months[month]+"-"+year);
                TV_income.setText(""+income);
                TV_expense.setText(""+expense);
                TV_balance.setText(""+balance);
            }
        });

        ArrayList<BudgetSatus> budgetSatuses = DBH.getBudgetStatus(month+1, year);

        mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        RV_budgetStatus.setLayoutManager(mLayoutManager);
        mRecycleAdapter = new RecycleViewAdapterForBudgetStatus(getContext(), budgetSatuses);
        RV_budgetStatus.setAdapter(mRecycleAdapter);
    }

    public class RecycleViewAdapterForBudgetStatus extends RecyclerView.Adapter<RecycleViewAdapterForBudgetStatus.ViewHolder> {

        ArrayList<BudgetSatus> budgetSatuses;
        Context context;

        public RecycleViewAdapterForBudgetStatus(Context context, ArrayList<BudgetSatus> budgetSatuses) {
            super();
            this.context = context;
            this.budgetSatuses = budgetSatuses;
            //Log.i(TAG,"RECYCLE VIEW Constructor");
        }

        @Override
        public RecycleViewAdapterForBudgetStatus.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View v = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.single_category, viewGroup, false);
            RecycleViewAdapterForBudgetStatus.ViewHolder viewHolder = new RecycleViewAdapterForBudgetStatus.ViewHolder(v);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(RecycleViewAdapterForBudgetStatus.ViewHolder viewHolder, final int i) {

            viewHolder.TV_catName.setText(budgetSatuses.get(i).getBudgetName()+" ["+budgetSatuses.get(i).getBudgetAmount()+"]["+budgetSatuses.get(i).getBudgetGet()+"]");

            if(budgetSatuses.get(i).getBudgetBalance()>0){
                viewHolder.TV_catType.setText(""+budgetSatuses.get(i).getBudgetBalance());
                viewHolder.TV_catType.setTextColor(getResources().getColor(R.color.dark_green));
            }else {
                viewHolder.TV_catType.setText(""+budgetSatuses.get(i).getBudgetBalance());

            }
            viewHolder.IV_cat.setImageDrawable(getResources().getDrawable(R.drawable.ic_check_green));

            viewHolder.setClickListener(new ItemClickListener() {
                @Override
                public void onClick(View view, final int position, boolean isLongClick) {
                    if (isLongClick) {

                    } else {
                        //getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new BudgetStatusDetailsFragment()).commit();

                        Bundle bundle = new Bundle();
                        bundle.putInt("ID", budgetSatuses.get(position).getCatID());
                        Log.i(TAG, "CID = "+budgetSatuses.get(position).getCatID());

                        BudgetStatusDetailsFragment nextFrag = new BudgetStatusDetailsFragment();
                        nextFrag.setArguments(bundle);

                        getActivity().getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragment_container, nextFrag, "BudgetStatus")
                                .addToBackStack(null)
                                .commit();
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return budgetSatuses.size();
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
