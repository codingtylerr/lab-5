Great! You've implemented a Binary Search Tree (BST) with various methods for insertion, deletion, and traversal. Now, let's integrate the `CustomCollection` interface methods into your `BST` class. Here's how you can do it:

1. Implement the `CustomCollection` interface.
2. Provide the necessary method implementations.

Here's the updated `BST` class:

```java
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;

public class BST<E> implements Tree<E>, CustomCollection<E> {

    protected TreeNode<E> root;
    protected int size = 0;
    protected Comparator<E> c;

    /** Create a default BST with a natural order comparator */
    public BST() {
        this.c = (e1, e2) -> ((Comparable<E>) e1).compareTo(e2);
    }

    /** Create a BST with a specified comparator */
    public BST(Comparator<E> c) {
        this.c = c;
    }

    /** Create a binary tree from an array of objects */
    public BST(E[] objects) {
        this.c = (e1, e2) -> ((Comparable<E>) e1).compareTo(e2);
        for (int i = 0; i < objects.length; i++)
            add(objects[i]);
    }

    @Override
    public boolean search(E e) {
        TreeNode<E> current = root; // Start from the root

        while (current != null) {
            if (c.compare(e, current.element) < 0) {
                current = current.left;
            } else if (c.compare(e, current.element) > 0) {
                current = current.right;
            } else // element matches current.element
                return true; // Element is found
        }

        return false;
    }

    @Override
    public boolean insert(E e) {
        if (root == null)
            root = createNewNode(e); // Create a new root
        else {
            // Locate the parent node
            TreeNode<E> parent = null;
            TreeNode<E> current = root;
            while (current != null)
                if (c.compare(e, current.element) < 0) {
                    parent = current;
                    current = current.left;
                } else if (c.compare(e, current.element) > 0) {
                    parent = current;
                    current = current.right;
                } else
                    return false; // Duplicate node not inserted

            // Create the new node and attach it to the parent node
            if (c.compare(e, parent.element) < 0)
                parent.left = createNewNode(e);
            else
                parent.right = createNewNode(e);
        }

        size++;
        return true; // Element inserted successfully
    }

    protected TreeNode<E> createNewNode(E e) {
        return new TreeNode<>(e);
    }

    @Override
    public void inorder() {
        inorder(root);
    }

    protected void inorder(TreeNode<E> root) {
        if (root == null)
            return;
        inorder(root.left);
        System.out.print(root.element + " ");
        inorder(root.right);
    }

    @Override
    public void postorder() {
        postorder(root);
    }

    protected void postorder(TreeNode<E> root) {
        if (root == null)
            return;
        postorder(root.left);
        postorder(root.right);
        System.out.print(root.element + " ");
    }

    @Override
    public void preorder() {
        preorder(root);
    }

    protected void preorder(TreeNode<E> root) {
        if (root == null)
            return;
        System.out.print(root.element + " ");
        preorder(root.left);
        preorder(root.right);
    }

    public static class TreeNode<E> {
        protected E element;
        protected TreeNode<E> left;
        protected TreeNode<E> right;

        public TreeNode(E e) {
            element = e;
        }
    }

    @Override
    public int getSize() {
        return size;
    }

    public TreeNode<E> getRoot() {
        return root;
    }

    public ArrayList<TreeNode<E>> path(E e) {
        ArrayList<TreeNode<E>> list = new ArrayList<>();
        TreeNode<E> current = root; // Start from the root

        while (current != null) {
            list.add(current); // Add the node to the list
            if (c.compare(e, current.element) < 0) {
                current = current.left;
            } else if (c.compare(e, current.element) > 0) {
                current = current.right;
            } else
                break;
        }

        return list; // Return an array list of nodes
    }

    @Override
    public boolean delete(E e) {
        TreeNode<E> parent = null;
        TreeNode<E> current = root;
        while (current != null) {
            if (c.compare(e, current.element) < 0) {
                parent = current;
                current = current.left;
            } else if (c.compare(e, current.element) > 0) {
                parent = current;
                current = current.right;
            } else
                break; // Element is in the tree pointed at by current
        }

        if (current == null)
            return false; // Element is not in the tree

        if (current.left == null) {
            if (parent == null) {
                root = current.right;
            } else {
                if (c.compare(e, parent.element) < 0)
                    parent.left = current.right;
                else
                    parent.right = current.right;
            }
        } else {
            TreeNode<E> parentOfRightMost = current;
            TreeNode<E> rightMost = current.left;

            while (rightMost.right != null) {
                parentOfRightMost = rightMost;
                rightMost = rightMost.right; // Keep going to the right
            }

            current.element = rightMost.element;

            if (parentOfRightMost.right == rightMost)
                parentOfRightMost.right = rightMost.left;
            else
                parentOfRightMost.left = rightMost.left;
        }

        size--;
        return true; // Element deleted successfully
    }

    @Override
    public Iterator<E> iterator() {
        return new InorderIterator();
    }

    private class InorderIterator implements Iterator<E> {
        private ArrayList<E> list = new ArrayList<>();
        private int current = 0;

        public InorderIterator() {
            inorder();
        }

        private void inorder() {
            inorder(root);
        }

        private void inorder(TreeNode<E> root) {
            if (root == null)
                return;
            inorder(root.left);
            list.add(root.element);
            inorder(root.right);
        }

        @Override
        public boolean hasNext() {
            return current < list.size();
        }

        @Override
        public E next() {
            return list.get(current++);
        }

        @Override
        public void remove() {
            if (current == 0)
                throw new IllegalStateException();

            delete(list.get(--current));
            list.clear();
            inorder();
        }
    }

    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object element : c) {
            if (!this.contains(element)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean isChanged = false;
        for (E element : c) {
            if (this.add(element)) {
                isChanged = true;
            }
        }
        return isChanged;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean isChanged = false;
        for (Object element : c) {
            if (this.remove(element)) {
                isChanged = true;
            }
        }
        return isChanged;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean isChanged = false;
        for (E element : this.toArray((E[]) new Object[this.size()])) {
            if (!c.contains(element)) {
                this.remove(element);
                isChanged = true;
            }
        }
        return isChanged;
    }

    @Override
    public Object[] toArray() {
        ArrayList<E> list = new ArrayList<>();
        this.forEach(list::add);
        return list.toArray();
    }

    @Override
    public <T> T[] toArray(T[] array) {
        ArrayList<E> list = new ArrayList<>();
        this.forEach(list::add);
        return list.toArray(array);
    }
}
