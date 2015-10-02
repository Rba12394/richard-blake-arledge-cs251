package vandy.cs251;

import java.lang.reflect.Array;
import java.lang.*;
import java.util.Arrays;

/**
 * Provides a wrapper facade around primitive char arrays, allowing
 * for dynamic resizing.
 */
public class CharArray implements Comparable<CharArray>,
                                  Cloneable {

    /**
     * The underlying array.
     */
    // TODO - you fill in here
    char[] myArray;

    /**
     * The current size of the array.
     */
    // TODO - you fill in here
    int mySize;


    /**
     * Default value for elements in the array.
     */
    // TODO - you fill in here
    char myDefaultVal;

    /**
     * Constructs an array of the given size.
     *
     * @param size Non-negative integer size of the desired array.
     */
    public CharArray(int size) {
        // TODO - you fill in here
        mySize = size;
        myArray = new char[mySize];
        //null char value
        Arrays.fill(myArray, myDefaultVal);
    }

    /**
     * Constructs an array of the given size, filled with the provided
     * default value.
     *
     * @param size Nonnegative integer size of the desired array.
     * @param mDefaultvalue A default value for the array.
     */
    public CharArray(int size,
                     char mDefaultvalue) {
        // TODO - you fill in here
        myArray = new char[size];
        myDefaultVal = mDefaultvalue;
    }

    /**
     * Copy constructor; creates a deep copy of the provided CharArray.
     *
     * @param s The CharArray to be copied.
     */
    public CharArray(CharArray s) {
        // TODO - you fill in here
        Arrays.copyOf(s.myArray, s.size());
    }

    /**
     * Creates a deep copy of this CharArray.  Implements the
     * Prototype pattern.
     */
    @Override
    public Object clone() {
        // TODO - you fill in here (replace return null with right implementation).
        CharArray temp = new CharArray(this);
        return temp;

    }

    /**
     * @return The current size of the array.
     */
    public int size() {
        // TODO - you fill in here (replace return 0 with right implementation).
    	return this.mySize;
    }

    /**
     * @return The current maximum capacity of the array.
     */
    public int capacity() {
        // TODO - you fill in here (replace return 0 with right implementation).
        int cap = this.size();
        return cap;
    }

    /**
     * Resizes the array to the requested size.
     *
     * Changes the capacity of this array to hold the requested number of elements.
     * Note the following optimizations/implementation details:
     * <ul>
     *   <li> If the requests size is smaller than the current maximum capacity, new memory
     *   is not allocated.
     *   <li> If the array was constructed with a default value, it is used to populate
     *   uninitialized fields in the array.
     * </ul>
     * @param size Nonnegative requested new size.
     */
    public void resize(int size) {
        // TODO - you fill in here
        if(size == mySize){
           // myArray = new char[size];
            if(size != 0){
                myDefaultVal = myArray[0];
            }
            Arrays.fill(myArray, myDefaultVal);
            mySize = size;
        }
        else if(size < mySize){
            Arrays.fill(myArray, myDefaultVal);
            mySize = size;
        }
        else{
            mySize = size;
            char[] b = myArray;
            myArray = new char[mySize];
            myArray = Arrays.copyOf(b,mySize);
            mySize = size;
        }
    }

    /**
     * @return the element at the requested index.
     * @param index Nonnegative index of the requested element.
     * @throws ArrayIndexOutOfBoundsException If the requested index is outside the
     * current bounds of the array.
     */
    public char get(int index) {
        // TODO - you fill in here (replace return '\0' with right implementation).
        rangeCheck(index);
        return myArray[index];
    }

    /**
     * Sets the element at the requested index with a provided value.
     * @param index Nonnegative index of the requested element.
     * @param value A provided value.
     * @throws ArrayIndexOutOfBoundsException If the requested index is outside the
     * current bounds of the array.
     */
    public void set(int index, char value) {
        // TODO - you fill in here
        rangeCheck(index);
        Array.setChar(myArray,index,value);
    }

    /**
     * Compares this array with another array.
     * <p>
     * This is a requirement of the Comparable interface.  It is used to provide
     * an ordering for CharArray elements.
     * @return a negative value if the provided array is "greater than" this array,
     * zero if the arrays are identical, and a positive value if the
     * provided array is "less than" this array. These arrays should be compred
     * lexicographically.
     */
    @Override
    public int compareTo(CharArray s) {
        // TODO - you fill in here (replace return 0 with right implementation).
        int result = 0;
        char a, b;
        // this is just the size portion here, the comparing of the characters has to be added
        if(mySize == s.size()) {
            for (int i = 0; i < mySize; i++) {
                a = this.myArray[i];
                b = s.myArray[i];
                if (a < b) {
                    result = 1;
                    break;
                } else if (a > b) {
                    result = -1;
                    break;
                }
            }
        }
        else{
            //attempting to loop through arrays and compare each char
            //theoretically the sizes are the same once they get here
            result = mySize - s.size();
            }
            return result;
        }


    /**
     * Throws an exception if the index is out of bound.
     */
    private void rangeCheck(int index) {
        // TODO - you fill in here
        if(index > mySize-1){
            throw new ArrayIndexOutOfBoundsException();
        }

    }

}
