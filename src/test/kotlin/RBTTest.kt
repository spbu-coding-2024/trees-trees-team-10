import io.kotest.assertions.throwables.shouldThrow
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import kotlin.random.Random
import kotlin.test.assertEquals

class RBTForTest<K : Comparable<K>, V> : RedBlackTree<K, V>() {
    fun blackRoot(): Boolean {
        return isBlack(root)
    }

    fun noRedNodeWithRedChild(): Boolean {
        val stack = ArrayDeque<RBTNode<K, V>?>()
        stack.addLast(root)
        while (!stack.isEmpty()) {
            val curNode = stack.removeLast() ?: continue
            if (isRed(curNode) && isRed(curNode.parent)) {
                return false
            }
            stack.addLast(curNode.right)
            stack.addLast(curNode.left)
        }
        return true
    }

    private fun blackHeight(): Int {
        var curNode = root
        var cnt = 0
        while (curNode != null) {
            if (isBlack(curNode)) {
                ++cnt
            }
            curNode = curNode.left
        }
        return cnt
    }

    fun sameBlackHeight(): Boolean {
        root ?: return true
        val bh = blackHeight()
        val stack = ArrayDeque<Pair<RBTNode<K, V>?, Int>>()
        stack.addLast(root to 1)
        while (!stack.isEmpty()) {
            val curPair = stack.removeLast()
            if (curPair.first?.right == null && curPair.first?.left == null && curPair.second != bh) {
                return false
            }
            if (curPair.first?.right != null) {
                if (isBlack(curPair.first?.right)) {
                    stack.addLast(curPair.first?.right to curPair.second + 1)
                } else {
                    stack.addLast(curPair.first?.right to curPair.second)
                }
            }
            if (curPair.first?.left != null) {
                if (isBlack(curPair.first?.left)) {
                    stack.addLast(curPair.first?.left to curPair.second + 1)
                } else {
                    stack.addLast(curPair.first?.left to curPair.second)
                }
            }
        }
        return true
    }

    fun isBST(): Boolean {
        return check(root, null, null)
    }

    private fun check(
        node: RBTNode<K, V>?,
        min: K?,
        max: K?,
    ): Boolean {
        node ?: return true
        if ((min != null && node.key < min) && (max != null && node.key > max)) {
            return false
        }
        return check(node.left, min, node.key) && check(node.right, node.key, max)
    }

    fun size(): Int {
        var cnt = 0
        val stack = ArrayDeque<RBTNode<K, V>?>()
        stack.addLast(root)
        while (!stack.isEmpty()) {
            val curNode = stack.removeLast() ?: continue
            ++cnt
            stack.addLast(curNode.right)
            stack.addLast(curNode.left)
        }
        return cnt
    }

    fun isRBT(): Boolean {
        val stack = ArrayDeque<Pair<RBTNode<K, V>?, Int>>()
        var blackHeight = 0
        stack.addLast(root to 1)
        while (!stack.isEmpty()) {
            val n = stack.removeLast()

            if (isRed(n.first) && isRed(n.first?.parent)) {
                return false
            }
            if (n.first?.left == null && n.first?.right == null) {
                if (blackHeight == 0) {
                    blackHeight = n.second
                }
                if (n.second != blackHeight) {
                    return false
                }
            }
            if (n.first?.left != null) {
                if (isBlack(n.first?.left)) {
                    stack.addLast(n.first?.left to n.second + 1)
                } else {
                    stack.addLast(n.first?.left to n.second)
                }
            }
            if (n.first?.right != null) {
                if (isBlack(n.first?.right)) {
                    stack.addLast(n.first?.right to n.second + 1)
                } else {
                    stack.addLast(n.first?.right to n.second)
                }
            }
        }
        return true
    }
}

class RBTTest {
    @Test
    fun `search test`() {
        val testTree = RBTForTest<Int, Int>()
        for (i in 1..10) {
            testTree.insert(i, i * 10)
        }
        for (i in 1..10) {
            assertEquals(testTree.search(i), i * 10)
        }
    }

    @Test
    fun `missing item search`() {
        val testTree = RBTForTest<Int, Int>()
        for (i in 1..10) {
            testTree.insert(i, i * 10)
        }
        assertNull(testTree.search(20))
    }

    @Test
    fun `search in empty tree`() {
        val testTree = RBTForTest<Int, Int>()
        assertNull(testTree.search(1))
    }

    @Test
    fun `insert test`() {
        val testTree = RBTForTest<Int, String>()
        testTree.insert(1, "1")
        assertEquals(testTree.search(1), "1")
    }

    @Test
    fun `repetitive insertion`() {
        val testTree = RBTForTest<Int, String>()
        testTree.insert(1, "1")
        assertEquals(testTree.size(), 1)
    }

    @Test
    fun `insert random values`() {
        val testTree = RBTForTest<Int, String>()
        val elements: ArrayList<Int> = arrayListOf()
        repeat(100) {
            val newEl = Random.nextInt(-1000, 1000)
            if (testTree.search(newEl) == null) {
                elements.add(newEl)
            }
            testTree.insert(newEl, "$newEl")
        }
        assertEquals(testTree.size(), elements.size)
        for (i in elements)
            assertEquals(testTree.search(i), "$i")
    }

    @Test
    fun `max element`() {
        val testTree = RBTForTest<Int, String>()
        for (i in 1..10)
            testTree.insert(i, "$i")
        assertEquals(testTree.max(), 10)
    }

    @Test
    fun `max element in empty tree`() {
        val testTree = RBTForTest<Int, String>()
        assertEquals(testTree.max(), null)
    }

    @Test
    fun `min element`() {
        val testTree = RBTForTest<Int, String>()
        for (i in 1..10)
            testTree.insert(i, "$i")
        assertEquals(testTree.min(), 1)
    }

    @Test
    fun `min element in empty tree`() {
        val testTree = RBTForTest<Int, String>()
        assertEquals(testTree.min(), null)
    }

    @Test
    fun deleteTest() {
        val testTree = RBTForTest<Int, String>()
        testTree.insert(1, "1")
        testTree.delete(1)
        assertNull(testTree.search(1))
    }

    @Test
    fun `delete of a missing element`() {
        val testTree = RBTForTest<Int, String>()
        for (i in 1..5)
            testTree.insert(i, "$i")
        testTree.delete(10)
        assertEquals(testTree.size(), 5)
    }

    @Test
    fun `delete 10 elements`() {
        val testTree = RBTForTest<Int, String>()
        for (i in 1..10)
            testTree.insert(i, "$i")
        for (i in 1..10)
            testTree.delete(i)
        for (i in 1..10)
            assertNull(testTree.search(i))
    }

    @Test
    fun `iteration test`() {
        val testTree = RBTForTest<Int, String>()
        for (i in 1..20)
            testTree.insert(i, "$i")
        var n = 1
        for (i in testTree) {
            assertEquals(testTree.search(n), "$n")
            ++n
        }
    }

    @Test
    fun `iteration test 2`() {
        val testTree = RBTForTest<Int, String>()
        for (i in 1..20)
            testTree.insert(i, "$i")
        var n = 1
        for (i in testTree) {
            assertEquals(i.key, n)
            assertEquals(i.value, "$n")
            ++n
        }
    }

    @Test
    fun `iteration test 3`() {
        val testTree = RBTForTest<Int, String>()
        for (i in 1..20)
            testTree.insert(i, "$i")
        var n = 1
        val iter = testTree.iterator()
        while (iter.hasNext()) {
            val i = iter.next()
            assertEquals(i.key, n)
            assertEquals(i.value, "$n")
            ++n
            if (n == 10) {
                val j = iter.next()
                assertEquals(j.key, 10)
                assertEquals(j.value, "10")
                testTree.insert(30, "30")
                break
            }
        }

        shouldThrow<ConcurrentModificationException> {
            if (iter.hasNext()) {
                iter.next()
            }
        }
    }

    @Test
    fun `insert while iterating`() {
        val testTree = RBTForTest<Int, String>()
        for (i in 1..10)
            testTree.insert(i, "$i")
        shouldThrow<ConcurrentModificationException> {
            for (i in testTree)
                testTree.insert(100, "100")
        }
    }

    @Test
    fun `delete while iterating`() {
        val testTree = RBTForTest<Int, String>()
        for (i in 1..10)
            testTree.insert(i, "$i")
        shouldThrow<ConcurrentModificationException> {
            for (i in testTree)
                testTree.delete(1)
        }
    }

    @Test
    fun `black root`() {
        val testTree = RBTForTest<Int, String>()
        for (i in 1..10) {
            testTree.insert(i, "$i")
            assert(testTree.blackRoot())
        }
        val arr = arrayOf(2, 7, 6, 4, 5, 1, 9, 10, 8, 3)
        for (i in arr) {
            testTree.delete(i)
            assert(testTree.blackRoot())
        }
    }

    @Test
    fun `no red node with a red child`() {
        val testTree = RBTForTest<Int, String>()
        for (i in 1..100) {
            testTree.insert(i, "$i")
            assert(testTree.noRedNodeWithRedChild())
        }
        for (i in 1..100) {
            testTree.delete(i)
            assert(testTree.noRedNodeWithRedChild())
        }
    }

    @Test
    fun `same black height`() {
        val testTree = RBTForTest<Int, String>()
        for (i in 1..100) {
            testTree.insert(i, "$i")
            assert(testTree.sameBlackHeight())
        }
        for (i in 1..100) {
            testTree.delete(i)
            assert(testTree.sameBlackHeight())
        }
    }

    @Test
    fun `is binary seek tree`() {
        val testTree = RBTForTest<Int, String>()
        for (i in 1..100) {
            testTree.insert(i, "$i")
            assert(testTree.isBST())
        }
        for (i in 1..100) {
            testTree.delete(i)
            assert(testTree.isBST())
        }
    }

    @Test
    fun `big test`() {
        val testTree = RBTForTest<Int, Int>()
        val cur: ArrayList<Int> = arrayListOf()
        var len = 0

        for (i in 1..20000) {
            val rand = Random.nextInt(0, 10)
            if (rand != 0 || len == 0) {
                val newNumber = Random.nextInt(-100000, 100000)
                if (!cur.contains(newNumber)) {
                    testTree.insert(newNumber, 0)
                    cur.add(newNumber)
                    ++len
                }
            } else {
                val index = Random.nextInt(0, len)
                testTree.delete(cur[index])
                cur.removeAt(index)
                --len
            }

            assert(testTree.isRBT())
        }

        while (!cur.isEmpty()) {
            val index = Random.nextInt(0, len)
            testTree.delete(cur[index])
            cur.removeAt(index)
            --len
            assert(testTree.isRBT())
        }
    }
}
