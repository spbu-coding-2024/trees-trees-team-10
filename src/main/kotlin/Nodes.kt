abstract class Node<K : Comparable<K>, V, T : Node<K, V, T>>(
    key: K,
    value: V,
    internal var left: T? = null,
    internal var right: T? = null,
) {
    var key: K = key
        internal set

    var value: V = value
        internal set
}

class BSTNode<K : Comparable<K>, V>(
    key: K,
    value: V,
    left: BSTNode<K, V>? = null,
    right: BSTNode<K, V>? = null,
) : Node<K, V, BSTNode<K, V>>(key, value, left, right)

internal enum class Color { RED, BLACK }

class RBTNode<K : Comparable<K>, V> internal constructor(
    key: K,
    value: V,
    left: RBTNode<K, V>? = null,
    right: RBTNode<K, V>? = null,
) : Node<K, V, RBTNode<K, V>>(key, value, left, right) {
    internal var parent: RBTNode<K, V>? = null
    internal var color: Color = Color.RED
}

class AVLNode<K : Comparable<K>, V> internal constructor(
    key: K,
    value: V,
    left: AVLNode<K, V>? = null,
    right: AVLNode<K, V>? = null,
): Node<K, V, AVLNode<K, V>>(key, value, left, right) {
    internal var height: Int = 1
}
