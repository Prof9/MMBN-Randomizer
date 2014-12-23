package rand.strat;

import rand.StreamStrategy;
import rand.ByteStream;
import java.util.ArrayList;

/**
 * A strategy that processes a list of pointers of a predetermined length and
 * applies a delegate strategy to each of them.
 */
public class PointerListStrategy implements StreamStrategy {
    private final StreamStrategy strategy;
    private final int basePointer;
    private final int length;
    private final boolean repeat;
    
    /**
     * Constructs a new ProcessPointerListStrategy with the given delegate
     * strategy and pointer list length that does not process repeated pointers
     * twice.
     * 
     * @param strategy The delegate strategy to use.
     * @param length The length of the pointer list.
     */
    public PointerListStrategy(final StreamStrategy strategy, int length) {
        this(strategy, length, false);
    }
    
    /**
     * Constructs a new ProcessPointerListStrategy with the given delegate
     * strategy and pointer list length that optionally can process repeated
     * pointers multiple times.
     * 
     * @param strategy The delegate strategy to use.
     * @param length The length of the pointer list.
     * @param repeat Whether repeated pointers should be processed again.
     */
    public PointerListStrategy(final StreamStrategy strategy, int length,
            boolean repeat) {
        this(strategy, length, 0, repeat);
    }
    
    /**
     * Constructs a new ProcessPointerListStrategy with the given delegate
     * strategy, base pointer and pointer list length that does not process
     * repeated pointers twice.
     * 
     * @param strategy The delegate strategy to use.
     * @param length The length of the pointer list.
     * @param basePtr The base pointer to use.
     */
    public PointerListStrategy(final StreamStrategy strategy, int length,
            int basePtr) {
        this(strategy, length, basePtr, false);
    }
    
    /**
     * Constructs a new ProcessPointerListStrategy with the given delegate
     * strategy, base pointer and pointer list length that optionally can
     * process repeated pointers multiple times.
     * 
     * @param strategy The delegate strategy to use.
     * @param length The length of the pointer list.
     * @param basePointer The base pointer to use.
     * @param repeat Whether repeated pointers should be processed again.
     */
    public PointerListStrategy(final StreamStrategy strategy, int length,
            int basePointer, boolean repeat) {
        this.repeat = repeat;
        this.length = length;
        this.strategy = strategy;
        this.basePointer = basePointer;
    }
    
    /**
     * Gets the preset length of the pointer list to be processed.
     * 
     * @return The length of the pointer list.
     */
    public int length() {
        return this.length;
    }
    
    /**
     * Gets a boolean that indicates whether this strategy processes pointers
     * multiple times when they are repeated in the pointer list.
     * 
     * @return Whether repeated pointers are processed again.
     */
    public boolean repeatsPointers() {
        return this.repeat;
    }
    
    /**
     * Processes the pointer list at the current position in the given byte
     * stream.
     * 
     * @param stream The byte stream to execute on.
     */
    @Override
    public void execute(ByteStream stream) {
        ArrayList<Integer> processed = new ArrayList<>();
        
        int next;
        for (int i = 0; i < this.length; i++) {
            next = stream.readInt32();
            if (repeat || !processed.contains(next)) {
                stream.push();
                stream.setPosition(basePointer + next);
                
                this.strategy.execute(stream);
                
                stream.pop();
                processed.add(next);
            }
        }
    }
}
