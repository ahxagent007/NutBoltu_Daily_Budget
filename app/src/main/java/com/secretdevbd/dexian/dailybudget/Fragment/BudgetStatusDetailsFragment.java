package com.secretdevbd.dexian.dailybudget.Fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.secretdevbd.dexian.dailybudget.DB.Budget;
import com.secretdevbd.dexian.dailybudget.DB.DBhandler;
import com.secretdevbd.dexian.dailybudget.DB.Transaction;
import com.secretdevbd.dexian.dailybudget.ItemClickListener;
import com.secretdevbd.dexian.dailybudget.R;

import java.util.ArrayList;
import java.util.Collections;


public class BudgetStatusDetailsFragment extends Fragment {

    TextView TV_budgetStatus;
    RecyclerView RV_budgetStatus;

    RecyclerView.LayoutManager mLayoutManager;
    RecyclerView.Adapter mRecycleAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =   inflater.inflate(R.layout.fragment_budget_status, container, false);

        TV_budgetStatus = view.findViewById(R.id.TV_budgetStatus);
        RV_budgetStatus = view.findViewById(R.id.RV_budgetStatus);

        return view;
    }


    public void generateTranscationsByCat(int cid){
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                TV_budgetStatus.setText("Transaction Details");
            }
        });

        ArrayList<Transaction> transactions = new DBhandler(getContext()).getAllTransactionsbyCategory(cid);
        Collections.reverse(transactions);

        mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        RV_budgetStatus.setLayoutManager(mLayoutManager);
        mRecycleAdapter = new RecycleViewAdapterForTransactionbyCat(getContext(), transactions);
        RV_budgetStatus.setAdapter(mRecycleAdapter);
    }

    public class RecycleViewAdapterForTransactionbyCat extends RecyclerView.Adapter<RecycleViewAdapterForTransactionbyCat.ViewHolder> {

        String TAG = "XIAN";

        ArrayList<Transaction> transactions;
        Context context;

        public RecycleViewAdapterForTransactionbyCat(Context context, ArrayList<Transaction> transactions) {
            super();
            this.context = context;
            this.transactions = transactions;
            //Log.i(TAG,"RECYCLE VIEW Constructor");
        }

        @Override
        public RecycleViewAdapterForTransactionbyCat.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View v = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.single_category, viewGroup, false);
            RecycleViewAdapterForTransactionbyCat.ViewHolder viewHolder = new RecycleViewAdapterForTransactionbyCat.ViewHolder(v);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(RecycleViewAdapterForTransactionbyCat.ViewHolder viewHolder, final int i) {



            viewHolder.TV_catName.setText(transactions.get(i).getCname()+" : "+transactions.get(i).getTnote());

            if(transactions.get(i).getCtype().equals("Income")){
                viewHolder.TV_catType.setText("+"+transactions.get(i).getTamount());
                viewHolder.TV_catType.setTextColor(getResources().getColor(R.color.dark_green));
            }else {
                viewHolder.TV_catType.setText("-"+transactions.get(i).getTamount());

            }
            viewHolder.IV_cat.setImageDrawable(getResources().getDrawable(R.drawable.ic_check_green));

            viewHolder.setClickListener(new ItemClickListener() {
                @Override
                public void onClick(View view, final int position, boolean isLongClick) {
                    if (isLongClick) {

                    } else {
                        Log.i(TAG, "TRANSACTIONS : "+transactions.get(i).getDay()+" : "+transactions.get(i).getMonth()+" : "+transactions.get(i).getYear());
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return transactions.size();
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
