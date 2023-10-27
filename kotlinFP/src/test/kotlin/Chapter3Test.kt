import kotlin.test.Test
import kotlin.test.assertEquals

class Chapter3Test {
    @Test
    fun listTest() {
        val list = FList(1, 2, 3, 4, 5)
        val nil = FList<Int>()

        val add: (it: Int, acc: Int)-> Int = { it, acc -> it + acc }
        val l5= FList(5)
        assertEquals(5, add(5, 0))
        assertEquals(5, l5.foldRight(0, add))
        assertEquals(add(5, 0), l5.foldRight(0, add))

        val l4= FList(4) + l5
        assertEquals(9, add(4, add(5, 0)))
        assertEquals(9, add(4, l5.foldRight(0, add)))
        assertEquals(9, l4.foldRight(0, add))

        val l3= FList(3) + l4
        val l2= FList(2) + l3
        val l1= FList(1) + l2


//        [1, 2, 3, 4, 5]
//        foldRight
//        1 + [2, 3, 4, 5]
//          => head1 + tail1
//        1 + (2 + [3, 4, 5])
//          => head1 + (head2 + tail2))
//        1 + (2 + (3 + [4, 5]))
//          => head1 + (head2 + (head3 + tail3))
//        1 + (2 + (3 + (4 + 5)))
//          => head1 + (head2 + (head3 + (head4 + tail4)))
//        1 + (2 + (3 + 9))
//          => head1 + (head2 + (head3 + 9))
//        1 + (2 + 12)
//          => head1 + (head2 + 12)
//        1 + 14
//          => head1 + 14
//        15

//        fold
//        1 + [2, 3, 4, 5]
//          => head1 + tail1
//        (1 + 2) + [3, 4, 5]
//          => (head1 + head2) + tail2
//        ((1 + 2) + 3) + [4, 5]
//          => ((head1 + head2) + head3) + tail3
//        (((1 + 2) + 3) + 4) + 5
//          => (((head1 + head2) + head3) + head4) + tail4
//        ((3 + 3) + 4) + 5
//          => ((3 + head3) + head4) + tail4
//        (6 + 4) + 5
//          => (6 + head4) + tail4
//        10 + 5
//          => 10 + tail4
//        15

        println("start list: $list, nil: $nil")
        assertEquals(list.fold(0) { acc, it -> acc + it }, 15)
        assertEquals(list.fold1(0) { acc, it -> acc + it }, 15)
        assertEquals(list.foldRight(0) { acc, it -> acc + it }, 15)
        assertEquals(list.foldRight1(0) { acc, it -> acc + it }, 15)
        assertEquals(list.foldRight2(0) { acc, it -> acc + it }, 15)
        assertEquals(list.foldRight3(0) { acc, it -> acc + it }, 15)
        println("end list: $list, nil: $nil")
//        assertEquals(list.fold(listOf()) { acc, each -> acc + each }, listOf(1, 2, 3, 4, 5))
//        assertEquals(list.foldRight(listOf()) { each, acc -> acc + each }, listOf(5, 4, 3, 2, 1))
//        assertEquals(list.size, 5)
//        assertEquals(list.size2, 5)
//        assertEquals(list.toList(), listOf(1, 2, 3, 4, 5))
//        assertEquals(list.setHead(10), FList(10, 2, 3, 4, 5))
//        assertEquals(list.addFirst(1), FList(1, 1, 2, 3, 4, 5))
//        assertEquals(list.addTail(1), FList(1, 2, 3, 4, 5, 1))
//        assertEquals(list.flatMap { FList("[$it]") }, FList("[1]", "[2]", "[3]", "[4]", "[5]"))
//        assertEquals(list.map { "[$it]" }, FList("[1]", "[2]", "[3]", "[4]", "[5]"))
//        assertEquals(list.toStringList(), FList("1", "2", "3", "4", "5"))
//        assertEquals(list, FList(FList(1, 2, 3), FList(4, 5)).flatten())
//        assertEquals(list.append(FList(6, 7)), FList(1, 2, 3, 4, 5, 6, 7))
//        assertEquals(list.append1(FList(6, 7)), FList(1, 2, 3, 4, 5, 6, 7))
//        assertEquals(list, FList(1,2,3) + FList(4,5))
//        assertEquals(list.drop(2), FList(3, 4, 5))
//
//        assertEquals(list, FList(1, 2, 3, 4, 5))
    }

    @Test
    fun treeTest() {

    }
}