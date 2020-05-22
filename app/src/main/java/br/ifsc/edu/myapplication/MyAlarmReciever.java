package br.ifsc.edu.myapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.VibrationEffect;
import android.util.Log;
import android.widget.Toast;

public class MyAlarmReciever extends BroadcastReceiver {

    MainActivity my = new MainActivity();

    @Override
    public void onReceive(Context context, Intent intent) {

        int miliseconds = 500000;
        if (my.player == null) {
            miliseconds= 50000+50000;
            my.player = MediaPlayer.create(context, R.raw.morning_flower);
            my.vibrator.vibrate(VibrationEffect.createOneShot(miliseconds, VibrationEffect.DEFAULT_AMPLITUDE));
            //precisa continuar vibrando

            my.player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    my.stopPlayer();
                }
            });
        }

        my.player.start();
        Toast.makeText(context, "OnReceive alarm test", Toast.LENGTH_SHORT).show();
    }
}
