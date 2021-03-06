package exercises

import scala.annotation.tailrec

trait MySet[A] extends (A => Boolean) {

  def apply(elem: A): Boolean =
    contains(elem)
  def contains(elem: A): Boolean
  def +(elem: A): MySet[A]
  def ++(anotherSet: MySet[A]): MySet[A] // "Union"
  def map[B](f: A => B): MySet[B]
  def flatMap[B](f: A => MySet[B]): MySet[B]
  def filter(predicate: A => Boolean): MySet[A]
  def foreach(f: A => Unit): Unit
// part 2
  def -(elem: A): MySet[A]
  def --(anotherSet: MySet[A]): MySet[A] // "Difference"
  def &(anotherSet: MySet[A]): MySet[A] // "Intersection"

  def unary_! : MySet[A]
}

class EmptySet[A] extends MySet[A] {

  def contains(elem: A): Boolean = false
  def +(elem: A): MySet[A] = new NonEmptySet[A](elem, this)
  def ++(anotherSet: MySet[A]): MySet[A] = anotherSet

  def map[B](f: A => B): MySet[B] = new EmptySet[B]
  def flatMap[B](f: A => MySet[B]): MySet[B] = new EmptySet[B]
  def filter(predicate: A => Boolean): MySet[A] = this
  def foreach(f: A => Unit): Unit = ()

  // part 2
  def -(elem: A): MySet[A] = this
  def --(anotherSet: MySet[A]): MySet[A] = this
  def &(anotherSet: MySet[A]): MySet[A] = this

  def unary_! : MySet[A] = new PropertyBasedSet[A](x => true)
}

class NonEmptySet[A](head: A, tail: MySet[A]) extends MySet[A] {

  def contains(elem: A): Boolean = {
    elem == head || tail.contains(elem)
  }
  def +(elem: A): MySet[A] = {
    if (this contains elem ) this // syntactic sugar!
    else new NonEmptySet[A](elem, this)
  }
  /*
  [1 2 3] ++ [4 5] =
  [2 3] ++ [4 5] + 1 =
  [3] ++ [4 5] + 1 + 2 =
  [] ++ [4 5] + 1 + 2 + 3 =
  [4 5] + 1 + 2 + 3 = [4 5 1 2 3]
  */
  def ++(anotherSet: MySet[A]): MySet[A] = {
    tail ++ anotherSet + head
  }
  def map[B](f: A => B): MySet[B] = (tail map f) + f(head)
  def flatMap[B](f: A => MySet[B]): MySet[B] = (tail flatMap f) ++ f(head)
  def filter(predicate: A => Boolean): MySet[A] = {
    val filteredTail = tail filter predicate
    if (predicate(head)) filteredTail + head
    else filteredTail
  }
  def foreach(f: A => Unit): Unit = {
    f(head)
    tail foreach f
  }

  // part 2
  def -(elem: A): MySet[A] = {
    if (head == elem) tail
    else tail - elem + head
  }
  def --(anotherSet: MySet[A]): MySet[A] = filter(!anotherSet)
  def &(anotherSet: MySet[A]): MySet[A] = filter(anotherSet) // intesection & filtering is the same thing // == filter(x => anotherSet.contains(x))

  // new operator
  def unary_! : MySet[A] = new PropertyBasedSet[A](x => !this.contains(x))
}

class AllInclusiveSet[A] extends MySet[A] {

  override def contains(elem: A): Boolean = true
  override def +(elem: A): MySet[A] = this
  override def ++(anotherSet: MySet[A]): MySet[A] = this

  override def map[B](f: A => B): MySet[B] = ???
  override def flatMap[B](f: A => MySet[B]): MySet[B] = ???
  override def foreach(f: A => Unit): Unit = ???

  override def filter(predicate: A => Boolean): MySet[A] = ???
  override def -(elem: A): MySet[A] = ???
  override def --(anotherSet: MySet[A]): MySet[A] = filter(!anotherSet)
  override def &(anotherSet: MySet[A]): MySet[A] = filter(anotherSet)

  override def unary_! : MySet[A] = new EmptySet[A] // AllInclusiveSet and EmptySet are the opposite
}

// all elements of type A which satisfy a property
// x in A | propperty(x)
class PropertyBasedSet[A](property: A => Boolean) extends MySet[A] {

  def contains(elem: A): Boolean = property(elem)
  def +(elem: A): MySet[A] = {
    new PropertyBasedSet[A](x => property(x) || x == elem)
  }
  def ++(anotherSet: MySet[A]): MySet[A] = {
    new PropertyBasedSet[A](x => property(x) || anotherSet(x))
  }
  // all integers => (_ % 3) => [0 1 2]
  def map[B](f: A => B): MySet[B] = politelyFail
  def flatMap[B](f: A => MySet[B]): MySet[B] = politelyFail
  def foreach(f: A => Unit): Unit = politelyFail

  def filter(predicate: A => Boolean): MySet[A] = new PropertyBasedSet[A](x => property(x) && predicate(x))

  // part 2
  def -(elem: A): MySet[A] = filter(x => x != elem)
  def --(anotherSet: MySet[A]): MySet[A] = filter(!anotherSet)
  def &(anotherSet: MySet[A]): MySet[A] = filter(anotherSet)

  def unary_! : MySet[A] = new PropertyBasedSet[A](x => !property(x))

  def politelyFail = throw new IllegalArgumentException("Realy deep rabbit hole!")
}

object MySet {
  def apply[A](values: A*): MySet[A] = {
    def buildSet(valSeq: Seq[A], acc: MySet[A]): MySet[A] = {
      if (valSeq.isEmpty) acc
      else buildSet(valSeq.tail, acc + valSeq.head)
    }
      buildSet(values.toSeq, new EmptySet[A])
  }
}

object MySetPlayground extends App {
  val s = MySet(1,2,3)
  s + 5 ++ MySet(-4, -5) + 3 flatMap (x => MySet(x, 10 * x)) filter (_ % 2 == 0) foreach println

  val negative = !s
  println(negative(2))
  println(negative(5))
}
