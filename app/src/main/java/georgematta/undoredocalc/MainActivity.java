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

public class MainActivity extends AppCompatActivity {

    private Calculator calc;

    // Buttons
    private Button eqButton;
    private Button undoButton;
    private Button redoButton;

    // User I/O
    private EditText operandEditText;
    private TextView resultTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Create the Calculator we will be using
        calc = new Calculator();

        // Initialize our I/O fields
        initOperandInput();
        initResultOutput();

        // Initialize our buttons
        initButtons();
    }

    // INITIALIZER
    // Find the Operand Input text box
    private void initOperandInput(){
        operandEditText = (EditText) this.findViewById(R.id.operandEdit);
        // Create the EditText listener
        operandEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                updateEqButton();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                updateEqButton();
            }

            @Override
            public void afterTextChanged(Editable s) {
                updateEqButton();
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

        // Reset to our original state (reset the inputs and disable equal button)
        resetOperandInput();
        calc.setOperator(null);
        updateEqButton();
    }
    // Operator + - * / Buttons
    public void operClick(View view) {
        Log.i("Button Clicked", "Operator");

        // Find the operator the user chose
        String operatorChosen = ((Button) view).getText().toString();

        // Set the calculator to the operator we chose
        calc.setOperator(operatorChosen);

        // See if we need to enable the equals button
        updateEqButton();
    }

    // Undo Button
    public void undoClick(View view) {
        Log.i("Button Clicked", "Undo");
    }
    // Redo Button
    public void redoClick(View view) {
        Log.i("Button Clicked", "Redo");
    }

    // Buttons on the Grid
    public void gridButtonClick(View view) {
        Log.i("Button Clicked", "Grid");
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

    // See if we need to update the equals button (enable/disable it)
    public void updateEqButton(){
        if (canEnableEquals()){
            enableButtons(eqButton);
        } else {
            disableButtons(eqButton);
        }
    }
    // Check if we fulfil the requirements to enable the equals button
    public boolean canEnableEquals(){
        return isDouble(getOperand()) && calc.validOperator();
    }
}
