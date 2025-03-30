abstract class BinarySearchTree<K : Comparable<K>, V, T : Node<K, V, T>> :
    BinaryTree<K, V>, Iterable<T> {
    private var modCnt: Long = 0
    protected var root: T? = null

    override fun insert(
        key: K,
        value: V,
    ) {
        ++modCnt
        insertInternal(key, value)
    }

    override fun delete(key: K) {
        ++modCnt
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
        var curNode = root
        if (root == null) {
            return null
        }
        while (curNode != null) {
            if (key == curNode.key) {
                return curNode
            }
            curNode = if (key > curNode.key) curNode.right else curNode.left
        }
        return null
    }

    fun max(): K? {
        return max(root)?.key
    }

    fun min(): K? {
        return min(root)?.key
    }

    protected fun max(node: T?): T? {
        var curNode = node
        while (curNode?.right != null)
            curNode = curNode.right
        return curNode
    }

    protected fun min(node: T?): T? {
        var curNode = node
        while (curNode?.left != null)
            curNode = curNode.left
        return curNode
    }

    override fun iterator(): Iterator<T> {
        return InOrderIterator()
    }

    private inner class InOrderIterator : Iterator<T> {
        val curModCnt: Long = modCnt
        val nodes = ArrayDeque<T>()

        init {
            addLeft(root)
        }

        fun addLeft(node: T?) {
            var curNode = node
            while (curNode != null) {
                nodes.addLast(curNode)
                curNode = curNode.left
            }
        }

        override fun hasNext(): Boolean {
            return !nodes.isEmpty()
        }

        override fun next(): T {
            if (curModCnt != modCnt) throw ConcurrentModificationException()
            if (!hasNext()) throw NoSuchElementException()
            val curNode = nodes.removeLast()
            addLeft(curNode.right)
            return curNode
        }
    }
}
