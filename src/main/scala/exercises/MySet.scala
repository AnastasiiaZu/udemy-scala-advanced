package exercises

import scala.annotation.tailrec

trait MySet[A] extends (A => Boolean) {

  def apply(elem: A): Boolean =
    contains(elem)
  def contains(elem: A): Boolean
  def +(elem: A): MySet[A]
  def ++(anotherSet: MySet[A]): MySet[A]

  def map[B](f: A => B): MySet[B]
  def flatMap[B](f: A => MySet[B]): MySet[B]
  def filter(predicate: A => Boolean): MySet[A]
  def forEach(f: A => Unit): Unit
}

class EmptySet[A] extends MySet[A]{

  def contains(elem: A): Boolean = false
  def +(elem: A): MySet[A] = new NonEmptySet[A](elem, this)
  def ++(anotherSet: MySet[A]): MySet[A] = anotherSet

  def map[B](f: A => B): MySet[B] = new EmptySet[B]
  def flatMap[B](f: A => MySet[B]): MySet[B]  = new EmptySet[B]
  def filter(predicate: A => Boolean): MySet[A] = this
  def forEach(f: A => Unit): Unit = ()

}

class NonEmptySet[A](head: A, tail: MySet[A]) extends MySet[A]{

  def contains(elem: A): Boolean = {
    elem == head || tail.contains(elem)
  }
  def +(elem: A): MySet[A] = {
    if (this contains elem) this
    else new NonEmptySet[A](elem, this)
  }
//    [1, 2, 3] ++ [4, 5] =
//    [2, 3] ++ [4, 5] + 1 =
//    [3] ++ [4, 5] + 1 + 2 =
//    [] ++ [4, 5, 1, 2, 3] =
//    [4, 5, 1, 2, 3]

   def ++(anotherSet: MySet[A]): MySet[A] = {
    tail ++ anotherSet + head
  }

  def map[B](f: A => B): MySet[B] = tail.map(f) + f(head) // OR define it through a flatMap = flatMap(x => MySet(f(x)))
  def flatMap[B](f: A => MySet[B]): MySet[B] = tail.flatMap(f) ++ f(head)
  def filter(predicate: A => Boolean): MySet[A] = {
    val filteredTail = tail.filter(predicate)
    if (predicate(head)) filteredTail + head
    else filteredTail
  }
  def forEach(f: A => Unit): Unit = {
    f(head)
    tail.forEach(f)
  }
}

object MySet {

  def apply[A](values: A*): MySet[A] = {
    @tailrec
    def buildSet(valueSeq: Seq[A], acc: MySet[A]): MySet[A] = {
      if (valueSeq.isEmpty) acc
      else buildSet(valueSeq.tail, acc + valueSeq.head)
    }
    buildSet(values.toSeq, new EmptySet[A])
  }
}

object Playground extends App {

  val myAwesomeSet = MySet(11, 22, 33)


  val normalSet = Set(1,2,3)

  normalSet + 1

  myAwesomeSet + 55 ++ MySet(66, -77) flatMap (x => MySet(x, x * 10)) filter (_ % 5 == 0) forEach println

}
