import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.assertThrows
import kotlin.random.Random
import kotlin.test.assertContentEquals

class AVLTreeTest {
    private lateinit var avlTree: TestAVLTree<Int, String>

    @BeforeEach
    fun setUp() {
        avlTree = TestAVLTree()
    }

    @AfterEach
    fun checkAVLTreeProperties() {
        assertTrue(avlTree.isBST())
        assertTrue(avlTree.isBalanced())
    }

    @Test
    fun `insert and search single element`() {
        avlTree.insert(10, "Ten")
        assertEquals("Ten", avlTree.search(10))
    }

    @Test
    fun `search in empty tree`() {
        assertNull(avlTree.search(10))
    }

    @Test
    fun `insert same key two time`() {
        avlTree.insert(10, "Ten")
        avlTree.insert(10, "Eleven")
        assertEquals("Eleven", avlTree.search(10))
    }

    @Test
    fun `LL insertion`(){
        val values = listOf(30, 20, 10)
        values.forEach {
            avlTree.insert(it, it.toString())
            assertTrue(avlTree.isBST())
            assertTrue(avlTree.isBalanced())
        }

        values.forEach { assertEquals(it.toString(), avlTree.search(it)) }
    }

    @Test
    fun `RR insertion`(){
        val values = listOf(10, 20, 30)
        values.forEach {
            avlTree.insert(it, it.toString())
            assertTrue(avlTree.isBST())
            assertTrue(avlTree.isBalanced())
        }

        values.forEach { assertEquals(it.toString(), avlTree.search(it)) }
    }

    @Test
    fun `LR insertion`(){
        val values = listOf(30, 10, 20)
        values.forEach {
            avlTree.insert(it, it.toString())
            assertTrue(avlTree.isBST())
            assertTrue(avlTree.isBalanced())
        }

        values.forEach { assertEquals(it.toString(), avlTree.search(it)) }
    }

    @Test
    fun `RL insertion`(){
        val values = listOf(10, 20, 15)
        values.forEach {
            avlTree.insert(it, it.toString())
            assertTrue(avlTree.isBST())
            assertTrue(avlTree.isBalanced())
        }

        values.forEach { assertEquals(it.toString(), avlTree.search(it)) }
    }

    @Test
    fun `delete non-existent`() {
        avlTree.insert(10, "Ten")
        avlTree.delete(20)
        assertEquals("Ten", avlTree.search(10))
    }

    @Test
    fun `delete leaf node`() {
        avlTree.insert(10, "Ten")
        avlTree.insert(5, "Five")
        avlTree.delete(5)
        assertNull(avlTree.search(5))
        assertEquals("Ten", avlTree.search(10))
    }

    @Test
    fun `delete node with one child`() {
        avlTree.insert(10, "Ten")
        avlTree.insert(5, "Five")
        avlTree.insert(2, "Two")
        avlTree.delete(5)
        assertNull(avlTree.search(5))
        assertEquals("Two", avlTree.search(2))
        assertEquals("Ten", avlTree.search(10))
    }

    @Test
    fun `delete node with two children`() {
        avlTree.insert(10, "Ten")
        avlTree.insert(5, "Five")
        avlTree.insert(15, "Fifteen")
        avlTree.insert(12, "Twelve")
        avlTree.delete(10)
        assertNull(avlTree.search(10))
        assertEquals("Five", avlTree.search(5))
        assertEquals("Fifteen", avlTree.search(15))
        assertEquals("Twelve", avlTree.search(12))
    }

    @Test
    fun `tree is balanced after multiple inserts`() {
        val values = listOf(10, 20, 30, 40, 35, 5, 2, 7, 9)
        values.forEach {
            avlTree.insert(it, it.toString())
            assertTrue(avlTree.isBST())
            assertTrue(avlTree.isBalanced())
        }

        values.forEach { assertEquals(it.toString(), avlTree.search(it)) }
    }

    @Test
    fun `tree is balanced after multiple deletes`() {
        val values = listOf(10, 20, 30, 40, 50, 25, 5, 15, 35, 45)
        values.forEach { avlTree.insert(it, it.toString()) }

        avlTree.delete(10)
        avlTree.delete(40)
        avlTree.delete(25)

        assertNull(avlTree.search(10))
        assertNull(avlTree.search(40))
        assertNull(avlTree.search(25))
    }

    @Test
    fun `min and max correctness`() {
        val values = listOf(10, 20, 30, 40, 50, 25, 5, 15, 35, 45)
        values.forEach { avlTree.insert(it, it.toString()) }

        assertEquals(5, avlTree.min())
        assertEquals(50, avlTree.max())
    }

    @Test
    fun `iterator returns elements in order`() {
        val values = listOf(10, 20, 30, 40, 50, 25, 5, 15, 35, 45)
        values.forEach { avlTree.insert(it, it.toString()) }

        val sortedValues = values.sorted()
        val iteratedValues = avlTree.iterator().asSequence().map { it.key }.toList()

        assertContentEquals(sortedValues, iteratedValues)
    }

    @Test
    fun `exception when tree is modified while iterating`() {
        avlTree.insert(10, "Ten")
        avlTree.insert(20, "Twenty")

        val iterator = avlTree.iterator()
        iterator.next()

        avlTree.insert(30, "Thirty")

        assertThrows<ConcurrentModificationException> { iterator.next() }
    }

    @Test
    fun `height correctness`() {
        avlTree.insert(10, "Ten")
        assertEquals(1, avlTree.getRoot()?.height)

        avlTree.insert(5, "Five")
        assertEquals(2, avlTree.getRoot()?.height)

        avlTree.insert(15, "Fifteen")
        assertEquals(2, avlTree.getRoot()?.height)

        avlTree.insert(3, "Three")
        assertEquals(3, avlTree.getRoot()?.height)

        avlTree.insert(1, "One")
        assertEquals(3, avlTree.getRoot()?.height)
    }

    @Test
    fun `random insertions maintain AVL properties`() {
        val random = Random(52)
        val keys = (1..1000).map { random.nextInt(100000) }.distinct()

        keys.forEach {
            avlTree.insert(it, it.toString())
            assertTrue(avlTree.isBalanced())
            assertTrue(avlTree.isBST())
        }

        keys.forEach { assertEquals(it.toString(), avlTree.search(it)) }
    }

    @Test
    @Tag("Slow")
    fun `random insertions and deletions maintain AVL properties`() {
        val random = Random(228)
        val keys = (1..5000).map { random.nextInt(100000) }.distinct()

        keys.forEach {
            avlTree.insert(it, it.toString())
            assertTrue(avlTree.isBalanced())
            assertTrue(avlTree.isBST())
        }

        val keysToDelete = keys.shuffled().take(keys.size / 2)
        keysToDelete.forEach {
            avlTree.delete(it)
            assertTrue(avlTree.isBalanced())
            assertTrue(avlTree.isBST())
        }

        keys.filter { it !in keysToDelete }.forEach {
            assertEquals(it.toString(), avlTree.search(it))
        }

        keysToDelete.forEach { assertNull(avlTree.search(it)) }
    }

    @Test
    @Tag("Slow")
    fun `random insertions and deletions maintain AVL properties 2`() {
        val random = Random(1337)
        val keys = (1..50000).map { random.nextInt(1000000000) }.distinct()

        keys.forEach { avlTree.insert(it, it.toString()) }

        val keysToDelete = keys.shuffled().take(keys.size / 2)
        keysToDelete.forEach { avlTree.delete(it) }

        keys.filter { it !in keysToDelete }.forEach {
            assertEquals(it.toString(), avlTree.search(it))
        }

        keysToDelete.forEach { assertNull(avlTree.search(it)) }
    }
}

private class TestAVLTree<K : Comparable<K>, V> : AVLTree<K, V>() {
    fun isBalanced(): Boolean = isBalanced(root)

    fun isBST(): Boolean = isBinarySearchTree(root)

    fun getRoot(): AVLNode<K, V>? = root

    private fun isBalanced(node: AVLNode<K, V>?): Boolean {
        if (node == null) return true
        val leftHeight = node.left?.height ?: 0
        val rightHeight = node.right?.height ?: 0
        return kotlin.math.abs(leftHeight - rightHeight) <= 1 &&
                isBalanced(node.left) &&
                isBalanced(node.right)
    }

    private fun isBinarySearchTree(node: AVLNode<K, V>?): Boolean {
        return isBinarySearchTreeHelper(node, null, null)
    }

    private fun isBinarySearchTreeHelper(
        node: AVLNode<K, V>?,
        min: K?,
        max: K?
    ): Boolean {
        if (node == null) return true
        if (min != null && node.key <= min) return false
        if (max != null && node.key >= max) return false
        return isBinarySearchTreeHelper(node.left, min, node.key) &&
                isBinarySearchTreeHelper(node.right, node.key, max)
    }
}
