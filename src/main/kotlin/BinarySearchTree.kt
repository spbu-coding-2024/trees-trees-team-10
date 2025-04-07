open class BinarySearchTree<K : Comparable<K>, V> : AbstractBinarySearchTree<K, V, BSTNode<K, V>>() {
    override fun insertInternal(
        key: K,
        value: V,
    ) {
        if (root == null) {
            root = BSTNode(key, value)
            return
        }
        var currentNode = root
        while (currentNode != null) {
            when {
                currentNode.key < key -> {
                    if (currentNode.right == null) {
                        currentNode.right = BSTNode(key, value)
                        return
                    }
                    currentNode = currentNode.right
                }

                currentNode.key > key -> {
                    if (currentNode.left == null) {
                        currentNode.left = BSTNode(key, value)
                        return
                    }
                    currentNode = currentNode.left
                }

                else -> {
                    currentNode.value = value
                    return
                }
            }
        }
    }

    override fun deleteInternal(key: K) {
        val (nodeToDelete, parent) = findNodeAndParent(key) ?: return

        if (nodeToDelete.left == null || nodeToDelete.right == null) {
            deleteNodeWithZeroOrOneChild(nodeToDelete, parent)
        } else {
            deleteNodeWithTwoChildren(nodeToDelete)
        }
    }

    private fun findNodeAndParent(key: K): Pair<BSTNode<K, V>, BSTNode<K, V>?>? {
        var currentNode = root
        var parent: BSTNode<K, V>? = null

        while (currentNode != null && currentNode.key != key) {
            parent = currentNode
            currentNode = if (key < currentNode.key) currentNode.left else currentNode.right
        }

        return currentNode?.let { Pair(it, parent) }
    }

    private fun deleteNodeWithZeroOrOneChild(node: BSTNode<K, V>, parent: BSTNode<K, V>?) {
        val child = node.left ?: node.right

        when {
            parent == null -> root = child
            node == parent.left -> parent.left = child
            else -> parent.right = child
        }
    }

    private fun deleteNodeWithTwoChildren(node: BSTNode<K, V>) {
        val successor = min(node.right) ?: return
        val successorRight = successor.right

        if (successor == node.right) {
            node.right = successorRight
        } else {
            findSuccessorParent(node.right, successor)?.left = successorRight
        }

        node.key = successor.key
        node.value = successor.value
    }

    private fun findSuccessorParent(startNode: BSTNode<K, V>?, successor: BSTNode<K, V>): BSTNode<K, V>? {
        var parent = startNode
        while (parent?.left != successor) {
            parent = parent?.left
        }
        return parent
    }
}
