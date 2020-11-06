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
import com.secretdevbd.dexian.dailybudget.DB.Transaction;
import com.secretdevbd.dexian.dailybudget.ItemClickListener;
import com.secretdevbd.dexian.dailybudget.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;

public class TransactionFragment extends Fragment {
    String TAG = "XIAN";

    RecyclerView RV_transactions;
    Button btn_txnBack, btn_txnNext;
    TextView TV_txnTitle;
    FloatingActionButton fab_addTxn;

    RecyclerView.LayoutManager mLayoutManager;
    RecyclerView.Adapter mRecycleAdapter;

    String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
    int curr_day, curr_month, curr_year;

    int curr_nav = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_transaction, container, false);

        RV_transactions = view.findViewById(R.id.RV_transactions);
        btn_txnBack = view.findViewById(R.id.btn_txnBack);
        btn_txnNext = view.findViewById(R.id.btn_txnNext);
        TV_txnTitle = view.findViewById(R.id.TV_txnTitle);
        fab_addTxn = view.findViewById(R.id.fab_addTxn);

        curr_day = Calendar.getInstance().get(Calendar.DATE);
        curr_month = Calendar.getInstance().get(Calendar.MONTH);
        curr_year = Calendar.getInstance().get(Calendar.YEAR);

        generateTransactions(curr_month, curr_year);

        fab_addTxn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addTransaction();
            }
        });

        btn_txnBack.setOnClickListener(new View.OnClickListener() {
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
        btn_txnNext.setOnClickListener(new View.OnClickListener() {
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
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                TV_txnTitle.setText("Transactions ("+months[month]+"-"+year+")");
            }
        });

        Log.i(TAG, "MONTH : "+month+" YEAR : "+year);
        ArrayList<Transaction> transactions = new DBhandler(getContext()).getAllTransactionsByDate(month+1, year);
        Collections.reverse(transactions);

        mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        RV_transactions.setLayoutManager(mLayoutManager);
        mRecycleAdapter = new RecycleViewAdapterForTransaction(getContext(), transactions);
        RV_transactions.setAdapter(mRecycleAdapter);
    }

    public class RecycleViewAdapterForTransaction extends RecyclerView.Adapter<RecycleViewAdapterForTransaction.ViewHolder> {

        String TAG = "XIAN";

        ArrayList<Transaction> transactions;
        Context context;

        public RecycleViewAdapterForTransaction(Context context, ArrayList<Transaction> transactions) {
            super();
            this.context = context;
            this.transactions = transactions;
            //Log.i(TAG,"RECYCLE VIEW Constructor");
        }

        @Override
        public RecycleViewAdapterForTransaction.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View v = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.single_category, viewGroup, false);
            RecycleViewAdapterForTransaction.ViewHolder viewHolder = new RecycleViewAdapterForTransaction.ViewHolder(v);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(RecycleViewAdapterForTransaction.ViewHolder viewHolder, final int i) {



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

    private void addTransaction(){

        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.add_transcation);

        dialog.setCancelable(true);
        dialog.getWindow().setLayout(((getWidth(getActivity()) / 100) * 100), ((getHeight(getActivity()) / 100) * 80));
        dialog.getWindow().setGravity(Gravity.CENTER);


        final EditText ET_txnAmount, ET_txnNote;
        final Spinner SP_txnCategory;
        Button btn_addTxn;

        ET_txnAmount = dialog.findViewById(R.id.ET_txnAmount);
        ET_txnNote = dialog.findViewById(R.id.ET_txnNote);
        SP_txnCategory = dialog.findViewById(R.id.SP_txnCategory);
        btn_addTxn = dialog.findViewById(R.id.btn_addTxn);

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
        SP_txnCategory.setAdapter(dataAdapter);

        dialog.show();

        if(categories.size()==0){
            Toast.makeText(getContext(), "Please add Category First !!", Toast.LENGTH_LONG).show();
            dialog.cancel();
        }else{
            ArrayList<Budget> budgets = new DBhandler(getContext()).getAllBudgetsbyMonthYear(curr_month+1, curr_year);
            if(budgets.size() == 0){
                Toast.makeText(getContext(), "Please add Budget of this Month First !!", Toast.LENGTH_LONG).show();
                dialog.cancel();
            }
        }



        btn_addTxn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int amount = 0;
                if(!ET_txnAmount.getText().toString().equals("")){
                    amount = Integer.parseInt(ET_txnAmount.getText().toString());
                }
                int cat_id = SP_txnCategory.getSelectedItemPosition();

                String note = ET_txnNote.getText().toString().toUpperCase();

                if(amount>0){
                    DBhandler DBH = new DBhandler(getActivity().getApplicationContext());

                    DBH.addTransaction(categories.get(cat_id).getCid(), amount, curr_day, curr_month+1, curr_year, note);

                    Toast.makeText(getActivity().getApplicationContext(), "Transaction Added", Toast.LENGTH_SHORT).show();
                    dialog.cancel();

                    reloadFragment();
                }else{
                    Toast.makeText(getActivity().getApplicationContext(), "Please Enter Correct Amount.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
