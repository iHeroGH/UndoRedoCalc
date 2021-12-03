package georgematta.undoredocalc;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Calculator calc;
    private HistoryManager historyManager;

    // Buttons
    private Button eqButton;
    private Button undoButton;
    private Button redoButton;

    // Grid
    int[] gridButtonIDs;
    Button[] gridButtons;
    int amtOfButtons;

    // User I/O
    private EditText operandEditText;
    private TextView resultTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Create the Calculator we will be using
        calc = new Calculator();
        // Create the HistoryManager
        historyManager = new HistoryManager();

        // Initialize our I/O fields
        initOperandInput();
        initResultOutput();

        // Initialize our buttons
        initButtons();

        // Initialize our grid
        initGrid();
    }

    // INITIALIZER
    // Find the Operand Input text box
    private void initOperandInput(){
        operandEditText = (EditText) this.findViewById(R.id.operandEdit);
        // Create the EditText listener
        operandEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                updateButtons();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                updateButtons();
            }

            @Override
            public void afterTextChanged(Editable s) {
                updateButtons();
            }
        });
    }
    // Find the Result text and set it to display 0
    private void initResultOutput(){
        resultTextView = (TextView) this.findViewById(R.id.resultTextView);
        setResultText(0);
    }
    // Find all our buttons and disable them
    private void initButtons(){
        eqButton = (Button) this.findViewById(R.id.eqButton);
        undoButton = (Button) this.findViewById(R.id.undoButton);
        redoButton = (Button) this.findViewById(R.id.redoButton);

        disableButtons(eqButton, undoButton, redoButton);
    }
    // Find all the grid buttons
    public void initGrid(){
        // Initialize an array of int IDs for the buttons in the grid view
        gridButtonIDs = new int[]{
                R.id.grid0, R.id.grid1, R.id.grid2, R.id.grid3,
                R.id.grid4, R.id.grid5, R.id.grid6, R.id.grid7,
                R.id.grid8, R.id.grid9, R.id.grid10, R.id.grid11,
                R.id.grid12, R.id.grid13, R.id.grid14, R.id.grid15,
                R.id.grid16, R.id.grid17, R.id.grid18, R.id.grid19,
                R.id.grid20, R.id.grid21, R.id.grid22, R.id.grid23
            };
        amtOfButtons = gridButtonIDs.length;

        // Create all the button objects so we don't have to keep remaking them
        gridButtons = new Button[amtOfButtons];
        for(int i = 0; i < amtOfButtons; i++){
            gridButtons[i] = this.findViewById(gridButtonIDs[i]);
        }

        // Update the entire grid
        updateGrid();
    }

    // Disable all passed buttons
    private void disableButtons(Button... buttons){
        for(Button b : buttons){
            b.setClickable(false);
        }
    }
    // Enable all passed buttons
    private void enableButtons(Button... buttons){
        for(Button b : buttons){
            b.setClickable(true);
        }
    }

    // CLICK METHODS
    // Equals Button
    public void eqClick(View view) {
        Log.i("Button Clicked", "Equal");

        // Get the user-inputed number and set the Calculator
        String operand = getOperand();
        // If the equals button is enabled, then we have a double as the operand
        calc.setNum(Double.parseDouble((operand)));

        // Calculate the value
        calc.calculate();

        // Set the Result text to display the new value
        setResultText(calc.getResult());

        // Record the interaction
        historyManager.add(calc.getOperator() + operand);
        updateGrid();

        // Reset to our original state (reset the inputs and disable equal button)
        resetOperandInput();
        calc.setOperator(null);
        historyManager.resetIndex();
        updateButtons();
    }
    // Operator + - * / Buttons
    public void operClick(View view) {
        Log.i("Button Clicked", "Operator");

        // Find the operator the user chose
        String operatorChosen = ((Button) view).getText().toString();

        // Set the calculator to the operator we chose
        calc.setOperator(operatorChosen);

        // See if we need to enable the equals button
        updateButtons();
    }

    // Undo Button
    public void undoClick(View view) {
        Log.i("Button Clicked", "Undo");

        int currentIndex = historyManager.getCurrentIndex();

        calculateFromButton(currentIndex, true, true);
        historyManager.incrementIndex(1);
        currentIndex = historyManager.getCurrentIndex();

        Log.i("Index Changed", "New Index: " + Integer.toString(currentIndex));

        updateUndoRedoButtons();
    }
    // Redo Button
    public void redoClick(View view) {
        Log.i("Button Clicked", "Redo");

        historyManager.decrementIndex(1);
        int currentIndex = historyManager.getCurrentIndex();

        calculateFromButton(currentIndex, false, true);

        Log.i("Index Changed", "New Index: " + Integer.toString(currentIndex));

        updateUndoRedoButtons();
    }

    // Buttons on the Grid
    public void gridButtonClick(View view) {
        Log.i("Button Clicked", "Grid");

        // The ID of the button that was pressed by the user
        int buttonIDPressed = view.getId();
        // Find the index of the button pressed
        int idIndex = linearSearch(gridButtonIDs, buttonIDPressed);

        calculateFromButton(idIndex, true, false);

        // We remove the entry from the history, update the grid, and reset our values
        historyManager.remove(idIndex);
        updateGrid();
        resetOperandInput();
        calc.setOperator(null);
    }

    // I/O METHODS
    // Get the String of the number typed by the user
    public String getOperand() {
        String operandText = operandEditText.getText().toString();

        return operandText;
    }
    // Change the number displayed in the Result text
    private void setResultText(double result){
        resultTextView.setText("Result = " + Double.toString(result));
    }
    // Reset the text on the Operand Input
    public void resetOperandInput(){
        operandEditText.setText("");
    }

    // MISC
    // Check if the user has entered a number
    public boolean isDouble(String input){
        try{
            Double.parseDouble(input);
            return true;
        } catch (NumberFormatException e){
            return false;
        }
    }
    // Find an element in an array and return its index
    public int linearSearch(int[] ids, int targetID){
        for(int i = 0; i < ids.length; i++){
            if (targetID == ids[i]){
                return i;
            }
        }
        return -1;
    }
    // Perform a calculation based on a button
    public void calculateFromButton(int buttonIndex, boolean opposite, boolean newButton){
        // Extract the operator and number from the button pressed
        String buttonText = gridButtons[buttonIndex].getText().toString();
        String buttonOperator = buttonText.substring(0, 1);
        double buttonNum = Double.parseDouble(buttonText.substring(1));

        // Set the calculator variables
        calc.setNum(buttonNum);
        String operator = buttonOperator;
        if (opposite) {
            operator = Calculator.oppositeOperator(buttonOperator);
        }
        calc.setOperator(operator);

        // Calculate the value
        calc.calculate();

        // Set the Result text to display the new value
        setResultText(calc.getResult());

        // Record the interaction
        if (newButton) {
            historyManager.add(calc.getOperator() + buttonNum);
            updateGrid();
        }
    }

    // Update the equals, undo, and redo buttons
    public void updateButtons(){
        updateEqButton();
        updateUndoRedoButtons();
    }
    // See if we need to update the equals button (enable/disable it)
    public void updateEqButton(){
        if (canEnableEquals()){
            enableButtons(eqButton);
        } else {
            disableButtons(eqButton);
        }
    }
    // See if we can enable or disable the undo and redo buttons
    public void updateUndoRedoButtons(){
        /*
        Redo:
        We enable the Redo button if we are not at index 0
        Undo:
        We enable the Undo button if we are not at the last index
         */
        int index = historyManager.getCurrentIndex();

        if(index != historyManager.size() - 1){
            enableButtons(undoButton);
        } else {
            disableButtons(undoButton);
        }
        if (index != 0){
            enableButtons(redoButton);
        } else {
            disableButtons(redoButton);
        }
    }
    // Check if we fulfil the requirements to enable the equals button
    public boolean canEnableEquals(){
        return isDouble(getOperand()) && calc.validOperator();
    }

    // Update the grid based on the history of transactions
    public void updateGrid(){
        // We've got a rolling window of the last 'amtOfButtons' entries in the full history
        List<String> history = historyManager.getHistory(amtOfButtons);

        String entry; // Current entry in the history
        Button currButton; // Current button based on the index
        // We loop through all the buttons
        for(int i = 0; i < amtOfButtons; i++){
            // If we are out of range default to 'no entry'
            if (i >= history.size()){
                entry = "";
            } else { // Otherwise, we just get the entry
                entry = history.get(i); // The current history entry (ex: "+5", "/4", etc)
            }
            currButton = gridButtons[i]; // The button ID at the curr index
            if(entry == null || entry == ""){ // If we don't have an entry we shouldn't have a visible button
                currButton.setVisibility(View.INVISIBLE);
            } else {
                // Make the button visible and add the text to it
                currButton.setVisibility(View.VISIBLE);
                currButton.setText(entry);
                fixTextSize(currButton);
            }
        }

        updateUndoRedoButtons();
    }

    // Numbers that are too big break the GridView, we change the text size for larger numbers
    // (y=mx+b / y = -2.2x + 27)
    public void fixTextSize(Button button) {
        int maxSize = 27;
        float sizeModifier = -2.2f; // Calculated this through a table on Desmos
        String text = button.getText().toString();
        int textLength = text.length();
        float newSize = Math.max((float) ((textLength * sizeModifier) + maxSize), 1);
        button.setTextSize(newSize);
        Log.i("New Text Size", "Button" + button.getId() + " size " + newSize);
    }
}
