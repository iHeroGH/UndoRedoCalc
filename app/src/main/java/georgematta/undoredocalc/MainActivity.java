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

public class MainActivity extends AppCompatActivity {

    String operator;
    String secondOperandText;
    double result;

    EditText secondOperandEditText;
    TextView resultTextView;


    Button undoButton;
    Button redoButton;
    Button eqButton;

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

        clearVars();

        Log.i("Equal Clicked - New Result", String.valueOf(result));
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