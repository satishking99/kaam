package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.model.EscrowStatus
import com.example.data.model.RequestStatus
import com.example.data.model.ServiceCategory
import com.example.data.model.VerificationLevel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("KaamWala", appName)
  }

  @Test
  fun `verify verification levels hierarchy`() {
    assertEquals(1, VerificationLevel.BASIC.stepNumber)
    assertEquals(2, VerificationLevel.PHONE_VERIFIED.stepNumber)
    assertEquals(3, VerificationLevel.IDENTITY_VERIFIED.stepNumber)
    assertEquals(4, VerificationLevel.SKILL_VERIFIED.stepNumber)
    assertEquals(5, VerificationLevel.TRUSTED_WORKER.stepNumber)
  }

  @Test
  fun `verify escrow status values`() {
    assertNotNull(EscrowStatus.SECURED_IN_ESCROW)
    assertNotNull(EscrowStatus.RELEASED_TO_WORKER)
    assertNotNull(RequestStatus.PAID_RELEASED)
    assertNotNull(ServiceCategory.MISTRI)
  }
}
