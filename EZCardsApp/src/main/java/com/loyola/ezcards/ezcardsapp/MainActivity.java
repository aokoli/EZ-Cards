package com.loyola.ezcards.ezcardsapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 *
 * @see com.loyola.ezcards.ezcardsapp.util.SystemUiHider
 */
public class MainActivity extends Activity implements View.OnClickListener {

    private Button btnCapture;
    private Button btnMyCards;
    private Button btnSelectCards;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        btnCapture = (Button) findViewById(R.id.btnCapture);
        btnMyCards = (Button) findViewById(R.id.btnMycard);
        btnSelectCards = (Button) findViewById(R.id.btnSelect);

        btnCapture.setOnClickListener(this);
        btnMyCards.setOnClickListener(this);
        btnSelectCards.setOnClickListener(this);

//
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnCapture:
                Intent intent = new Intent(this, OCRServiceActivity.class);
                startActivity(intent);
                break;
            case R.id.btnMycard:
                Intent intent1 = new Intent(this,MycardsAct.class);
                startActivity(intent1);
                break;
            case R.id.btnSelect:
                Intent intent2 = new Intent(this,SelectcardsAct.class);
                startActivity(intent2);
                break;
        }

    }

}

