package georgematta.undoredocalc;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private String operator;
    private String secondOperand;

    public void eqClick(View view){
        return;
    }

    public void operClick(View view){

        String operatorChosen = ((Button) view).getText().toString();

        this.operator = operatorChosen;

        Log.i("New Operator Chosen", this.operator);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}