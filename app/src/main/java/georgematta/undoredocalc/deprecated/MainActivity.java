package georgematta.undoredocalc.deprecated;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import georgematta.undoredocalc.R;

public class MainActivity extends AppCompatActivity {

    // Handlers
    private EqualHandler eqHandler;
    private UndoRedoHandler unReHandler;
    private InformationHandler infoHandler;
    private GridHandler gridHandler;

    // Re-reference the click methods
    public void eqClick(View view) {
        eqHandler.eqClick(view);
    }

    public void undoClick(View view) {
        unReHandler.undoClick(view);
    }

    public void redoClick(View view) {
        unReHandler.redoClick(view);
    }

    public void operClick(View view) {
        infoHandler.operClick(view);
    }

    public void gridButtonClick(View view) {
        gridHandler.gridButtonClick(view);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        eqHandler = new EqualHandler(this);
        unReHandler = new UndoRedoHandler(this);
        infoHandler = new InformationHandler(this);
        gridHandler = new GridHandler(this);

        eqHandler.setGridHandler(gridHandler);
        eqHandler.setInfoHandler(infoHandler);

        infoHandler.setEqualHandler(eqHandler);

        unReHandler.setGridHandler(gridHandler);

        gridHandler.setInfoHandler(infoHandler);
        gridHandler.setUnReHandler(unReHandler);
    }


}