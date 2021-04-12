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

    // Values
    String operator;
    String secondOperandText;
    double result;

    // User Input and Output
    EditText secondOperandEditText;
    TextView resultTextView;

    // Buttons
    Button undoButton;
    Button redoButton;
    Button eqButton;

    // Grid Buttons
    int[] gridButtons;
    int changeCounter = 1;

    // Undo/Redo
    int actionIndex;

    public void gridButtonClick(View view){
        String applied = applyOperation(view, true);

        resultTextView.setText("Result = " + Double.toString(result));

        moveFutureButtonsBack(view);

        addToGrid(applied.substring(0, 1), applied.substring(1));

        Log.i("Grid button Clicked - New Result", String.valueOf(result));
    }

    public void moveFutureButtonsBack(View view){
        int index = findGridIndex(view);
        recursiveRewrite(index, index);
        setEmptyButtonInvis();
    }

    public void setEmptyButtonInvis(){
        for(int i = 0; i < gridButtons.length; i++){
            Button currButton = (Button) findViewById(gridButtons[i]);
            if (currButton.getText() == "" && currButton.getVisibility() == View.VISIBLE){
                currButton.setVisibility(View.INVISIBLE);
            }
        }
    }

    public int findGridIndex(View view){
        Button pressedButton = (Button) view;
        // Find the pressed button in the array
        int index = 0;
        while(index < gridButtons.length){
            Log.i("Finding Grid Index", String.valueOf(index));
            if(gridButtons[index] != pressedButton.getId()){
                index++;
            }
            return index;
        }

        return -1;
    }

    public void recursiveRewrite(int buttonIndex, int currIndex){
        Log.i("Recursive Rewrite", buttonIndex + " " + currIndex);
        if(buttonIndex == currIndex-22){ // Checks if we have reached the button before the one we're rewriting to
            return; // Stop rewriting
        }
        if(((Button) findViewById(gridButtons[currIndex])).getVisibility() == View.INVISIBLE) { // If the current button is invisible
            return;
        }
        Button currButton = (Button) findViewById(gridButtons[currIndex]);
        Button nextButton = (Button) findViewById(gridButtons[currIndex+1]);

        currButton.setText(nextButton.getText());
        currButton.setTag(nextButton.getTag());

        recursiveRewrite(buttonIndex, currIndex+1);
    }

    public String applyOperation(View view, boolean opposite){
        String applied = "";
        // Apply the chosen operation on the provided second operand
        if (!opposite) {
            double secondOperand = Double.parseDouble(secondOperandText);
            switch (operator) {
                case "+":
                    result += secondOperand;
                    applied = "+" +  (int) secondOperand;
                    break;
                case "-":
                    result -= secondOperand;
                    applied = "-" +  (int) secondOperand;
                    break;
                case "*":
                    result *= secondOperand;
                    applied = "*" +  (int) secondOperand;
                    break;
                case "/":
                    result /= secondOperand;
                    applied = "/" +  (int) secondOperand;
                    break;
            }
        }

        // If we're removing something from the GridView
        else{
            Button gridButton = (Button) view;
            String buttonText = gridButton.getText().toString();
            String buttonOperator = buttonText.substring(0, 1);
            double buttonNum = Double.parseDouble(buttonText.substring(1));
            switch (buttonOperator) {
                case "+":
                    result -= buttonNum;
                    applied = "-" + (int) buttonNum;
                    break;
                case "-":
                    result += buttonNum;
                    applied = "+" +  (int) buttonNum;
                    break;
                case "*":
                    result /= buttonNum;
                    applied = "/" +  (int) buttonNum;
                    break;
                case "/":
                    result *= buttonNum;
                    applied = "*" +  (int) buttonNum;
                    break;
            }
        }

        return applied;
    }

    public void eqClick(View view){

        applyOperation(view,false);

        resultTextView.setText("Result = " + Double.toString(result));

        addToGrid(operator, secondOperandText);

        clearVars();

        Log.i("Equal Clicked - New Result", String.valueOf(result));
    }

    public void addToGrid(String operator, String secondOperandText){
        Button gridButton = findFirstOpenButton();
        gridButton.setText(operator + secondOperandText);
        fixTextSize(gridButton);
        gridButton.setVisibility(View.VISIBLE);
        actionIndex = buttonIndex(gridButton);
        checkUndoRedo();
        Log.i("Action Index Changed (Grid)", String.valueOf(actionIndex));
    }

    // Returns the index of a grid button
    public int buttonIndex(Button gridButton){
        for(int i = 0; i < gridButtons.length; i++){
            if(gridButtons[i] == gridButton.getId()){
                return i+1;
            }
        }
        return -1;
    }

    public void actionIndexChange(boolean increase){
        if (increase) {
            actionIndex = Math.min(actionIndex+1, 23);
        } else{
            actionIndex = Math.max(actionIndex-1, 0);
        }
        Log.i("Action Index Changed", String.valueOf(actionIndex));
        checkUndoRedo();
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

    public void checkUndoRedo(){
        if (((Button) findViewById(gridButtons[getActionIndex("before")])).getVisibility() == View.VISIBLE){
            undoButton.setClickable(true);
            Log.i("Undo Button Clickable", "Enabled");
        } else{
            undoButton.setClickable(false);
            Log.i("Undo Button Clickable", "Disabled");
        }
        if (((Button) findViewById(gridButtons[getActionIndex("next")])).getVisibility() == View.VISIBLE){
            redoButton.setClickable(true);
            Log.i("Redo Button Clickable", "Enabled");

        } else {
            redoButton.setClickable(false);
            Log.i("Redo Button Clickable", "Disabled");
        }
    }

    public void undoClick(View view){
        actionIndexChange(false);
    }

    public void redoClick(View view){
        actionIndexChange(true);
    }

    public int getActionIndex(String identifier){
        if(identifier.equals("before")){
            if(actionIndex == 0){
                return 23; // Last button
            }
            return actionIndex-1;
        }
        else{
            if (actionIndex == 24){
                return 0;
            }
            return actionIndex;
        }
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
        actionIndex = 0;

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