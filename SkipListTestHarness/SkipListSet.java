import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.SortedSet;
import java.util.Random;
import java.util.Set;

import org.w3c.dom.Node;
import java.util.Objects;

public class SkipListSet<T extends Comparable<T>> implements SortedSet<T> {

    Collection<SkipListSetItem<T>> list = new LinkedList<>();
    SkipListSetItem<T> head;
    int maxHeight = 8;
    int size;
    Random random;

    @Override
    public Iterator<T> iterator() {
        return new SkipListSetIterator();
    }

    private class SkipListSetIterator implements Iterator<T> {

        SkipListSetItem<T> current;//the current iterator item
        SkipListSetItem<T> lastReturned; //used for remove < V
        boolean canRemove;

        //constructor, starts the iteration with the first existing value
        public SkipListSetIterator() {
            this.current = head.next[0];
            this.lastReturned = null;
            this.canRemove = false;
        }

        //does the iterator have a next node to traverse to
        @Override
        public boolean hasNext() {
            return current != null;
        }

        //goes to the next item in the iteration
        //updates some pointers
        @Override
        public T next() {
            if (!hasNext())
                throw new NoSuchElementException();
            T value = current.value;
            lastReturned = current;
            current = current.next[0];
            canRemove = true;
            return value;
        }

        //removes an item from the iteration and the skiplistset
        //updates some pointers to let us know that value is no longer able to be removed
        @Override
        public void remove() {
            if (!canRemove || lastReturned == null) {
                throw new IllegalStateException("remove() is not valid here.");
            }
            SkipListSet.this.remove(lastReturned.value);
            lastReturned = null;
            canRemove = false;
        }

    }

    public int hashCode() {
        int hash = 0;
        for (T element : this) {
            hash += element.hashCode();
        }
        return hash;
    }

    private class SkipListSetItem<T extends Comparable<T>> {

        T value = null;
        SkipListSetItem<T>[] next;
        int height;

        // constructor with value and level
        public SkipListSetItem(T value, int height) {
            this.value = value;
            this.next = new SkipListSetItem[height];
            this.height = height;
        }

        // helper methods below. may not be used...
        public void setValue(T value) {
            this.value = value;
        }

        public T getValue() {
            return value;
        }

        public void nullifyValue() {
            value = null;
        }

        public void setHeight(int height) {
            this.height = height;
        }

        public int getHeight() {
            return height;
        }

        public int compareTo(T otherValue) {
            return this.value.compareTo(otherValue);
        }
    }

    // creates an empty skip list
    public SkipListSet() {
        this.head = new SkipListSetItem<>(null, maxHeight);
        this.size = 0;
        this.random = new Random();
    }

    //called as more nodes are added to the skip list
    //we make the max height base log 2 of the number of nodes in the skip list
    //calls the updateHeadNext function to make our head node the height of the
    //new max height calculated
    public int maxHeight() {
        int nodeCount = size();
        int newHeight = nodeCount == 0 ? 0 : (int) (Math.log(nodeCount) / Math.log(2));
        updateHeadNext(newHeight);
        return newHeight;
    }

    //called when the max height is updated
    //we make the null head node the height of the new max height
    //and reassign next pointers
    private void updateHeadNext(int newHeight) {
        if(newHeight > head.height){
            SkipListSetItem<T>[] newNext = Arrays.copyOf(head.next, newHeight);
            Arrays.fill(newNext, head.height, newHeight, null);
            head.next = newNext;
            head.height = newHeight;
        }
    }

    //get the predecessor item of the value passed,
    //and return the predecessor array height with its next pointers
    //used for removing values so we can easily reassign next pointers
    //and adding so we can reassign next pointers to the value added
    @SuppressWarnings("unchecked")
    public SkipListSetItem<T>[] getPredecessor(T value) {
        SkipListSetItem<T>[] predecessors = new SkipListSetItem[maxHeight];
        SkipListSetItem<T> current = head;

        for (int level = maxHeight - 1; level >= 0; level--) {
            while (current.next[level] != null && current.next[level].compareTo(value) < 0) {
                current = current.next[level];
            }
            predecessors[level] = current;
        }

        return predecessors;
    }

    // generate a random height for a new item being added to the skip list
    public int generateRandomHeight() {
        int level = 1;
        while (random.nextDouble() < 0.5 && level < maxHeight) {
            level++;
        }

        return level;
    }

    // return a new skip list containing the elements in the collection
    public SkipListSet(Collection<T> collection) {
        this();
        addAll(collection);
    }

    //returns the size of the skiplist
    @Override
    public int size() {
        return size;
    }

    //returns true if the skiplist is empty
    @Override
    public boolean isEmpty() {
        if (size == 0)
            return true;
        return false;
    }

    //returns true if the element passed exists in the skip list
    @Override
    @SuppressWarnings("unchecked")
    public boolean contains(Object o) {
        if (!(o instanceof Comparable))
            return false;
        try {
            T value = (T) o;
            SkipListSetItem<T> node = getPredecessor(value)[0].next[0];
            return node != null && node.value.compareTo(value) == 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    //returns an object array for the elements in the skiplist
    @Override
    public Object[] toArray() {
        Object[] result = new Object[size()];
        int i = 0;
        for (T element : this) {
            result[i++] = element;
        }
        return result;
    }

    //converts an arraylist to an array, used for making next pointers
    //for items a much easier task
    @Override
    @SuppressWarnings("unchecked")
    public Object[] toArray(Object[] array) {
        int size = size();
        if (array.length < size) {
            array = Arrays.copyOf(array, size);
        }

        int i = 0;
        for (T element : this) {
            array[i++] = element;
        }

        if (i < array.length) {
            array[i] = null;
        }

        return array;
    }

    //adds the passed element into the skipList
    //assigns next pointers for the predecessor and the new item
    @Override
    public boolean add(T value) {
        SkipListSetItem<T>[] predecessors = getPredecessor(value);

        // Check if the predecessors next is the value we want to add ( this mean T
        // value is already present in the skip list)
        if (predecessors[0].next[0] != null && predecessors[0].next[0].value.compareTo(value) == 0)
            return false;

        int valueHeight = generateRandomHeight();
        SkipListSetItem<T> newItem = new SkipListSetItem<>(value, valueHeight);

        for (int level = 0; level < valueHeight; level++) {
            newItem.next[level] = predecessors[level].next[level];
            predecessors[level].next[level] = newItem;
        }

        size++;
        if (maxHeight() > 8)
            maxHeight = maxHeight();
        return true;
    }

    //removes the element passed into the function
    //from the skip list and reassigns next pointers
    //for the predecessor of the passed element
    @Override
    @SuppressWarnings("unchecked")
    public boolean remove(Object o) {
        if (!(o instanceof Comparable))
            return false;
        try {
            T value = (T) o;
            SkipListSetItem<T>[] predecessor = getPredecessor(value);

            SkipListSetItem<T> item = predecessor[0].next[0];
            if (item == null || item.value.compareTo(value) != 0)
                return false;

            for (int level = 0; level < item.next.length; level++) {
                if (predecessor[level].next[level] == item) {
                    predecessor[level].next[level] = item.next[level];
                }
            }

            size--;
            if (maxHeight() > 8)
                maxHeight = maxHeight();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    //returns true if all the elements in the collection
    //are contained in the existing skip list
    @Override
    public boolean containsAll(Collection<?> collection) {
        for (Object element : collection) {
            if (!contains(element)) {
                return false;
            }
        }

        return true;
    }

    //adds all of the elements contained in the collection
    @Override
    public boolean addAll(Collection<? extends T> collection) {
        boolean added = false;

        for (T element : collection) {
            if (add(element)) {
                added = true;
            }
        }
        //System.out.println("Here");
        return added;
    }

    //method that keeps only elements in the existing skiplist
    //that are contained in the specific collection
    @Override
    public boolean retainAll(Collection<?> collection) {
        boolean retained = false;

        Iterator<T> iterator = iterator();
        while (iterator.hasNext()) {
            T element = iterator.next();
            if (!collection.contains(element)) {
                iterator.remove();
                retained = true;
            }
        }

        return retained;
    }

    //Removes all items passed from the skiplist
    //using iterator
    @Override
    public boolean removeAll(Collection<?> collection) {
        boolean removed = false;
        Iterator<T> iterator = iterator();
        while (iterator.hasNext()) {
            T element = iterator.next();
            if (collection.contains(element)) {
                iterator.remove();
                removed = true;
            }
        }

        return removed;
    }

    //clears the skip list set and starts a new one
    @Override
    public void clear() {
        head = new SkipListSetItem<>(null, maxHeight);
        size = 0;
    }

    //returns true if object equals the other object
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Set)) {
            return false;
        }
        Set<?> other = (Set<?>) obj;
        if (other.size() != size) {
            return false;
        }
        return containsAll(other);
    }

    //no need
    @Override
    public Comparator<T> comparator() {
        return null;
    }

    //no need
    @Override
    public SortedSet<T> subSet(T fromElement, T toElement) {
        throw new UnsupportedOperationException("Unimplemented method 'subSet'");
    }

    //no need
    @Override
    public SortedSet<T> headSet(T toElement) {
        throw new UnsupportedOperationException("Unimplemented method 'headSet'");
    }

    //no need
    @Override
    public SortedSet<T> tailSet(T fromElement) {
        throw new UnsupportedOperationException("Unimplemented method 'tailSet'");
    }

    //no need
    @Override
    public T first() {
        return head.next[0].value;
    }

    // no need
    @Override
    public T last() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'last'");
    }

    //resizes all the nodes in the skip list
    //with the help of the resizeItem function
    public void reBalance() {
        int newHeight = maxHeight();
        SkipListSetItem<T> current = head.next[0];
        while (current != null) {
            resizeItem(current, newHeight);
            current = current.next[0];
        }
    }

    //Called from rebalance
    //resizes a node and makes the correct next pointers for the new height
    public void resizeItem(SkipListSetItem<T> item, int newHeight) {
        if (item.height < newHeight) {
            SkipListSetItem<T>[] newNext = Arrays.copyOf(item.next, newHeight);
            Arrays.fill(newNext, item.height, newHeight, null);
            item.next = newNext;
            item.height = newHeight;
        }
    }

}