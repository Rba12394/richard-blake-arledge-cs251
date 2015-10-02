package vandy.cs251;

import java.lang.ArrayIndexOutOfBoundsException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Provides a generic dynamically-(re)sized array abstraction.
 */
public class Array<T extends Comparable<T>>
             implements Comparable<Array<T>>,
                        Iterable<T>,
                        Cloneable {
    /**
     * The underlying array of type T.
     */
    // TODO - you fill in here.
   private T[] mArray;

    /**
     * The current size of the array.
     */
    // TODO - you fill in here.
    int mSize;


    /**
     * Default value for elements in the array.
     */
    // TODO - you fill in here.
    private T mDefaultvalue;

    /**
     * Constructs an array of the given size.
     * @param size Nonnegative integer size of the desired array.
     * @throws NegativeArraySizeException if the specified size is
     *         negative.
     */
    @SuppressWarnings("unchecked")
    public Array(int size) {
        // TODO - you fill in here.
        mSize = size;
        mArray = (T[]) new Comparable[size];
    }

    /**
     * Constructs an array of the given size, filled with the provided
     * default value.
     * @param size Nonnegative integer size of the desired array.
     * @param mDefaultvalue A default value for the array.
     * @throws NegativeArraySizeException if the specified size is
     *         negative.
     */
    @SuppressWarnings("unchecked")
    public Array(int size,
                 T defaultValue) {
        // TODO - you fill in here.
        this(size);
        mDefaultvalue = defaultValue;
        Arrays.fill(mArray, mDefaultvalue);
    }

    /**
     * Copy constructor; creates a deep copy of the provided array.
     * @param s The array to be copied.
     */
    @SuppressWarnings("unchecked")
    public Array(Array<T> s) {
        // TODO - you fill in here.
        mArray = Arrays.copyOf(s.mArray, s.size());
        mSize = s.size();
        mDefaultvalue = s.mDefaultvalue;
    }

    /**
     * Creates a deep copy of this Array.  Implements the
     * Prototype pattern.
     */
    @Override
    public Object clone() {
        // TODO - you fill in here (replace null with proper return value).
        return new Array(this);
    }

    /**
     * @return The current size of the array.
     */
    public int size() {
        // TODO - you fill in here (replace 0 with proper return
        // value).
        return this.mSize;
    }

    /**
     * @return The current maximum capacity of the array withough
     */
    public int capacity() {
        // TODO - you fill in here (replace 0 with proper return value).
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
        // TODO - you fill in here.

        if (size > mSize) {
            if (size > capacity()) {
                mArray = Arrays.copyOf(mArray, size);
            }
            Arrays.fill(mArray, mSize, size, mDefaultvalue);
        }

        mSize = size;

    }

    /**
     * @return the element at the requested index.
     * @param index Nonnegative index of the requested element.
     * @throws ArrayIndexOutOfBoundsException If the requested index is outside the
     * current bounds of the array.
     */
    public T get(int index) {
        // TODO - you fill in here (replace null with proper return value).
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
    public void set(int index, T value) {
        // TODO - you fill in here.
        rangeCheck(index);
        mArray[index] = value;
    }

    /**
     * Removes the element at the specified position in this Array.
     * Shifts any subsequent elements to the left (subtracts one from their
     * indices).  Returns the element that was removed from the Array.
     *
     * @throws ArrayIndexOutOfBoundsException if the index is out of range.
     * @param index the index of the element to remove
     * @return element that was removed
     */
    public T remove(int index) {
        // TODO - you fill in here (replace null with proper return value).
        rangeCheck(index);
        mArray[index] = null;
        for(int i = 0; i < this.mSize; i++){
            if(mArray[i].equals(null)){
                mArray[i]=mArray[i+1];
            }
        }
        return mArray[index];
    }

    /**
     * Compares this array with another array.
     * <p>
     * This is a requirement of the Comparable interface.  It is used to provide
     * an ordering for Array elements.
     * @return a negative value if the provided array is "greater than" this array,
     * zero if the arrays are identical, and a positive value if the
     * provided array is "less than" this array.
     */
    @Override
    public int compareTo(Array<T> s) {
        // TODO - you fill in here (replace 0 with proper return value).

        for (int i = 0; i < Math.min(mSize,s.mSize); i++){
            int diff = mArray[i].compareTo(s.mArray[i]);
            if (diff != 0){
                return diff;
            }
        }

        return mSize - s.mSize;
    }

    /** 
     * Throws an exception if the index is out of bound. 
     */
    private void rangeCheck(int index) {
        // TODO - you fill in here.
        if(index > this.mSize-1){
            throw new ArrayIndexOutOfBoundsException();
        }
    }

    public class ArrayIterator 
           implements java.util.Iterator<T> {
        // Current position in the Array (defaults to 0).
        // TODO - you fill in here.
        private int current = 0;

        // Index of last element returned; -1 if no such element.
        // TODO - you fill in here.
        private int lastReturned;

        /**
         * @return True if the iteration has more elements that
         * haven't been iterated through yet, else false.
         */
        @Override
        public boolean hasNext() {
        // TODO - you fill in here (replace false with proper boolean
        // expression).
            return (current < mArray.length);
        }

        /**
         * @return The next element in the iteration.
         */
        @Override
        public T next() {
        // TODO - you fill in here (replace null with proper return value).
            if(hasNext() == false){
                throw new NoSuchElementException();
            }
            lastReturned = current++;
            return mArray[lastReturned];
        }

        /**
         * Removes from the underlying collection the last element
         * returned by this iterator. This method can be called only
         * once per call to next().
         * @throws IllegalStateException if no last element was
         * returned by the iterator.
         */
        @Override
        public void remove() {
            // TODO - you fill in here
            if(lastReturned == 0){
                throw new IllegalStateException();
            }
            if(current >= 0 && current <= mArray.length){
                mArray[current-1] = null ;
            }
        }
    }

    /**
     * Factory method that returns an Iterator.
     */
    public Iterator<T> iterator() {
        // TODO - you fill in here (replace null with proper return value).
        return this.iterator();
    }
}
