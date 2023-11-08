import kotlin.test.Test
import kotlin.test.assertEquals

class Chapter3Test {
    @Test
    fun listTest() {
        val list = KhsList.of(1, 2, 3, 4)

        assertEquals(list, KhsList.of(1, 2, 3, 4))
        // 1
        assertEquals(KhsList.tail(list), KhsList.of(2, 3, 4))
        // 2
        assertEquals(KhsList.setHead(list, 10), KhsList.of(10, 2, 3, 4))
        // 3
        assertEquals(KhsList.drop(list, 2), KhsList.of(3, 4))
        // 4
        assertEquals(KhsList.dropWhile(list) {it == 3}, KhsList.of(1, 2, 4))
        // 5
        assertEquals(KhsList.init(list), KhsList.of(1, 2, 3))
        // 8
        assertEquals(KhsList.length(list), 4)
        // 9
        assertEquals(KhsList.foldLeft(list, 0) {acc, it -> acc+it}, 10)
        // 10
        assertEquals(KhsList.sum10(list), 10)
        assertEquals(KhsList.product10(KhsList.of(1.0, 2.0, 3.0)), 6.0)

//        val list = FList(1, 2, 3, 4, 5)
//        val nil = FList<Int>()
//
//        val add: (it: Int, acc: Int)-> Int = { it, acc -> it + acc }
//        val l5= FList(5)
//        assertEquals(5, add(5, 0))
//        assertEquals(5, l5.foldRight(0, add))
//        assertEquals(add(5, 0), l5.foldRight(0, add))
//
//        val l4= FList(4) + l5
//        assertEquals(9, add(4, add(5, 0)))
//        assertEquals(9, add(4, l5.foldRight(0, add)))
//        assertEquals(9, l4.foldRight(0, add))
//
//        val l3= FList(3) + l4
//        val l2= FList(2) + l3
//        val l1= FList(1) + l2
//
//        println("start list: $list, nil: $nil")
//        assertEquals(list.fold(0) { acc, it -> acc + it }, 15)
//        assertEquals(list.fold1(0) { acc, it -> acc + it }, 15)
//        assertEquals(list.foldRight(0) { acc, it -> acc + it }, 15)
//        assertEquals(list.foldRight1(0) { acc, it -> acc + it }, 15)
//        assertEquals(list.foldRight2(0) { acc, it -> acc + it }, 15)
//        assertEquals(list.foldRight3(0) { acc, it -> acc + it }, 15)
//        println("end list: $list, nil: $nil")
    }

    @Test
    fun treeTest() {

    }
}