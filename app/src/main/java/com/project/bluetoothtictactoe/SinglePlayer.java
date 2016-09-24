package com.project.bluetoothtictactoe;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("CutPasteId")
public class SinglePlayer extends Activity {
    private static final int ID_OK_DIALOG = 10;
    private static final int ID_PLAYER1_WIN = 12;
    private static final int ID_PLAYER2_WIN = 13;
    private static final int ID_GAME_DRAW = 14;
    public int flag_win = 0;
    protected CharSequence player_name_2 = "player 2";
    protected CharSequence player_name_1 = "player 1";
    protected int returnCode;
    // score initialized to 0.
    Integer score_player_1 = 0;
    Integer score_player_2 = 0;
    Integer count_draws = 0;
    int count = 0;
    int k = 0;
    int j;
    int x = 0;
    int skin_cross = R.drawable.default_x;
    int skin_dot = R.drawable.default_o;
    Integer[][] m = new Integer[3][3];
    OnClickListener button_listener = new View.OnClickListener() {
        @SuppressWarnings("deprecation")
        public void onClick(View v) {
            ImageButton ibutton = (ImageButton) v;

            // Button inactive for further clicks until a result is obtained.
            ibutton.setClickable(false);

            // Increment Count on clicking the button.
            count += 1;
            if (count % 2 != 0) {
                //ibutton.setImageResource(skin_cross);
                ibutton.setBackgroundResource(skin_cross);
                returnCode = setMatrix(ibutton.getId(), 1, count);
            } else if (count % 2 == 0) {
                ibutton.setBackgroundResource(skin_dot);
                returnCode = setMatrix(ibutton.getId(), 2, count);
            }
            //****check if return was a win for player 1, 2 or a draw?//
            if (returnCode == 1) {
                score_player_1 = score_player_1 + 1;
                CharSequence tempVar = score_player_1.toString();

                TextView p1wincount = (TextView) findViewById(R.id.textView4);
                p1wincount.setText(tempVar);
                showDialog(ID_PLAYER1_WIN);
                Log.i("truef", "returned 1 for p1 win");

            } else if (returnCode == 2) {
                score_player_2 += 1;
                CharSequence tempVar1 = score_player_2.toString();
                TextView p2wincount = (TextView) findViewById(R.id.textView5);
                p2wincount.setText(tempVar1);

                showDialog(ID_PLAYER2_WIN);
            } else if (returnCode == 0 && count == 9) {
                count_draws += 1;
                CharSequence tempvar2 = count_draws.toString();
                TextView drawCount = (TextView) findViewById(R.id.textView6);
                drawCount.setText(tempvar2);
                showDialog(ID_GAME_DRAW);
            }

        }
    };
    private EditText mtext;
    private Button mbutt;

    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_singleplayer);
        // Initialize the compose field with a listener for the return key
        mtext = (EditText) findViewById(R.id.edit_text_out);
        //  mOutEditText.setOnEditorActionListener(mWriteListener);
        mtext.setVisibility(View.GONE);
        // Initialize the send button with a listener that for click events
        mbutt = (Button) findViewById(R.id.button_send);

        mbutt.setVisibility(View.GONE);

        mbutt.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                //void code//
            }
        });


        showDialog(ID_OK_DIALOG);
    }

    //**creates Dialog for names of players//
    @SuppressLint("CutPasteId")
    protected Dialog onCreateDialog(int id) {
        Dialog mDialog = new Dialog(this);
        switch (id) {
            case ID_OK_DIALOG:
                mDialog.setContentView(R.layout.activity_dialog);
                mDialog.setTitle("Player Names");
                mDialog.setCancelable(false);
                final EditText namep1 = (EditText) mDialog.findViewById(R.id.player_1_name);
                final EditText namep2 = (EditText) mDialog.findViewById(R.id.player_2_name);
                //mDialog.show();
                score_player_1 = 0;
                score_player_2 = 0;
                count_draws = 0;

                CharSequence tempVar = score_player_1.toString();
                TextView p1wincount = (TextView) findViewById(R.id.textView4);
                p1wincount.setText(tempVar);

                CharSequence tempVar1 = score_player_2.toString();
                TextView p2wincount = (TextView) findViewById(R.id.textView5);
                p2wincount.setText(tempVar1);

                CharSequence tempVar2 = count_draws.toString();
                TextView drawCount = (TextView) findViewById(R.id.textView6);
                drawCount.setText(tempVar2);

                Button ok_b = (Button) mDialog.findViewById(R.id.bDialogOk);
                ok_b.setOnClickListener(new OnClickListener() {
                    @SuppressWarnings("deprecation")
                    @Override
                    public void onClick(View arg0) {
                        player_name_1 = namep1.getText();
                        player_name_2 = namep2.getText();
                        start_game(player_name_1, player_name_2);
                        dismissDialog(ID_OK_DIALOG);
                    }

                });
                break;
            case ID_PLAYER1_WIN:
                mDialog.setContentView(R.layout.activity_dialog_win);
                mDialog.setTitle(player_name_1 + " Wins");
                mDialog.setCancelable(false);
                TextView tPlayer = (TextView) mDialog.findViewById(R.id.textWin);
                tPlayer.setText(player_name_1 + " Wins!!\nCongratulations");
                Log.i("dialoggg", "success");
                Button bcontinue = (Button) mDialog.findViewById(R.id.bContinue);
                bcontinue.setOnClickListener(new OnClickListener() {
                    @SuppressWarnings("deprecation")
                    @Override
                    public void onClick(View arg0) {
                        start_game(player_name_1, player_name_2);
                        dismissDialog(ID_PLAYER1_WIN);
                    }
                });
                break;
            case ID_PLAYER2_WIN:
                mDialog.setContentView(R.layout.activity_dialog_win);
                mDialog.setTitle(player_name_2 + " wins");
                mDialog.setCancelable(false);
                TextView tPlayer1 = (TextView) mDialog.findViewById(R.id.textWin);
                tPlayer1.setText(player_name_2 + " Wins!!\nCongratulations");
                Button bcontinue1 = (Button) mDialog.findViewById(R.id.bContinue);

                bcontinue1.setOnClickListener(new OnClickListener() {
                    @SuppressWarnings("deprecation")
                    @Override
                    public void onClick(View arg0) {
                        start_game(player_name_1, player_name_2);
                        dismissDialog(ID_PLAYER2_WIN);

                    }
                });

                break;
            case ID_GAME_DRAW:
                mDialog.setContentView(R.layout.activity_dialog_win);
                mDialog.setTitle("Match  Drawn");
                mDialog.setCancelable(false);
                TextView tPlayer2 = (TextView) mDialog.findViewById(R.id.textWin);
                tPlayer2.setText("Match darwn");
                Button bcontinue2 = (Button) mDialog.findViewById(R.id.bContinue);

                bcontinue2.setOnClickListener(new OnClickListener() {
                    @SuppressWarnings("deprecation")
                    @Override
                    public void onClick(View arg0) {
                        start_game(player_name_1, player_name_2);
                        dismissDialog(ID_GAME_DRAW);
                    }
                });

                break;
        }
        return mDialog;
    }

    //** start the single player game//
    protected void start_game(CharSequence player_name_1, CharSequence player_name_2) {
        setContentView(R.layout.activity_singleplayer);
        TextView t1 = (TextView) findViewById(R.id.textView1);
        TextView t2 = (TextView) findViewById(R.id.textView3);
        t1.setText(player_name_1);
        t2.setText(player_name_2);

        CharSequence tempVar = score_player_1.toString();
        TextView p1wincount = (TextView) findViewById(R.id.textView4);
        p1wincount.setText(tempVar);

        CharSequence tempVar1 = score_player_2.toString();
        TextView p2wincount = (TextView) findViewById(R.id.textView5);
        p2wincount.setText(tempVar1);

        CharSequence tempVar2 = count_draws.toString();
        TextView drawCount = (TextView) findViewById(R.id.textView6);
        drawCount.setText(tempVar2);

        count = 0;

        Button BLeaveGame = (Button) findViewById(R.id.bLeaveGame);

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

        //****Exit Button In Game.
        BLeaveGame.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SinglePlayer.this);
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
        });
        // Re-enable the Click-able property of buttons.
        b1.setClickable(true);
        b2.setClickable(true);
        b3.setClickable(true);
        b4.setClickable(true);
        b5.setClickable(true);
        b6.setClickable(true);
        b7.setClickable(true);
        b8.setClickable(true);
        b9.setClickable(true);
        //init if matrix
        for (k = 0; k < 3; k++) {
            for (x = 0; x < 3; x++) {
                m[k][x] = 0;
            }
        }
    }

//**function to set 1= x player1 , 2= O player2 && winning condition//

    protected int setMatrix(int id, int i, int count2) {

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
            Log.i("sandy", m[2][2].toString() + "valueeee");
        }

        if (count2 >= 5) //for player Win: //
        {
            //**** for Rows checking//

            for (j = 0; j < 3; j++) {
                if (m[j][0] == i && m[j][1] == i && m[j][2] == i) {
                    Log.i("truef", "true");
                    Toast.makeText(getApplicationContext(), "kkjjsadasdasdas " + count2, Toast.LENGTH_SHORT).show();

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
        return 0;


    }
}







