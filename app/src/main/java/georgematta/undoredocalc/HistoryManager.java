package georgematta.undoredocalc;

import java.util.ArrayList;
import java.util.List;

/**
 * This is the HistoryManager class, which consists of an ArrayList of entry strings and
 * a undo-redo index.
 * The ArrayList gets populated as users interact with the Undo-Redo calculator and adds
 * "Operator" + "Operand" entries to the history ArrayList.
 *
 * As the user interacts with the entries the undo-redo pointer
 * is affected accordingly.
 *
 * There are also methods to check if the index is at any common bounds
 *
 * @author George Matta
 * @version 1.0
 */
public class HistoryManager{

    /**
     * This will be a list of Strings of the entries "<operator><operand>".
     * We could think of this string as an Entry object with an Entry.operator and Entry.operand
     * but having it as a string works well.
     */
    private ArrayList<String> history;
    /**
     * The int denoting the current index at which the undo-redo is targeting.
     */
    private int currentIndex;

    /**
     * Creates a HistoryManager object and initializes the ArrayList to be empty and
     * the currentIndex to be 0.
     */
    public HistoryManager(){
        this.history = new ArrayList<>();
        this.currentIndex = 0;
    }

    // GETTERS

    /**
     * A getter method to retrieve the current index.
     *
     * @return The index that we are pointing at with regards to undo-redo
     */
    public int getCurrentIndex(){
        return this.currentIndex;
    }

    /**
     * A getter method to return the entirety of the history log.
     *
     * @return An Arraylist of Strings for each entry made by each interaction the user makes
     */
    public ArrayList<String> getHistory(){
        return history;
    }

    /**
     * Since we can only populate a limited amount of GridView buttons, we retrieve a smaller
     * window of history's elements.
     *
     * If the window is larger than the amount of entries we have, we just return all we have.
     *
     * @param window The maximum amount of entries to return from the history.
     * @return A List of Strings obtained by calling subList() on the history ArrayList.
     */
    public List<String> getHistory(int window){
        // Return a subset of the list from the first to the window'th element
        int maxSize = size();
        if (window > maxSize){
            window = maxSize;
        }
        return history.subList(0, window);
    }

    // ARRAYLIST CONTROL

    /**
     * A method which is very similar to the ArrayList get method.
     * We retrieve the element at the requested index.
     *
     * If the index is too large, we return an empty String.
     *
     * @param index The index to get the element from.
     * @return The String at the specified element.
     */
    public String get(int index){
        if (index >= size()){
            return "";
        }
        return history.get(index);
    }

    /**
     * A method to add a given element at the start of the history.
     * This method calls the add(int, String) method with 0 as the index.
     *
     * @param element The String to add to the log.
     */
    public void add(String element){
        add(0, element);
    }

    /**
     * A method to add a String to the specified index.
     * If the index is out of range (determined by the outOfRange() method), we simply exit
     * the method.
     *
     * If the index where we are adding the element to is less than the undo-redo index, we
     * <b>increment</b> the undo-redo index to reflect the addition.
     *
     * @param index The index to add the element to (if the method is called without an index
     *              we default to 0).
     * @param element The element to add to the Arraylist.
     */
    public void add(int index, String element) {
        // If the index is out of range, we don't do anything
        if (outOfRange(index)) return;
        // If the index we are adding is ahead of the undoredo index, we increment it
        if (index <= currentIndex){
            incrementIndex();
        }
        history.add(index, element);
    }
    /**
     * A method to remove an element from the ArrayList with the specified index.
     * If the index is out of range (determined by the outOfRange() method), we simply exit
     * the method.
     *
     * If the index where we are adding the element to is less than the undo-redo index, we
     * <b>decrement</b> the undo-redo index to reflect the addition.
     *
     * @param index The index to remove the element from.
     */
    public void remove(int index){
        // If the index is out of range, we don't do anything
        if (outOfRange(index, true)) return;
        // If the index we are popping is ahead of the undoredo index, we decrement it
        if (index <= currentIndex) {
            decrementIndex();
        }
        history.remove(index);
    }

    /**
     * A method to retrieve the size of the full history log.
     *
     * @return An int denoting the size of the history ArrayList.
     */
    public int size(){
        return history.size();
    }

    // INDEX CONTROL
    /**
     * A base function to increment the index by 1.
     */
    public void incrementIndex(){
        incrementIndex(1);
    }

    /**
     * A base function to decrement the index by 1.
     */
    public void decrementIndex(){
        decrementIndex(1);
    }

    /**
     * A method to increment the index by a specified amount.
     * This method is recursive in nature. Our base case is checking if the amt is 0, otherwise, we
     * increment the index by 1 and decrease the amt by 1.
     *
     * We also check if the index isMax as to not reach an index that is out of bounds.
     *
     * @param amt An int denoting how many times to increment the index.
     */
    public void incrementIndex(int amt) {
        if (amt == 0) {
            return;
        }
        if (!indexIsMax()) {
            this.currentIndex++;
            incrementIndex(amt - 1);
        }
    }

    /**
     * A method to decrement the index by a specified amount.
     * This method is recursive in nature. Our base case is checking if the amt is 0, otherwise, we
     * decrement the index by 1 and decrease the amt by 1.
     *
     * We also check if the index isZero as to not reach an index that is out of bounds.
     *
     * @param amt An int denoting how many times to decrement the index.
     */
    public void decrementIndex(int amt){
        if(amt == 0){
            return;
        }
        if (!indexIsZero()){
            this.currentIndex--;
            decrementIndex(amt-1);
        }
    }

    /**
     * A method to reset the index back to 0.
     */
    public void resetIndex(){
        this.currentIndex = 0;
    }

    // CHECKS

    /**
     * A method to check if the index is equal to 0.
     *
     * @return A boolean denoting if the index is 0.
     */
    public boolean indexIsZero(){
        return indexIsNum(0);
    }

    /**
     * A method to return if the index is equal to the maximum size of the Arraylist.
     *
     * @return A boolean denoting if the index is equal to the size of the ArryayList.
     */
    public boolean indexIsMax(){
        return indexIsNum(size());
    }

    /**
     * A method to return if the index is equal to a given number.
     *
     * @param num The number to check if the index is equal to.
     * @return A boolean denoting if the currentIndex is equal to num.
     */
    public boolean indexIsNum(int num){
        return this.currentIndex == num;
    }

    /**
     * A method to return if the index given is out of range.
     * This method delegates to the outOfRange(int, boolean) method, so users can choose to
     * check if an index is outOfRange, but forgo checking if the ArrayList is populated with any
     * elements (checking if the size is 0).
     *
     * @param index The index to check if it's out of range.
     * @return A boolean denoting if the index passed is out of range.
     */
    public boolean outOfRange(int index){
        return outOfRange(index, false);
    }

    /**
     * A method to return if the index given is out of range.
     *
     * If the method is called without checkList, we default to false as per outOfRange(int).
     *
     * We have three separate conditions to check for:
     * <p>
     * If the list exists (optional, based on checkList)
     * <p>
     * If the index is negative (less than 0)
     * <p>
     * If the index is >= the size of the ArrayList (too large)
     *
     * @param index The index to check if it's out of range.
     * @param checkList A boolean denoting if we are to check if a the list exists.
     *                  Users may want to ignore this check if they are adding to the list.
     *                  You can add to an empty list, but you can't remove from it.
     * @return A boolean denoting if the list is empty && (index is negative || too large).
     */
    public boolean outOfRange(int index, boolean checkList){
        int size = size();
        boolean listExists;
        boolean negativeIndex = index < 0;
        boolean largeIndex = index >= size;
        // We permit the list to be empty if we are adding onto it
        if(checkList) {
            listExists = !isEmpty();
        } else {
            listExists = true;
        }
        // There is no list && (negative index || index too large)
        return !listExists && (negativeIndex || largeIndex);
    }

    /**
     * A method to check if the list is empty.
     *
     * @return A boolean denoting if the size() is 0.
     */
    public boolean isEmpty(){
        return size() == 0;
    }

}
