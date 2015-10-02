package vandy.cs251;

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
    private char[] mArray;

    /**
     * The current size of the array.
     */
    // TODO - you fill in here
    int mSize;

    /**
     * Default value for elements in the array.
     */
    // TODO - you fill in here
    // @@ you don't need this
    char mDefaultVal;

    /**
     * Constructs an array of the given size.
     *
     * @param size Non-negative integer size of the desired array.
     */
    public CharArray(int size) {
        // TODO - you fill in here
        mSize = size;
        mArray = new char[mSize];
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
        this(size);
        mDefaultVal = mDefaultvalue;
        Arrays.fill(mArray, mDefaultVal);
    }

    /**
     * Copy constructor; creates a deep copy of the provided CharArray.
     *
     * @param s The CharArray to be copied.
     */
    public CharArray(CharArray s) {
        // TODO - you fill in here
	// @@ This is inefficient - you should be using Arrays.copyOf():
        mArray = Arrays.copyOf(s.mArray, s.size());
        mSize = s.size();
        mDefaultVal = s.mDefaultVal;
    }

    /**
     * Creates a deep copy of this CharArray.  Implements the
     * Prototype pattern.
     */
    @Override
    public Object clone() {
        // TODO - you fill in here (replace return null with right implementation).
        // @@ Please just delegate to the copy constructor
        return new CharArray(this);
    }

    /**
     * @return The current size of the array.
     */
    public int size() {
        // TODO - you fill in here (replace return 0 with right implementation).
    	return this.mSize;
    }

    /**
     * @return The current maximum capacity of the array.
     */
    public int capacity() {
        // TODO - you fill in here (replace return 0 with right implementation).
        return mArray.length;
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
        // @@ Please restructure & remove duplicate code.
        if (size > mSize) {
            if (size > capacity()) {
                mArray = Arrays.copyOf(mArray, size);
            }
            Arrays.fill(mArray, mSize, size, mDefaultVal);
        }

        mSize = size;
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
        return mArray[index];
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
        mArray[index] = value;
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
        // @@ You should be comparing content first.
        // @@ This is inefficient - just use subtraction:

        for (int i = 0; i < Math.min(mSize, s.mSize); i++) {
            int difference = mArray[i] - s.mArray[i];
            if (difference != 0) {
                return difference;
            }
        }

        return mSize - s.mSize;
    }

    /**
     * Throws an exception if the index is out of bound.
     */
    private void rangeCheck(int index) {
        // TODO - you fill in here
        if(index > mSize -1){
            throw new ArrayIndexOutOfBoundsException();
        }
    }
}
