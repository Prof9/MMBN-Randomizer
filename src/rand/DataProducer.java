package rand;

public interface DataProducer<T> {
    public String getDataName();
    
    public T readFromStream(ByteStream stream);
    
    public void writeToStream(ByteStream stream, T data);
}