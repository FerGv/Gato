package fer.gv.com.gato;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    EditText box1, box2, box3, box4, box5, box6, box7, box8, box9;
    EditText[] arrayBoxes;
    Button btnRecord, btnRepeat;
    int sizeArrayBoxes = 0, counterTurn = 0;
    boolean locked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeComponents();
    }

    public void initializeComponents() {
        this.box1 = (EditText) findViewById(R.id.activity_main_box1);
        this.box2 = (EditText) findViewById(R.id.activity_main_box2);
        this.box3 = (EditText) findViewById(R.id.activity_main_box3);
        this.box4 = (EditText) findViewById(R.id.activity_main_box4);
        this.box5 = (EditText) findViewById(R.id.activity_main_box5);
        this.box6 = (EditText) findViewById(R.id.activity_main_box6);
        this.box7 = (EditText) findViewById(R.id.activity_main_box7);
        this.box8 = (EditText) findViewById(R.id.activity_main_box8);
        this.box9 = (EditText) findViewById(R.id.activity_main_box9);
        this.btnRecord = (Button) findViewById(R.id.activity_main_btnRecord);
        this.btnRepeat = (Button) findViewById(R.id.activity_main_btnNewGame);

        this.arrayBoxes = new EditText[]{this.box1, this.box2, this.box3, this.box4, this.box5, this.box6, this.box7, this.box8, this.box9};
        this.sizeArrayBoxes = this.arrayBoxes.length;

        for (int i = 0; i < this.sizeArrayBoxes; i++) {
            arrayBoxes[i].setOnTouchListener(
                    new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            if (locked == false) {
                                fillBox(v);
                                validateBoxes();
                            }
                            return true;
                        }
                    }
            );
        }
    }

    public void fillBox(View sender) {
        EditText txt = (EditText) sender;
        if (txt.getText().toString().equals("")) {
            if (this.counterTurn % 2 == 0) {
                txt.setText("X");
            } else {
                txt.setText("O");
            }
            this.counterTurn++;
        }
    }

    public void validateBoxes() {
        int[] filledBoxes = new int[9];
        int sizeFilledBoxes = 0;

        for (int i = 0; i < this.sizeArrayBoxes; i++) {
            if (!(this.arrayBoxes[i].getText().toString().trim().equals(""))) {
                filledBoxes[i] = i + 1;
                sizeFilledBoxes++;
            }
        }

        if (validateOne(filledBoxes) || validateFive(filledBoxes) || validateNine(filledBoxes)) {
            SharedPreferences prefs = this.getSharedPreferences("prefs", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();

            if (this.counterTurn % 2 != 0) {
                Toast.makeText(this, "Gana X", Toast.LENGTH_SHORT).show();
                int counterX = prefs.getInt("counterX", -1);
                if (counterX != -1) {
                    editor.putInt("counterX", ++counterX);
                    editor.apply();
                } else {
                    editor.putInt("counterX", 1);
                    editor.apply();
                }
            } else {
                Toast.makeText(this, "Gana O", Toast.LENGTH_SHORT).show();
                int counterO = prefs.getInt("counterO", -1);
                if (counterO != -1) {
                    editor.putInt("counterO", ++counterO);
                    editor.apply();
                } else {
                    editor.putInt("counterO", 1);
                    editor.apply();
                }
            }
            this.locked = true;
        } else if (sizeFilledBoxes == 9){
            Toast.makeText(this, "Gato", Toast.LENGTH_SHORT).show();
        }
    }

    public void btnRecord_click(View sender) {
        SharedPreferences prefs = this.getSharedPreferences("prefs", Context.MODE_PRIVATE);
        int counterX = prefs.getInt("counterX", 0);
        int counterO = prefs.getInt("counterO", 0);
        String record = "X: " + counterX + " ganada(s)   |   O: " + counterO + " ganada(s)";
        Toast.makeText(this, record, Toast.LENGTH_LONG).show();
    }

    public void btnNewGame_click(View sender) {
        for (int i = 0; i < this.sizeArrayBoxes; i++) {
            this.arrayBoxes[i].setText("");
            this.arrayBoxes[i].setBackgroundResource(R.drawable.border);
        }
        this.counterTurn = 0;
        this.locked = false;
        Toast.makeText(this, "Inicia X", Toast.LENGTH_SHORT).show();
    }

    public void btnDeleteRecord_click(View sender) {
        SharedPreferences prefs = this.getSharedPreferences("prefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putInt("counterX", 0);
        editor.putInt("counterO", 0);
        editor.apply();

        Toast.makeText(this, "Historial Borrado", Toast.LENGTH_SHORT).show();
    }

    static boolean searchNumber(int[] filledBoxes, int number) {
        for (int i = 0; i < filledBoxes.length; i++) {
            if (filledBoxes[i] == number) {
                return true;
            }
        }
        return false;
    }

    public boolean validateOne(int[] filledBoxes) {
        if (searchNumber(filledBoxes, 1)) {
            if (box1.getText().toString().trim().equals(box2.getText().toString().trim()) && box1.getText().toString().trim().equals(box3.getText().toString().trim())) {
                changeBackground(this.box1, this.box2, this.box3);
                return true;
            } else if (box1.getText().toString().trim().equals(box4.getText().toString().trim()) && box1.getText().toString().trim().equals(box7.getText().toString().trim())) {
                changeBackground(this.box1, this.box4, this.box7);
                return true;
            } else if (box1.getText().toString().trim().equals(box5.getText().toString().trim()) && box1.getText().toString().trim().equals(box9.getText().toString().trim())) {
                changeBackground(this.box1, this.box5, this.box9);
                return true;
            }
        }
        return false;
    }

    public boolean validateFive(int[] filledBoxes) {
        if (searchNumber(filledBoxes, 5)) {
            if (box5.getText().toString().trim().equals(box2.getText().toString().trim()) && box5.getText().toString().trim().equals(box8.getText().toString().trim())) {
                changeBackground(this.box5, this.box2, this.box8);
                return true;
            } else if (box5.getText().toString().trim().equals(box4.getText().toString().trim()) && box5.getText().toString().trim().equals(box6.getText().toString().trim())) {
                changeBackground(this.box5, this.box4, this.box6);
                return true;
            } else if (box5.getText().toString().trim().equals(box3.getText().toString().trim()) && box5.getText().toString().trim().equals(box7.getText().toString().trim())) {
                changeBackground(this.box5, this.box3, this.box7);
                return true;
            }
        }
        return false;
    }

    public boolean validateNine(int[] filledBoxes) {
        if (searchNumber(filledBoxes, 9)) {
            if (box9.getText().toString().trim().equals(box3.getText().toString().trim()) && box9.getText().toString().trim().equals(box6.getText().toString().trim())) {
                changeBackground(this.box9, this.box3, this.box6);
                return true;
            } else if (box9.getText().toString().trim().equals(box7.getText().toString().trim()) && box9.getText().toString().trim().equals(box8.getText().toString().trim())) {
                changeBackground(this.box9, this.box7, this.box8);
                return true;
            }
        }
        return false;
    }

    public void changeBackground(EditText txt1, EditText txt2, EditText txt3) {
        txt1.setBackgroundResource(R.drawable.border_win);
        txt2.setBackgroundResource(R.drawable.border_win);
        txt3.setBackgroundResource(R.drawable.border_win);
    }
}

