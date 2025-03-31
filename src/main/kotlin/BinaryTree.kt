interface BinaryTree<K : Comparable<K>, V> {
    fun search(key: K): V?

    fun insert(
        key: K,
        value: V,
    )

    fun delete(key: K)
}
