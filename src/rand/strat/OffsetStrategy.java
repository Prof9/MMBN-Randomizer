package rand.strat;

import rand.StreamStrategy;
import rand.ByteStream;

/**
 * A strategy that modifies the position of the ROM relative to its current
 * position.
 */
public class OffsetStrategy implements StreamStrategy {
	private final StreamStrategy strategy;
	private final int before;
	private final int after;

	/**
	 * Constructs a new OffsetStrategy that advances the position of the ROM by
	 * the given amount, then executes the given strategy.
	 *
	 * @param strategy The delegate strategy to use.
	 * @param before The amount to advance the position of the ROM.
	 */
	public OffsetStrategy(final StreamStrategy strategy, int before) {
		this(strategy, before, 0);
	}

	/**
	 * Constructs a new OffsetStrategy that advances the position of the ROM by
	 * the given amount, executes the given strategy, then advances the position
	 * of the ROM by the other given amount.
	 *
	 * @param strategy The delegate strategy to use.
	 * @param before The amount to advance the position of the ROM before
	 * executing the delegate strategy.
	 * @param after The amount to advance the position of the ROM after
	 * executing the delegate strategy.
	 */
	public OffsetStrategy(final StreamStrategy strategy, int before,
			int after) {
		this.strategy = strategy;
		this.before = before;
		this.after = after;
	}

	/**
	 * Process the given byte stream.
	 *
	 * @param stream The byte stream to execute on.
	 */
	@Override
	public void execute(ByteStream stream) {
		stream.advance(this.before);
		this.strategy.execute(stream);
		stream.advance(this.after);
	}
}
