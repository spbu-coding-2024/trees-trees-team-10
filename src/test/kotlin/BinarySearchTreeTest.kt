import net.jqwik.api.ForAll
import net.jqwik.api.Property
import net.jqwik.api.constraints.Size
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class BinarySearchTreeTest {
    private lateinit var bst: TestableBinarySearchTree<Int, String>

    @BeforeEach
    fun setUp() {
        bst = TestableBinarySearchTree()
    }

    @Test
    fun `iterator should traverse in order`() {
        bst.insert(5, "5")
        bst.insert(3, "3")
        bst.insert(7, "7")
        bst.insert(2, "2")
        bst.insert(4, "4")
        bst.insert(6, "6")
        bst.insert(8, "8")

        val expected = listOf(2, 3, 4, 5, 6, 7, 8)
        val actual = bst.map { it.key }

        assertEquals(expected, actual)
    }

    @Test
    fun `iterator should throw ConcurrentModificationException when modified during iteration`() {
        bst.insert(1, "1")
        bst.insert(2, "2")

        assertThrows(ConcurrentModificationException::class.java) {
            for (node in bst) {
                bst.insert(3, "3")
            }
        }
    }

    @Test
    fun `iterator should throw NoSuchElementException when no more elements`() {
        bst.insert(1, "1")
        val iterator = bst.iterator()
        iterator.next()

        assertThrows(NoSuchElementException::class.java) {
            iterator.next()
        }
    }

    @Test
    fun `search return correct value`() {
        bst.insert(1, "Value")
        assertEquals("Value", bst.search(1))
    }

    @Test
    fun `search return null for non-existent key`() {
        bst.insert(1, "Value")
        assertNull(bst.search(2))
    }

    @Test
    fun `search in empty tree`() {
        assertNull(bst.search(1))
    }

    @Test
    fun `insert update existing key`() {
        bst.insert(1, "Value")
        bst.insert(1, "New value")
        assertEquals("New value", bst.search(1))
    }

    @Test
    fun `delete node with no children`() {
        bst.insert(5, "5")
        bst.insert(3, "3")
        bst.delete(3)

        assertNull(bst.search(3))
        assertEquals("5", bst.search(5))
    }

    @Test
    fun `delete node with one child`() {
        bst.insert(5, "5")
        bst.insert(3, "3")
        bst.insert(2, "2")
        bst.delete(3)

        assertNull(bst.search(3))
        assertEquals(listOf(2, 5), bst.map { it.key })
    }

    @Test
    fun `delete node with two children`() {
        bst.insert(5, "5")
        bst.insert(3, "3")
        bst.insert(7, "7")
        bst.insert(2, "2")
        bst.insert(4, "4")
        bst.insert(6, "6")
        bst.insert(8, "8")

        bst.delete(5)

        assertNull(bst.search(5))
        assertEquals(listOf(2, 3, 4, 6, 7, 8), bst.map { it.key })
    }

    @Test
    fun `min should return smallest key`() {
        bst.insert(5, "5")
        bst.insert(3, "3")
        bst.insert(7, "7")
        assertEquals(3, bst.findMinKey())
    }

    @Test
    fun `max should return largest key`() {
        bst.insert(5, "5")
        bst.insert(3, "3")
        bst.insert(7, "7")
        assertEquals(7, bst.findMaxKey())
    }

    @Test
    fun `min should return null for empty tree`() {
        assertNull(bst.findMinKey())
    }

    @Test
    fun `max should return null for empty tree`() {
        assertNull(bst.findMaxKey())
    }

    @Test
    fun `test complex scenario`() {
        for (i in 1..10) {
            bst.insert(i, "value$i")
        }
        for (i in 1..10) {
            assertEquals("value$i", bst.search(i))
        }
        for (i in 2..10 step 2) {
            bst.delete(i)
        }
        for (i in 1..10) {
            if (i % 2 == 1) {
                assertEquals("value$i", bst.search(i))
            } else {
                assertNull(bst.search(i))
            }
        }
        assertEquals(1, bst.findMinKey())
        assertEquals(9, bst.findMaxKey())
        val expected = listOf(1, 3, 5, 7, 9)
        val actual = bst.map { it.key }
        assertEquals(expected, actual)
    }

    @Test
    fun `test large tree`() {
        val size = 10000
        for (i in 0..size) {
            bst.insert(i, "value$i")
        }
        assertEquals("value0", bst.search(0))
        assertEquals("value${size / 2}", bst.search(size / 2))
        assertEquals("value${size - 1}", bst.search(size - 1))
        for (i in 0..size / 2) {
            bst.delete(i)
        }
        assertNull(bst.search(0))
        assertNull(bst.search(size / 2))
        assertEquals("value${size / 2 + 1}", bst.search(size / 2 + 1))
    }

    @Test
    fun `concurrent modification counter test`() {
        bst.insert(1, "1")
        val iterator1 = bst.iterator()
        bst.insert(2, "2")
        assertThrows(ConcurrentModificationException::class.java) {
            iterator1.next()
        }
        val iterator2 = bst.iterator()
        assertEquals(1, iterator2.next().key)
    }
}

internal class BinarySearchTreeProperties {
    @Property
    fun `find inserted value`(
        @ForAll key: Int,
        @ForAll value: String,
    ) {
        val bst = TestableBinarySearchTree<Int, String>()
        bst.insert(key, value)
        assertThat(bst.search(key)).isEqualTo(value)
    }

    @Property
    fun `delete value`(
        @ForAll key: Int,
        @ForAll value: String,
    ) {
        val bst = TestableBinarySearchTree<Int, String>()
        bst.insert(key, value)
        bst.delete(key)
        assertThat(bst.search(key)).isNull()
    }

    @Property
    fun `BST invariants`(
        @ForAll keys: List<Int>,
    ) {
        val bst = TestableBinarySearchTree<Int, String>()
        keys.forEach { bst.insert(it, "value_$it") }

        assert(bst.isBinarySearchTree())
    }

    @Property
    fun `min should return smallest key`(
        @ForAll @Size(min = 1) keys: List<Int>,
    ) {
        val bst = TestableBinarySearchTree<Int, String>()
        keys.forEach { bst.insert(it, "value_$it") }

        assertThat(bst.findMinKey()).isEqualTo(keys.minOrNull())

        var current = bst.getRoot()
        while (current?.left != null) {
            current = current.left
        }
        assertThat(current?.key).isEqualTo(keys.minOrNull())
    }

    @Property
    fun `max should return largest key`(
        @ForAll @Size(min = 1) keys: List<Int>,
    ) {
        val bst = TestableBinarySearchTree<Int, String>()
        keys.forEach { bst.insert(it, "value_$it") }

        assertThat(bst.findMaxKey()).isEqualTo(keys.maxOrNull())

        var current = bst.getRoot()
        while (current?.right != null) {
            current = current.right
        }
        assertThat(current?.key).isEqualTo(keys.maxOrNull())
    }

    @Property
    fun `iterator should return elements in order`(
        @ForAll keys: List<Int>,
    ) {
        val bst = TestableBinarySearchTree<Int, String>()
        val uniqueKeys = keys.toSet()
        uniqueKeys.forEach { bst.insert(it, "value_$it") }
        val traversed = bst.iterator().asSequence().map { it.key }.toList()

        assertThat(traversed).isEqualTo(uniqueKeys.sorted())
        assertThat(bst.collectInOrder()).isEqualTo(uniqueKeys.sorted())
    }
}
