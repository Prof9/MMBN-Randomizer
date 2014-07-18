package rand;

public interface DataProducer<T> {
    public T readFromStream(ByteStream stream);
    
    public void writeToStream(ByteStream stream, T data);
}