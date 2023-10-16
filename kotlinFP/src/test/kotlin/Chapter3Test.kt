import kotlin.test.Test
import kotlin.test.assertEquals

class Chapter3Test {
    @Test
    fun listTest() {
        val list1 = FList(1, 2, 3)
        val nil = FList<Int>()
        assertEquals(list1.size, 3)
        assertEquals(list1.setHead(2).toList(), listOf(2, 2) + 3)
    }
}