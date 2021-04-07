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
    double result;

    EditText secondOperandEditText;
    TextView resultTextView;


    Button undoButton;
    Button redoButton;
    Button eqButton;

    public void eqClick(View view){

        double secondOperand = Double.parseDouble(secondOperandEditText.getText().toString());

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

        Log.i("Equal Clicked - New Result", String.valueOf(result));
    }

    public void operClick(View view){

        String operatorChosen = ((Button) view).getText().toString();

        this.operator = operatorChosen;

        Log.i("New Operator Chosen", this.operator);

    }

    // This method checks if the given CharSequence's length is 0 (the EditText is empty) to disable or enable the Equal button
    public boolean checkEditText(CharSequence s){

        boolean status = true;

        if (operator == null){ // Automatically disable if no operator has been chosen
            status = false;
        }
        if (s.length() == 0) {
            status = false;
        }

        eqButton.setClickable(status);
        Log.i("Equal Button Status", String.valueOf(status));

        return status;
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
                checkEditText(s);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.i("Text Changed", s.toString());
                checkEditText(s);
            }

            @Override
            public void afterTextChanged(Editable s) {
                checkEditText(s);
            }
        });
    }
}