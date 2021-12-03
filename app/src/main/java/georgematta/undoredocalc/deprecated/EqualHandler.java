package georgematta.undoredocalc.deprecated;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import georgematta.undoredocalc.R;

public class EqualHandler extends AppCompatActivity {

    private Activity activity;

    private Button eqButton;

    // Handlers
    private InformationHandler infoHandler;
    private GridHandler gridHandler;

    public EqualHandler(Activity activity) {
        this.activity = activity;
        eqButton = (Button) activity.findViewById(R.id.eqButton);
        eqButton.setClickable(false);
    }

    public void setInfoHandler(InformationHandler infoHandler) {
        this.infoHandler = infoHandler;
    }

    public void setGridHandler(GridHandler gridHandler) {
        this.gridHandler = gridHandler;
    }

    public void eqClick(View view) {

        infoHandler.applyOperation(view, false);

        infoHandler.setResultTextViewText("Result = " + Double.toString(infoHandler.getResult()));

        gridHandler.addToGrid(infoHandler.getOperator(), infoHandler.getSecondOperandText(), true);

        infoHandler.clearVars();

        Log.i("Equal Clicked - New Result", String.valueOf(infoHandler.getResult()));
    }

    // This method checks if equal button should be enabled
    public void checkEqualButton() {

        boolean status = true;

        if (infoHandler.getOperator() == null) { // Disable if no operator has been chosen
            status = false;
        }
        if (infoHandler.getSecondOperandText() == null) { // To prevent an error for checking length if there is no text
            infoHandler.setSecondOperandText("");
        }
        if (infoHandler.getSecondOperandText().length() == 0) { // Disable if the second operand field is empty
            status = false;
        }

        eqButton.setClickable(status);
        Log.i("Equal Button Status", String.valueOf(status));
    }

}
