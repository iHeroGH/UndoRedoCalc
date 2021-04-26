package georgematta.undoredocalc;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class GridHandler extends AppCompatActivity {

    private Activity activity;

    private ArrayList<Integer> gridButtons = new ArrayList<Integer>();
    private int changeCounter = 1;

    // Handlers
    private UndoRedoHandler unReHandler;
    private InformationHandler infoHandler;

    public GridHandler(Activity activity) {
        this.activity = activity;
        // Initialize an array of int IDs for the buttons in the grid view
        int[] gridButtonsArray = new int[]{R.id.grid0, R.id.grid1, R.id.grid2, R.id.grid3, R.id.grid4, R.id.grid5, R.id.grid6, R.id.grid7, R.id.grid8, R.id.grid9, R.id.grid10, R.id.grid11, R.id.grid12, R.id.grid13, R.id.grid14, R.id.grid15, R.id.grid16, R.id.grid17, R.id.grid18, R.id.grid19, R.id.grid20, R.id.grid21, R.id.grid22, R.id.grid23};
        for(int id : gridButtonsArray){
            gridButtons.add(id);
        }
    }

    public void setInfoHandler(InformationHandler infoHandler){
        this.infoHandler = infoHandler;
    }
    public void setUnReHandler(UndoRedoHandler unReHandler){
        this.unReHandler = unReHandler;
    }

    public void gridButtonClick(View view){
        String applied = infoHandler.applyOperation(view, true);

        infoHandler.setResultTextViewText("Result = " + Double.toString(infoHandler.getResult()));
        Log.i("test", ((Button)view).getText().toString());
        moveFutureButtonsBack(view);

        unReHandler.checkUndoRedo();

        Log.i("Grid button Clicked - New Result", String.valueOf(infoHandler.getResult()));
    }

    public void gridButtonClick(View view, String identifier, boolean addButton){
        boolean opposite;
        if(identifier.equals("undo")){
            opposite = true;
        } else{
            opposite = false;
        }
        String applied = infoHandler.applyOperation(view, opposite);

        if (addButton) {
            addToGrid(applied.substring(0, 1), applied.substring(1), false);
        }

        infoHandler.setResultTextViewText("Result = " + Double.toString(infoHandler.getResult()));

        unReHandler.checkUndoRedo();

        Log.i("Undo/Redo Operation - New Result", String.valueOf(infoHandler.getResult()));
    }

    public void addToGrid(String operator, String secondOperandText, boolean resetIndex) {
        Button gridButton = this.activity.findViewById(gridButtons.get(findFirstOpenButton()));
        gridButton.setText(operator + secondOperandText);
        fixTextSize(gridButton);
        gridButton.setVisibility(View.VISIBLE);
        if (resetIndex) {
            unReHandler.setActionIndex(buttonIndex(gridButton));
            Log.i("Action Index Changed (Grid)", String.valueOf(unReHandler.getActionIndex()));
        }
        unReHandler.checkUndoRedo();
        Log.i("New Button Added", gridButton.getText().toString());
    }

    // Numbers that are too big occasionally break the grid view, we reduce the text size of larger numbers linearly (y=mx+b / y = -2.2x + 27)
    public void fixTextSize(Button button){
        int maxSize = 27;
        float sizeModifier = -2.2f;
        String text = (String) button.getText();
        int textLength = text.length();
        float newSize = Math.max((float) ((textLength * sizeModifier) + maxSize), 1);
        button.setTextSize(newSize);
        Log.i("New Text Size", "Button" + button.getId() + " size " + newSize);
    }

    // Returns the index of the first available button
    public int findFirstOpenButton(){
        for(int i = 0; i < gridButtons.size(); i++){
            Button gridButton = (Button) this.activity.findViewById(gridButtons.get(i));
            // Return first invisible button
            if(gridButton.getVisibility() == View.INVISIBLE){
                return i;
            }
        }
        // If none are invisible, return the oldest visible button
        for(int i = 0; i < gridButtons.size(); i++){
            Button gridButton = (Button) this.activity.findViewById(gridButtons.get(i));
            if (Integer.parseInt(gridButton.getTag().toString()) < changeCounter){
                gridButton.setTag(changeCounter);
                Log.i("New Tag Set", "Button" + gridButton.getId() + " tag " + changeCounter);
                return i;
            }
        }
        // All the buttons have been changed changeCounter amount of times, increase the change counter and re-find a button
        changeCounter++;
        Log.i("New Change Counter", String.valueOf(changeCounter));
        return findFirstOpenButton();
    }

    // Returns the index of a grid button
    public int buttonIndex(Button gridButton){
        for(int i = 0; i < gridButtons.size(); i++){
            if(gridButtons.get(i) == gridButton.getId()){
                return i+1;
            }
        }
        return -1;
    }

    public void moveFutureButtonsBack(View view){
        int index = findGridIndex(view);
        recursiveRewrite(index, index);
        setEmptyButtonInvis();
    }

    public void setEmptyButtonInvis(){
        for(int i = 0; i < gridButtons.size(); i++){
            Button currButton = (Button) this.activity.findViewById(gridButtons.get(i));
            if (currButton.getText() == "" && currButton.getVisibility() == View.VISIBLE){
                currButton.setVisibility(View.INVISIBLE);
            }
        }
    }

    public int findGridIndex(View view){
        Button pressedButton = (Button) view;
        // Find the pressed button in the array
        int index = 0;
        while(index < gridButtons.size()){
            Log.i("Finding Grid Index", String.valueOf(index));
            if(gridButtons.get(index) == pressedButton.getId()){
                return index;
            }
            index++;
        }

        return -1;
    }

    public void recursiveRewrite(int buttonIndex, int currIndex){
        Log.i("Recursive Rewrite", buttonIndex + " " + currIndex);
        if(buttonIndex == currIndex-22){ // Checks if we have reached the button before the one we're rewriting to
            return; // Stop rewriting
        }

        Button currButton = (Button) this.activity.findViewById(gridButtons.get(currIndex));
        Button nextButton = (Button) this.activity.findViewById(gridButtons.get(currIndex+1));

        if(nextButton.getVisibility() == View.INVISIBLE) { // If the next button is invisible
            currButton.setVisibility(View.INVISIBLE);
            currButton.setText("");
            return;
        }

        currButton.setText(nextButton.getText());
        currButton.setTag(nextButton.getTag());

        setEmptyButtonInvis();

        recursiveRewrite(buttonIndex, currIndex+1);
    }

    public ArrayList<Integer> getGridButtons() {
        return gridButtons;
    }
}
