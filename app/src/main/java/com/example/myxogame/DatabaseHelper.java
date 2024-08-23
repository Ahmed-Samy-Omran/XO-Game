package com.example.myxogame;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "xo_game.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_SCORES = "scores";

    private static final String COLUMN_ID = "id";
    private static final String COLUMN_PLAYER_NAME = "player_name";
    private static final String COLUMN_SCORE = "score";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_SCORES + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_PLAYER_NAME + " TEXT, " +
                COLUMN_SCORE + " INTEGER)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SCORES);
        onCreate(db);
    }

    public void saveScore(String playerName, int score) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Check if the player already exists
        String query = "SELECT " + COLUMN_SCORE + " FROM " + TABLE_SCORES + " WHERE " + COLUMN_PLAYER_NAME + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{playerName});

        ContentValues values = new ContentValues();
        values.put(COLUMN_PLAYER_NAME, playerName);

        if (cursor.moveToFirst()) {
            // Player exists, update the score
            @SuppressLint("Range") int currentScore = cursor.getInt(cursor.getColumnIndex(COLUMN_SCORE));
            values.put(COLUMN_SCORE, currentScore + score);
            db.update(TABLE_SCORES, values, COLUMN_PLAYER_NAME + " = ?", new String[]{playerName});
        } else {
            // New player, insert the score
            values.put(COLUMN_SCORE, score);
            db.insert(TABLE_SCORES, null, values);
        }

        cursor.close();
        db.close();
    }

    @SuppressLint("Range")
    public List<Score> getAllScores() {
        List<Score> scores = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_SCORES + " ORDER BY " + COLUMN_SCORE + " DESC";
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Score score = new Score();
                score.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)));
                score.setPlayerName(cursor.getString(cursor.getColumnIndex(COLUMN_PLAYER_NAME)));
                score.setScore(cursor.getInt(cursor.getColumnIndex(COLUMN_SCORE)));
                scores.add(score);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return scores;
    }

    @SuppressLint("Range")
    public int getScore(String playerName) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT " + COLUMN_SCORE + " FROM " + TABLE_SCORES + " WHERE " + COLUMN_PLAYER_NAME + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{playerName});

        int score = 0;
        if (cursor.moveToFirst()) {
            score = cursor.getInt(cursor.getColumnIndex(COLUMN_SCORE));
        }
        cursor.close();
        db.close();
        return score;
    }

}
