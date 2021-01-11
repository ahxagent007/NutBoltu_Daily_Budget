package com.secretdevbd.dexian.dailybudget.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DBhandler extends SQLiteOpenHelper {
    String TAG = "XIAN";

    public static final String DATABASE_NAME = "DailyBudget.db";

    public static final String CATEGORY_TABLE_NAME = "CATEGORY";
    public static final String CATEGORY_ID = "cid";
    public static final String CATEGORY_TYPE = "ctype";
    public static final String CATEGORY_NAME = "cname";

    public static final String BUDGET_TABLE_NAME = "BUDGET";
    public static final String BUDGET_ID = "bid";
    public static final String BUDGET_AMOUNT = "bamount";
    public static final String BUDGET_CID = "cid";
    public static final String BUDGET_MONTH = "bmonth";
    public static final String BUDGET_YEAR = "byear";


    public static final String TRANSACTION_TABLE_NAME = "TRANSACTION_TABLE";
    public static final String TRANSACTION_ID = "tid";
    public static final String TRANSACTION_NOTE = "tnote";
    public static final String TRANSACTION_CID = "cid";
    public static final String TRANSACTION_DAY = "tday";
    public static final String TRANSACTION_MONTH = "tmonth";
    public static final String TRANSACTION_YEAR = "tyear";
    public static final String TRANSACTION_AMOUNT = "tamount";

    public static final String SETTING_TABLE_NAME = "SETTING";
    public static final String SETTING_ID = "sid";
    public static final String SETTING_NAME = "sname";
    public static final String SETTING_SETTING = "ssetting";

    public static final String CAT_TYPE_INCOME = "INCOME";
    public static final String CAT_TYPE_EXPENSE = "EXPENSE";

    public DBhandler(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(
                "create table " + CATEGORY_TABLE_NAME + " " +
                        "("
                        + CATEGORY_ID + " integer primary key, " +
                        CATEGORY_TYPE + " text," +
                        CATEGORY_NAME + " text);"
        );
        db.execSQL(
                "create table " + BUDGET_TABLE_NAME + " " +
                        "(" + BUDGET_ID + " integer primary key, " + BUDGET_CID + " integer,"+ BUDGET_AMOUNT + " integer," + BUDGET_MONTH + " integer," + BUDGET_YEAR + " integer);"
        );

        db.execSQL(
                "create table " + TRANSACTION_TABLE_NAME + " " +
                        "("
                        + TRANSACTION_ID + " integer primary key, " +
                        TRANSACTION_NOTE + " text," +
                        TRANSACTION_CID + " integer," +
                        TRANSACTION_DAY + " integer," +
                        TRANSACTION_MONTH + " integer, " +
                        TRANSACTION_YEAR + " integer, " +
                        TRANSACTION_AMOUNT + " integer );"
        );

        db.execSQL(
                "create table " + SETTING_TABLE_NAME + " " +
                        "(" + SETTING_ID + " integer primary key, " + SETTING_NAME + " text," + SETTING_SETTING + " text);"
        );


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS " + CATEGORY_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + BUDGET_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TRANSACTION_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS "+ SETTING_TABLE_NAME);
        onCreate(db);
    }

    public boolean addCategory(String type, String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(CATEGORY_NAME, name);
        contentValues.put(CATEGORY_TYPE, type);

        db.insert(CATEGORY_TABLE_NAME, null, contentValues);
        return true;
    }

    public boolean addBudget(int cid, int amount, int month, int year) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(BUDGET_CID, cid);
        contentValues.put(BUDGET_AMOUNT, amount);
        contentValues.put(BUDGET_MONTH, month);
        contentValues.put(BUDGET_YEAR, year);

        db.insert(BUDGET_TABLE_NAME, null, contentValues);
        return true;
    }

    public boolean addTransaction(int cid, int amount, int day, int month, int year, String note) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TRANSACTION_CID, cid);
        contentValues.put(TRANSACTION_AMOUNT, amount);
        contentValues.put(TRANSACTION_DAY, day);
        contentValues.put(TRANSACTION_MONTH, month);
        contentValues.put(TRANSACTION_YEAR, year);
        contentValues.put(TRANSACTION_NOTE, note);

        db.insert(TRANSACTION_TABLE_NAME, null, contentValues);
        return true;
    }

    public boolean updateBudget(int bid, long amount) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(BUDGET_AMOUNT, amount);

        db.update(BUDGET_TABLE_NAME, contentValues, "id = ? ", new String[]{Integer.toString(bid)});
        return true;
    }

    public ArrayList<Budget> getAllBudgets() {
        ArrayList<Budget> budgets = new ArrayList<Budget>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT BUDGET.bid, BUDGET.cid, BUDGET.bamount, BUDGET.byear, BUDGET.bmonth,  CATEGORY.ctype, CATEGORY.cname " +
                "FROM " + BUDGET_TABLE_NAME +" JOIN "+CATEGORY_TABLE_NAME+" ON "+BUDGET_TABLE_NAME+"."+BUDGET_CID+" = "+CATEGORY_TABLE_NAME+"."+CATEGORY_ID, null);
        res.moveToFirst();

        while (res.isAfterLast() == false) {
            Budget bgt = new Budget(res.getInt(res.getColumnIndex(BUDGET_ID)), res.getInt(res.getColumnIndex(BUDGET_CID)),
                    res.getInt(res.getColumnIndex(BUDGET_AMOUNT)), res.getInt(res.getColumnIndex(BUDGET_YEAR)), res.getInt(res.getColumnIndex(BUDGET_MONTH)) );
            bgt.setCname(res.getString(res.getColumnIndex(CATEGORY_NAME)));
            bgt.setCtype(res.getString(res.getColumnIndex(CATEGORY_TYPE)));
            budgets.add(bgt);

            res.moveToNext();
        }
        return budgets;
    }

    public ArrayList<Budget> getTotalBudgets(int month, int year) {
        ArrayList<Budget> budgets = new ArrayList<Budget>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery( "SELECT SUM(BUDGET.bamount) as bamount, BUDGET.bid, BUDGET.cid, BUDGET.bmonth, BUDGET.byear, CATEGORY.ctype, CATEGORY.cname FROM BUDGET JOIN CATEGORY ON BUDGET.cid = CATEGORY.cid WHERE BUDGET.bmonth = "+month+" and BUDGET.byear = "+year+" GROUP by BUDGET.cid, BUDGET.bmonth, BUDGET.byear",null);
        res.moveToFirst();

        while (res.isAfterLast() == false) {
            Budget bgt = new Budget(res.getInt(res.getColumnIndex(BUDGET_ID)), res.getInt(res.getColumnIndex(BUDGET_CID)),
                    res.getInt(res.getColumnIndex(BUDGET_AMOUNT)), res.getInt(res.getColumnIndex(BUDGET_YEAR)), res.getInt(res.getColumnIndex(BUDGET_MONTH)) );
            bgt.setCname(res.getString(res.getColumnIndex(CATEGORY_NAME)));
            bgt.setCtype(res.getString(res.getColumnIndex(CATEGORY_TYPE)));
            budgets.add(bgt);

            res.moveToNext();
        }
        return budgets;
    }

    public ArrayList<Budget> getAllBudgetsbyMonthYear(int month, int year) {
        ArrayList<Budget> budgets = new ArrayList<Budget>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT BUDGET.bid, BUDGET.cid, BUDGET.bamount, BUDGET.byear, BUDGET.bmonth,  CATEGORY.ctype, CATEGORY.cname " +
                "FROM " + BUDGET_TABLE_NAME +" JOIN "+CATEGORY_TABLE_NAME+" ON "+BUDGET_TABLE_NAME+"."+BUDGET_CID+" = "+CATEGORY_TABLE_NAME+"."+CATEGORY_ID+" " +
                "WHERE BUDGET.bmonth = "+month+" AND BUDGET.byear = "+year, null);
        res.moveToFirst();

        while (res.isAfterLast() == false) {
            Budget bgt = new Budget(res.getInt(res.getColumnIndex(BUDGET_ID)), res.getInt(res.getColumnIndex(BUDGET_CID)),
                    res.getInt(res.getColumnIndex(BUDGET_AMOUNT)), res.getInt(res.getColumnIndex(BUDGET_YEAR)), res.getInt(res.getColumnIndex(BUDGET_MONTH)) );
            bgt.setCname(res.getString(res.getColumnIndex(CATEGORY_NAME)));
            bgt.setCtype(res.getString(res.getColumnIndex(CATEGORY_TYPE)));
            budgets.add(bgt);

            res.moveToNext();
        }
        return budgets;
    }

    public ArrayList<Budget> getBudgetsByCategory(int cid) {
        ArrayList<Budget> budgets = new ArrayList<Budget>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT BUDGET.bid, BUDGET.cid, BUDGET.bamount, BUDGET.byear, BUDGET.bmonth,  CATEGORY.ctype, CATEGORY.cname " +
                "FROM " + BUDGET_TABLE_NAME +" JOIN "+CATEGORY_TABLE_NAME+" ON "+BUDGET_TABLE_NAME+"."+BUDGET_CID+" = "+CATEGORY_TABLE_NAME+"."+CATEGORY_ID+ " " +
                "WHERE BUDGET.cid = "+cid, null);
        res.moveToFirst();

        while (res.isAfterLast() == false) {
            Budget bgt = new Budget(res.getInt(res.getColumnIndex(BUDGET_ID)), res.getInt(res.getColumnIndex(BUDGET_CID)),
                    res.getInt(res.getColumnIndex(BUDGET_AMOUNT)), res.getInt(res.getColumnIndex(BUDGET_YEAR)), res.getInt(res.getColumnIndex(BUDGET_MONTH)) );
            bgt.setCname(res.getString(res.getColumnIndex(CATEGORY_NAME)));
            bgt.setCtype(res.getString(res.getColumnIndex(CATEGORY_TYPE)));
            budgets.add(bgt);

            res.moveToNext();
        }
        return budgets;
    }

    public ArrayList<Category> getAllCategories() {
        ArrayList<Category> categories = new ArrayList<Category>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + CATEGORY_TABLE_NAME, null);
        res.moveToFirst();

        while (res.isAfterLast() == false) {
            Category cat = new Category(res.getInt(res.getColumnIndex(CATEGORY_ID)), res.getString(res.getColumnIndex(CATEGORY_TYPE)), res.getString(res.getColumnIndex(CATEGORY_NAME)));
            categories.add(cat);
            res.moveToNext();
        }
        return categories;
    }

    public ArrayList<Category> getCategoriesByType(String type) {
        ArrayList<Category> categories = new ArrayList<Category>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + CATEGORY_TABLE_NAME+ " WHERE "+CATEGORY_TYPE+" = \""+type+"\";", null);
        res.moveToFirst();

        while (res.isAfterLast() == false) {
            Category cat = new Category(res.getInt(res.getColumnIndex(CATEGORY_ID)), res.getString(res.getColumnIndex(CATEGORY_TYPE)), res.getString(res.getColumnIndex(CATEGORY_NAME)));
            categories.add(cat);
            res.moveToNext();
        }
        return categories;
    }

    public ArrayList<Transaction> getAllTransactions() {
        ArrayList<Transaction> transactions = new ArrayList<Transaction>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + TRANSACTION_TABLE_NAME, null);
        res.moveToFirst();

        while (res.isAfterLast() == false) {
            Transaction txn = new Transaction(res.getInt(res.getColumnIndex(TRANSACTION_ID)),res.getInt(res.getColumnIndex(TRANSACTION_CID)),
                    res.getInt(res.getColumnIndex(TRANSACTION_AMOUNT)),res.getInt(res.getColumnIndex(TRANSACTION_DAY)),res.getInt(res.getColumnIndex(TRANSACTION_MONTH)),
                    res.getInt(res.getColumnIndex(TRANSACTION_YEAR)),res.getString(res.getColumnIndex(TRANSACTION_NOTE)));
            transactions.add(txn);
            res.moveToNext();
        }
        return transactions;
    }

    public ArrayList<Transaction> getAllTransactionsbyCategory(int cid, int month) {
        ArrayList<Transaction> transactions = new ArrayList<Transaction>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + TRANSACTION_TABLE_NAME + " JOIN CATEGORY ON CATEGORY.cid = TRANSACTION_TABLE.cid WHERE TRANSACTION_TABLE.cid = "+cid+ " AND tmonth = "+month, null);
        res.moveToFirst();

        while (res.isAfterLast() == false) {
            Transaction txn = new Transaction(res.getInt(res.getColumnIndex(TRANSACTION_ID)),res.getInt(res.getColumnIndex(TRANSACTION_CID)),
                    res.getInt(res.getColumnIndex(TRANSACTION_AMOUNT)),res.getInt(res.getColumnIndex(TRANSACTION_DAY)),res.getInt(res.getColumnIndex(TRANSACTION_MONTH)),
                    res.getInt(res.getColumnIndex(TRANSACTION_YEAR)),res.getString(res.getColumnIndex(TRANSACTION_NOTE)));
            txn.setCtype(res.getString(res.getColumnIndex(CATEGORY_TYPE)));
            txn.setCname(res.getString(res.getColumnIndex(CATEGORY_NAME)));
            transactions.add(txn);
            res.moveToNext();
        }
        return transactions;
    }

    public ArrayList<Transaction> getAllTransactionsByDate(int month, int year) {
        ArrayList<Transaction> transactions = new ArrayList<Transaction>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT TRANSACTION_TABLE.tid, TRANSACTION_TABLE.tnote, TRANSACTION_TABLE.cid, TRANSACTION_TABLE.tamount, TRANSACTION_TABLE.tday, TRANSACTION_TABLE.tmonth, TRANSACTION_TABLE.tyear, " +
                "CATEGORY.ctype, CATEGORY.cname FROM TRANSACTION_TABLE " +
                "JOIN CATEGORY ON TRANSACTION_TABLE.cid = CATEGORY.cid " +
                "WHERE tmonth = "+month+" AND tyear = "+year+";", null);
        res.moveToFirst();

        while (res.isAfterLast() == false) {
            Transaction txn = new Transaction(res.getInt(res.getColumnIndex(TRANSACTION_ID)),res.getInt(res.getColumnIndex(TRANSACTION_CID)),
                    res.getInt(res.getColumnIndex(TRANSACTION_AMOUNT)),res.getInt(res.getColumnIndex(TRANSACTION_DAY)),res.getInt(res.getColumnIndex(TRANSACTION_MONTH)),
                    res.getInt(res.getColumnIndex(TRANSACTION_YEAR)),res.getString(res.getColumnIndex(TRANSACTION_NOTE)));
            txn.setCname(res.getString(res.getColumnIndex(CATEGORY_NAME)));
            txn.setCtype(res.getString(res.getColumnIndex(CATEGORY_TYPE)));
            transactions.add(txn);
            res.moveToNext();
        }
        return transactions;
    }

    public ArrayList<Transaction> getAllTransactionsByMonth(int month, int year) {
        ArrayList<Transaction> transactions = new ArrayList<Transaction>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + TRANSACTION_TABLE_NAME +" WHERE month = "+month+" AND year = "+year+";", null);
        res.moveToFirst();

        while (res.isAfterLast() == false) {
            Transaction txn = new Transaction(res.getInt(res.getColumnIndex(TRANSACTION_ID)),res.getInt(res.getColumnIndex(TRANSACTION_CID)),
                    res.getInt(res.getColumnIndex(TRANSACTION_AMOUNT)),res.getInt(res.getColumnIndex(TRANSACTION_DAY)),res.getInt(res.getColumnIndex(TRANSACTION_MONTH)),
                    res.getInt(res.getColumnIndex(TRANSACTION_YEAR)),res.getString(res.getColumnIndex(TRANSACTION_NOTE)));
            transactions.add(txn);
            res.moveToNext();
        }
        return transactions;
    }

    public void getMonthAndyears(){}

    public boolean deleteCategory(int id){

        SQLiteDatabase db = this.getReadableDatabase();
        try{
            db.execSQL("DELETE FROM " + CATEGORY_TABLE_NAME + " WHERE "+CATEGORY_ID+ " = "+id);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    public boolean deleteTransaction(int id){

        SQLiteDatabase db = this.getReadableDatabase();
        try{
            db.execSQL("DELETE FROM " + TRANSACTION_TABLE_NAME + " WHERE "+TRANSACTION_ID+ " = "+id);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    public boolean deleteBudget(int id){

        SQLiteDatabase db = this.getReadableDatabase();
        try{
            db.execSQL("DELETE FROM " + BUDGET_TABLE_NAME + " WHERE "+BUDGET_ID+ " = "+id);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    public ArrayList<BudgetSatus> getBudgetStatus(int month, int year){
        ArrayList<BudgetSatus> budgetSatuses = new ArrayList<BudgetSatus>();

        //QUERY PROBLEM
        String sql_qury = "SELECT a.total_amount, b.total_txn, a.cid, a.bid,  b.tid, b.tday, c.ctype, c.cname " +
                " FROM (SELECT SUM(bamount) AS total_amount, cid, bid, bmonth, byear " +
                "    FROM BUDGET " +
                "    WHERE " +
                "    bmonth = "+month+" AND byear = "+year+" " +
                "    GROUP By cid) AS a  " +
                "JOIN (SELECT SUM(tamount) AS total_txn, tid, tday, cid " +
                "  FROM TRANSACTION_TABLE\n" +
                "  WHERE tmonth = "+month+" AND tyear = "+year+" " +
                "  GROUP BY cid) as b  " +
                "ON a.cid=b.cid " +
                "JOIN CATEGORY as c ON c.cid = a.cid " +
                "WHERE a.bmonth = "+month+" AND a.byear = "+year;


        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery( sql_qury,null);
        res.moveToFirst();

        while (res.isAfterLast() == false) {
            BudgetSatus bgt = new BudgetSatus();
            if(res.getString(res.getColumnIndex(CATEGORY_TYPE)).equals("Income")){
                bgt = new BudgetSatus(res.getString(res.getColumnIndex(CATEGORY_NAME)), res.getInt(res.getColumnIndex("total_amount")), res.getInt(res.getColumnIndex("total_txn")),
                        res.getInt(res.getColumnIndex("total_txn"))-res.getInt(res.getColumnIndex("total_amount")), res.getString(res.getColumnIndex(CATEGORY_TYPE)));
            }else{
                bgt = new BudgetSatus(res.getString(res.getColumnIndex(CATEGORY_NAME)), res.getInt(res.getColumnIndex("total_amount")), res.getInt(res.getColumnIndex("total_txn")),
                        res.getInt(res.getColumnIndex("total_amount"))-res.getInt(res.getColumnIndex("total_txn")), res.getString(res.getColumnIndex(CATEGORY_TYPE)));
            }
            bgt.setCatID(res.getInt(res.getColumnIndex("cid")));
            budgetSatuses.add(bgt);
            res.moveToNext();
        }
        return budgetSatuses;
    }

    public int getTotalIncome(int month, int year){

        String sql_qury = "SELECT SUM(TRANSACTION_TABLE.tamount) as total_income FROM TRANSACTION_TABLE " +
                "JOIN CATEGORY ON CATEGORY.cid = TRANSACTION_TABLE.cid " +
                "WHERE CATEGORY.ctype = \"Income\" AND TRANSACTION_TABLE.tmonth = "+month+" AND TRANSACTION_TABLE.tyear = "+year+";";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery( sql_qury,null);
        res.moveToFirst();

        int income = res.getInt(res.getColumnIndex("total_income"));

        return income;
    }

    public int getTotalExpense(int month, int year){

        String sql_qury = "SELECT SUM(TRANSACTION_TABLE.tamount) as total_expense FROM TRANSACTION_TABLE " +
                "JOIN CATEGORY ON CATEGORY.cid = TRANSACTION_TABLE.cid " +
                "WHERE CATEGORY.ctype = \"Expense\" AND TRANSACTION_TABLE.tmonth = "+month+" AND TRANSACTION_TABLE.tyear = "+year+";";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery( sql_qury,null);
        res.moveToFirst();

        int income = res.getInt(res.getColumnIndex("total_expense"));

        return income;
    }


    /*
    public ArrayList<DownloadDB> getAllDownloads() {
        ArrayList<DownloadDB> downloadDBS = new ArrayList<DownloadDB>();


        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + DOWNLOAD_TABLE_NAME, null);
        res.moveToFirst();

        while (res.isAfterLast() == false) {
            DownloadDB dn = new DownloadDB();
            File downloadedFile = new File(res.getString(res.getColumnIndex(DOWNLOAD_PATH)) + "/" + res.getString(res.getColumnIndex(DOWNLOAD_FILE_NAME)));
            if (downloadedFile.exists()) {
                dn.setFilename(res.getString(res.getColumnIndex(DOWNLOAD_FILE_NAME)));
                dn.setDownload_id(res.getInt(res.getColumnIndex(DOWNLOAD_COLUMN_ID)));
                dn.setDone(res.getString(res.getColumnIndex(DOWNLOAD_DONE)));
                dn.setLink(res.getString(res.getColumnIndex(DOWNLOAD_LINK)));
                dn.setPath(res.getString(res.getColumnIndex(DOWNLOAD_PATH)));
                dn.setPercentage(res.getInt(res.getColumnIndex(DOWNLOAD_PERCENT)));
                dn.setWebsite(res.getString(res.getColumnIndex(DOWNLOAD_WEBSITE)));

                downloadDBS.add(dn);
                Log.i(TAG, dn.toString());
            }
            res.moveToNext();
        }
        return downloadDBS;
    }

    public int getLastDownloadID() {

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select download_id from " + DOWNLOAD_TABLE_NAME + " ORDER BY download_id DESC LIMIT 1;", null);
        res.moveToFirst();

        int id = 0;

        while (res.isAfterLast() == false) {

            if (res != null) {
                id = res.getInt(res.getColumnIndex(DOWNLOAD_COLUMN_ID));
            }

            res.moveToNext();

        }
        return id;
    }

    public ArrayList<FTP_Server> getActiveFTP() {
        ArrayList<FTP_Server> ftp_server_list = new ArrayList<FTP_Server>();


        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + SERVER_TABLE_NAME, null);
        res.moveToFirst();
        if (res.getCount() < 1) {
            return ftp_server_list;
        }
        while (res.isAfterLast() == false) {
            if (res.isAfterLast()) {
                break;
            }
            if (res.getString(res.getColumnIndex(SERVER_COLUMN_STATUS)).equals(STATUS_OK)) {
                FTP_Server ftp = new FTP_Server();

                try {
                    ftp.setFtp_server(res.getString(res.getColumnIndex(SERVER_COLUMN_SERVER_NAME)));
                    ftp.setType(res.getString(res.getColumnIndex(SERVER_COLUMN_SERVER_NAME)));
                    ftp.setServer_name(res.getString(res.getColumnIndex("server_name")));
                    ftp.setRanking(res.getInt(res.getColumnIndex("ranking")));
                    ftp.setImage_url(res.getString(res.getColumnIndex("image_url")));
                    ftp.setPinging_url(res.getString(res.getColumnIndex("pinging_url")));

                    ftp_server_list.add(ftp);
                } catch (Exception e) {
                    Log.i(TAG, "getActiveFTP : " + e);
                }


            }

            res.moveToNext();
        }
        return ftp_server_list;
    }

    public ArrayList<FTP_Server> getActiveFTPviaType(String type) {

        ArrayList<FTP_Server> ftp_server_list = new ArrayList<FTP_Server>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + SERVER_TABLE_NAME + " WHERE " + SERVER_COLUMN_TYPE + " = \"" + type + "\" AND connect_status = \"OK\";", null);
        res.moveToFirst();
        FTP_Server ftp;
        while (res.isAfterLast() == false) {
            ftp = new FTP_Server();

            ftp.setFtp_server(res.getString(res.getColumnIndex(SERVER_COLUMN_SERVER_NAME)));
            ftp.setType(res.getString(res.getColumnIndex(SERVER_COLUMN_SERVER_NAME)));
            ftp.setServer_name(res.getString(res.getColumnIndex("server_name")));
            ftp.setRanking(res.getInt(res.getColumnIndex("ranking")));
            ftp.setImage_url(res.getString(res.getColumnIndex("image_url")));
            ftp.setPinging_url(res.getString(res.getColumnIndex("pinging_url")));

            ftp_server_list.add(ftp);
            res.moveToNext();
        }
        return ftp_server_list;
    }

    public ArrayList<String> getActiveFTPWithoutHTTP() {
        ArrayList<String> ftp_server_list = new ArrayList<String>();


        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + SERVER_TABLE_NAME, null);
        res.moveToFirst();
        String ser = "";
        while (res.isAfterLast() == false) {
            if (res.getString(res.getColumnIndex(SERVER_COLUMN_STATUS)).equals(STATUS_OK))
                ser = res.getString(res.getColumnIndex(SERVER_COLUMN_SERVER_NAME)).toString();
            ftp_server_list.add(ser.substring(7, ser.length()));
            res.moveToNext();
        }
        return ftp_server_list;
    }

    public ArrayList<String> getAllServerTypes() {
        ArrayList<String> type = new ArrayList<String>();


        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select DISTINCT  " + SERVER_COLUMN_TYPE + " from " + SERVER_TABLE_NAME, null);
        res.moveToFirst();

        while (res.isAfterLast() == false) {

            String typee = res.getString(res.getColumnIndex(SERVER_COLUMN_TYPE));

            type.add(typee);

            res.moveToNext();
        }
        return type;

    }

    public void deleteAllServerRecords() {
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("DELETE FROM " + SERVER_TABLE_NAME + ";");
    }

    public void deleteAllYoutubeVideos() {
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("DELETE FROM " + YT_TABLE_NAME + ";");
    }

    public void deleteAllYoutubeArtist() {
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("DELETE FROM youtube_artist;");
    }

    public boolean addAllYoutubeVideos(ArrayList<YoutubeVideo> youtubeVideos) {

        this.deleteAllYoutubeVideos();

        SQLiteDatabase db = this.getWritableDatabase();

        for (int i = 0; i < youtubeVideos.size(); i++) {

            ContentValues contentValues = new ContentValues();
            contentValues.put(YT_COLUMN_CHANNEL_ID, youtubeVideos.get(i).channel_id);
            contentValues.put(YT_COLUMN_ID_YOUTUBE, youtubeVideos.get(i).id_youtube);
            contentValues.put(YT_COLUMN_CHANNEL_NAME, youtubeVideos.get(i).channel_name);
            contentValues.put(YT_COLUMN_TITLE, youtubeVideos.get(i).title);
            contentValues.put(YT_COLUMN_DURATION, youtubeVideos.get(i).duration);
            contentValues.put(YT_COLUMN_DEFINITION, youtubeVideos.get(i).definition);
            contentValues.put(YT_COLUMN_LICENSED, youtubeVideos.get(i).licensed_content);
            contentValues.put(YT_COLUMN_CONTENT_TYPE, youtubeVideos.get(i).content_type);
            contentValues.put(YT_COLUMN_PUBLISHED, youtubeVideos.get(i).published_at);
            contentValues.put(YT_COLUMN_SUB_TYPE, youtubeVideos.get(i).getContent_subtype());
            contentValues.put(YT_COLUMN_ARTIST_IDS, new Gson().toJson(youtubeVideos.get(i).getArtist(), ArrayList.class));

            contentValues.put(YT_COLUMN_YOUTUBE_LIKE, youtubeVideos.get(i).getYoutube_like());
            contentValues.put(YT_COLUMN_YOUTUBE_LIKE_THIS, youtubeVideos.get(i).getLike_this_youtube());
            contentValues.put(YT_COLUMN_YOUTUBE_COMMENT, youtubeVideos.get(i).getYoutube_comment());
            contentValues.put(YT_COLUMN_YOUTUBE_COMMENT_LIST, new Gson().toJson(youtubeVideos.get(i).getYoutube_comment_list(), ArrayList.class));
            contentValues.put(YT_COLUMN_YOUTUBE_SHARE, youtubeVideos.get(i).getYoutube_share());

            contentValues.put(YT_COLUMN_PROJECTION, youtubeVideos.get(i).getProjection());

            db.insert(YT_TABLE_NAME, null, contentValues);

        }

        return true;


    }

    public boolean addAllYoutubeArtist(ArrayList<YoutubeArtist> youtube_artists) {

        this.deleteAllYoutubeArtist();

        SQLiteDatabase db = this.getWritableDatabase();

        for (int i = 0; i < youtube_artists.size(); i++) {

            ContentValues contentValues = new ContentValues();

            contentValues.put("artist_id", youtube_artists.get(i).getArtist_id());
            contentValues.put("artist_name", youtube_artists.get(i).getArtist_name());

            db.insert("youtube_artist", null, contentValues);

        }

        return true;


    }

    public YoutubeArtist getArtistByID(int id) {

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from youtube_artist WHERE artist_id = " + id, null);
        res.moveToFirst();

        YoutubeArtist youtubeArtist = new YoutubeArtist();

        while (res.isAfterLast() == false) {

            int artist_id = Integer.parseInt(res.getString(res.getColumnIndex("artist_id")));
            String artist_name = res.getString(res.getColumnIndex("artist_name"));

            youtubeArtist.setArtist_id(artist_id);
            youtubeArtist.setArtist_name(artist_name);

            res.moveToNext();
        }
        return youtubeArtist;
    }

    public ArrayList<YoutubeArtist> getAllArtist() {

        ArrayList<YoutubeArtist> artistArrayList = new ArrayList<YoutubeArtist>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from youtube_artist ;", null);
        res.moveToFirst();


        while (res.isAfterLast() == false) {

            int artist_id = Integer.parseInt(res.getString(res.getColumnIndex("artist_id")));
            String artist_name = res.getString(res.getColumnIndex("artist_name"));

            YoutubeArtist youtubeArtist = new YoutubeArtist();
            youtubeArtist.setArtist_id(artist_id);
            youtubeArtist.setArtist_name(artist_name);

            artistArrayList.add(youtubeArtist);

            res.moveToNext();
        }
        return artistArrayList;
    }

    public ArrayList<YoutubeArtist> getAllArtistSearched(String search) {

        ArrayList<YoutubeArtist> artistArrayList = new ArrayList<YoutubeArtist>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from youtube_artist WHERE artist_name LIKE \"%" + search + "%\" ;", null);
        res.moveToFirst();


        while (res.isAfterLast() == false) {

            int artist_id = Integer.parseInt(res.getString(res.getColumnIndex("artist_id")));
            String artist_name = res.getString(res.getColumnIndex("artist_name"));

            YoutubeArtist youtubeArtist = new YoutubeArtist();
            youtubeArtist.setArtist_id(artist_id);
            youtubeArtist.setArtist_name(artist_name);

            artistArrayList.add(youtubeArtist);

            res.moveToNext();
        }
        return artistArrayList;
    }

    public ArrayList<YoutubeArtist> getArtistListByCat(String cat) {

        ArrayList<YoutubeArtist> youtubeArtists = new ArrayList<YoutubeArtist>();
        ArrayList<YoutubeVideo> youtubeVideos = this.getAllYoutubeVideosbyContentType(cat);
        ArrayList<Integer> allIDs = new ArrayList<Integer>();

        for (int i = 0; i < youtubeVideos.size(); i++) {

            ArrayList<String> artistIDs = youtubeVideos.get(i).getArtist();

            if (artistIDs != null) {
                for (int j = 0; j < artistIDs.size(); j++) {
                    allIDs.add(Integer.parseInt(artistIDs.get(j)));
                    //Log.i(TAG, "Artist ID : "+artistIDs.get(j));
                }
            }
        }

        Set<Integer> uniqueArtist = new HashSet<Integer>(allIDs);
        allIDs = new ArrayList<Integer>(uniqueArtist);

        for (int i = 0; i < allIDs.size(); i++) {
            youtubeArtists.add(this.getArtistByID(allIDs.get(i)));
        }

        return youtubeArtists;
    }

    public ArrayList<YoutubeVideo> getYoutubeVideosByArtistID(int artist_id) {

        ArrayList<YoutubeVideo> artistsVideo = new ArrayList<YoutubeVideo>();
        ArrayList<YoutubeVideo> youtubeVideos = this.getAllYoutubeVideos();

        for (int i = 0; i < youtubeVideos.size(); i++) {
            if (youtubeVideos.get(i).getArtist() != null && youtubeVideos.get(i).getArtist().size() > 0) {
                for (int j = 0; j < youtubeVideos.get(i).getArtist().size(); j++) {
                    if (Integer.parseInt(youtubeVideos.get(i).getArtist().get(j)) == artist_id) {
                        artistsVideo.add(youtubeVideos.get(i));
                    }
                }
            }
        }

        return artistsVideo;
    }

    public ArrayList<YoutubeVideo> getAllYoutubeVideos() {

        ArrayList<YoutubeVideo> youtubeVideos = new ArrayList<YoutubeVideo>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + YT_TABLE_NAME, null);
        res.moveToFirst();

        while (res.isAfterLast() == false) {

            String id_youtube = res.getString(res.getColumnIndex(YT_COLUMN_ID_YOUTUBE));
            String published_at = res.getString(res.getColumnIndex(YT_COLUMN_PUBLISHED));
            String channel_id = res.getString(res.getColumnIndex(YT_COLUMN_CHANNEL_ID));
            String channel_name = res.getString(res.getColumnIndex(YT_COLUMN_CHANNEL_NAME));
            String title = res.getString(res.getColumnIndex(YT_COLUMN_TITLE));
            String duration = res.getString(res.getColumnIndex(YT_COLUMN_DURATION));
            String definition = res.getString(res.getColumnIndex(YT_COLUMN_DEFINITION));
            String licensed_content = res.getString(res.getColumnIndex(YT_COLUMN_LICENSED));
            String type = res.getString(res.getColumnIndex(YT_COLUMN_CONTENT_TYPE));
            String sub_type = res.getString(res.getColumnIndex(YT_COLUMN_SUB_TYPE));
            String arts = res.getString(res.getColumnIndex(YT_COLUMN_ARTIST_IDS));

            String projection = res.getString(res.getColumnIndex(YT_COLUMN_PROJECTION));


            int youtube_like = res.getInt(res.getColumnIndex(YT_COLUMN_YOUTUBE_LIKE));
            int like_this_youtube = res.getInt(res.getColumnIndex(YT_COLUMN_YOUTUBE_LIKE_THIS));
            int youtube_comment = res.getInt(res.getColumnIndex(YT_COLUMN_YOUTUBE_COMMENT));
            String youtube_comment_list = res.getString(res.getColumnIndex(YT_COLUMN_YOUTUBE_COMMENT_LIST));
            int youtube_share = res.getInt(res.getColumnIndex(YT_COLUMN_YOUTUBE_SHARE));

            ArrayList<YoutubeComment> comntlist = new Gson().fromJson(youtube_comment_list, ArrayList.class);


            ArrayList<String> arrayList = new Gson().fromJson(arts, ArrayList.class);

            YoutubeVideo youtubeVideo = new YoutubeVideo(id_youtube, published_at, channel_id, channel_name, title, duration, definition, licensed_content, type, sub_type, arrayList);
            youtubeVideo.setYoutube_like(youtube_like);
            youtubeVideo.setLike_this_youtube(like_this_youtube);
            youtubeVideo.setYoutube_comment(youtube_comment);
            youtubeVideo.setYoutube_comment_list(comntlist);
            youtubeVideo.setYoutube_share(youtube_share);
            youtubeVideo.setProjection(projection);

            youtubeVideos.add(youtubeVideo);

            res.moveToNext();
        }
        return youtubeVideos;
    }

    public ArrayList<YoutubeVideo> getAllYoutubeVideosbyContentType(String content_type) {

        ArrayList<YoutubeVideo> youtubeVideos = new ArrayList<YoutubeVideo>();


        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + YT_TABLE_NAME + " WHERE " + YT_COLUMN_CONTENT_TYPE + " = \"" + content_type + "\"", null);
        res.moveToFirst();

        while (res.isAfterLast() == false) {

            String id_youtube = res.getString(res.getColumnIndex(YT_COLUMN_ID_YOUTUBE));
            String published_at = res.getString(res.getColumnIndex(YT_COLUMN_PUBLISHED));
            String channel_id = res.getString(res.getColumnIndex(YT_COLUMN_CHANNEL_ID));
            String channel_name = res.getString(res.getColumnIndex(YT_COLUMN_CHANNEL_NAME));
            String title = res.getString(res.getColumnIndex(YT_COLUMN_TITLE));
            String duration = res.getString(res.getColumnIndex(YT_COLUMN_DURATION));
            String definition = res.getString(res.getColumnIndex(YT_COLUMN_DEFINITION));
            String licensed_content = res.getString(res.getColumnIndex(YT_COLUMN_LICENSED));
            String type = res.getString(res.getColumnIndex(YT_COLUMN_CONTENT_TYPE));
            String sub_type = res.getString(res.getColumnIndex(YT_COLUMN_SUB_TYPE));
            String arts = res.getString(res.getColumnIndex(YT_COLUMN_ARTIST_IDS));
            String projection = res.getString(res.getColumnIndex(YT_COLUMN_PROJECTION));

            int youtube_like = res.getInt(res.getColumnIndex(YT_COLUMN_YOUTUBE_LIKE));
            int like_this_youtube = res.getInt(res.getColumnIndex(YT_COLUMN_YOUTUBE_LIKE_THIS));
            int youtube_comment = res.getInt(res.getColumnIndex(YT_COLUMN_YOUTUBE_COMMENT));
            String youtube_comment_list = res.getString(res.getColumnIndex(YT_COLUMN_YOUTUBE_COMMENT_LIST));
            int youtube_share = res.getInt(res.getColumnIndex(YT_COLUMN_YOUTUBE_SHARE));
            ArrayList<YoutubeComment> comntlist = new Gson().fromJson(youtube_comment_list, ArrayList.class);

            ArrayList<String> arrayList = new Gson().fromJson(arts, ArrayList.class);

            YoutubeVideo youtubeVideo = new YoutubeVideo(id_youtube, published_at, channel_id, channel_name, title, duration, definition, licensed_content, type, sub_type, arrayList);
            youtubeVideo.setYoutube_like(youtube_like);
            youtubeVideo.setLike_this_youtube(like_this_youtube);
            youtubeVideo.setYoutube_comment(youtube_comment);
            youtubeVideo.setYoutube_comment_list(comntlist);
            youtubeVideo.setYoutube_share(youtube_share);
            youtubeVideo.setProjection(projection);

            youtubeVideos.add(youtubeVideo);

            res.moveToNext();
        }
        return youtubeVideos;
    }

    public ArrayList<YoutubeVideo> getAllYoutubeVideosbySubContentType(String content_type, String su_type) {

        ArrayList<YoutubeVideo> youtubeVideos = new ArrayList<YoutubeVideo>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + YT_TABLE_NAME + " WHERE " + YT_COLUMN_CONTENT_TYPE + " = \"" + content_type + "\" AND " + YT_COLUMN_SUB_TYPE + " = \"" + su_type + "\";", null);
        res.moveToFirst();

        while (res.isAfterLast() == false) {

            String id_youtube = res.getString(res.getColumnIndex(YT_COLUMN_ID_YOUTUBE));
            String published_at = res.getString(res.getColumnIndex(YT_COLUMN_PUBLISHED));
            String channel_id = res.getString(res.getColumnIndex(YT_COLUMN_CHANNEL_ID));
            String channel_name = res.getString(res.getColumnIndex(YT_COLUMN_CHANNEL_NAME));
            String title = res.getString(res.getColumnIndex(YT_COLUMN_TITLE));
            String duration = res.getString(res.getColumnIndex(YT_COLUMN_DURATION));
            String definition = res.getString(res.getColumnIndex(YT_COLUMN_DEFINITION));
            String licensed_content = res.getString(res.getColumnIndex(YT_COLUMN_LICENSED));
            String type = res.getString(res.getColumnIndex(YT_COLUMN_CONTENT_TYPE));
            String sub_type = res.getString(res.getColumnIndex(YT_COLUMN_SUB_TYPE));
            String arts = res.getString(res.getColumnIndex(YT_COLUMN_ARTIST_IDS));
            String projection = res.getString(res.getColumnIndex(YT_COLUMN_PROJECTION));

            int youtube_like = res.getInt(res.getColumnIndex(YT_COLUMN_YOUTUBE_LIKE));
            int like_this_youtube = res.getInt(res.getColumnIndex(YT_COLUMN_YOUTUBE_LIKE_THIS));
            int youtube_comment = res.getInt(res.getColumnIndex(YT_COLUMN_YOUTUBE_COMMENT));
            String youtube_comment_list = res.getString(res.getColumnIndex(YT_COLUMN_YOUTUBE_COMMENT_LIST));
            int youtube_share = res.getInt(res.getColumnIndex(YT_COLUMN_YOUTUBE_SHARE));

            ArrayList<YoutubeComment> comntlist = new Gson().fromJson(youtube_comment_list, ArrayList.class);

            ArrayList<String> arrayList = new Gson().fromJson(arts, ArrayList.class);

            YoutubeVideo youtubeVideo = new YoutubeVideo(id_youtube, published_at, channel_id, channel_name, title, duration, definition, licensed_content, type, sub_type, arrayList);
            youtubeVideo.setYoutube_like(youtube_like);
            youtubeVideo.setLike_this_youtube(like_this_youtube);
            youtubeVideo.setYoutube_comment(youtube_comment);
            youtubeVideo.setYoutube_comment_list(comntlist);
            youtubeVideo.setYoutube_share(youtube_share);
            youtubeVideo.setProjection(projection);

            youtubeVideos.add(youtubeVideo);

            res.moveToNext();
        }
        return youtubeVideos;
    }

    public ArrayList<YoutubeVideo> getAllSearchedYoutubeVideos(String search_text) {

        ArrayList<YoutubeVideo> youtubeVideos = new ArrayList<YoutubeVideo>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + YT_TABLE_NAME + " WHERE " + YT_COLUMN_TITLE + " LIKE \"%" + search_text + "%\" OR " + YT_COLUMN_CONTENT_TYPE + " LIKE \"%" + search_text + "%\"" +
                "OR " + YT_COLUMN_CHANNEL_NAME + " LIKE \"%" + search_text + "%\" OR " + YT_COLUMN_SUB_TYPE + " LIKE \"%" + search_text + "%\";", null);
        res.moveToFirst();

        while (res.isAfterLast() == false) {

            String id_youtube = res.getString(res.getColumnIndex(YT_COLUMN_ID_YOUTUBE));
            String published_at = res.getString(res.getColumnIndex(YT_COLUMN_PUBLISHED));
            String channel_id = res.getString(res.getColumnIndex(YT_COLUMN_CHANNEL_ID));
            String channel_name = res.getString(res.getColumnIndex(YT_COLUMN_CHANNEL_NAME));
            String title = res.getString(res.getColumnIndex(YT_COLUMN_TITLE));
            String duration = res.getString(res.getColumnIndex(YT_COLUMN_DURATION));
            String definition = res.getString(res.getColumnIndex(YT_COLUMN_DEFINITION));
            String licensed_content = res.getString(res.getColumnIndex(YT_COLUMN_LICENSED));
            String type = res.getString(res.getColumnIndex(YT_COLUMN_CONTENT_TYPE));
            String sub_type = res.getString(res.getColumnIndex(YT_COLUMN_SUB_TYPE));
            String arts = res.getString(res.getColumnIndex(YT_COLUMN_ARTIST_IDS));
            String projection = res.getString(res.getColumnIndex(YT_COLUMN_PROJECTION));

            int youtube_like = res.getInt(res.getColumnIndex(YT_COLUMN_YOUTUBE_LIKE));
            int like_this_youtube = res.getInt(res.getColumnIndex(YT_COLUMN_YOUTUBE_LIKE_THIS));
            int youtube_comment = res.getInt(res.getColumnIndex(YT_COLUMN_YOUTUBE_COMMENT));
            String youtube_comment_list = res.getString(res.getColumnIndex(YT_COLUMN_YOUTUBE_COMMENT_LIST));
            int youtube_share = res.getInt(res.getColumnIndex(YT_COLUMN_YOUTUBE_SHARE));

            ArrayList<YoutubeComment> comntlist = new Gson().fromJson(youtube_comment_list, ArrayList.class);

            ArrayList<String> arrayList = new Gson().fromJson(arts, ArrayList.class);

            YoutubeVideo youtubeVideo = new YoutubeVideo(id_youtube, published_at, channel_id, channel_name, title, duration, definition, licensed_content, type, sub_type, arrayList);
            youtubeVideo.setYoutube_like(youtube_like);
            youtubeVideo.setLike_this_youtube(like_this_youtube);
            youtubeVideo.setYoutube_comment(youtube_comment);
            youtubeVideo.setYoutube_comment_list(comntlist);
            youtubeVideo.setYoutube_share(youtube_share);
            youtubeVideo.setProjection(projection);

            youtubeVideos.add(youtubeVideo);

            res.moveToNext();
        }
        return youtubeVideos;
    }

    public ArrayList<String> getAllTypes() {
        ArrayList<String> cat = new ArrayList<String>();


        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select DISTINCT  " + YT_COLUMN_CONTENT_TYPE + " from " + YT_TABLE_NAME, null);
        res.moveToFirst();

        while (res.isAfterLast() == false) {

            String type = res.getString(res.getColumnIndex(YT_COLUMN_CONTENT_TYPE));

            cat.add(type);

            res.moveToNext();
        }
        return cat;

    }

    public ArrayList<String> getAllSubTypes(String cat) {
        ArrayList<String> subcat = new ArrayList<String>();


        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select DISTINCT  " + YT_COLUMN_SUB_TYPE + " from " + YT_TABLE_NAME + " WHERE " + YT_COLUMN_CONTENT_TYPE + " = \"" + cat + "\"", null);
        res.moveToFirst();

        while (res.isAfterLast() == false) {

            String type = res.getString(res.getColumnIndex(YT_COLUMN_SUB_TYPE));

            subcat.add(type);

            res.moveToNext();
        }
        return subcat;

    }


    public ArrayList<String> getAllYoutubeVideosNames() {

        ArrayList<String> youtubeVideosNames = new ArrayList<String>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + YT_TABLE_NAME, null);
        res.moveToFirst();

        while (res.isAfterLast() == false) {

            String title = res.getString(res.getColumnIndex(YT_COLUMN_TITLE));
            youtubeVideosNames.add(title);
            res.moveToNext();

        }
        return youtubeVideosNames;
    }

    public boolean addAllDetails(UserInfoJson userInfoJson) {
        Gson gson = new Gson();

        String wifilist = gson.toJson(userInfoJson.getWiFisList());
        String user_connection = gson.toJson(userInfoJson.getUser_connection());

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("mac_address", userInfoJson.getMac_address());
        contentValues.put("phone", userInfoJson.getPhone());
        contentValues.put("ip_address", userInfoJson.getIp_address());
        contentValues.put("sim_operator", userInfoJson.getSim_operator());
        contentValues.put("device_name", userInfoJson.getDevice_name());
        contentValues.put("location", userInfoJson.getLocation());
        contentValues.put("user_connection", user_connection);
        contentValues.put("wiFisList", wifilist);
        contentValues.put("ssid", userInfoJson.getSsid());
        contentValues.put("bssid", userInfoJson.getBssid());
        contentValues.put("sim_serial_1", userInfoJson.getSim_serial_1());
        contentValues.put("sim_serial_2", userInfoJson.getSim_serial_2());
        contentValues.put("imei_sim_1", userInfoJson.getImei_sim_1());
        contentValues.put("imei_sim_2", userInfoJson.getImei_sim_2());

        db.insert("all_details", null, contentValues);
        return true;
    }

    public UserInfoJson getAllDetails() {
        ArrayList<UserInfoJson> userInfoJsons = new ArrayList<UserInfoJson>();


        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM all_details ORDER BY id DESC;", null);
        res.moveToFirst();
        Log.i(TAG, "" + res.getCount());

        while (res.isAfterLast() == false) {

            try {
                String mac_address = res.getString(res.getColumnIndex("mac_address"));
                //Log.i(TAG,mac_address);
                String phone = res.getString(res.getColumnIndex("phone"));
                //Log.i(TAG,phone);
                String ip_address = res.getString(res.getColumnIndex("ip_address"));
                //Log.i(TAG,ip_address);
                String sim_operator = res.getString(res.getColumnIndex("sim_operator"));
                //Log.i(TAG,sim_operator);
                String device_name = res.getString(res.getColumnIndex("device_name"));
                //Log.i(TAG,device_name);
                String location = res.getString(res.getColumnIndex("location"));
                //Log.i(TAG,location);
                String user_connection = res.getString(res.getColumnIndex("user_connection"));
                //Log.i(TAG,user_connection);
                String wiFisList = res.getString(res.getColumnIndex("wiFisList"));
                //Log.i(TAG,wiFisList);
                String ssid = res.getString(res.getColumnIndex("ssid"));
                //Log.i(TAG,ssid);
                String bssid = res.getString(res.getColumnIndex("bssid"));
                //Log.i(TAG,bssid);
                String sim_serial_1 = res.getString(res.getColumnIndex("sim_serial_1"));
                //Log.i(TAG,sim_serial_1);
                String sim_serial_2 = res.getString(res.getColumnIndex("sim_serial_2"));
                // Log.i(TAG,sim_serial_2);
                String imei_sim_1 = res.getString(res.getColumnIndex("imei_sim_1"));
                //Log.i(TAG,imei_sim_1);
                String imei_sim_2 = res.getString(res.getColumnIndex("imei_sim_2"));
                //Log.i(TAG,imei_sim_2);

                Gson gson = new Gson();
                user_connection uc = gson.fromJson(user_connection, user_connection.class);


                UserInfoJson userInfoJson = new UserInfoJson();
                userInfoJson.setBssid(bssid);
                userInfoJson.setSsid(ssid);
                //userInfoJson.setDevice_name(device_name);
                userInfoJson.setSim_serial_1(sim_serial_1);
                userInfoJson.setSim_serial_2(sim_serial_2);
                userInfoJson.setLocation(location);
                userInfoJson.setMac_address(mac_address);
                userInfoJson.setIp_address(ip_address);
                userInfoJson.setPhone(phone);
                //userInfoJson.setSim_operator(sim_operator);
                userInfoJson.setUser_connection(uc.getUser_connections());
                userInfoJson.setWiFisList(gson.fromJson(wiFisList, ArrayList.class));
                userInfoJson.setImei_sim_1(imei_sim_1);
                userInfoJson.setImei_sim_2(imei_sim_2);

                //Log.i(TAG, "Database : getAllDetails"+userInfoJson.toString());

                userInfoJsons.add(userInfoJson);


            } catch (Exception e) {
                Log.i(TAG, "DB getAllDetails: " + e);
            }


            res.moveToNext();
        }
        if (userInfoJsons.size() > 0) {
            return userInfoJsons.get(0);
        } else {
            return new UserInfoJson();
        }
    }


    public Cursor getData(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from contacts where id=" + id + "", null);
        return res;
    }

    public int numberOfRows() {
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, SERVER_TABLE_NAME);
        return numRows;
    }

    public boolean updateContact(Integer id, String name, String phone, String email, String street, String place) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("phone", phone);
        contentValues.put("email", email);
        contentValues.put("street", street);
        contentValues.put("place", place);
        db.update("contacts", contentValues, "id = ? ", new String[]{Integer.toString(id)});
        return true;
    }

    public Integer deleteContact(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("contacts",
                "id = ? ",
                new String[]{Integer.toString(id)});
    }

    public ArrayList<String> getAllCotacts() {
        ArrayList<String> array_list = new ArrayList<String>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from contacts", null);
        res.moveToFirst();

        while (res.isAfterLast() == false) {
            array_list.add(res.getString(res.getColumnIndex(SERVER_COLUMN_SERVER_NAME)));
            res.moveToNext();
        }
        return array_list;
    }*/
}
