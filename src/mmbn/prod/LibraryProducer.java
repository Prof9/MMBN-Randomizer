package mmbn.prod;

import rand.ByteStream;
import rand.DataProducer;
import rand.Library;

public abstract class LibraryProducer<T> implements DataProducer<T> {
	protected final Library<T> library;

	public LibraryProducer(final Library<T> library) {
		this.library = library;
	}

	@Override
	public final T readFromStream(ByteStream stream) {
		int ptr = stream.getPosition();
		T data = deferredReadFromStream(stream);
		this.library.addElement(ptr, data);
		return data;
	}

	protected abstract T deferredReadFromStream(ByteStream stream);
}
