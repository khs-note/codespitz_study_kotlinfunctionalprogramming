sealed class KhsList<out A> {
    companion object {
        fun <A> of(vararg aa: A): KhsList<A> {
            val tail = aa.sliceArray(1 until aa.size)
            return if(aa.isEmpty()) KhsNil else KhsCons(aa[0], of(*tail))
        }
        fun sum(ints: KhsList<Int>): Int
        = when(ints) {
            is KhsNil -> 0
            is KhsCons -> ints.head + sum(ints.tail)
        }
        fun product(doubles: KhsList<Double>): Double
        = when(doubles) {
            is KhsNil -> 1.0
            is KhsCons ->
                if(doubles.head == 0.0) 0.0
                else doubles.head * product(doubles.tail)
        }
        /*
        연습문제 3.1
        List의 첫 번째 원소를 제거하는 tail 함수를 구현하라.
        이 함수는 상수 시간에 실행이 끝나야 한다.
        List가 Nil일 때 선택할 수 있는 여러 가지 처리 방법을 생각해보라.
        다음 장에서 이 경우(함수가 정상 작동하지 못하는 경우)를 다시 살펴본다.
         */
        fun <A> tail(xs: KhsList<A>): KhsList<A>
        = when(xs) {
            is KhsNil -> KhsNil
            is KhsCons -> xs.tail
        }
        /*
        연습문제 3.2
        연습문제 3.1과 같은 아이디어를 사용해 List의 첫 원소를 다른 값으로
        대치하는 setHead 함수를 작성하라.
         */
        fun <A> setHead(xs: KhsList<A>, x: A): KhsList<A>
        = when(xs) {
            is KhsNil -> KhsNil
            is KhsCons -> KhsCons(x, xs.tail)
        }
        /*
        연습문제 3.3
        tail을 더 일반화해서 drop함수를 작성하라.
        drop은 리스트 맨 앞부터 n개 원소를 제거한다.
        이 함수는 삭제할 원소의 개수에 비례해 시간이 걸린다는 사실(
        따라서 여러분이 전체 List를 복사할 필요가 없다는 사실)을
        알아두라.
         */
        fun <A> drop(l: KhsList<A>, n: Int): KhsList<A>
        = when(l) {
            is KhsNil -> KhsNil
            is KhsCons ->
                if(n == 0) l else drop(l.tail, n - 1)
        }
        /*
        연습문제 3.4
        dropWhile을 구현하라.
        이 함수는 List의 맨 앞에서부터 주어진 술어를 만족(술어 함수가 true를 반환)
        하는 연속적인 원소를 삭제한다.(다른 말로 하면, 이 함수는 주어진 술어를 만족하는
        접두사를 List에서 제거한다.)
         */
        fun <A> dropWhile(l: KhsList<A>, f: (A) -> Boolean): KhsList<A>
        = when(l) {
            is KhsNil -> l
            is KhsCons -> {
                if(f(l.head)) dropWhile(l.tail, f) else KhsCons(l.head, dropWhile(l.tail, f))
            }
        }
        /*
        연습문제 3.5
        코드가 리스트를 연결하는 코드의 경우처럼 항상 제대로 작동하는 것은 아니다.
        어떤 List에서 마지막 원소를 제외한 나머지 모든 원소로 이뤄진(순서는 동일한)
        새 List를 반환하는 init 함수를 정의하라.
        예를 들어 List(1,2,3,4)에 대해 init(1,2,3)을 돌려줘야 한다.
        이 함수를 tail처럼 상수 시간에 구현할 수 없는 이유는 무엇일까?
         */
        fun <A> init(l: KhsList<A>): KhsList<A>
        = when(l) {
            is KhsNil -> KhsNil
            is KhsCons ->
                if(l.tail == KhsNil) KhsNil else KhsCons(l.head, init(l.tail))
        }

        /** 리스트 3.11 */
        fun <A, B> foldRight(xs: KhsList<A>, z: B, f: (A, B) -> B): B
        = when(xs) {
            is KhsNil -> z
            is KhsCons -> f(xs.head, foldRight(xs.tail, z, f))
        }
        /*
        연습문제 3.8
        foldRight를 사용해 리스트 길이를 계산하라.
         */
        fun <A> length(xs: KhsList<A>): Int = foldRight(xs, 0) {_, acc -> acc+1}
        /*
        연습문제 3.9
        우리가 구현한 foldRight는 꼬리 재귀가 아니므로 리스트가 긴경우 StackOverFlowError를
        발생시킨다. 정말 우리 구현이 스택 안전하지 않은지 확인하라. 그 후 다른 리스트 재귀 함수
        foldLeft를 2장에서 설명한 기법을 사용해 꼬리 재귀로 작성하라.
        다음은 foldLeft의 시그니처다.
         */
        tailrec fun <A, B> foldLeft(xs: KhsList<A>, z: B, f: (B, A) -> B): B
        = when(xs) {
            is KhsNil -> z
            is KhsCons -> foldLeft(xs.tail, f(z, xs.head), f)
        }
        /*
        연습문제 3.10
        foldLeft를 사용해 sum, product, 리스트 길이 계산 함수를 작성하라
         */
        fun sum10(l: KhsList<Int>): Int = foldLeft(l, 0) {acc, it -> acc+it}
        fun product10(l: KhsList<Double>): Double = foldLeft(l, 1.0) {acc, it -> acc*it}
//                = when(doubles) {
//            is KhsNil -> 1.0
//            is KhsCons ->
//                if(doubles.head == 0.0) 0.0
//                else doubles.head * product(doubles.tail)
//        }

    }
}
object KhsNil: KhsList<Nothing>()
data class KhsCons<out A>(val head: A, val tail: KhsList<A>): KhsList<A>()


//@file:Suppress("NOTHING_TO_INLINE", "FunctionName")
//import FList.Nil
//import FList.Cons
//
//sealed class FList<out ITEM: Any> {
//    companion object {
//        inline operator fun <ITEM : Any> invoke(vararg items: ITEM): FList<ITEM> = items.foldRight(invoke(), ::Cons)
//        inline operator fun <ITEM : Any> invoke(): FList<ITEM> = Nil
//    }
//
//    data object Nil : FList<Nothing>()
//    data class Cons<out ITEM : Any> @PublishedApi internal constructor(
//        @PublishedApi internal val head: ITEM,
//        @PublishedApi internal val tail: FList<ITEM>
//    ) : FList<ITEM>()
//}
//
////** base --------------------------------------------------
//tailrec fun <ITEM: Any, ACC: Any> FList<ITEM>.fold(acc: ACC, block: (ACC, ITEM)-> ACC): ACC
//= when(this) {
//    is Cons-> tail.fold(block(acc, head), block)
//    is Nil -> acc
//}
//inline fun <ITEM: Any, ACC: Any> FList<ITEM>.foldRight(base: ACC, crossinline block: (ITEM, ACC) -> ACC): ACC
//= reverse().fold(base) { acc, it -> block(it, acc) }
//fun <ITEM: Any, ACC: Any> FList<ITEM>.foldRightWhile(base: ACC, cond: (ITEM)-> Boolean, block: (ITEM, ACC)-> ACC): ACC
//= _foldRightWhile(base, cond, block) {it}
//tailrec fun <ITEM: Any, ACC: Any> FList<ITEM>._foldRightWhile(base: ACC, cond: (ITEM) -> Boolean, origin: (ITEM, ACC) -> ACC, block: (ACC) -> ACC): ACC
//= when(this) {
//    is Cons-> if(cond(head)) when(tail) {
//        is Cons-> tail._foldRightWhile(base, cond, origin) {acc -> block(origin(head, base)) }
//        is Nil-> block(origin(head, base))
//    } else block(base)
//    is Nil-> base
//}
//fun <ITEM: Any, ACC: Any> FList<ITEM>.foldRightIndexed(base: ACC, block: (Int, ITEM, ACC)-> ACC): ACC
//= reverse().foldIndexed(base) {index, acc, it -> block(index, it, acc)}
//inline fun <ITEM: Any, OTHER: Any> FList<ITEM>.map(crossinline block: (ITEM) -> OTHER): FList<OTHER>
//= foldRight(FList()) {it, acc -> Cons(block(it), acc)}
//inline fun <ITEM: Any, OTHER: Any> FList<ITEM>.flatMap(noinline block: (ITEM)-> FList<OTHER>): FList<OTHER>
//= foldRight(FList()) {it, acc ->
//    when(val v = block(it)) {
//        is Cons-> v.foldRight(acc, ::Cons)
//        is Nil-> acc
//    }
//}
////flatten
//fun <ITEM: Any> FList<FList<ITEM>>.flatten(): FList<ITEM>
//= foldRight(FList()) {it, acc -> it.foldRight(acc, ::Cons)}
//fun<ITEM: Any, ACC: Any> FList<ITEM>.fold1(base: ACC, block: (ACC, ITEM)-> ACC): ACC
//= foldRight({ it:ACC-> it }) { item, acc->
//    { acc(block(it, item)) }
//}(base)
//inline fun <ITEM: Any, ACC: Any> FList<ITEM>.foldIndexed(base: ACC, noinline block: (Int, ACC, ITEM) -> ACC): ACC
//= fold(base to 0) { (acc, index), it -> block(index, acc, it) to index + 1 }.first
//inline fun <ITEM : Any> FList<ITEM>.reverse(): FList<ITEM>
//= fold(FList()) { acc, it -> Cons(it, acc) }
//fun<ITEM: Any, ACC: Any> FList<ITEM>.foldRight1(acc: ACC, block: (ITEM, ACC) -> ACC): ACC
//= when(this) {
//    is Cons-> block(head, tail.foldRight1(acc, block))
//    is Nil-> acc
//}
//fun<ITEM: Any, ACC: Any> FList<ITEM>.foldRight2(acc: ACC, block: (ITEM, ACC) -> ACC): ACC
//= _foldRight(acc, block) {it}
//fun<ITEM: Any, ACC: Any> FList<ITEM>.foldRight3(base: ACC, block: (ITEM, ACC) -> ACC): ACC
//= fold({ it: ACC -> it }) { acc, item ->
//    { acc(block(item, it)) }
//}(base)
//fun <ITEM: Any, OTHER: Any> FList<ITEM>.flatMap2(block: (ITEM) -> FList<OTHER>): FList<OTHER>
//= map(block).flatten()
//fun <ITEM: Any> FList<FList<ITEM>>.flatten1(): FList<ITEM>
//= when(this) {
//    is Cons-> drop(1).fold(head) {acc, it -> acc.append(it)}
//    is Nil-> this
//}
//tailrec fun<ITEM: Any, ACC: Any> FList<ITEM>._foldRight(base: ACC, origin: (ITEM, ACC)-> ACC, block: (ACC) -> ACC): ACC
//= when(this) {
//    is Cons-> when(tail) {
//        is Cons-> tail._foldRight(base, origin) { acc -> block(origin(head, acc))}
//        is Nil-> block(origin(head, base))
//    }
//    is Nil-> base
//}
//
////** addFirst --------------------------------------------------
//inline val <ITEM: Any> FList<ITEM>.size: Int get() = this._size(0)
//inline val <ITEM: Any> FList<ITEM>.size1: Int get() = this.foldRight(0) {_, acc -> acc + 1}
//@PublishedApi internal tailrec fun <ITEM: Any> FList<ITEM>._size(acc: Int): Int
//= when(this) {
//    is Cons-> tail._size(acc + 1)
//    is Nil-> acc
//}
//inline fun <ITEM: Any> FList<ITEM>.toList(): List<ITEM>
//= fold(listOf()) {acc, it -> acc + it}
//inline fun <ITEM: Any> FList<ITEM>.setHead(item: ITEM): FList<ITEM>
//= when(this) {
//    is Cons-> Cons(item, tail)
//    is Nil-> this
//}
//inline fun <ITEM: Any> FList<ITEM>.addFirst(item: ITEM): FList<ITEM>
//= when(this) {
//    is Cons-> Cons(item, this)
//    is Nil-> this
//}
//
////** append --------------------------------------------------
//fun <ITEM : Any> FList<ITEM>.append(list: FList<ITEM> = FList()): FList<ITEM> = foldRight(list, ::Cons)
//fun <ITEM: Any> FList<ITEM>.append1(list: FList<ITEM>): FList<ITEM>
//= when(this) {
//    is Cons-> Cons(head, tail.append1(list))
//    is Nil-> list
//}
//inline fun <ITEM: Any> FList<ITEM>.copy(): FList<ITEM> = append()
//inline operator fun <ITEM: Any> FList<ITEM>.plus(list: FList<ITEM>): FList<ITEM> = append(list)
//
////** drop --------------------------------------------------
//tailrec fun <ITEM: Any> FList<ITEM>.drop(n: Int = 1): FList<ITEM>
//= if(n > 0 && this is Cons) tail.drop(n - 1) else this
//tailrec fun <ITEM: Any> FList<ITEM>.dropWhile(block: (ITEM) -> Boolean): FList<ITEM>
//= if(this is Cons && block(head)) tail.dropWhile(block) else this
//inline fun <ITEM: Any> FList<ITEM>.dropWhileIndexed(noinline block: (Int, ITEM) -> Boolean): FList<ITEM>
//= _dropWhileIndexed(0, block)
//@PublishedApi
//internal tailrec fun <ITEM: Any> FList<ITEM>._dropWhileIndexed(index: Int, block: (Int, ITEM) -> Boolean): FList<ITEM>
//= if(this is Cons && block(index, head)) tail._dropWhileIndexed(index + 1, block) else this
//
////** dropLast --------------------------------------------------
//@PublishedApi
//internal tailrec fun <ITEM: Any> FList<ITEM>._dropLastWhileIndexed(index: Int, block: (Int, ITEM) -> Boolean): FList<ITEM>
//= when(this) {
//    is Cons-> if(!block(index, head)) reverse() else tail._dropLastWhileIndexed(index + 1, block)
//    is Nil-> reverse()
//}
//inline fun <ITEM: Any> FList<ITEM>.dropLastWhileIndexed(noinline block: (Int, ITEM) -> Boolean): FList<ITEM>
//= reverse()._dropLastWhileIndexed(0, block)
//inline  fun <ITEM: Any> FList<ITEM>.dropLastWhile(noinline block: (ITEM) -> Boolean): FList<ITEM>
//= reverse()._dropLastWhileIndexed(0) {_, it -> block(it)}
//fun <ITEM: Any> FList<ITEM>.dropLastOne(): FList<ITEM>
//= when(this) {
//    is Cons-> if(tail is Nil) Nil else Cons(head, tail.dropLastOne())
//    is Nil-> this
//}
//inline fun <ITEM: Any> FList<ITEM>.dropLast(n: Int = 1): FList<ITEM>
//= reverse()._dropLastWhileIndexed(0) {index, _ -> index < n}
//fun <ITEM: Any> FList<ITEM>.dropLast1(n: Int = 1): FList<ITEM>
//= when(this) {
//    is Cons-> if(n > 0) dropLastOne().dropLast(n - 1) else this
//    is Nil-> this
//}
//
////** utils --------------------------------------------------
//fun <ITEM: Any> FList<ITEM>.filter(block: (ITEM) -> Boolean): FList<ITEM>
//= foldRight(FList()) {it, acc -> if(block(it)) Cons(it, acc) else acc}
//fun <ITEM: Any> FList<ITEM>.filter1(block: (ITEM)-> Boolean): FList<ITEM>
//= flatMap {if(block(it)) FList(it) else Nil }
//tailrec fun <ITEM: Any> FList<ITEM>.sliceFrom(item: ITEM): FList<ITEM>
//= if(this is Cons && head != item) tail.sliceFrom(item) else this
//tailrec fun <ITEM: Any> FList<ITEM>.slice(from: Int): FList<ITEM>
//= if(this is Cons && from > 0) tail.slice(from - 1) else this
//inline fun <ITEM: Any> FList<ITEM>.slice(from: Int, to: Int): FList<ITEM>
//= slice(from).dropLast(to - from)
//tailrec fun <ITEM: Any> FList<ITEM>.startWith(target: FList<ITEM>): Boolean
//= when(this) {
//    is Cons-> when(target) {
//        is Cons-> if(head == target.head) tail.startWith(target.tail) else false
//        is Nil-> true
//    }
//    is Nil-> target is Nil
//}
//fun <ITEM: Any> FList<ITEM>.startWith1(target: FList<ITEM>): Boolean
//= fold(true to target) {(acc, other), it ->
//    when(other) {
//        is Cons-> if(acc && it == other.head) true to other.tail else false to FList()
//        is Nil-> acc to other
//    }
//}.first
//tailrec operator fun <ITEM: Any> FList<ITEM>.contains(target: ITEM): Boolean
//= when(this) {
//    is Cons-> if(head == target) true else target in tail
//    is Nil-> false
//}
//tailrec operator fun <ITEM: Any> FList<ITEM>.contains(target: FList<ITEM>): Boolean
//= when(this) {
//    is Cons-> when(target) {
//        is Cons-> if(startWith(target)) true else target in tail
//        is Nil-> false
//    }
//    is Nil-> target is Nil
//}
//inline fun <ITEM: Any, OTHER: Any, RESULT: Any> FList<ITEM>.zipWith(other: FList<OTHER>, noinline block: (ITEM, OTHER) -> RESULT): FList<RESULT>
//= fold(FList<RESULT>() to other) {(acc, other), it ->
//    when(other) {
//        is Cons-> Cons(block(it, other.head), acc) to other.tail
//        is Nil-> acc to other
//    }
//}.first.reverse()
//
//sealed class FTree<out ITEM: Any> {
//    companion object{
//        operator fun <ITEM: Any> invoke(left: FTree<ITEM>, right: FTree<ITEM>): FTree<ITEM> = Branch(left, right)
//        operator fun <ITEM: Any> invoke(value: ITEM): FTree<ITEM> = Leaf(value)
//    }
//
//    data class Leaf<ITEM: Any> @PublishedApi internal constructor(
//        @PublishedApi internal  val item: ITEM
//    ): FTree<ITEM>()
//    data class Branch<ITEM: Any> @PublishedApi internal constructor(
//        @PublishedApi internal val left: FTree<ITEM>,
//        @PublishedApi internal val right: FTree<ITEM>
//    ): FTree<ITEM>()
//}
//
//
//sealed class TList<out ITEM: Any> {
//    companion object{
//        fun <ITEM: Any> of(vararg items: ITEM): TList<ITEM> {
//            return if(items.isEmpty()) TNil
//            else {
//                TCons(items[0], of(*items.sliceArray(1..<items.size)))
//            }
//        }
//    }
//}
//data object TNil: TList<Nothing>()
//data class TCons<out ITEM : Any> internal constructor(
//    internal val head: ITEM,
//    internal val tail: TList<ITEM>
//) : TList<ITEM>()
//
///*
//Cons(1, Cons(2, Cons(3, Cons(4, Cons(5,Nil)))))
//
//foldRight
//[1, 2, 3, 4, 5]
//val base = 0
//val Add = (it, acc)-> it + acc
//list.foldRight(base, Add)
//==> 헤더에 꼬리 전체의 합을 더한다.
//= 1+ [2, 3, 4, 5]
//= 1+ (2+ [3, 4, 5])
//= 1+ (2+ (3+ [4, 5]))
//= 1+ (2+ (3+ (4+ [5])))
//= 1+ (2+ (3+ (4+ (5 + []))))
//= 1+ (2+ (3+ (4+ (5 + 0))))
//
//
//
//Add(1, Add(2, Add(3, Add(4, Add(5, 0)))))
//0= nil
//5= c5.h
//5= Add(5, 0)= Add(c5.h, nil)
//    = c4.t.foldRight(c5.h, nil)
//9= Add(4, 5)= Add(c4.h, c4.t.foldRight(c5.h, nil))
//    = c3.t.foldRight(c4.h, c4.t.foldRight)
//12= Add(3, 9)= Add(c3.h, c4.t.foldRight(c4.h, c5.t.foldRight(c5.h, nil)))
//    = c2.t.foldRight(c3.h, c3.t.foldRight)
//14= Add(2, 12)= Acc(c2.h, c3.t.foldRight(c3.h, c4.t.foldRight(c4.h, c5.t.foldRight(c5.h, nil))))
//    = c1.t.foldRight(c2.h, c2.t.foldRight)
//15= Add(1, 14)= Acc(c1.h, c2.t.foldRight(c2.h, c3.t.foldRight(c3.h, c4.t.foldRight(c4.h, c5.t.foldRight(c5.h, nil)))))
//    = list.foldRight(acc, Add)
//
//fold
//[1, 2, 3, 4, 5]
//==> 꼬리에서 한개씩 빼서 헤더에 계속 더한다.
//=> 1 + [2, 3, 4, 5]
//=> (1 + 2) + [3, 4, 5]
//=> ((1+2)+3)+ [4, 5]
//=> (((1+2)+3)+4)+ [5]
//=> ((((1+2)+3)+4)+5) 0
//*/