package br.ifsc.edu.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

public class Tocando extends AppCompatActivity {

    MainActivity m = new MainActivity();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tocando);

    }

    public void pararAlarm(View view){
        m.stop(view);
        finish();
    }
}
