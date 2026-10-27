package com.example

import org.junit.Assert.*
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
  @Test
  fun addition_isCorrect() {
    assertEquals(4, 2 + 2)
  }

  @Test
  fun testTaskStatusTransitions() {
    val initial = com.example.data.model.TaskStatus.PUBLISHED
    assertEquals("Open for Application", initial.displayName)
    
    val approved = com.example.data.model.TaskStatus.APPROVED
    assertEquals("Verified & Approved", approved.displayName)
  }

  @Test
  fun testReviewScoreCalculation() {
    val codeQuality = 5
    val timeliness = 5
    val communication = 4
    val overall = ((codeQuality + timeliness + communication) / 3.0 * 10).toInt() / 10.0
    assertEquals(4.6, overall, 0.01)
  }
}
