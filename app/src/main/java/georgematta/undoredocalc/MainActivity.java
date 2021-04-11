package georgematta.undoredocalc;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    String operator;
    String secondOperandText;
    double result;
    int changeCounter = 1;

    EditText secondOperandEditText;
    TextView resultTextView;

    Button undoButton;
    Button redoButton;
    Button eqButton;

    int[] gridButtons;

    public void eqClick(View view){

        double secondOperand = Double.parseDouble(secondOperandText);

        switch (operator){
            case "+":
                result += secondOperand;
                break;
            case "-":
                result -= secondOperand;
                break;
            case "*":
                result *= secondOperand;
                break;
            case "/":
                result /= secondOperand;
                break;
        }

        resultTextView.setText("Result = " + Double.toString(result));

        addToGrid();

        clearVars();

        Log.i("Equal Clicked - New Result", String.valueOf(result));
    }

    public void addToGrid(){
        Button gridButton = findFirstOpenButton();
        gridButton.setText(operator + secondOperandText);
        fixTextSize(gridButton);
        gridButton.setVisibility(View.VISIBLE);
    }

    // Numbers that are too big occasionally break the grid view, we reduce the text size of larger numbers linearly (y=mx+b / y = -2.2x + 27)
    public void fixTextSize(Button button){
        int maxSize = 27;
        float sizeModifier = -2.2f;
        String text = (String) button.getText();
        int textLength = text.length();
        float newSize = Math.max((float) ((textLength * sizeModifier) + maxSize), 1);
        button.setTextSize(newSize);
        Log.i("New Text Size", "Button" + button.getId() + " size " + newSize);
    }

    // Returns the ID of the first available button
    public Button findFirstOpenButton(){
        for(int id : gridButtons){
            Button gridButton = (Button) findViewById(id);
            // Return first invisible button
            if(gridButton.getVisibility() == View.INVISIBLE){
                return gridButton;
            }
        }
        // If none are invisible, return the oldest visible button
        for(int id : gridButtons){
            Button gridButton = (Button) findViewById(id);
            if (Integer.parseInt(gridButton.getTag().toString()) < changeCounter){
                gridButton.setTag(changeCounter);
                Log.i("New Tag Set", "Button" + gridButton.getId() + " tag " + changeCounter);
                return gridButton;
            }
        }
        // All the buttons have been changed changeCounter amount of times, increase the change counter and re-find a button
        changeCounter++;
        Log.i("New Change Counter", String.valueOf(changeCounter));
        return findFirstOpenButton();
    }

    // Clear the vars after equal button is pressed
    public void clearVars(){
        secondOperandEditText.setText("");
        operator = null;
    }

    // Set the new operator whenever an operator is clicked
    public void operClick(View view){

        String operatorChosen = ((Button) view).getText().toString();

        this.operator = operatorChosen;

        checkEqualButton();

        Log.i("New Operator Chosen", this.operator);

    }

    // This method checks if equal button should be enabled
    public void checkEqualButton(){

        boolean status = true;

        if (operator == null){ // Disable if no operator has been chosen
            status = false;
        }
        if(secondOperandText == null){ // To prevent an error for checking length if there is no text
            secondOperandText = "";
        }
        if (secondOperandText.length() == 0) { // Disable if the second operand field is empty
            status = false;
        }

        eqButton.setClickable(status);
        Log.i("Equal Button Status", String.valueOf(status));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize an array of int IDs for the buttons in the grid view
        gridButtons = new int[]{R.id.grid0, R.id.grid1, R.id.grid2, R.id.grid3, R.id.grid4, R.id.grid5, R.id.grid6, R.id.grid7, R.id.grid8, R.id.grid9, R.id.grid10, R.id.grid11, R.id.grid12, R.id.grid13, R.id.grid14, R.id.grid15, R.id.grid16, R.id.grid17, R.id.grid18, R.id.grid19, R.id.grid20, R.id.grid21, R.id.grid22, R.id.grid23};

        // Set our button and the EditText variables
        secondOperandEditText = (EditText) findViewById(R.id.secondOperandEdit);
        undoButton = (Button) findViewById(R.id.undoButton);
        redoButton = (Button) findViewById(R.id.redoButton);
        eqButton = (Button) findViewById(R.id.eqButton);

        // Initialize the result text
        resultTextView = (TextView) findViewById(R.id.resultTextView);
        resultTextView.setText(resultTextView.getText() + Double.toString(result));

        // Initially disable Undo, Redo, and Equals buttons
        eqButton.setClickable(false);
        undoButton.setClickable(false);
        redoButton.setClickable(false);

        // Create the EditText listener
        secondOperandEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                secondOperandText = s.toString();
                checkEqualButton();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                secondOperandText = s.toString();
                checkEqualButton();
            }

            @Override
            public void afterTextChanged(Editable s) {
                secondOperandText = s.toString();
                checkEqualButton();
            }
        });
    }
}