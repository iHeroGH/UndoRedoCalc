package georgematta.undoredocalc.deprecated;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import georgematta.undoredocalc.R;

public class UndoRedoHandler extends AppCompatActivity {

    private Activity activity;

    private Button undoButton;
    private Button redoButton;
    private int actionIndex;

    // Handlers
    private GridHandler gridHandler;

    public UndoRedoHandler(Activity activity) {
        this.activity = activity;
        undoButton = (Button) activity.findViewById(R.id.undoButton);
        redoButton = (Button) activity.findViewById(R.id.redoButton);
        undoButton.setClickable(false);
        redoButton.setClickable(false);
    }

    public void setGridHandler(GridHandler gridHandler) {
        this.gridHandler = gridHandler;
    }

    public void actionIndexChange(boolean increase) {
        Log.i("Starting Action Index", String.valueOf(actionIndex));
        if (increase) {
            actionIndex = actionIndex + 1;
        } else {
            actionIndex = Math.max(actionIndex - 1, 0);
        }
        Log.i("Action Index Changed", String.valueOf(actionIndex));
        checkUndoRedo();
    }

    public void checkUndoRedo() {
        ArrayList<Integer> gridButtons = gridHandler.getGridButtons();

        if (actionIndex == 0) {
            undoButton.setClickable(false);
            Log.i("Undo Button Clickable", "Disabled");
        } else if (((Button) this.activity.findViewById(gridButtons.get(getActionIndex("before")))).getVisibility() == View.VISIBLE) {
            undoButton.setClickable(true);
            Log.i("Undo Button Clickable", "Enabled");
        } else {
            undoButton.setClickable(false);
            Log.i("Undo Button Clickable", "Disabled");
        }
        if (actionIndex == gridButtons.size() - 1) {
            redoButton.setClickable(false);
            Log.i("Redo Button Clickable", "Disabled");
        } else if (((Button) this.activity.findViewById(gridButtons.get(getActionIndex("next")))).getVisibility() == View.VISIBLE) {
            redoButton.setClickable(true);
            Log.i("Redo Button Clickable", "Enabled");

        } else {
            redoButton.setClickable(false);
            Log.i("Redo Button Clickable", "Disabled");
        }
    }

    public void undoClick(View view) {
        actionIndexChange(false);
        actionOperation("undo");
        gridHandler.setEmptyButtonInvis();
    }

    public void redoClick(View view) {
        actionIndexChange(true);
        actionOperation("redo");
        gridHandler.setEmptyButtonInvis();
    }

    public void actionOperation(String identifier) {
        ArrayList<Integer> gridButtons = gridHandler.getGridButtons();

        int index = actionIndex;
        if (identifier.equals("redo")) {
            index = actionIndex - 1;
        }
        if (index < 0) {
            index = gridButtons.size() + index;
        }
        View focusButton = this.activity.findViewById(gridButtons.get(index));
        Log.i("Applying " + identifier, String.valueOf(actionIndex));
        boolean addButton = true;
//        if(identifier.equals("redo")) {
//            addButton = false;
//        }
        gridHandler.gridButtonClick(focusButton, identifier, addButton);
    }

    public int getActionIndex(String identifier) {
        if (identifier.equals("before")) {
            return actionIndex - 1;
        } else {
            return actionIndex;
        }
    }

    public int getActionIndex() {
        return actionIndex;
    }

    public void setActionIndex(int actionIndex) {
        this.actionIndex = actionIndex;
    }
}
