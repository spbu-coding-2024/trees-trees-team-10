abstract class AbstractBinarySearchTree<K : Comparable<K>, V, T : Node<K, V, T>> :
    BinaryTree<K, V>, Iterable<T> {
    private var modificationCount: Long = 0
    protected var root: T? = null

    override fun insert(
        key: K,
        value: V,
    ) {
        ++modificationCount
        insertInternal(key, value)
    }

    override fun delete(key: K) {
        ++modificationCount
        deleteInternal(key)
    }

    protected abstract fun insertInternal(
        key: K,
        value: V,
    )

    protected abstract fun deleteInternal(key: K)

    override fun search(key: K): V? {
        return findNode(key)?.value
    }

    protected fun findNode(key: K): T? {
        var currentNode = root
        if (root == null) {
            return null
        }
        while (currentNode != null) {
            if (key == currentNode.key) {
                return currentNode
            }
            currentNode = if (key > currentNode.key) currentNode.right else currentNode.left
        }
        return null
    }

    fun isEmpty(): Boolean {
        return root == null
    }

    fun max(): K? {
        return max(root)?.key
    }

    fun min(): K? {
        return min(root)?.key
    }

    protected fun max(node: T?): T? {
        var currentNode = node
        while (currentNode?.right != null)
            currentNode = currentNode.right
        return currentNode
    }

    protected fun min(node: T?): T? {
        var currentNode = node
        while (currentNode?.left != null)
            currentNode = currentNode.left
        return currentNode
    }

    override fun iterator(): Iterator<T> {
        return InOrderIterator()
    }

    private inner class InOrderIterator : Iterator<T> {
        val currentModificationCount: Long = modificationCount
        val nodes = ArrayDeque<T>()

        init {
            addLeft(root)
        }

        fun addLeft(node: T?) {
            var currentNode = node
            while (currentNode != null) {
                nodes.addLast(currentNode)
                currentNode = currentNode.left
            }
        }

        override fun hasNext(): Boolean {
            return !nodes.isEmpty()
        }

        override fun next(): T {
            if (currentModificationCount != modificationCount) throw ConcurrentModificationException()
            if (!hasNext()) throw NoSuchElementException()
            val currentNode = nodes.removeLast()
            addLeft(currentNode.right)
            return currentNode
        }
    }
}
