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
        var curNode = root
        var parent: BSTNode<K, V>? = null
        while (curNode != null && curNode.key != key) {
            parent = curNode
            curNode = if (key < curNode.key) curNode.left else curNode.right
        }
        val nodeToDelete = curNode ?: return
        if (nodeToDelete.left == null || nodeToDelete.right == null) {
            val child = nodeToDelete.left ?: nodeToDelete.right
            when {
                parent == null -> root = child
                nodeToDelete == parent.left -> parent.left = child
                else -> parent.right = child
            }
        } else {
            val successor = min(nodeToDelete.right) ?: return
            val successorRight = successor.right
            if (successor == nodeToDelete.right) {
                nodeToDelete.right = successorRight
            } else {
                var successorParent = nodeToDelete.right
                while (successorParent?.left != successor) {
                    successorParent = successorParent?.left
                }
                successorParent.left = successorRight
            }
            nodeToDelete.key = successor.key
            nodeToDelete.value = successor.value
        }
    }
}
