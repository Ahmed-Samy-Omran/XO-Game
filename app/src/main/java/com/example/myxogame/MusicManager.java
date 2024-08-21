package com.example.myxogame;


import android.content.Context;
import android.media.MediaPlayer;
public class MusicManager {
    private static MediaPlayer mediaPlayer;
    private static boolean isPlaying = false;

    public static void playMusic(Context context) {
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(context, R.raw.music_background);
            mediaPlayer.setLooping(true); // Set looping
        }

        if (!isPlaying) {
            mediaPlayer.start();
            isPlaying = true;
        }
    }

    public static void stopMusic() {
        if (mediaPlayer != null && isPlaying) {
            mediaPlayer.pause();
            isPlaying = false;
        }
    }

    public static void release() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
            isPlaying = false;
        }
    }

    public static boolean isPlaying() {
        return isPlaying;
    }
}