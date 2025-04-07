class TestableBinarySearchTree<K : Comparable<K>, V> : BinarySearchTree<K, V>() {
    fun getRoot(): BSTNode<K, V>? = root

    fun isBinarySearchTree(): Boolean {
        return checkNode(root)
    }

    private fun checkNode(node: BSTNode<K, V>?): Boolean {
        node ?: return true
        val leftValid = node.left?.let { it.key < node.key } ?: true
        val rightValid = node.right?.let { it.key > node.key } ?: true
        return leftValid && rightValid && checkNode(node.left) && checkNode(node.right)
    }

    fun collectInOrder(): List<K> {
        val node = root ?: return emptyList()
        return collectInOrder(node.left) + listOf(node.key) + collectInOrder(node.right)
    }

    private fun collectInOrder(node: BSTNode<K, V>?): List<K> {
        node ?: return emptyList()
        return collectInOrder(node.left) + listOf(node.key) + collectInOrder(node.right)
    }

    fun insertMap(items: Map<K, V>){
        items.forEach { (key, value) -> insert(key, value) }
    }
}