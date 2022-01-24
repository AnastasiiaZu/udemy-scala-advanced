package myLectures.part2af

import exercises.{MySet, NonEmptySet, EmptySet}

object FunctionalCollections extends App {

  // e.g. Sets are callable like functions set(2) to return a boolean whether the element exists in a set
  // Set is a function[A => Boolean]!!!

  val set = Set(1,2,3)

  println(set(1))

  val mySet = NonEmptySet[Int](34, NonEmptySet[Int](45, NonEmptySet[Int](33, EmptySet[Int])))

  println(mySet(33))
}
