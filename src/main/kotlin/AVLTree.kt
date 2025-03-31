open class AVLTree<K: Comparable<K>, V> internal constructor() : AbstractBinarySearchTree<K, V, AVLNode<K, V>>() {

    override fun insertInternal(key: K, value: V) {
        root = insertRecursive(root, key, value)
    }

    override fun deleteInternal(key: K) {
        root = deleteNode(root, key)
    }

    private fun getHeight(node: AVLNode<K, V>?): Int {
        return node?.height ?: 0
    }

    private fun updateHeight(node: AVLNode<K, V>) {
        node.height =  maxOf(getHeight(node.right), getHeight(node.left)) + 1
    }

    private fun getBFactor(node: AVLNode<K, V>?): Int {
        return getHeight(node?.right)-getHeight(node?.left)
    }

    private fun rotateLeft(a: AVLNode<K, V>): AVLNode<K, V> {
        val b = a.right ?: return a
        a.right = b.left
        b.left = a
        updateHeight(a)
        updateHeight(b)
        return b
    }

    private fun rotateRight(a: AVLNode<K, V>): AVLNode<K, V> {
        val b = a.left ?: return a
        a.left = b.right
        b.right = a
        updateHeight(a)
        updateHeight(b)
        return b
    }

    private fun balance(node: AVLNode<K, V>): AVLNode<K, V> {
        updateHeight(node)
        val bfactor = getBFactor(node)

        if (bfactor > 1) {

            if (getBFactor(node.right) < 0) {
                node.right = node.right?.let { rotateRight(it) }
            }
            return rotateLeft(node)
        }

        if (bfactor < -1) {
            if (getBFactor(node.left) > 0) {
                node.left = node.left?.let { rotateLeft(it) }
            }
            return rotateRight(node)
        }

        return node
    }

    private fun insertRecursive(node: AVLNode<K, V>?, key: K, value: V): AVLNode<K, V> {
        if (node == null) {
            return AVLNode(key, value)
        }
        when {
            key < node.key -> node.left = insertRecursive(node.left, key, value)
            key > node.key -> node.right = insertRecursive(node.right, key, value)
            else -> node.value = value
        }
        return balance(node)
    }

    private fun deleteNode(node: AVLNode<K, V>?, key: K): AVLNode<K, V>? {
        if (node == null) return null

        when {
            key < node.key -> node.left = deleteNode(node.left, key)
            key > node.key -> node.right = deleteNode(node.right, key)
            else -> {
                if (node.left == null) return node.right
                if (node.right == null) return node.left

                val minNode = findMin(node.right ?: node)
                node.key = minNode.key
                node.value = minNode.value
                node.right = deleteNode(node.right, minNode.key)
            }
        }

        return balance(node)
    }

    private fun findMin(node: AVLNode<K, V>): AVLNode<K, V> {
        var current = node
        while (current.left != null) {
            current = current.left ?: current
        }
        return current
    }
}
