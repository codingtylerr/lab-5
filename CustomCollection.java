import java.util.ArrayList;
import java.util.Collection;

public interface CustomCollection<E> extends Collection<E> {

    @Override
    default boolean containsAll(Collection<?> c) {
        for (Object element : c) {
            if (!this.contains(element)) {
                return false; // If any element is missing, return false
            }
        }
        return true;
    }

    @Override
    default boolean addAll(Collection<? extends E> c) {
        boolean isChanged = false;
        for (E element : c) {
            if (this.add(element)) {
                isChanged = true; // If at least one element is added, set flag
            }
        }
        return isChanged;
    }

    @Override
    default boolean removeAll(Collection<?> c) {
        boolean isChanged = false;
        for (Object element : c) {
            if (this.remove(element)) {
                isChanged = true; // If at least one element is removed, set flag
            }
        }
        return isChanged;
    }

    @Override
    default boolean retainAll(Collection<?> c) {
        boolean isChanged = false;
        for (E element : this.toArray((E[]) new Object[this.size()])) {
            if (!c.contains(element)) {
                this.remove(element); // Remove elements not in the given collection
                isChanged = true;
            }
        }
        return isChanged;
    }

    @Override
    default Object[] toArray() {
        // Use an inorder traversal to collect elements in sorted order
        ArrayList<E> list = new ArrayList<>();
        this.forEach(list::add);
        return list.toArray();
    }

    @Override
    default <T> T[] toArray(T[] array) {
        // Use an inorder traversal to collect elements in sorted order
        ArrayList<E> list = new ArrayList<>();
        this.forEach(list::add);
        return list.toArray(array);
    }
}