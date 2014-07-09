package rand.lib;

import java.util.ArrayList;
import java.util.List;
import rand.ByteStream;

public abstract class Library<T> {
    /** The pointers of the elements contained in this library. */
    protected final ArrayList<Integer> ptrs;
    /** The elements contained in this library. */
    protected final ArrayList<T> elements;
    
    /**
     * Constructs a new library with no registered elements.
     */
    protected Library() {
        this.ptrs = new ArrayList<>();
        this.elements = new ArrayList<>();
    }
    
    /**
     * Gets the amount of elements currently contained in this library.
     * 
     * @return The amount of elements.
     */
    public final int size() {
        return this.elements.size();
    }
    
    /**
     * Adds the given element to the library.
     * 
     * @param ptr The element pointer.
     * @param element The element.
     */
    public final void addElement(int ptr, T element) {
        this.ptrs.add(ptr);
        this.elements.add(element);
    }
    
    /**
     * Loads an element from the given byte stream and adds it to the library.
     * 
     * @param stream The byte stream to load from.
     */
    public final void addElement(ByteStream stream) {
        // Load the element from the ROM.
        int ptr = stream.getPosition();
        T element = loadFromStream(stream);
        
        // Add the chip to the library.
        addElement(ptr, element);
    }
    
    protected abstract T loadFromStream(ByteStream stream);
    
    /**
     * Gets all elements with the given indices.
     * 
     * @param indices The list of indices.
     * @return The list of elements.
     */
    public final List<T> makeList(List<Integer> indices) {
        ArrayList<T> result = new ArrayList<>(indices.size());
        
        for (int i : indices) {
            result.add(getElement(i));
        }
        
        return result;
    }
    
    /**
     * Gets all elements in this library.
     * 
     * @return The list of elements.
     */
    public final List<T> elements() {
        return this.elements;
    }
    
    /**
     * Gets the index of the element with the given pointer.
     * 
     * @param ptr The pointer.
     * @return The element index.
     */
    public final int getIndexFromPointer(int ptr) {
        return this.ptrs.indexOf(ptr);
    }
    
    /**
     * Gets the element with the given index.
     * 
     * @param index The index.
     * @return The element.
     */
    public final T getElement(int index) {
        return this.elements.get(index);
    }
    
    /**
     * Sets the element with the given index.
     * 
     * @param index The index.
     * @param element The new element.
     */
    public final void setElement(int index, T element) {
        this.elements.set(index, element);
    }
}
