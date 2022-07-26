import scala.annotation.{tailrec, targetName}

abstract class MyStream[+A] {
  def isEmpty: Boolean
  def head: A
  def tail: MyStream[A]

  @targetName("prepend")
  def #::[B >: A](element: B): MyStream[B] //prepend operator
  @targetName("concatenate")
  def ++[B >: A](anotherStream: => MyStream[B]): MyStream[B] // concatenate two streams

  def foreach(f: A => Unit): Unit
  def map[B](f: A => B): MyStream[B]
  def flatMap[B](f: A => MyStream[B]): MyStream[B]
  def filter(predicate: A => Boolean): MyStream[A]

  def take(n: Int): MyStream[A] // takes the first n elems out of this list

  @tailrec
  final def toList[B >: A](acc: List[B] = Nil): List[B] = {
    if (isEmpty) acc
    else tail.toList(head :: acc)
  }
}

object EmptyStream extends MyStream[Nothing] {
  def isEmpty: Boolean = true
  def head: Nothing = throw new NoSuchElementException
  def tail: MyStream[Nothing] = throw new NoSuchElementException

  @targetName("prepend")
  def #::[B >: Nothing](element: B): MyStream[B] = new Cons(element, this)
  @targetName("concatenate")
  def ++[B >: Nothing](anotherStream: => MyStream[B]): MyStream[B] = anotherStream

  def foreach(f: Nothing => Unit): Unit = ()
  def map[B](f: Nothing => B): MyStream[B] = this
  def flatMap[B](f: Nothing => MyStream[B]): MyStream[B] = this
  def filter(predicate: Nothing => Boolean): MyStream[Nothing] = this

  def take(n: Int): MyStream[Nothing] = this
}

class Cons[+A](hd: A, tl: => MyStream[A]) extends MyStream[A] { // call by name 'tail: => MyStream[A]'
  def isEmpty: Boolean = false
  override val head: A = hd
  override lazy val tail: MyStream[A] = tl

/*
val s = new Cons(1, EmptyStream)
val prepended = 1 #:: s = new Cons(1, s)
*/
  @targetName("prepend")
  def #::[B >: A](element: B): MyStream[B] = new Cons(element, this)
  @targetName("concatenate")
  def ++[B >: A](anotherStream: => MyStream[B]): MyStream[B] = new Cons(head, tail ++ anotherStream)

  def foreach(f: A => Unit): Unit = {
    f(head)
    tail.foreach(f)
  }
  def map[B](f: A => B): MyStream[B] = new Cons(f(head), tail.map(f)) // map preserves lazy evaluation
  def flatMap[B](f: A => MyStream[B]): MyStream[B] = f(head) ++ tail.flatMap(f) // flatmap preserves lazy evaluation
  def filter(predicate: A => Boolean): MyStream[A] = {
    if (predicate(head)) new Cons(head, tail.filter(predicate)) // also lazily evaluated
    else tail.filter(predicate)
  }

  def take(n: Int): MyStream[A] = { // takes the first n elems out of this list
    if (n <= 0) EmptyStream
    else if (n == 1) new Cons (head, EmptyStream)
    else new Cons(head, tail.take(n - 1))
  }
}

object MyStream {
  def from[A](start: A)(generator: A => A): MyStream[A] = {
    new Cons(start, MyStream.from(generator(start))(generator))
  }
}

object StreamsPlayground extends App {

  val naturals = MyStream.from(1)(_ + 1)
  println(naturals.head)
  println(naturals.tail.head)
  println(naturals.tail.tail.head)

  val startFrom0 = 0 #:: naturals
  println(startFrom0.head) // == 0

  startFrom0.take(10000).foreach(println)

  println(startFrom0.filter(_ < 10).take(10).toList()) // does not guarantee whether this expression is a finite stream

  // Exercises on streams
  // 1. Fibonacci numbers
  // 2. Stream of prime numbers - Eratosthenes' sieve

  def fibonacci(first: Int, second: Int): MyStream[Int] = {
    new Cons(first, fibonacci(second, first + second))
  }

  def primeNumbers(numbers: MyStream[Int]): MyStream[Int] = {
    if (numbers.isEmpty) numbers
    else new Cons(numbers.head, primeNumbers(numbers.tail.filter(_ % numbers.head != 0)))
  }
  println(primeNumbers(MyStream.from(2)(_ + 1)).take(100).toList())
}