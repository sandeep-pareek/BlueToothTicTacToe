package com.project.bluetoothtictactoe;

import android.R.drawable;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class BluetoothTicTacToe extends Activity {
    // Message types sent from the BluetoothChatService Handler
    public static final int MESSAGE_STATE_CHANGE = 1;
    public static final int MESSAGE_READ = 2;
    public static final int MESSAGE_WRITE = 3;
    public static final int MESSAGE_DEVICE_NAME = 4;
    public static final int MESSAGE_TOAST = 5;
    // Key names received from the BluetoothChatService Handler
    public static final String DEVICE_NAME = "device_name";
    public static final String TOAST = "toast";
    // Debugging
    private static final String TAG = "Bluetoothtic";
    private static final boolean D = true;
    private static final int ID_PLAYER1_WIN = 12;
    private static final int ID_PLAYER2_WIN = 13;
    private static final int ID_GAME_DRAW = 14;
    // Intent request codes
    private static final int REQUEST_CONNECT_DEVICE = 1;
    private static final int REQUEST_ENABLE_BT = 2;
    public static int count = 0;
    protected int returnCode;
    protected CharSequence player_name_2 = "player 2";
    protected CharSequence player_name_1 = "You";
    int skin_cross = R.drawable.default_x;
    int skin_dot = R.drawable.default_o;
    int skin_default = R.drawable.default_my;
    int k = 0;
    //int j;
    int x = 0;
    Integer[][] m = new Integer[3][3];
    int j = 0;
    private EditText mOutEditText;
    private Button mSendButton;
    // Name of the connected device
    private String mConnectedDeviceName = null;
    /* // Array adapter for the conversation thread
     private ArrayAdapter<String> mConversationArrayAdapter;
    */ // String buffer for outgoing messages
    private StringBuffer mOutStringBuffer;
    // Local Bluetooth adapter
    private BluetoothAdapter mBluetoothAdapter = null;
    // Member object for the chat services
    private BlueToothTicTacToeService mticTacToeService = null;
    OnClickListener button_listener = new View.OnClickListener() {

        @SuppressWarnings("deprecation")
        @Override
        public void onClick(View v) {
            ImageButton ibutton = (ImageButton) v;
            count++;

            // Button inactive for further clicks until a result is obtained.
            Integer message = ibutton.getId();
            String ch = message.toString();

            sendMessage(ch);

            ibutton.setClickable(false);

            if (count % 2 != 0) {
                //ibutton.setImageResource(skin_cross);
                ibutton.setBackgroundResource(skin_cross);
                returnCode = setMatrix(message, 1);
            } else if (count % 2 == 0) {
                ibutton.setBackgroundResource(skin_dot);
                returnCode = setMatrix(message, 2);
            }
            //****check if return was a win for player 1, 2 or a draw?//
            if (returnCode == 1) {
                //	score_player_1= score_player_1 + 1;
                //	CharSequence tempVar = score_player_1.toString();

                //	TextView p1wincount = (TextView) findViewById(R.id.textView4);
                //	p1wincount.setText(tempVar);


                showDialog(ID_PLAYER1_WIN);

                Toast.makeText(getApplicationContext(), "player 1 \"X\" wins", Toast.LENGTH_SHORT).show();
            } else if (returnCode == 2) {
                //	score_player_2+=1;
                //	CharSequence tempVar1 = score_player_2.toString();
                //	TextView p2wincount= (TextView) findViewById(R.id.textView5);
                //	p2wincount.setText(tempVar1);

                Toast.makeText(getApplicationContext(), "player 2 \"O\" wins", Toast.LENGTH_SHORT).show();

                showDialog(ID_PLAYER2_WIN);
            } else if (returnCode == 0 && count == 9) {
                //	count_draws += 1;
                //	CharSequence tempvar2=count_draws.toString();
                //	TextView drawCount= (TextView) findViewById(R.id.textView6);
                //	drawCount.setText(tempvar2);

                Toast.makeText(getApplicationContext(), "drawwwww", Toast.LENGTH_SHORT).show();

                showDialog(ID_GAME_DRAW);
            }


        }
    };
    // The Handler that gets information back from the BluetoothTicTacToetService
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_STATE_CHANGE:
                    if (D) Log.i(TAG, "MESSAGE_STATE_CHANGE: " + msg.arg1);
                    switch (msg.arg1) {
                        case BlueToothTicTacToeService.STATE_CONNECTED:


                            //set the board
                            setBoard();

                            // 	Toast.makeText(getApplicationContext()," -----connected--== to "+mConnectedDeviceName, Toast.LENGTH_SHORT).show();
                            //set names//
                            TextView t1 = (TextView) findViewById(R.id.textView1);
                            TextView t2 = (TextView) findViewById(R.id.textView3);
                            t1.setText(player_name_1);
                            t2.setText(mConnectedDeviceName);


                            break;
                        case BlueToothTicTacToeService.STATE_CONNECTING:

                            break;
                        case BlueToothTicTacToeService.STATE_LISTEN:
                        case BlueToothTicTacToeService.STATE_NONE:
                            //     Toast.makeText(getApplicationContext(),"not connected to any devices", Toast.LENGTH_SHORT).show();
                            break;
                    }
                    break;
                case MESSAGE_WRITE:
                    byte[] writeBuf = (byte[]) msg.obj;
                    // construct a string from the buffer
                    String writeMessage = new String(writeBuf);
                    // mConversationArrayAdapter.add("Me:  " + writeMessage);


                    break;
                case MESSAGE_READ:
                    byte[] readBuf = (byte[]) msg.obj;
                    count++;

                    // construct a string from the valid bytes in the buffer
                    String readMessage = new String(readBuf, 0, msg.arg1);
                    //mConversationArrayAdapter.add(mConnectedDeviceName+":  " + readMessage);
                    int id = Integer.parseInt(readMessage);
                    if (id == R.id.b1) {
                        final ImageButton b1 = (ImageButton) findViewById(R.id.b1);

                        if (count % 2 != 0) {
                            b1.setBackgroundResource(skin_cross);

                        } else if (count % 2 == 0) {
                            b1.setBackgroundResource(skin_dot);
                        }
                        b1.setClickable(false);
                    } else if (id == R.id.b2) {
                        final ImageButton b2 = (ImageButton) findViewById(R.id.b2);


                        if (count % 2 != 0) {
                            b2.setBackgroundResource(skin_cross);
                        } else if (count % 2 == 0) {
                            b2.setBackgroundResource(skin_dot);
                        }
                        b2.setClickable(false);
                    } else if (id == R.id.b3) {
                        final ImageButton b3 = (ImageButton) findViewById(R.id.b3);

                        if (count % 2 != 0) {
                            b3.setBackgroundResource(skin_cross);
                        } else if (count % 2 == 0) {
                            b3.setBackgroundResource(skin_dot);
                        }
                        b3.setClickable(false);
                    } else if (id == R.id.b4) {
                        final ImageButton b4 = (ImageButton) findViewById(R.id.b4);
                        if (count % 2 != 0) {
                            b4.setBackgroundResource(skin_cross);
                        } else if (count % 2 == 0) {
                            b4.setBackgroundResource(skin_dot);
                        }
                        b4.setClickable(false);
                    } else if (id == R.id.b5) {
                        final ImageButton b5 = (ImageButton) findViewById(R.id.b5);
                        if (count % 2 != 0) {
                            b5.setBackgroundResource(skin_cross);
                        } else if (count % 2 == 0) {
                            b5.setBackgroundResource(skin_dot);
                        }
                        b5.setClickable(false);
                    } else if (id == R.id.b6) {
                        final ImageButton b6 = (ImageButton) findViewById(R.id.b6);
                        if (count % 2 != 0) {
                            b6.setBackgroundResource(skin_cross);
                        } else if (count % 2 == 0) {
                            b6.setBackgroundResource(skin_dot);
                        }
                        b6.setClickable(false);
                    } else if (id == R.id.b7) {
                        final ImageButton b7 = (ImageButton) findViewById(R.id.b7);
                        if (count % 2 != 0) {
                            b7.setBackgroundResource(skin_cross);
                        } else if (count % 2 == 0) {
                            b7.setBackgroundResource(skin_dot);
                        }
                        b7.setClickable(false);
                    } else if (id == R.id.b8) {
                        final ImageButton b8 = (ImageButton) findViewById(R.id.b8);
                        if (count % 2 != 0) {
                            b8.setBackgroundResource(skin_cross);
                        } else if (count % 2 == 0) {
                            b8.setBackgroundResource(skin_dot);
                        }
                        b8.setClickable(false);
                    } else if (id == R.id.b9) {
                        final ImageButton b9 = (ImageButton) findViewById(R.id.b9);
                        if (count % 2 != 0) {
                            b9.setBackgroundResource(skin_cross);
                        } else if (count % 2 == 0) {
                            b9.setBackgroundResource(skin_dot);
                        }
                        b9.setClickable(false);
                    }
//                Toast.makeText(getApplicationContext(), id, Toast.LENGTH_SHORT).show();

                    break;
                case MESSAGE_DEVICE_NAME:
                    // save the connected device's name
                    mConnectedDeviceName = msg.getData().getString(DEVICE_NAME);
                    Toast.makeText(getApplicationContext(), "Connected to "
                            + mConnectedDeviceName, Toast.LENGTH_SHORT).show();
                    break;

                case MESSAGE_TOAST:
                    Toast.makeText(getApplicationContext(), msg.getData().getString(TOAST),
                            Toast.LENGTH_SHORT).show();
                    break;
            }
        }

        public void setBoard() {
            final ImageButton b3 = (ImageButton) findViewById(R.id.b3);
            final ImageButton b2 = (ImageButton) findViewById(R.id.b2);
            final ImageButton b1 = (ImageButton) findViewById(R.id.b1);

            final ImageButton b6 = (ImageButton) findViewById(R.id.b6);
            final ImageButton b5 = (ImageButton) findViewById(R.id.b5);
            final ImageButton b4 = (ImageButton) findViewById(R.id.b4);

            final ImageButton b9 = (ImageButton) findViewById(R.id.b9);
            final ImageButton b8 = (ImageButton) findViewById(R.id.b8);
            final ImageButton b7 = (ImageButton) findViewById(R.id.b7);

            // set the OnClickListeners.
            b1.setOnClickListener(button_listener);
            b2.setOnClickListener(button_listener);
            b3.setOnClickListener(button_listener);
            b4.setOnClickListener(button_listener);
            b5.setOnClickListener(button_listener);
            b6.setOnClickListener(button_listener);
            b7.setOnClickListener(button_listener);
            b8.setOnClickListener(button_listener);
            b9.setOnClickListener(button_listener);

            //setRes(button_listener);
            for (k = 0; k < 3; k++) {
                for (x = 0; x < 3; x++) {
                    m[k][x] = 0;
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_singleplayer);
        Log.i(TAG, "set content ttt");

        // Get local Bluetooth adapter
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        // If the adapter is null, then Bluetooth is not supported
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, "Bluetooth is not available", Toast.LENGTH_LONG).show();
            finish();
            return;
        }
        // ensureDiscoverable();
    }

    protected Dialog onCreateDialog(int id) {
        Dialog mDialog = new Dialog(this);
        switch (id) {
            case ID_PLAYER1_WIN:
                mDialog.setContentView(R.layout.activity_dialog_win);
                //	mDialog.setTitle(player_name_1 +" Won" );
                mDialog.setTitle("Player 1 Wins");
                mDialog.setCancelable(false);
                TextView tPlayer = (TextView) mDialog.findViewById(R.id.textWin);
                //	tPlayer.setText(player_name_1 + " Wins!!\nCongratulations");
                tPlayer.setText("You Won!\nCongratulations");
                //reset the game if the player 1 has won//
                resetGame();

                Button bcontinue = (Button) mDialog.findViewById(R.id.bContinue);
                bcontinue.setOnClickListener(new OnClickListener() {
                    @SuppressWarnings("deprecation")
                    @Override
                    public void onClick(View arg0) {
                        //	start_game(player_name_1, player_name_2);
                        //resetGame();
                        dismissDialog(ID_PLAYER1_WIN);
                    }
                });
                break;


            case ID_PLAYER2_WIN:
                mDialog.setContentView(R.layout.activity_dialog_win);
                mDialog.setTitle("Player 2 Wins");
                mDialog.setCancelable(false);
                TextView tPlayer1 = (TextView) mDialog.findViewById(R.id.textWin);
                tPlayer1.setText("You Won!\nCongratulations");


                //reset the game if the player 2 has won//
                resetGame();

                Button bcontinue1 = (Button) mDialog.findViewById(R.id.bContinue);

                bcontinue1.setOnClickListener(new OnClickListener() {
                    @SuppressWarnings("deprecation")
                    @Override
                    public void onClick(View arg0) {
                        //	start_game(player_name_1, player_name_2);
                        dismissDialog(ID_PLAYER2_WIN);

                    }
                });

                break;


            case ID_GAME_DRAW:
                mDialog.setContentView(R.layout.activity_dialog_win);
                mDialog.setTitle("Match  Drawn");
                mDialog.setCancelable(false);
                TextView tPlayer2 = (TextView) mDialog.findViewById(R.id.textWin);
                tPlayer2.setText("Match is Darwn\nHard Luck");


                //reset the game if its a draw//

                resetGame();

                Button bcontinue2 = (Button) mDialog.findViewById(R.id.bContinue);

                bcontinue2.setOnClickListener(new OnClickListener() {
                    @SuppressWarnings("deprecation")
                    @Override
                    public void onClick(View arg0) {
                        //	start_game(player_name_1, player_name_2);
                        dismissDialog(ID_GAME_DRAW);
                    }
                });

                break;
        }
        return mDialog;
    }

    protected void resetGame() {
        final ImageButton b3 = (ImageButton) findViewById(R.id.b3);
        final ImageButton b2 = (ImageButton) findViewById(R.id.b2);
        final ImageButton b1 = (ImageButton) findViewById(R.id.b1);

        final ImageButton b6 = (ImageButton) findViewById(R.id.b6);
        final ImageButton b5 = (ImageButton) findViewById(R.id.b5);
        final ImageButton b4 = (ImageButton) findViewById(R.id.b4);

        final ImageButton b9 = (ImageButton) findViewById(R.id.b9);
        final ImageButton b8 = (ImageButton) findViewById(R.id.b8);
        final ImageButton b7 = (ImageButton) findViewById(R.id.b7);

        // set the OnClickListeners.
        b1.setOnClickListener(button_listener);
        b2.setOnClickListener(button_listener);
        b3.setOnClickListener(button_listener);
        b4.setOnClickListener(button_listener);
        b5.setOnClickListener(button_listener);
        b6.setOnClickListener(button_listener);
        b7.setOnClickListener(button_listener);
        b8.setOnClickListener(button_listener);
        b9.setOnClickListener(button_listener);

        b1.setBackgroundResource(R.drawable.default_my);

        b2.setBackgroundResource(R.drawable.default_my);

        b3.setBackgroundResource(R.drawable.default_my);

        //    b1.setBackground("@android:drawable/btn_default_holo_light");
        //reset
        for (k = 0; k < 3; k++) {
            for (x = 0; x < 3; x++) {
                m[k][x] = 0;
            }
        }
        //make count to zero//
        count = 0;
        Toast.makeText(getApplicationContext(), "count is  reset to zero", Toast.LENGTH_SHORT).show();


    }

    @Override
    public void onStart() {
        super.onStart();
        if (D) Log.e(TAG, "++ ON START ++");

        // If BT is not on, request that it be enabled.
        // setupChat() will then be called during onActivityResult
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
            // Otherwise, setup the chat session
        } else {
            if (mticTacToeService == null) setupChat();
        }
    }

    private void setupChat() {
        //Log.d(TAG, "setupChat()");
        /////////////////////////////////////////
        ///testing

        // Initialize the compose field with a listener for the return key
        mOutEditText = (EditText) findViewById(R.id.edit_text_out);
        //  mOutEditText.setOnEditorActionListener(mWriteListener);
        mOutEditText.setVisibility(View.GONE);
        // Initialize the send button with a listener that for click events
        mSendButton = (Button) findViewById(R.id.button_send);

        mSendButton.setVisibility(View.GONE);

        mSendButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                //void code//
            }
        });

        //
        /////////////////////////////////////////


        Button BLeaveGame = (Button) findViewById(R.id.bLeaveGame);
        //****Exit Button In Game.
        BLeaveGame.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                AlertDialog.Builder builder = new AlertDialog.Builder(BluetoothTicTacToe.this);
                builder.setMessage("Are you Sure, You Wanna Exit?");
                builder.setCancelable(false);
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        if (mticTacToeService != null) mticTacToeService.stop();
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
        });


        // Initialize the BluetoothTicTacToeService to perform bluetooth connections
        mticTacToeService = new BlueToothTicTacToeService(this, mHandler);


        /////////////////////added later//
        mticTacToeService.startt();


        // Initialize the buffer for outgoing messages
        mOutStringBuffer = new StringBuffer("");
    }

    //**function to set 1= x player1 , 2= O player2 && winning condition//

    protected int setMatrix(int id, int i) {

        if (id == R.id.b1) {
            m[0][0] = i;
        } else if (id == R.id.b2) {
            m[0][1] = i;
        } else if (id == R.id.b3) {
            m[0][2] = i;
        } else if (id == R.id.b4) {
            m[1][0] = i;
        } else if (id == R.id.b5) {
            m[1][1] = i;
        } else if (id == R.id.b6) {
            m[1][2] = i;
        } else if (id == R.id.b7) {
            m[2][0] = i;
        } else if (id == R.id.b8) {
            m[2][1] = i;
        } else if (id == R.id.b9) {
            m[2][2] = i;
            //Log.i("sandy", m[2][2].toString()+ "valueeee");
        }

        if (count >= 5) //for player Win: //
        {
            //**** for Rows checking//

            for (j = 0; j < 3; j++) {
                if (m[j][0] == i && m[j][1] == i && m[j][2] == i) {
                    Log.i("truef", "true");

                    return i;
                }
            }
            //****for column checking//
            for (j = 0; j < 3; j++) {
                if (m[0][j] == i && m[1][j] == i && m[2][j] == i)

                {
                    return i;
                }
            }
            //****for diagonal 1
            if (m[0][0] == i && m[1][1] == i && m[2][2] == i)
                return i;
            //****for diagonal 2
            if (m[0][2] == i && m[1][1] == i && m[2][0] == i)
                return i;
        }
        //else ret 0 it wont affect as other condition ==9 fails//
        return 0;

    }

    @Override
    public synchronized void onPause() {
        super.onPause();
        if (D) Log.e(TAG, "- ON PAUSE -");
    }

    @Override
    public void onStop() {
        super.onStop();
        if (D) Log.e(TAG, "-- ON STOP --");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Stop the Bluetooth chat services
        if (mticTacToeService != null) mticTacToeService.stop();
        if (D) Log.e(TAG, "--- ON DESTROY ---");
    }

    private void ensureDiscoverable() {
        if (D) Log.d(TAG, "ensure discoverable");

        if (mBluetoothAdapter.getScanMode() !=
                BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
            Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
            Toast.makeText(getApplicationContext(), "300 sec discovrable", Toast.LENGTH_SHORT).show();

            startActivity(discoverableIntent);
        }

    }


    /**
     * Sends a message.
     *
     * @param message A string of text to send.
     */
    private void sendMessage(String message) {
        // Check that we're actually connected before trying anything
        if (mticTacToeService.getState() != BlueToothTicTacToeService.STATE_CONNECTED) {
            Toast.makeText(this, "not_connecteddd", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check that there's actually something to send
        if (message.length() > 0) {
            // Get the message bytes and tell the BluetoothChatService to write
            byte[] send = message.getBytes();
            mticTacToeService.write(send);

            // Reset out string buffer to zero and clear the edit text field
            mOutStringBuffer.setLength(0);
            mOutEditText.setText(mOutStringBuffer);

        }
    }

    @Override
    public synchronized void onResume() {
        super.onResume();
        if (D) Log.e(TAG, "+ ON RESUME +");
        //for resume acti//
        if (mticTacToeService != null) {
            // Only if the state is STATE_NONE, do we know that we haven't started already
            if (mticTacToeService.getState() == BlueToothTicTacToeService.STATE_NONE) {
                // Start the Bluetooth chat services
                mticTacToeService.startt();

            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CONNECT_DEVICE:
                // When DeviceListActivity returns with a device to connect
                if (resultCode == Activity.RESULT_OK) {
                    // Get the device MAC address
                    String address = data.getExtras()
                            .getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
                    // Get the BLuetoothDevice object
                    BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
                    // Attempt to connect to the device

                    mticTacToeService.connect(device);
                }
                break;
            case REQUEST_ENABLE_BT:
                // When the request to enable Bluetooth returns
                if (resultCode == Activity.RESULT_OK) {
                    // Bluetooth is now enabled, so set up a chat session
                    setupChat();
                } else {
                    // User did not enable Bluetooth or an error occured
                    Log.d(TAG, "BT not enabled");
                    Toast.makeText(this, "bluetooth was not enabled, leaving", Toast.LENGTH_SHORT).show();
                    finish();
                }
        }


    }//ends

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.start:
                Toast.makeText(getApplicationContext(), "startttt", Toast.LENGTH_SHORT).show();

                resetGame();

                return true;
            case R.id.scan:
                // Launch the DeviceListActivity to see devices and do scan
                Intent serverIntent = new Intent(this, DeviceListActivity.class);
                startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);
                return true;

            case R.id.discoverable:
                // Ensure this device is discoverable by others
                ensureDiscoverable();
                return true;
        }
        return false;


    }
}
