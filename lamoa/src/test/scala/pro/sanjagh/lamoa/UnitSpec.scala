package pro.sanjagh.lamoa

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.{BeforeAndAfter, OptionValues}

abstract class UnitSpec
    extends AnyWordSpec
    with BeforeAndAfter
    with OptionValues
    with Matchers
