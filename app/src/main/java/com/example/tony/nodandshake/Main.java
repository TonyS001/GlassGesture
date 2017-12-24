package com.example.tony.nodandshake;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Main extends AppCompatActivity {

    private Button stepCalculator;
    private Button nodAndShake;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        stepCalculator = (Button) findViewById(R.id.stepCalculator);
        stepCalculator.setOnClickListener(new myOnClickListener());
        nodAndShake = (Button) findViewById(R.id.nodAndShake);
        nodAndShake.setOnClickListener(new myOnClickListener());
    }

    private class myOnClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view){
            switch (view.getId()) {
                case R.id.stepCalculator:
                    Intent intent1 = new Intent(getApplicationContext(), StepCalculator.class);
                    startActivity(intent1);
                    break;
                case R.id.nodAndShake:
                    Intent intent2 = new Intent(getApplicationContext(), NodShake.class);
                    startActivity(intent2);
                    break;
                default:
                    break;
            }
        }
    }
}
