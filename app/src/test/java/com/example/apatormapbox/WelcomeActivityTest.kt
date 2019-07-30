package com.example.apatormapbox

import android.content.Intent
import android.os.Build
import android.view.View
import androidx.fragment.app.Fragment
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import com.example.apatormapbox.activities.MainActivity
import com.example.apatormapbox.fragments.MapFragment
import com.example.apatormapbox.fragments.SettingsFragment
import com.mapbox.geojson.Point
import com.mapbox.mapboxsdk.Mapbox.getApplicationContext
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.navigator.NavigationStatus
import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.Mockito.RETURNS_DEEP_STUBS
import org.mockito.Mockito.mock
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.Shadows
import org.robolectric.Shadows.shadowOf
import org.robolectric.annotation.Config
import org.robolectric.util.FragmentTestUtil.startFragment
import java.lang.Exception
import kotlin.reflect.full.createInstance
import org.mockito.Mockito.`when`

@Config(sdk = [Build.VERSION_CODES.O_MR1])
@RunWith(RobolectricTestRunner::class)
class WelcomeActivityTest {

//    @get:Rule
//    val rule = ActivityTestRule(MainActivity::class.java)
//
//    @Test
//    fun clickingLogin_shouldStartLoginActivity() {
//        shadowOf(rule.activity.findViewById<MapView>(R.id.mapView))
//        rule.activity.findViewById<View>(R.id.locate_device_btn).performClick()
//
//        onView(withId(R.id.locate_device_btn)).perform(click())
//
//        val expectedIntent = Intent(rule.activity, MapFragment::class.java)
//        val actual = shadowOf(RuntimeEnvironment.application).nextStartedActivity
//        assertEquals(expectedIntent.component, actual.component)
//    }
}