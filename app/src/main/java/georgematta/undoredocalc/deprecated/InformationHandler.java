package georgematta.undoredocalc.deprecated;

import android.app.Activity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import georgematta.undoredocalc.R;

public class InformationHandler extends AppCompatActivity {

    private Activity activity;

    // User Input and Output
    private EditText secondOperandEditText;
    private TextView resultTextView;

    // Values
    private String operator;
    private String secondOperandText;
    private double result;

    // Handlers
    private EqualHandler eqHandler;

    public InformationHandler(Activity activity) {
        this.activity = activity;
        secondOperandEditText = (EditText) activity.findViewById(R.id.operandEdit);
        addListener();
        resultTextView = (TextView) activity.findViewById(R.id.resultTextView);
        resultTextView.setText(resultTextView.getText() + Double.toString(result));
    }

    public void setEqualHandler(EqualHandler eqHandler) {
        this.eqHandler = eqHandler;
    }

    public void addListener() {
        // Create the EditText listener
        secondOperandEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                secondOperandText = s.toString();
                eqHandler.checkEqualButton();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                secondOperandText = s.toString();
                eqHandler.checkEqualButton();
            }

            @Override
            public void afterTextChanged(Editable s) {
                secondOperandText = s.toString();
                eqHandler.checkEqualButton();
            }
        });
    }

    public String applyOperation(View view, boolean opposite) {
        String applied = "";
        double buttonNum = 0;
        String buttonOperator = operator;
        // Apply the chosen operation on the provided second operand
        if (!opposite) {
            if (((Button) view).getText().toString().equals("=")) {
                buttonOperator = operator;
                buttonNum = Double.parseDouble(secondOperandText);
            } else {
                Button gridButton = (Button) view;
                String buttonText = gridButton.getText().toString();
                buttonOperator = buttonText.substring(0, 1);
                buttonNum = Double.parseDouble(buttonText.substring(1));
            }
            switch (buttonOperator) {
                case "+":
                    result += buttonNum;
                    applied = "+" + (int) buttonNum;
                    break;
                case "-":
                    result -= buttonNum;
                    applied = "-" + (int) buttonNum;
                    break;
                case "*":
                    result *= buttonNum;
                    applied = "*" + (int) buttonNum;
                    break;
                case "/":
                    result /= buttonNum;
                    applied = "/" + (int) buttonNum;
                    break;
            }
        }

        // If we're removing something from the GridView
        else {
            Button gridButton = (Button) view;
            String buttonText = gridButton.getText().toString();
            buttonOperator = buttonText.substring(0, 1);
            buttonNum = Double.parseDouble(buttonText.substring(1));
            switch (buttonOperator) {
                case "+":
                    result -= buttonNum;
                    applied = "-" + (int) buttonNum;
                    break;
                case "-":
                    result += buttonNum;
                    applied = "+" + (int) buttonNum;
                    break;
                case "*":
                    result /= buttonNum;
                    applied = "/" + (int) buttonNum;
                    break;
                case "/":
                    result *= buttonNum;
                    applied = "*" + (int) buttonNum;
                    break;
            }
        }

        Log.i("Applied Operation", applied);
        return applied;
    }

    // Clear the vars after equal button is pressed
    public void clearVars() {
        secondOperandEditText.setText("");
        operator = null;
    }

    // Set the new operator whenever an operator is clicked
    public void operClick(View view) {

        String operatorChosen = ((Button) view).getText().toString();

        this.operator = operatorChosen;

        eqHandler.checkEqualButton();

        Log.i("New Operator Chosen", this.operator);

    }

    public EditText getSecondOperandEditText() {
        return secondOperandEditText;
    }

    public TextView getResultTextView() {
        return resultTextView;
    }

    public String getOperator() {
        return operator;
    }

    public String getSecondOperandText() {
        return secondOperandText;
    }

    public double getResult() {
        return result;
    }

    public void setSecondOperandText(String str) {
        secondOperandText = str;
    }

    public void setResultTextViewText(String str) {
        resultTextView.setText(str);
    }
}
