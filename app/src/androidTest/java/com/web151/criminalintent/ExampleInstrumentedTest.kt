package com.web151.criminalintent

import android.view.View
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.contrib.RecyclerViewActions.scrollToPosition
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.hamcrest.Description
import com.web151.criminalintent.CrimeListFragment
import com.web151.criminalintent.R
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.web151.criminalintent", appContext.packageName)
    }
}

@RunWith(AndroidJUnit4::class)
class MyFragmentTest {
    @Test
    fun testFragmentUI() {
        val scenario = launchFragmentInContainer<CrimeDetailFragment>(themeResId = R.style.Theme_CriminalIntent)

        onView(withId(R.id.crime_date)).check(matches(isDisplayed()))
    }

    @Test
    fun testIfCallPoliceButtonDisplayed() {
        val scenario = launchFragmentInContainer<CrimeListFragment>(themeResId = R.style.Theme_CriminalIntent)
        val policeCrimePositions = mutableListOf<Int>()

        scenario.onFragment { fragment ->
            val crimeListViewModel = fragment.crimeListViewModel
            val crimesList = crimeListViewModel.crimes
            for (i in crimesList.indices) {
                if (crimesList[i].requiresPolice) {
                    policeCrimePositions.add(i)
                }
            }
        }

        if (policeCrimePositions.isNotEmpty()) {
            for (position in policeCrimePositions) {
                // Scroll to the item first
                onView(withId(R.id.crime_recycler_view))
                    .perform(scrollToPosition<RecyclerView.ViewHolder>(position))

                onView(withRecyclerView(R.id.crime_recycler_view).atPositionOnView(position, R.id.call_police_button))
                    .check(matches(isDisplayed()))
            }
        }
    }

    fun withRecyclerView(recyclerViewId: Int): RecyclerViewMatcher {
        return RecyclerViewMatcher(recyclerViewId)
    }

    class RecyclerViewMatcher(private val recyclerViewId: Int) {
        fun atPositionOnView(position: Int, targetViewId: Int): Matcher<View> {
            return object : TypeSafeMatcher<View>() {
                override fun describeTo(description: Description?) {
                    description?.appendText("RecyclerView item at position $position with view ID $targetViewId")
                }

                override fun matchesSafely(view: View): Boolean {
                    val recyclerView = view.rootView.findViewById<RecyclerView>(recyclerViewId)
                    val viewHolder = recyclerView.findViewHolderForAdapterPosition(position)
                        ?: return false  // If not visible, return false
                    return viewHolder.itemView.findViewById<View>(targetViewId) == view
                }
            }
        }
    }
}

