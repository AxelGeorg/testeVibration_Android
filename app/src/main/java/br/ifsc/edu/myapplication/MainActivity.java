package br.ifsc.edu.myapplication;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Camera;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.lang.reflect.Parameter;
import java.security.Policy;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    MediaPlayer player;
    Vibrator vibrator;
    Date dataHoraAtual = new Date();
    private  int dia,mes,ano,hora,minutos;
    TextView dataText,TextViewData,TextViewHora;
    String diaAtual,mesAtual,anoAtual,horaAtual,minutosAtual;

    private AlarmManager alarmMgr;
    private PendingIntent alarmIntent;

    boolean isFlashOn;
    Camera camera;
    Parameters params;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextViewData=findViewById(R.id.TextViewData);
        TextViewHora=findViewById(R.id.TextViewHora);

        diaAtual = new SimpleDateFormat("dd").format(dataHoraAtual);
        mesAtual = new SimpleDateFormat("MM").format(dataHoraAtual);
        anoAtual = new SimpleDateFormat("yyyy").format(dataHoraAtual);
        horaAtual = new SimpleDateFormat("HH").format(dataHoraAtual);
        minutosAtual = new SimpleDateFormat("mm").format(dataHoraAtual);



        Button Vibration = findViewById(R.id.Vibration);
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);

        Vibration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (Build.VERSION.SDK_INT >= 26) {
                    vibrator.vibrate(VibrationEffect.createOneShot(2000, VibrationEffect.DEFAULT_AMPLITUDE));
                    // vibrator.vibrate(VibrationEffect.createOneShot(2000, VibrationEffect.DEFAULT_AMPLITUDE), AudioAttributes.);
                } else {
                    vibrator.vibrate(500);
                }
            }
        });


    }


    public void escolheData(View view){
        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        Calendar cal_alarme = Calendar.getInstance();

        dia=cal_alarme.get(Calendar.DAY_OF_MONTH);
        mes=cal_alarme.get(Calendar.MONTH);
        ano=cal_alarme.get(Calendar.YEAR);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                TextViewData.setText("  "+dayOfMonth+"/"+(monthOfYear+1)+"/"+year);
            }
        }
                ,dia,mes,ano);
        //mes += 1;

        datePickerDialog.updateDate(ano,mes,dia);
        datePickerDialog.show();
    }

    public void escolheHora(View view){
        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        Calendar cal_alarme = Calendar.getInstance();

        hora = cal_alarme.get(Calendar.HOUR_OF_DAY);
        minutos = cal_alarme.get(Calendar.MINUTE);

        final TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                TextViewHora.setText("  "+hourOfDay+":"+minute);
            }
        },hora,minutos,false);
        timePickerDialog.show();
    }

    public void mostraData(View view){

        String dia = new SimpleDateFormat("dd").format(dataHoraAtual);
        String mes = new SimpleDateFormat("MM").format(dataHoraAtual);
        String ano = new SimpleDateFormat("yyyy").format(dataHoraAtual);

        Toast.makeText(this, "dia="+dia+"---mes="+mes+"----ano="+ano, Toast.LENGTH_LONG).show();
    }

    public void mostraHora(View view){
        String data = new SimpleDateFormat("dd/MM/yyyy").format(dataHoraAtual);
        String hora = new SimpleDateFormat("HH:mm").format(dataHoraAtual);

        String Hora = new SimpleDateFormat("HH").format(dataHoraAtual);
        String minutos = new SimpleDateFormat("mm").format(dataHoraAtual);

        Toast.makeText(this, "Hora="+Hora+"---Minutos="+minutos, Toast.LENGTH_LONG).show();
    }









    public void flash_effect() throws InterruptedException{

        Thread a = new Thread()
        {
            public void run()
            {
                for(int i =0; i < 50; i++)
                {

                    turnOnFlash();
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    turnOffFlash();
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {

                        e.printStackTrace();
                    }

                }
            }
        };

        a.start();
    }

    
    public void turnOnFlash() {
        if (!isFlashOn) {
            if (camera == null || params == null) {
                return;
            }
            camera = Camera.open();
            Policy.Parameters p = camera.getParameters();
            p.setFlashMode(Parameters.FLASH_MODE_TORCH);
            camera.setParameters(p);
            camera.startPreview();
        }
    }
    public void turnOffFlash() {
        if (isFlashOn) {
            if (camera == null || params == null) {
                return;
            }

            params = camera.getParameters();
            params.setFlashMode(Parameters.FLASH_MODE_OFF);
            camera.setParameters(params);
            camera.stopPreview();
            isFlashOn = false;

        }
    }


    public void flashOnOffOnOff(View view) throws InterruptedException {
        flash_effect();
    }










    public void pronto(View view){

        alarmMgr = (AlarmManager)this.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, MyAlarmReciever.class);
        alarmIntent = PendingIntent.getBroadcast(this, 0, intent, 0);

        // Set the alarm to start at 8:30 a.m.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, hora);
        calendar.set(Calendar.MINUTE, minutos);

        // setRepeating() lets you specify a precise custom interval--in this case,
        // 20 minutes.
        alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                1000 * 60 * 20, alarmIntent);







//        String diaEscolhido = String.valueOf(dia);
//        String mesEscolhido = String.valueOf(mes);
//        String anoEscolhido = String.valueOf(ano);
//        String horaEscolhido = String.valueOf(hora);
//        String minutosEscolhido = String.valueOf(minutos);
//
//
//        if((diaAtual==diaEscolhido)&&(mesAtual==mesEscolhido)&&(anoAtual==anoEscolhido)
//        &&(horaAtual==horaEscolhido)&&(minutosAtual==minutosEscolhido)){
//
//            Intent abrirAlarm = new Intent(this,Tocando.class);
//            startActivity(abrirAlarm);
//
//            play(view);
//        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void play(View v) {
        int miliseconds = 500000;
        if (player == null) {
            miliseconds= 50000+50000;
            player = MediaPlayer.create(this, R.raw.morning_flower);
            vibrator.vibrate(VibrationEffect.createOneShot(miliseconds, VibrationEffect.DEFAULT_AMPLITUDE));
            //precisa continuar vibrando

            player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    stopPlayer();
                }
            });
        }

        player.start();
    }

    public void pause(View v) {
        if (player != null) {
            player.pause();
            vibrator.cancel();
        }
    }

    public void stop(View v) {
        stopPlayer();
    }

    void stopPlayer() {
        if (player != null) {
            player.release();
            player = null;
            Toast.makeText(this, "MediaPlayer released", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopPlayer();
    }

}