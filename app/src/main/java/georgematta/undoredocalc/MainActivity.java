package georgematta.undoredocalc;

import android.graphics.Color;
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

/**
 * This is the Main center for all Undo-Redo Calculator interactions
 * All the Input-Output and user interface methods are present here
 * The MainActivity will use a Calculator object to run all calculations
 * The MainActivity also uses a HistoryManager object to store all
 * of the interactions made by the user
 * Info on each of thse classes will be within their own JavaDoc
 *
 * @author George Matta
 * @version 2.0
 */
public class MainActivity extends AppCompatActivity {

    /**
     * The Calculator calc which will be used for every operation, as well as to find
     * the oposite operator of a given oprator (ex: + and - are opposites)
     */
    private Calculator calc;
    /**
     * The HistoryManager historyManager will be used to store every interaction the user
     * has with the program
     * They are stored in an ArrayList in the HistoryManager class as a
     * list of Strings "OperatorOperand"
     */
    private HistoryManager historyManager;

    // Buttons
    /**
     * A reference to the Equals Button so we can enable and disable it within the program
     */
    private Button eqButton;
    /**
     * A reference to the Undo Button so we can enable and disable it within the program
     */
    private Button undoButton;
    /**
     * A reference to the Redo Button so we can enable and disable it within the program
     */
    private Button redoButton;

    // Grid
    /**
     * An array of the Android Studio <b>IDs</b> for every button in the Grid
     */
    int[] gridButtonIDs;
    /**
     * An array of the <b>Button</b> objects created by gridButtonIDs
     */
    Button[] gridButtons;
    /**
     * An int denoting the total amount of buttons on the grid
     * (it gets set to 24 for our purposes, but it can be changed by changing how many buttons
     * there are in the grid)
     */
    int amtOfButtons;

    // User I/O
    /**
     * A reference to the Input TextBox so we can get the information entered by the user
     * And so we can clear it after the Equal button is pressed
     */
    private EditText operandEditText;
    /**
     * A reference to the output Result text so we can update it as the user makes interactions
     */
    private TextView resultTextView;

    /**
     * The onCreate method is called when the user runs the app. This can be thought of as a
     * constructor for our application
     *
     * This is where we initialize all of our variables
     * @param savedInstanceState This parameter is one that we don't especially use for our program
     *                           it is designed to allow for information to be passed
     *                           from instance to instance of the program,
     *                           but we just reset the program when it is closed.
     */
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
    /**
     * Initializes the Operand Input Text. We can't do this before the app is initialized, so we
     * find the Input box based on its ID. We also add a listener to the textbox.
     * This listener tells us when a user manipulates the input box
     * (before they enter text, as they enter text, and after they enter text).
     * We use this informatioon to see if we should enable or disable our buttons)
     */
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

    /**
     * Initializes the output text. Again, we can't find elements on the screen until our app is
     * initialized, so we do this in the onCreate method. We also set the initial result text to 0
     */
    private void initResultOutput(){
        resultTextView = (TextView) this.findViewById(R.id.resultTextView);
        setResultText(0);
    }

    /**
     * Initializes our equals, undo, and redo buttons by finding them from their IDs. We also
     * start the program with all three of these buttons disabled.
     */
    private void initButtons(){
        eqButton = (Button) this.findViewById(R.id.eqButton);
        undoButton = (Button) this.findViewById(R.id.undoButton);
        redoButton = (Button) this.findViewById(R.id.redoButton);

        disableButtons(eqButton, undoButton, redoButton);
    }

    /**
     * Initializes the GridView buttons.
     * We have a hard-coded list of the gridButtonIDs which would be R.id.grid0 - R.id.grid23
     * Next, we loop through those IDs and call findViewById(id)
     * (which finds the actual button based on the ID)
     * and we initalize the gridButtons array
     */
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

    /**
     * Disables all the buttons passed in from being pressed by the user (setClickable(false))
     * @param buttons A vararg of the buttons to be disabled
     */
    private void disableButtons(Button... buttons){
        for(Button b : buttons){
            b.setClickable(false);
        }
    }
    /**
     * Enables all the buttons passed in to be pressed by the user (setClickable(true))
     * @param buttons A vararg of the buttons to be enabled
     */
    private void enableButtons(Button... buttons){
        for(Button b : buttons){
            b.setClickable(true);
        }
    }

    // CLICK METHODS

    /**
     * The method called when the Equals button is pressed.
     *
     * First, we get the number typed by the user and set the Calculator's number to that number
     * We then calculate the new value and set the result text to the new result
     * We place this interaction in the historyManager
     * (add a String 'operator' + 'operand' in the manager
     * and we update the grid to reflect this new addition
     * (so we recreate the grid based on the new history log)
     * If the user has undone or redone an action, the index wouldn't be at the start of the list
     * If the user clicks the equals button, however, we place the undo-redo index back at 0
     *
     * Finally, we reset our variables to their original state:
     * We clear the input text box
     * We set the operand in our Calculator to null
     * and we update our buttons to see if we need to enable or disable them (we would most likely
     * be disabling the equals button)
     *
     * @param view The View object associated with the Equals button
     */
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
        historyManager.resetIndex();
        updateGrid();

        // Reset to our original state (reset the inputs and disable equal button)
        resetOperandInput();
        calc.resetOperator();
        updateButtons();
    }

    /**
     * The method called when any of the Operation (+-*\/) buttons are pressed
     *
     * First, we find the operator chosen by the user by analyzying the text of the button
     * Next, we set the operator in our Calculator to the operator chosen
     * Finally, we update our buttons to see if we need to enable or disable them
     * (An example case would be if the user has already entered a number in the text bot
     * if they then choose an operator, we should enable to equals button)
     *
     * @param view The View object associated with the Operation button pressed
     */
    public void operClick(View view) {
        Log.i("Button Clicked", "Operator");

        // Find the operator the user chose
        String operatorChosen = ((Button) view).getText().toString();

        // Set the calculator to the operator we chose
        calc.setOperator(operatorChosen);

        // See if we need to enable the equals button
        updateEqButton();
    }

    /**
     * The method called when the Undo button is pressed
     *
     * First, we find the current index from the HistoryManager
     * This index is the index of the button that the user is currently targeting
     *
     * In the undoClick method, we run the opposite operation of the current button selected
     * and increment the index <b>after</b> we have completed the operation
     * We also add a new button to the grid
     *
     * Finally, we re-highlight the index which is being looked at by the undo/redo index
     * and update the undo and redo buttons to reflect the index change
     *
     * @param view The View object associated with the Undo button
     */
    public void undoClick(View view) {
        Log.i("Button Clicked", "Undo");

        int currentIndex = historyManager.getCurrentIndex();

        calculateFromButton(currentIndex, true, true);
        historyManager.incrementIndex();
        highlightIndex();
        currentIndex = historyManager.getCurrentIndex();

        Log.i("Index Changed", "New Index: " + Integer.toString(currentIndex));

        updateUndoRedoButtons();
    }

    /**
     * The method called when the Redo button is pressed
     *
     * Alternatively, for the redoClick method, we decrement the index <b>first</b>
     * and apply the <b>non-opposite</b> operation on the button
     *
     * Finally, we re-highlight the index which is being looked at by the undo/redo index
     * and update the undo and redo buttons to reflect the index change
     *
     * @param view The View object associated with the Undo button
     */
    public void redoClick(View view) {
        Log.i("Button Clicked", "Redo");

        historyManager.decrementIndex();
        highlightIndex();
        int currentIndex = historyManager.getCurrentIndex();

        calculateFromButton(currentIndex, false, true);

        Log.i("Index Changed", "New Index: " + Integer.toString(currentIndex));

        updateUndoRedoButtons();
    }

    /**
     * The method called when any of the Grid buttons are pressed
     *
     * First, we find the ID of the button pressed
     * We then apply a Linear Search algorithm to find the associated index of the button
     * in the gridButtonIDs array. Using this ID, we apply the opposite calculation and
     * we don't create a new button in the grid.
     * Essentially, we find the button in the grid, apply the opposite operation, and
     * remove it from our grid
     *
     * We then remove it from the historyManager, update the grid, and reset our variables
     *
     * @param view The View object associated with the Grid button pressed
     */
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
        calc.resetOperator();
    }

    // I/O METHODS

    /**
     * A helper method to return the number typed by the user
     * We just find the charSequence of the text in the operandEditText object
     * (with the getText() method)
     * and call .toString() on that sequence
     *
     * @return A string representing the text in the input text box
     */
    public String getOperand() {
        String operandText = operandEditText.getText().toString();

        return operandText;
    }

    /**
     * A helper method to set the output text to "Result = " and the number passed in
     * @param result The number we want to be displayed in the result output
     */
    private void setResultText(double result){
        resultTextView.setText("Result = " + Double.toString(result));
    }

    /**
     * A helper method to reset the text within the input text box.
     * This allows the user to type in a new number without having to erase the number
     * they already typed
     * This also allows the text box to redisplay the hint text (Ex: "Enter Operand")
     */
    public void resetOperandInput(){
        operandEditText.setText("");
    }

    // MISC
    /**
     * A utility method to check if a String which was passed in is a double or not.
     * This method is static
     * @param input The String we would like to check
     * @return A boolean denoting if the input String can be parsed to a double or not
     */
    public static boolean isDouble(String input){
        try{
            Double.parseDouble(input);
            return true;
        } catch (NumberFormatException e){
            return false;
        }
    }

    /**
     * The linear search algorithm we use to find the index of the button ID based on a target ID
     * It is implemented normally, as a linear search algorithm would, and it is a static method
     *
     * @param ids An array of ints that we search through
     * @param targetID The int that we are searching for in the array
     * @return The index of the target if it is found. We return -1 if it isn't in the array
     */
    public static int linearSearch(int[] ids, int targetID){
        for(int i = 0; i < ids.length; i++){
            if (targetID == ids[i]){
                return i;
            }
        }
        return -1;
    }

    /**
     * A method we use to perform a new calculation based on a given button's index
     *
     * First, we extract the operator and the number from the button pressed by looking at its text
     * We set the Calculator's variables to the ones found in the button
     *
     * If we are trying to find the opposite operation, we use the static method
     * oppositeOperator(String) in the Calculator class
     *
     * and then apply the operation
     *
     * We set the result text to the new result.
     * Additionally, we add a new entry to the historyManager if newButton is true
     *
     * @param buttonIndex The index of the button (from the gridButtons array) we
     *                    are extracting information from
     * @param opposite A boolean denoting if we should be applying the opposite operator than
     *                 the one found in the button
     * @param newButton A boolean denoting if we should add a new entry to our HistoryManager
     */
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

    /**
     * A catch-all method to update the Equals button as well as the Undo and Redo buttons
     */
    public void updateButtons(){
        updateEqButton();
        updateUndoRedoButtons();
    }

    /**
     * The method that enables or disables the Equals button
     * This method calles the canEnableEquals() method to check if we fulfill the requirements
     * for enabling or disabling
     */
    public void updateEqButton(){
        if (canEnableEquals()){
            enableButtons(eqButton);
        } else {
            disableButtons(eqButton);
        }
    }

    /**
     * A helper method which checks if we can enable the Equal button
     * We calculate this by seeing if there is a parseable double in the operand text box
     * (By running the isDouble helper method)
     * We also check if the Calculator has a valid operator (ie: not null)
     *
     * @return A boolean denoting whether or not we can enable the Equals button
     */
    public boolean canEnableEquals(){
        return isDouble(getOperand()) && calc.validOperator();
    }

    /**
     *  The method that enables or disables the Undo and Redo buttons
     *
     *  The logic follows:
     *
     *  If the size of the historyManager is 0 (meaning there are no buttons)
     *      we disable both buttons
     *
     *  If we are not at the last index:
     *      we enable the Undo button
     *  If we are not at index 0:
     *      we enable the Redo button
     *
     * Similarly to the updateEqButton() method, the actual conditions are
     * split up into the helper methods
     * canEnableUndo() and canEnableRedo()
     *
     */
    public void updateUndoRedoButtons(){
        // If we don't have any buttons to scroll through, disable both buttons
        if (historyManager.isEmpty()){
            disableButtons(undoButton, redoButton);
            return;
        }

        // Based on the index, we check if we can enable the undo button
        int index = historyManager.getCurrentIndex();
        if(canEnableUndo(index)){
            enableButtons(undoButton);
        } else {
            disableButtons(undoButton);
        }

        // We then check if we can enabl the redo button
        if (canEnableRedo(index)){
            enableButtons(redoButton);
        } else {
            disableButtons(redoButton);
        }
    }

    /**
     * A helper method to see if we can enable the undo button.
     * This method is an optional version of the canEnableUndo(int) method.
     * This method doesn't take an index like the other method. This method only delegates
     * to the other method and calculates the index from the historyManager to pass it through to
     * the main canEnableUndo() method
     *
     * @return A boolean denoting if we can enable the Undo button
     */
    public boolean canEnableUndo(){
        int index = historyManager.getCurrentIndex(); // If we don't have the index, we calculate it
        // Then run the main method
        return canEnableUndo(index);
    }

    /**
     * A helper method to see if we can enable the redo button.
     * This method is an optional version of the canEnableRedo(int) method.
     * This method doesn't take an index like the other method. This method only delegates
     * to the other method and calculates the index from the historyManager to pass it through to
     * the main canEnableRedo() method
     *
     * @return A boolean denoting if we can enable the Redo button
     */
    public boolean canEnableRedo(){
        int index = historyManager.getCurrentIndex(); // If we don't have the index, we calculate it
        // Then run the main method
        return canEnableRedo(index);
    }

    /**
     * This method also calculates if we can enable the Undo button, but this is where we actually
     * make the check
     *
     * We check if the index passed in is not the last index of the history log
     *
     * @param index The index of the undo-redo pointer in the historyManager
     * @return A boolean denoting if we can enable the Undo button
     */
    public boolean canEnableUndo(int index){
        return index != (historyManager.size() - 1);
    }

    /**
     * This method also calculates if we can enable the Redo button, but this is where we actually
     * make the check
     *
     * We check if the index passed in is not the 0th index of the history log
     *
     * @param index The index of the undo-redo pointer in the historyManager
     * @return A boolean denoting if we can enable the Redo button
     */
    public boolean canEnableRedo(int index){
        return index != 0;
    }

    /**
     * A method to update the entire grid to reflect changes made to historyManager
     *
     * First, we get the history log from index 0 to index amtOfButtons
     * (Essentially, the first amtOfButtons amount of entries in the log)
     *
     * We create variables for the entry and the current button, then loop through the buttons
     * If we don't have an entry associated with the index of the current button, we initialize the
     * entry to be an empty string. Otherwise, we just find the entry
     *
     * Next, we get the button from gridButtons based on the current ID
     *
     * If the entry turns out to be null or empty, we make the button invisible
     * Otherwise, we make the button visible, add the text from the log to the button, and run the
     * necessary size manipulations
     *
     * After we have looped through the entire list of buttons, we see if we need to update the
     * undo or redo buttons (to reflect additions or removals in the list)
     * And we re-highlight the index of the undo-redo pointer
     */
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
            if(entry == null || entry == ""){ // If we don't have an entry, the button is invisible
                currButton.setVisibility(View.INVISIBLE);
            } else {
                // Make the button visible and add the text to it
                currButton.setVisibility(View.VISIBLE);
                currButton.setText(entry);
                fixTextSize(currButton);
            }
        }

        updateUndoRedoButtons();
        highlightIndex();
    }

    /**
     * A helper method to find the index of the button we are pointing at with the
     * historyManager undo-redo index
     *
     * We change the text color of this button to be red, while setting all the other buttons' text
     * to white
     */
    public void highlightIndex(){
        // First, we reset all the buttons to white
        for(Button b : gridButtons){
            b.setTextColor(Color.WHITE);
        }

        // Then we set the target button to red
        int targetIndex = historyManager.getCurrentIndex();
        gridButtons[targetIndex].setTextColor(Color.RED);
    }

    /**
     * A method to calculate a new size for text based on how large it is
     *
     * Since numbers that are too large ocassionaly break the GridView, we change the text size
     * for larger numbers
     *
     * The equation to calculate this new size is linear (y = mx + b)
     * The parameters of the function were found by analyzing a table of values using Desmos
     *
     * The equation is y = -2.2x + 27
     * Meaning the maximum text size is 27 and the size changes by a rate of -2.2
     *
     * @param button The button we are changing the text size for
     */
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
