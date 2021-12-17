package com.example.easy_learning;


import androidx.test.espresso.DataInteraction;
import androidx.test.espresso.ViewInteraction;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import static androidx.test.InstrumentationRegistry.getInstrumentation;
import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static androidx.test.espresso.action.ViewActions.*;
import static androidx.test.espresso.assertion.ViewAssertions.*;
import static androidx.test.espresso.matcher.ViewMatchers.*;

import com.example.easy_learning.R;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.is;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class RegisterActivityTest {

    @Rule
    public ActivityTestRule<RegisterActivity> mActivityTestRule = new ActivityTestRule<>(RegisterActivity.class);

    @Test
    public void registerActivityTest() {
        ViewInteraction materialButton = onView(
                allOf(withId(R.id.btn_register_register), withText("Register"),
                        childAtPosition(
                                allOf(withId(R.id.linear2_register),
                                        childAtPosition(
                                                withId(R.id.linear1_register),
                                                6)),
                                1),
                        isDisplayed()));
        materialButton.perform(click());

        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.et_nama_register),
                        childAtPosition(
                                allOf(withId(R.id.linear1_register),
                                        childAtPosition(
                                                withId(R.id.constraint_register),
                                                0)),
                                1),
                        isDisplayed()));
        appCompatEditText.perform(replaceText("candra"), closeSoftKeyboard());

        ViewInteraction materialButton2 = onView(
                allOf(withId(R.id.btn_register_register), withText("Register"),
                        childAtPosition(
                                allOf(withId(R.id.linear2_register),
                                        childAtPosition(
                                                withId(R.id.linear1_register),
                                                6)),
                                1),
                        isDisplayed()));
        materialButton2.perform(click());

        ViewInteraction appCompatEditText2 = onView(
                allOf(withId(R.id.et_telepon_register),
                        childAtPosition(
                                allOf(withId(R.id.linear1_register),
                                        childAtPosition(
                                                withId(R.id.constraint_register),
                                                0)),
                                2),
                        isDisplayed()));
        appCompatEditText2.perform(replaceText("081227287189"), closeSoftKeyboard());

        ViewInteraction materialButton3 = onView(
                allOf(withId(R.id.btn_register_register), withText("Register"),
                        childAtPosition(
                                allOf(withId(R.id.linear2_register),
                                        childAtPosition(
                                                withId(R.id.linear1_register),
                                                6)),
                                1),
                        isDisplayed()));
        materialButton3.perform(click());

        ViewInteraction appCompatEditText3 = onView(
                allOf(withId(R.id.et_umur_register),
                        childAtPosition(
                                allOf(withId(R.id.linear1_register),
                                        childAtPosition(
                                                withId(R.id.constraint_register),
                                                0)),
                                3),
                        isDisplayed()));
        appCompatEditText3.perform(replaceText("25"), closeSoftKeyboard());

        ViewInteraction materialButton4 = onView(
                allOf(withId(R.id.btn_register_register), withText("Register"),
                        childAtPosition(
                                allOf(withId(R.id.linear2_register),
                                        childAtPosition(
                                                withId(R.id.linear1_register),
                                                6)),
                                1),
                        isDisplayed()));
        materialButton4.perform(click());

        ViewInteraction appCompatEditText4 = onView(
                allOf(withId(R.id.et_email_register),
                        childAtPosition(
                                allOf(withId(R.id.linear1_register),
                                        childAtPosition(
                                                withId(R.id.constraint_register),
                                                0)),
                                4),
                        isDisplayed()));
        appCompatEditText4.perform(replaceText("candrapointblank123@gmail.com"), closeSoftKeyboard());

        ViewInteraction materialButton5 = onView(
                allOf(withId(R.id.btn_register_register), withText("Register"),
                        childAtPosition(
                                allOf(withId(R.id.linear2_register),
                                        childAtPosition(
                                                withId(R.id.linear1_register),
                                                6)),
                                1),
                        isDisplayed()));
        materialButton5.perform(click());

        ViewInteraction appCompatEditText5 = onView(
                allOf(withId(R.id.et_password_register),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.linear1_register),
                                        5),
                                0),
                        isDisplayed()));
        appCompatEditText5.perform(replaceText("123"), closeSoftKeyboard());

        ViewInteraction materialButton6 = onView(
                allOf(withId(R.id.btn_register_register), withText("Register"),
                        childAtPosition(
                                allOf(withId(R.id.linear2_register),
                                        childAtPosition(
                                                withId(R.id.linear1_register),
                                                6)),
                                1),
                        isDisplayed()));
        materialButton6.perform(click());

        ViewInteraction appCompatEditText6 = onView(
                allOf(withId(R.id.et_password_register), withText("123"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.linear1_register),
                                        5),
                                0),
                        isDisplayed()));
        appCompatEditText6.perform(click());

        ViewInteraction appCompatEditText7 = onView(
                allOf(withId(R.id.et_password_register), withText("123"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.linear1_register),
                                        5),
                                0),
                        isDisplayed()));
        appCompatEditText7.perform(replaceText("123456"));

        ViewInteraction appCompatEditText8 = onView(
                allOf(withId(R.id.et_password_register), withText("123456"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.linear1_register),
                                        5),
                                0),
                        isDisplayed()));
        appCompatEditText8.perform(closeSoftKeyboard());

        ViewInteraction materialButton7 = onView(
                allOf(withId(R.id.btn_register_register), withText("Register"),
                        childAtPosition(
                                allOf(withId(R.id.linear2_register),
                                        childAtPosition(
                                                withId(R.id.linear1_register),
                                                6)),
                                1),
                        isDisplayed()));
        materialButton7.perform(click());
    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}
