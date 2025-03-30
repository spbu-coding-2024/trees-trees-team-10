open class RedBlackTree<K : Comparable<K>, V> internal constructor() : BinarySearchTree<K, V, RBTNode<K, V>>() {
    override fun insertInternal(
        key: K,
        value: V,
    ) {
        insertCase1(makeLeaf(key, value))
    }

    override fun deleteInternal(key: K) {
        val n = findNode(key)
        delete(n)
    }

    private fun grandparent(n: RBTNode<K, V>?): RBTNode<K, V>? {
        if (n?.parent != null) {
            return n.parent?.parent
        }
        return null
    }

    private fun uncle(n: RBTNode<K, V>?): RBTNode<K, V>? {
        val g = grandparent(n) ?: return null
        return if (n?.parent == g.left) g.right else g.left
    }

    private fun leftRotate(n: RBTNode<K, V>?) {
        n ?: return
        val pivot = n.right
        pivot ?: return
        val p = n.parent

        pivot.parent = p
        if (p != null) {
            if (p.right == n) {
                p.right = pivot
            } else {
                p.left = pivot
            }
        } else {
            root = pivot
        }

        n.right = pivot.left
        pivot.left?.parent = n

        pivot.left = n
        n.parent = pivot
    }

    private fun rightRotate(n: RBTNode<K, V>?) {
        n ?: return
        val pivot = n.left
        pivot ?: return
        val p = n.parent

        pivot.parent = p
        if (p != null) {
            if (p.right == n) {
                p.right = pivot
            } else {
                p.left = pivot
            }
        } else {
            root = pivot
        }

        n.left = pivot.right
        pivot.right?.parent = n

        pivot.right = n
        n.parent = pivot
    }

    private fun makeLeaf(
        key: K,
        value: V,
    ): RBTNode<K, V>? {
        val newNode = RBTNode(key, value)

        if (root == null) {
            root = newNode
            return root
        }

        var curNode = root
        while (curNode != null) {
            if (key > curNode.key) {
                if (curNode.right == null) {
                    newNode.parent = curNode
                    curNode.right = newNode
                    return newNode
                }
                curNode = curNode.right
            } else if (key < curNode.key) {
                if (curNode.left == null) {
                    newNode.parent = curNode
                    curNode.left = newNode
                    return newNode
                }
                curNode = curNode.left
            } else {
                break
            }
        }
        return null
    }

    private fun isLeftChild(n: RBTNode<K, V>?): Boolean {
        return (n == n?.parent?.left)
    }

    private fun isRightChild(n: RBTNode<K, V>?): Boolean {
        return (n == n?.parent?.right)
    }

    private fun insertCase1(n: RBTNode<K, V>?) {
        n ?: return
        if (n.parent == null) {
            n.color = Color.BLACK
            return
        }
        insertCase2(n)
    }

    private fun insertCase2(n: RBTNode<K, V>?) {
        n ?: return
        if (isBlack(n.parent)) {
            return
        } else {
            insertCase3(n)
        }
    }

    private fun insertCase3(n: RBTNode<K, V>?) {
        n ?: return
        val u = uncle(n)
        if (isRed(n.parent) && isRed(u)) {
            n.parent?.color = Color.BLACK
            u?.color = Color.BLACK
            val g = grandparent(n)
            g?.color = Color.RED
            insertCase1(g)
        } else {
            insertCase4(n)
        }
    }

    private fun insertCase4(n: RBTNode<K, V>?) {
        n ?: return
        var mutN = n
        val p = n.parent ?: return
        if (isLeftChild(p) && isRightChild(n)) {
            leftRotate(p)
            mutN = n.left
        } else if (isRightChild(p) && isLeftChild(n)) {
            rightRotate(p)
            mutN = n.right
        }
        insertCase5(mutN)
    }

    private fun insertCase5(n: RBTNode<K, V>?) {
        n ?: return
        val p = n.parent ?: return
        val g = grandparent(n) ?: return
        p.color = Color.BLACK
        g.color = Color.RED
        if (isLeftChild(n) && isLeftChild(p)) {
            rightRotate(g)
        } else {
            leftRotate(g)
        }
    }

    private fun sibling(node: RBTNode<K, V>?): RBTNode<K, V>? {
        return if (node == node?.parent?.right) {
            node?.parent?.left
        } else {
            node?.parent?.right
        }
    }

    protected fun isBlack(node: RBTNode<K, V>?): Boolean {
        return (node == null || node.color == Color.BLACK)
    }

    protected fun isRed(node: RBTNode<K, V>?): Boolean {
        return (node != null && node.color == Color.RED)
    }

    private fun delete(n: RBTNode<K, V>?) {
        n ?: return
        var deleteNode: RBTNode<K, V>? = max(n.left)
        if (deleteNode == null) {
            deleteNode = min(n.right)
        }
        deleteNode = deleteNode ?: n
        if (deleteNode == root) {
            root = null
            return
        }
        n.key = deleteNode.key
        n.value = deleteNode.value

        val child =
            if (deleteNode.left != null) {
                deleteNode.left
            } else {
                deleteNode.right
            }
        if (isRed(deleteNode)) {
            if (deleteNode.parent?.left == deleteNode) {
                deleteNode.parent?.left = null
            } else {
                deleteNode.parent?.right = null
            }
            return
        }
        if (isRed(child)) {
            child?.parent = deleteNode.parent
            if (deleteNode.parent?.left == deleteNode) {
                deleteNode.parent?.left = child
            } else {
                deleteNode.parent?.right = child
            }
            child?.color = Color.BLACK
            return
        }
        deleteCase1(deleteNode)

        child?.parent = deleteNode.parent
        if (isLeftChild(deleteNode)) {
            deleteNode.parent?.left = child
        } else {
            deleteNode.parent?.right = child
        }
    }

    private fun deleteCase1(n: RBTNode<K, V>?) {
        if (n != root) {
            deleteCase2(n)
        }
    }

    private fun deleteCase2(n: RBTNode<K, V>?) {
        val s = sibling(n)
        if (isRed(s)) {
            n?.parent?.color = Color.RED
            s?.color = Color.BLACK
            if (isLeftChild(n)) {
                leftRotate(n?.parent)
            } else {
                rightRotate(n?.parent)
            }
        }
        deleteCase3(n)
    }

    private fun deleteCase3(n: RBTNode<K, V>?) {
        val s = sibling(n)
        if (isBlack(n?.parent) && isBlack(s) && isBlack(s?.left) && isBlack(s?.right)) {
            s?.color = Color.RED
            deleteCase1(n?.parent)
        } else {
            deleteCase4(n)
        }
    }

    private fun deleteCase4(n: RBTNode<K, V>?) {
        val s = sibling(n)
        if (isRed(n?.parent) && isBlack(s) && isBlack(s?.left) && isBlack(s?.right)) {
            s?.color = Color.RED
            n?.parent?.color = Color.BLACK
        } else {
            deleteCase5(n)
        }
    }

    private fun deleteCase5(n: RBTNode<K, V>?) {
        val s = sibling(n)
        if (isBlack(s)) {
            if (isLeftChild(n) && isRed(s?.left) && isBlack(s?.right)) {
                s?.color = Color.RED
                s?.left?.color = Color.BLACK
                rightRotate(s)
            } else if (isRightChild(n) && isBlack(s?.left) && isRed(s?.right)) {
                s?.color = Color.RED
                s?.right?.color = Color.BLACK
                leftRotate(s)
            }
        }
        deleteCase6(n)
    }

    private fun deleteCase6(n: RBTNode<K, V>?) {
        val s = sibling(n)
        s?.color = n?.parent?.color ?: Color.BLACK
        n?.parent?.color = Color.BLACK
        if (isLeftChild(n)) {
            s?.right?.color = Color.BLACK
            leftRotate(n?.parent)
        } else {
            s?.left?.color = Color.BLACK
            rightRotate(n?.parent)
        }
    }
}

fun <K : Comparable<K>, V> emptyRedBlackTree(): RedBlackTree<K, V> = RedBlackTree()

fun <K : Comparable<K>, V> redBlackTreeOf(vararg pairs: Pair<K, V>): RedBlackTree<K, V> {
    val tree = RedBlackTree<K, V>()
    pairs.forEach { (key, value) -> tree.insert(key, value) }
    return tree
}
