package scalatests

import com.github.nscala_time.time.Imports._
import org.specs2.mutable._
import org.joda.time

class DateTimeTests extends Specification {

  // this is necessary to avoid ambiguous implicit formatting of e.g. INT.minutes
  override def intToRichLong(v: Int) = super.intToRichLong(v)

  val inputPattern = "dd.MM.yyyy HHmm"
  val inputFormatter = DateTimeFormat.forPattern(inputPattern)
  val outputPattern = "dd.MM.yyyy HH:mm"
  val outputFormatter = DateTimeFormat.forPattern(outputPattern)

  "a date time" should {
    val dateTime1 = inputFormatter.parseDateTime("02.03.2013 1330")
    "be parsed and printed" in {
      outputFormatter.print(dateTime1) mustEqual "02.03.2013 13:30"
    }
    "build a new time by adding some minutes" in {
      val dateTime2 = dateTime1 + 45.minutes
      outputFormatter.print(dateTime2) mustEqual "02.03.2013 14:15"
    }
  }

  "an interval" should {
    val dateTime1 = inputFormatter.parseDateTime("02.03.2013 1330")
    val dateTime2 = dateTime1 + 45.minutes
    val interval: time.Interval = dateTime1 to dateTime2
    "be buildable with two date times" in {
      outputFormatter.print(interval.getStart) mustEqual "02.03.2013 13:30"
      outputFormatter.print(interval.getEnd) mustEqual "02.03.2013 14:15"
      interval.toDuration.getStandardMinutes mustEqual 45
    }
    "be extendable by some minutes" in {
      val newInterval = interval.withDurationAfterStart(interval.toDuration + 10.minutes)
      outputFormatter.print(newInterval.getStart) mustEqual "02.03.2013 13:30"
      outputFormatter.print(newInterval.getEnd) mustEqual "02.03.2013 14:25"
      newInterval.toDuration.getStandardMinutes mustEqual 55
    }
    "be shifted by some time" in {
      val newInterval = new Interval(interval.getStart + 1.week + 30.minutes, interval.toDuration)
      outputFormatter.print(newInterval.getStart) mustEqual "09.03.2013 14:00"
      outputFormatter.print(newInterval.getEnd) mustEqual "09.03.2013 14:45"
      newInterval.toDuration.getStandardMinutes mustEqual 45
    }



  }


}
