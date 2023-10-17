import kotlin.test.Test
import kotlin.test.assertEquals

class Chapter4Test {
    @Test
    fun test4_1() {
        val some= Option(1)
        val none= Option.none<Int>()
        assertEquals(some.map() { "$it" }, Option("1"))
        assertEquals(some.getOrElse() { 0 }, 1)
        assertEquals(none.getOrElse() { 0 }, 0)
        assertEquals(some.flatMap() { Option("$it") }, Option("1"))
        assertEquals(none.flatMap() { Option("$it") }, Option.none())
        assertEquals(some.orElse() { Option(2) }, Option(1))
        assertEquals(none.orElse() { Option(2) }, Option(2))
        assertEquals(some.filter() { it == 1 }, Option(1))
        assertEquals(none.filter() { it == 1 }, none)
    }

    @Test
    fun test4_2() {
        val list = FList(1.0, 2.0, 3.0)
        assertEquals(mean(list), Option(2.0))
        assertEquals(variance(list), Option(0.6666666666666666))
    }

    @Test
    fun test4_3() {
        val some= Option(1)
        val none= Option.none<Int>()
        assertEquals(map2(some, Option(2)) { a, b -> a + b }, Option(3))
        assertEquals(map2(some, none) { a, b -> a + b }, Option.none())
        assertEquals(some.mapTow(Option(2)) { a, b -> a + b }, Option(3))
    }

    @Test
    fun test4_4() {
        val listOption = FList(Option(1), Option(2), Option(3))
        val optionList = Option(FList(1, 2, 3))
        assertEquals(sequence(listOption), optionList)
        assertEquals(traverse(listOption) {it}, optionList)
    }

    @Test
    fun test4_5() {
        val listOption = FList(Option(1), Option(2), Option(3))
        val optionList = Option(FList(1, 2, 3))
        assertEquals(sequence2(listOption), optionList)
    }

    @Test
    fun test4_6() {
        assertEquals(Left(1).map {  }, Left(1))
        assertEquals(Right(1).map { it + 2 }, Right(3))
        assertEquals(Right(1).flatMap { Right(it + 2) }, Right(3))
        assertEquals(Right(1).orElse { Right(2) }, Right(1))
        assertEquals(mapEither(Right(1), Right(2)) { r1, r2 -> r1 + r2}, Right(3))
    }

    @Test
    fun test4_7() {
        val listEither = FList(Right(1), Right(2), Right(3))
        val eitherList = Right(FList(1, 2, 3))
        assertEquals(traverseEither(listEither) {it}, eitherList)
        assertEquals(sequenceEither(listEither), eitherList)
    }

    @Test
    fun test4_8() {
        val name = mkName("khs")
        val age = mkAge(11)
        val person = mkPerson("khs", 11)
        assertEquals(name, Right(Name("khs")))
        assertEquals(age, Right(Age(11)))
        assertEquals(person, Right(Person(Name("khs"), Age(11))))
    }
}