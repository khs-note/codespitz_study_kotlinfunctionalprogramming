import Option.None
import Option.Some
import java.time.OffsetDateTime
import kotlin.math.pow

sealed class Option<out ITEM> {
    data class Some<out ITEM>(val item: ITEM): Option<ITEM>()
    data object None: Option<Nothing>()

    companion object {
        operator fun <ITEM> invoke(item: ITEM) = Some(item)
        fun <ITEM> none(): Option<ITEM> = None
    }
}

/*
연습문제 4.2
앞에 있는 Option에 대한 모든 함수를 구현하라.
각 함수를 구현할 때 각 함수의 의미가 무엇이고 어떤 상황에서 각 함수를 사용하지 생각해보라.
나중에 각 함수를 언제 사용할지 살펴본다.
다음은 이 연습문제를 풀기 위한 몇 가지 힌트다.

- 매칭을 사용해도 좋다. 하지만 map과 getOrElse 이외의 모든 함수를 매칭 없이 구현 할 수 있다.
- map과 flapMap의 경우 타입 시그니처만으로 구현을 결정할 수 있다.
- getOrElse는 Option이 Some인 경우 결과를 반환하지만 Option이 None인 경우 주어진 디폴트 값을 반환한다.
- orElse는 첫 번째 Option의 값이 정의된 경우(즉, Some인 경우) 그 Option을 반환한다.
  그렇지 않은 경우 두 번째 Option을 반환한다.
*/

fun <ITEM, OTHER> Option<ITEM>.map(f: (ITEM)-> OTHER): Option<OTHER>
= when(this) {
    is Some -> Some(f(item))
    is None -> None
}

fun <ITEM> Option<ITEM>.getOrElse(default: ()-> ITEM): ITEM
= when(this) {
    is Some-> item
    is None-> default()
}

fun <ITEM, OTHER> Option<ITEM>.flatMap(f: (ITEM)-> Option<OTHER>): Option<OTHER>
= map(f).getOrElse { None }

fun <ITEM> Option<ITEM>.orElse(ob: ()-> Option<ITEM>): Option<ITEM>
= map() { Option(it) }.getOrElse { ob() }

fun <ITEM> Option<ITEM>.filter(f: (ITEM)-> Boolean): Option<ITEM>
= flatMap { v ->
    if(f(v)) Option(v) else None
}

/*
연습문제 4.2
flatMap을 사용해 variance 함수를 구현하라.
시퀀스의 평균이 m이면, 분산은 시퀀스의 원소를 x라 할 때 x-m을 제곱한 값의 평균이다.
코드로 쓰면(x - m).pow(2)라 할 수 있다.
리스트 4.2에서 만든 mean 메서드를 사용해 이 함수를 구현할 수 있다.
*/
fun mean(xs: FList<Double>): Option<Double>
= if(xs == FList<Double>()) None else Option(xs.sum() / xs.size)

fun variance(xs: FList<Double>): Option<Double>
= mean(xs).flatMap { v ->
    mean(xs.map { (it - v).pow(2) })
}

/*
연습문제 4.3
두 Option값을 이항 함수를 통해 조립하는 제네릭 함수 map2를 작성하라.
두 Option중 어느 하나라도 None이면 반환값도 None이다.
다음은 map2의 시그니처다.
*/
fun <P1, P2, RESULT> map2(p1: Option<P1>, p2: Option<P2>, f: (P1, P2)-> RESULT): Option<RESULT>
        = p1.flatMap { v1 ->
    p2.map { v2 -> f(v1, v2) }
}

fun <ITEM, P, RESULT> Option<ITEM>.mapTow(b: Option<P>, f: (ITEM, P)-> RESULT): Option<RESULT>
        = flatMap { v1 ->
    b.map { v2 -> f(v1, v2) }
}

/*
연습문제 4.4
원소가 Option인 리스트를 원소가 리스트인 Option으로 합쳐주는 sequence 함수를 작성하라.
반환되는 Option의 원소는 원래 리스트에서 Some인 값들만 모은 리스트다.
원래 리스트 안에 None이 단 하나라도 있으면 결괏값이 None이어야 하며,
그렇지 않으면 모든 정상 값이 모인 리스트가 들어 있는 Some이 결과값이어야 한다.
시그니처는 다음과 같다.
*/
fun <ITEM: Any> sequence(xs: FList<Option<ITEM>>): Option<FList<ITEM>>
= xs.foldRight(Option(FList())) {oa1: Option<ITEM>, oa2: Option<FList<ITEM>> ->
    map2(oa1, oa2) {a1: ITEM, a2: FList<ITEM> -> FList(a1).append(a2)}
}

/*
연습문제 4.5
traverse 함수를 구현하라.
map을 한 다음에 sequence를 하면 간단하지만, 리스트를 단 한번만 순회하는 더 효율적인 구현을 시도해보라.
코드를 작성하고 나면 sequence를 traverse를 사용해 구현하라.
*/
fun <ITEM: Any, OTHER: Any> traverse(xa: FList<ITEM>, f: (ITEM)-> Option<OTHER>): Option<FList<OTHER>>
= when(xa) {
    is FList.Cons->
        map2(f(xa.head), traverse(xa.tail, f)) {b, xb ->
            FList(b).append(xb)
        }
    is FList.Nil-> Option(FList.Nil)
}

fun <ITEM: Any> sequence2(xs: FList<Option<ITEM>>): Option<FList<ITEM>>
= traverse(xs) {it}

/*
연습문제 4.6
Right값에 대해 활용할 수 있는 map, flatMap, orElse, map2를 구현하라.
*/
sealed class Either<out LEFT, out RIGHT>
data class Left<out LEFT>(val value: LEFT): Either<LEFT, Nothing>()
data class Right<out RIGHT>(val value: RIGHT): Either<Nothing, RIGHT>()

fun <LEFT, RIGHT, OTHER> Either<LEFT, RIGHT>.map(f: (RIGHT)-> OTHER): Either<LEFT, OTHER>
= when(this) {
    is Left-> this
    is Right-> Right(f(value))
}

fun <LEFT, RIGHT, OTHER> Either<LEFT, RIGHT>.flatMap(f: (RIGHT)-> Either<LEFT, OTHER>): Either<LEFT, OTHER>
= when(this) {
    is Left-> this
    is Right-> f(value)
}

fun <LEFT, RIGHT> Either<LEFT, RIGHT>.orElse(f: ()-> Either<LEFT, RIGHT>): Either<LEFT, RIGHT>
= when(this) {
    is Left-> f()
    is Right-> this
}

fun <LEFT, RIGHT1, RIGHT2, OTHER> mapEither(
    e1: Either<LEFT, RIGHT1>,
    e2: Either<LEFT, RIGHT2>,
    f: (RIGHT1, RIGHT2)-> OTHER
): Either<LEFT, OTHER>
= e1.flatMap { r1 -> e2.map { r2 -> f(r1, r2) }}

/*
연습문제 4.7
Either에 대한 sequence와 traverse를 구현하라.
두 함수는 오류가 생긴 경우 최초로 발생한 오류를 반환해야 한다.
*/
fun <LEFT, ITEM: Any, OTHER: Any> traverseEither(xs: FList<ITEM>, f: (ITEM)-> Either<LEFT, OTHER>): Either<LEFT, FList<OTHER>>
= when(xs) {
    is FList.Nil-> Right(FList<OTHER>())
    is FList.Cons->
        mapEither(f(xs.head), traverseEither(xs.tail, f)) { b, xb ->
            FList(b).append(xb)
        }
}

fun <E, A: Any> sequenceEither(es: FList<Either<E, A>>): Either<E, FList<A>>
= traverseEither(es) {it}

/*
연습문제 4.8

*/