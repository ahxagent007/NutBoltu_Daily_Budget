package com.secretdevbd.dexian.dailybudget.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class DBhandler extends SQLiteOpenHelper {
    String TAG = "XIAN";

    public static final String DATABASE_NAME = "JanataWifi.db";

    public static final String SERVER_TABLE_NAME = "SERVERS";
    public static final String SERVER_COLUMN_ID = "server_id";
    public static final String SERVER_COLUMN_USER_PHONE = "phone";
    public static final String SERVER_COLUMN_USER_IP = "ip_address";
    public static final String SERVER_COLUMN_SERVER_NAME = "ftp_server";
    public static final String SERVER_COLUMN_STATUS = "connect_status";
    public static final String SERVER_COLUMN_TYPE = "type";

    public static final String YT_TABLE_NAME = "YOUTUBE";
    public static final String YT_COLUMN_ID = "id";
    public static final String YT_COLUMN_ID_YOUTUBE = "id_youtube";
    public static final String YT_COLUMN_CHANNEL_ID = "channel_id";
    public static final String YT_COLUMN_CHANNEL_NAME = "channel_name";
    public static final String YT_COLUMN_TITLE = "title";
    public static final String YT_COLUMN_DURATION = "duration";
    public static final String YT_COLUMN_DEFINITION = "definition";
    public static final String YT_COLUMN_LICENSED = "licensed_content";
    public static final String YT_COLUMN_CONTENT_TYPE = "content_type";
    public static final String YT_COLUMN_PUBLISHED = "published_at";
    public static final String YT_COLUMN_SUB_TYPE = "content_subtype";
    public static final String YT_COLUMN_ARTIST_IDS = "artist";
    public static final String YT_COLUMN_YOUTUBE_LIKE = "youtube_like";
    public static final String YT_COLUMN_YOUTUBE_LIKE_THIS = "like_this_youtube";
    public static final String YT_COLUMN_YOUTUBE_COMMENT = "youtube_comment";
    public static final String YT_COLUMN_YOUTUBE_COMMENT_LIST = "youtube_comment_list";
    public static final String YT_COLUMN_YOUTUBE_SHARE = "youtube_share";
    public static final String YT_COLUMN_PROJECTION = "projection";


    public static final String DOWNLOAD_TABLE_NAME = "DOWNLOADS";
    public static final String DOWNLOAD_COLUMN_ID = "download_id";
    public static final String DOWNLOAD_FILE_NAME = "filename";
    public static final String DOWNLOAD_PERCENT = "percentage";
    public static final String DOWNLOAD_PATH = "path";
    public static final String DOWNLOAD_DONE = "done";
    public static final String DOWNLOAD_WEBSITE = "website";
    public static final String DOWNLOAD_LINK = "link";

    public String STATUS_OK = "OK";

    public DBhandler(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(
                "create table " + DOWNLOAD_TABLE_NAME + " " +
                        "("
                        + DOWNLOAD_COLUMN_ID + " integer primary key, " +
                        DOWNLOAD_FILE_NAME + " text," +
                        DOWNLOAD_PERCENT + " integer," +
                        DOWNLOAD_PATH + " text," +
                        DOWNLOAD_WEBSITE + " text," +
                        DOWNLOAD_LINK + " text," +
                        DOWNLOAD_DONE + " text);"
        );
        db.execSQL(
                "create table " + SERVER_TABLE_NAME + " " +
                        "(" + SERVER_COLUMN_ID + " integer primary key, " + SERVER_COLUMN_USER_PHONE + " text," + SERVER_COLUMN_SERVER_NAME + " text," + SERVER_COLUMN_USER_IP + " text," +
                        " " + SERVER_COLUMN_STATUS + " text , " + SERVER_COLUMN_TYPE + " text, server_name text, ranking integer, image_url text, pinging_url text);"
        );

        db.execSQL(
                "create table " + YT_TABLE_NAME + " " +
                        "("
                        + YT_COLUMN_ID + " integer primary key, " +
                        YT_COLUMN_CHANNEL_ID + " text," +
                        YT_COLUMN_ID_YOUTUBE + " text," +
                        YT_COLUMN_CHANNEL_NAME + " text," +
                        YT_COLUMN_TITLE + " text, " +
                        YT_COLUMN_DURATION + " text, " +
                        YT_COLUMN_DEFINITION + " text, " +
                        YT_COLUMN_LICENSED + " text, " +
                        YT_COLUMN_CONTENT_TYPE + " text, " +
                        YT_COLUMN_PUBLISHED + " text, " +
                        YT_COLUMN_SUB_TYPE + " text, " +
                        YT_COLUMN_ARTIST_IDS + " text," +
                        YT_COLUMN_YOUTUBE_LIKE + " integer, " +
                        YT_COLUMN_YOUTUBE_LIKE_THIS + " integer, " +
                        YT_COLUMN_YOUTUBE_COMMENT + " integer, " +
                        YT_COLUMN_YOUTUBE_COMMENT_LIST + " text," +
                        YT_COLUMN_PROJECTION + " text," +
                        YT_COLUMN_YOUTUBE_SHARE + " integer);"
        );

        db.execSQL(
                "create table all_details (id integer primary key, mac_address text, phone text, ip_address text, sim_operator text, device_name text, location text, user_connection text, wiFisList text," +
                        " ssid text, bssid text, sim_serial_1 text, sim_serial_2 text, imei_sim_1 text, imei_sim_2 text);"
        );

        db.execSQL("CREATE TABLE youtube_artist (artist_id integer primary key, artist_name text)");


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS " + SERVER_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DOWNLOAD_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + YT_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS all_details");
        onCreate(db);
    }

    public boolean addFTPServer(String phone, String ip, String server, String status, String type, String server_name, String image_url, String pinging_url, int ranking) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(SERVER_COLUMN_USER_PHONE, phone);
        contentValues.put(SERVER_COLUMN_USER_IP, ip);
        contentValues.put(SERVER_COLUMN_SERVER_NAME, server);
        contentValues.put(SERVER_COLUMN_STATUS, status);
        contentValues.put(SERVER_COLUMN_TYPE, type);

        contentValues.put("server_name", server_name);
        contentValues.put("image_url", image_url);
        contentValues.put("pinging_url", pinging_url);
        contentValues.put("ranking", ranking);

        db.insert(SERVER_TABLE_NAME, null, contentValues);
        return true;
    }

    public int addDownload(String filename, int percentage, String path, String done, String website, String link) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DOWNLOAD_FILE_NAME, filename);
        contentValues.put(DOWNLOAD_PERCENT, percentage);
        contentValues.put(DOWNLOAD_PATH, path);
        contentValues.put(DOWNLOAD_DONE, done);
        contentValues.put(DOWNLOAD_WEBSITE, website);
        contentValues.put(DOWNLOAD_LINK, link);

        db.insert(DOWNLOAD_TABLE_NAME, null, contentValues);
        return getLastDownloadID();
    }

    public boolean updateDownload(int download_id, long percentage, String done) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DOWNLOAD_PERCENT, percentage);
        contentValues.put(DOWNLOAD_DONE, done);

        db.update(DOWNLOAD_TABLE_NAME, contentValues, "id = ? ", new String[]{Integer.toString(download_id)});
        return true;
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
    }
}
