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
    // @@ You don't need this
    int myCapacity;

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
        //call the second constructor from here with mDefaultvalue = '\u0000' ?
        mySize = size;
        //setting the capacity to the original allocated size
        myCapacity = size;
        myArray = new char[mySize];
	// @@ you don't need this
        myDefaultVal = '\u0000';
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
	// @@ Make sure to delegate to the other constructor
        mySize = size;
        myCapacity = size;
        myArray = new char[mySize];
        myDefaultVal = mDefaultvalue;
        Arrays.fill(myArray, myDefaultVal);
    }

    /**
     * Copy constructor; creates a deep copy of the provided CharArray.
     *
     * @param s The CharArray to be copied.
     */
    public CharArray(CharArray s) {
        // TODO - you fill in here
	// @@ This is inefficient - you should be using Arrays.copyOf():
        mySize = s.size();
        myCapacity = s.size();
        myArray = new char[mySize];
	// @@ This doesn't work like you expect it to.
        s.clone();
    }

    /**
     * Creates a deep copy of this CharArray.  Implements the
     * Prototype pattern.
     */
    @Override
    public Object clone() {
        // TODO - you fill in here (replace return null with right implementation).
	// @@ Please just delegate to the copy constructor
        CharArray prototype = new CharArray(this.mySize);
        prototype.myCapacity = this.mySize;
        prototype.myArray = this.myArray;
        return prototype;

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
    	return myCapacity;
        //this is the incorrect way of doing it
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
	// Please restructure & remove duplicate code. 
        if(size == myCapacity){
           // myArray = new char[size];
            if(size != 0){
                myDefaultVal = myArray[0];
            }
            Arrays.fill(myArray, myDefaultVal);
            mySize = size;
        }
        else if(size < myCapacity){
            //myDefaultVal =
            Arrays.fill(myArray, myDefaultVal);
            mySize = size;
            //myCapacity is unchanged?
        }
        else if(size > myCapacity){
            myCapacity = size;
            mySize = size;
            char[] b;
            b = myArray;
            myArray = new char[myCapacity];
            myArray = Arrays.copyOf(b,myCapacity);
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
    	//return Array.getChar(myArray ,index);
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
        // this is just the size portion here, the comparing of the characters has to be added
	// @@ You should be comparing content first.
	// @@ This is inefficient - just use subtraction:
        if(s.size() > mySize){
            return -1;
        }
        else if(s.size() < mySize){
            return 1;
        }
        else{
            //attempting to loop through arrays and compare each char
            //theoretically the sizes are the same once they get here
            char a, b;
            for(int i = 0; i < mySize; i++){
		// @@ This is inefficient - just use subtraction:
                a = this.myArray[i];
               // char []temp = s.myArray;
               // b = Array.getChar(temp,i);
                b = s.myArray[i];

		// @@ Please remove they debugging print
                System.out.println("a>>b = " + a + ">>" + b + "(i = " + i + ")");
                if(a < b){
                    //debug
                    System.out.println("less than");
                    result = 1;
                    break;
                }
                else if(a > b){
                    //debug
                    System.out.println("greater than");
                    result = -1;
                    break;
                }
                else{
                    //bebug
                    System.out.println("equal to");
                    //what goes here?
                }
            }
            System.out.println("Reached the end of the loop");
            System.out.println("res: " + result);
            return result;
        }
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
