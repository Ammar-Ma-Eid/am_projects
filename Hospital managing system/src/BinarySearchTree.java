class Node<T> {
    T data;
    Node<T> left, right;

    public Node(T data) {
        this.data = data;
        this.left = this.right = null;
    }
}
public class BinarySearchTree <T extends Comparable<T>> {

    Node<T> root;

    public void insert(T data) {
        root = insertRec(root, data);
    }

    private Node<T> insertRec(Node<T> root, T data) {
        if (root == null) {
            root = new Node<>(data);
            return root;
        }
        if (data.compareTo(root.data) < 0) {
            root.left = insertRec(root.left, data);
        } else if (data.compareTo(root.data) > 0) {
            root.right = insertRec(root.right, data);
        }
        return root;
    }

    public boolean contains(T data) {
        return containsRec(root, data);
    }

    private boolean containsRec(Node<T> root, T data) {
        if (root == null) return false;
        if (data.compareTo(root.data) == 0) return true;
        return data.compareTo(root.data) < 0 ? containsRec(root.left, data) : containsRec(root.right, data);
    }

}
