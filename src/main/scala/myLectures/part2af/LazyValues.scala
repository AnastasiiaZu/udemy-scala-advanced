package myLectures.part2af

import scala.annotation.targetName

object LazyValues extends App {

  val normalVal = false
  lazy val lazyVal = {
    println("Boo!")
    true
  }

  val boolean = if (normalVal && lazyVal) true else false
  println(boolean)

  // in conjunction with call by name
  def byNameMethod(n: => Int): Int = {
    // CALL BY NEED
    lazy val t = n // only evaluated once
    t + t + t + 1
  }
  def retrieveMagicValue = {
    // side effect or a long computation
    println("waiting")
    Thread.sleep(1000)
    42
  }

  println(byNameMethod(retrieveMagicValue))

}

abstract class MyStream[+A] {
  def isEmpty: Boolean
  def head: A
  def tail: MyStream[A]

  @targetName("prepend")
  def #::[B >: A](element: B): MyStream[B] //prepend operator
  def ++[B >: A](anotherStream: MyStream[B]): MyStream[B] // concatenate two streams

  def foreach(f: A => Unit): Unit
  def map[B](f: A => B): MyStream[B]
  def flatMap[B](f: A => MyStream[B]): MyStream[B]
  def filter(predicate: A => Boolean): MyStream[A]

  def take(n: Int): MyStream[A] // takes the first n elems out of this list
  def takeAsList(n: Int): List[A]
}

object MyStream {
  def from[A](start: A)(generator: A => A): MyStream[A] = ???
}

/*
* naturals = MyStream.from(1)(x => x + 1) = stream of natural numbers
naturals.take(100) // lazily evaluated stream of the first 100 naturals (finite stream)
naturals.foreach(println // will crash - it's infinite!
naturals.map(_ * 2) // stream of all even numbers (potentially infinite)
* */
