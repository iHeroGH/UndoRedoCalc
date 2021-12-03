package georgematta.undoredocalc;

import java.util.ArrayList;
import java.util.List;

public class HistoryManager{

    ArrayList<String> history;
    int currentIndex;

    public HistoryManager(){
        this.history = new ArrayList<>();
        this.currentIndex = 0;
    }

    // GETTERS
    public int getCurrentIndex(){
        return this.currentIndex;
    }
    // Returns the entire history
    public ArrayList<String> getHistory(){
        return history;
    }
    // Return the last 'window' elements in the index
    public List<String> getHistory(int window){
        // Return a subset of the list from the first to the windowth element
        int maxSize = size();
        if (window > maxSize){
            window = maxSize;
        }
        return history.subList(0, window);
    }

    // ARRAYLIST CONTROL
    // Returns the element at a specific index
    public String get(int index){
        if (index >= size()){
            return "";
        }
        return history.get(index);
    }
    // Adds an element to the start of the list
    public void add(String element){
        add(0, element);
    }
    // Adds an element to a specific index of the list
    public void add(int index, String element){
        incrementIndex(1);
        history.add(index, element);
    }
    // Removes an element from the list given an index
    public void remove(int index){
        decrementIndex(1);
        history.remove(index);
    }
    // Returns the total size of the history
    public int size(){
        return history.size();
    }

    // INDEX CONTROL
    // Increment or decrement in the index
    public void incrementIndex(int amt){
        if(amt == 0){
            return;
        }
        if (!indexIsMax()) {
            this.currentIndex++;
            incrementIndex(amt-1);
        }
    }
    public void decrementIndex(int amt){
        if(amt == 0){
            return;
        }
        if (!indexIsZero()){
            this.currentIndex--;
            decrementIndex(amt-1);
        }
    }
    public void resetIndex(){
        this.currentIndex = 0;
    }

    // CHECKS
    // Check if our index is at any bounds
    public boolean indexIsZero(){
        return indexIsNum(0);
    }
    public boolean indexIsMax(){
        return indexIsNum(size());
    }
    public boolean indexIsNum(int num){
        return this.currentIndex == num;
    }


}
