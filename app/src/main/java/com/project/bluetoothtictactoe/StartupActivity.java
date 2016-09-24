package com.project.bluetoothtictactoe;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class StartupActivity extends Activity implements OnClickListener {
    BluetoothAdapter btAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startup);
        Button b_singleP = (Button) findViewById(R.id.bSinglePlayer);
        Button b_multiP = (Button) findViewById(R.id.bMultiPlayer);
        Button b_about = (Button) findViewById(R.id.bAbout);
        Button b_exit = (Button) findViewById(R.id.bExitGame);

        b_singleP.setOnClickListener(this);
        b_multiP.setOnClickListener(this);
        b_about.setOnClickListener(this);
        b_exit.setOnClickListener(this);
        btAdapter = BluetoothAdapter.getDefaultAdapter();
        if (btAdapter != null) {
            if (!btAdapter.isEnabled()) {
                Intent iBlueEnabled = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(iBlueEnabled, 1);
                //b_multiP.setVisibility()
            }

        } else if (btAdapter == null) {
            Toast.makeText(getApplicationContext(), "No BlueTooth adapter present. You can only play LOCALLY", Toast.LENGTH_SHORT).show();
            //finish();
            b_multiP.setClickable(false);


        }


    }//end of onCreate();

    public void getIntent(Class<?> str) {
        Intent iNavigation = new Intent(this, str);
        startActivity(iNavigation);

    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.bSinglePlayer) {
            Toast.makeText(getApplicationContext(), "MultiPlayer Local Game", Toast.LENGTH_SHORT).show();
            getIntent(SinglePlayer.class);


        } else if (v.getId() == R.id.bMultiPlayer) {

            Toast.makeText(getApplicationContext(), "Multi Player Game using Bluetooth", Toast.LENGTH_SHORT).show();

            //getIntent(dialog_playername.class);

            getIntent(BluetoothTicTacToe.class);
            return;

        } else if (v.getId() == R.id.bAbout) {
            Toast.makeText(getApplicationContext(), "About The Developers", Toast.LENGTH_SHORT).show();
            getIntent(AboutDevelopers.class);


        } else if (v.getId() == R.id.bExitGame) {
            //Toast.makeText(getApplicationContext(), "Exiting the Game", Toast.LENGTH_SHORT).show();
            AlertDialog.Builder builder = new AlertDialog.Builder(StartupActivity.this);
            builder.setMessage("Are you Sure, You Wanna Exit?");
            builder.setCancelable(false);
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    finish();

                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    arg0.cancel();

                }
            });
            AlertDialog alert = builder.create();
            alert.show();


        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.startup, menu);
        return true;
    }

    @Override
    public void onBackPressed() {

        //Toast.makeText(getApplicationContext(), "back clickedd", Toast.LENGTH_SHORT).show();


        //Toast.makeText(getApplicationContext(), "Exiting the Game", Toast.LENGTH_SHORT).show();
        AlertDialog.Builder builder = new AlertDialog.Builder(StartupActivity.this);
        builder.setMessage("Are you Sure, You Wanna Exit?");
        builder.setCancelable(false);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                ///////

                ///////////
                finish();

            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                arg0.cancel();

            }
        });
        AlertDialog alert = builder.create();
        alert.show();


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_CANCELED) {
            Toast.makeText(getApplicationContext(), "EXITING THE APP, BYE BYE", Toast.LENGTH_SHORT).show();
            finish();
        }
        if (resultCode == RESULT_OK) {

        }
    }

}

