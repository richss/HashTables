/**
 * CS 315
 *
 * Generic Keyed Item class to store objects with their key.
 *
 * @author Dr. Richard S. Stansbury
 *
 */
public class KeyedItem<T>
        implements Comparable<KeyedItem<T>> {

    public Comparable key;
    public T item;
    boolean valid;

    /**
     * Creates a keyed item
     * @param key - key for the item
     * @param item - item to be stored.
     */
    public KeyedItem(Comparable key,
                     T item)
    {
        this.key = key;
        this.item = item;
        this.valid = true;
    }

    /**
     * Stores keyed item with integer as a key.
     *
     * @param key - key
     * @param item - item
     */
    public KeyedItem(int key,
                     T item)
    {
        this(new Integer(key), item);
    }

    /**
     * Returns the item.
     * @return item being stored in the tree.
     */
    public Object getItem()
    {
        return item;
    }

    /**
     * Returns the key
     * @return key to store or order data.
     */
    public Comparable getKey()
    {
        return key;
    }


    /**
     * Compares keyed item with other item.
     * @param other
     * @return -1 if item is less, 0 if same, 1 if greater
     */
    public int compareTo(KeyedItem<T> other)
    {
        if (other instanceof KeyedItem)
            return key.compareTo(other.getKey());
        else {
            System.out.println("Invalid Comaprison");
            return 0;
        }
    }


    /**
     * Returns a string of the keyed item.
     *
     * Therefore you may do System.out.println(keyedItemVar);
     */
    public String toString()
    {
        return key.toString() + " => " + item.toString();
    }

    public boolean isValid()
    {
        return valid;
    }

    public void setInvalid()
    {
        valid = false;
    }


}
