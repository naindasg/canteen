package com.example.canteen;


import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.espresso.ViewInteraction;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class PaymentActivityTest {

    @Rule
    public ActivityTestRule<LoginActivity> mActivityTestRule = new ActivityTestRule<>(LoginActivity.class);

    @Test
    public void paymentActivityTest2() {
        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.email),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                1),
                        isDisplayed()));
        appCompatEditText.perform(replaceText("nsg5@student.le.ac.uk"), closeSoftKeyboard());

        ViewInteraction appCompatEditText2 = onView(
                allOf(withId(R.id.password),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                4),
                        isDisplayed()));
        appCompatEditText2.perform(replaceText("password"), closeSoftKeyboard());

        ViewInteraction materialButton = onView(
                allOf(withId(R.id.loginButton), withText("Login"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                6),
                        isDisplayed()));
        materialButton.perform(click());

        try {
            Thread.sleep(8000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        ViewInteraction recyclerView = onView(
                allOf(withId(R.id.menu_reycler),
                        childAtPosition(
                                withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                                0)));
        recyclerView.perform(actionOnItemAtPosition(0, click()));

        ViewInteraction materialButton2 = onView(
                allOf(withId(R.id.addToBasket), withText("Add to basket"),
                        childAtPosition(
                                allOf(withId(R.id.linearLayout),
                                        childAtPosition(
                                                withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                                                4)),
                                1),
                        isDisplayed()));
        materialButton2.perform(click());

        ViewInteraction materialButton3 = onView(
                allOf(withId(R.id.viewBasket), withText("View basket"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                1),
                        isDisplayed()));
        materialButton3.perform(click());

        try {
            Thread.sleep(8000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        ViewInteraction materialButton4 = onView(
                allOf(withId(R.id.button_add_payment_new), withText("Make payment"),
                        childAtPosition(
                                allOf(withId(R.id.basket_layout),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                3),
                        isDisplayed()));
        materialButton4.perform(click());

        ViewInteraction cardNumberEditText = onView(
                allOf(withId(R.id.card_number_edit_text),
                        childAtPosition(
                                childAtPosition(
                                        allOf(withId(R.id.card_number_text_input_layout), withContentDescription("Card number")),
                                        0),
                                0),
                        isDisplayed()));
        cardNumberEditText.perform(click());

        ViewInteraction cardNumberEditText2 = onView(
                allOf(withId(R.id.card_number_edit_text),
                        childAtPosition(
                                childAtPosition(
                                        allOf(withId(R.id.card_number_text_input_layout), withContentDescription("Card number")),
                                        0),
                                0),
                        isDisplayed()));
        cardNumberEditText2.perform(replaceText("42424"), closeSoftKeyboard());

        ViewInteraction cardNumberEditText3 = onView(
                allOf(withId(R.id.card_number_edit_text), withText("4242 4"),
                        childAtPosition(
                                childAtPosition(
                                        allOf(withId(R.id.card_number_text_input_layout), withContentDescription("Card number")),
                                        0),
                                0),
                        isDisplayed()));
        cardNumberEditText3.perform(replaceText("4242 42424"));

        ViewInteraction cardNumberEditText4 = onView(
                allOf(withId(R.id.card_number_edit_text), withText("4242 42424"),
                        childAtPosition(
                                childAtPosition(
                                        allOf(withId(R.id.card_number_text_input_layout), withContentDescription("Card number")),
                                        0),
                                0),
                        isDisplayed()));


        ViewInteraction cardNumberEditText5 = onView(
                allOf(withId(R.id.card_number_edit_text), withText("4242 4242 4"),
                        childAtPosition(
                                childAtPosition(
                                        allOf(withId(R.id.card_number_text_input_layout), withContentDescription("Card number")),
                                        0),
                                0),
                        isDisplayed()));
        cardNumberEditText5.perform(replaceText("4242 4242 42424"));

        ViewInteraction cardNumberEditText6 = onView(
                allOf(withId(R.id.card_number_edit_text), withText("4242 4242 42424"),
                        childAtPosition(
                                childAtPosition(
                                        allOf(withId(R.id.card_number_text_input_layout), withContentDescription("Card number")),
                                        0),
                                0),
                        isDisplayed()));

        ViewInteraction cardNumberEditText7 = onView(
                allOf(withId(R.id.card_number_edit_text), withText("4242 4242 4242 4"),
                        childAtPosition(
                                childAtPosition(
                                        allOf(withId(R.id.card_number_text_input_layout), withContentDescription("Card number")),
                                        0),
                                0),
                        isDisplayed()));
        cardNumberEditText7.perform(replaceText("4242 4242 4242 4242"));

        ViewInteraction cardNumberEditText8 = onView(
                allOf(withId(R.id.card_number_edit_text), withText("4242 4242 4242 4242"),
                        childAtPosition(
                                childAtPosition(
                                        allOf(withId(R.id.card_number_text_input_layout), withContentDescription("Card number")),
                                        0),
                                0),
                        isDisplayed()));
        cardNumberEditText8.perform(closeSoftKeyboard());

        ViewInteraction expiryDateEditText = onView(
                allOf(withId(R.id.expiry_date_edit_text),
                        childAtPosition(
                                childAtPosition(
                                        allOf(withId(R.id.expiry_date_text_input_layout), withContentDescription("Expiration date")),
                                        0),
                                0),
                        isDisplayed()));
        expiryDateEditText.perform(replaceText("06"), closeSoftKeyboard());

        ViewInteraction expiryDateEditText2 = onView(
                allOf(withId(R.id.expiry_date_edit_text), withText("06/"),
                        childAtPosition(
                                childAtPosition(
                                        allOf(withId(R.id.expiry_date_text_input_layout), withContentDescription("Expiration date")),
                                        0),
                                0),
                        isDisplayed()));
        expiryDateEditText2.perform(replaceText("06/22"));

        ViewInteraction expiryDateEditText3 = onView(
                allOf(withId(R.id.expiry_date_edit_text), withText("06/22"),
                        childAtPosition(
                                childAtPosition(
                                        allOf(withId(R.id.expiry_date_text_input_layout), withContentDescription("Expiration date")),
                                        0),
                                0),
                        isDisplayed()));
        expiryDateEditText3.perform(closeSoftKeyboard());

        ViewInteraction cvcEditText = onView(
                allOf(withId(R.id.cvc_edit_text),
                        childAtPosition(
                                childAtPosition(
                                        allOf(withId(R.id.cvc_text_input_layout), withContentDescription("CVC")),
                                        0),
                                0),
                        isDisplayed()));
        cvcEditText.perform(replaceText("562"), closeSoftKeyboard());

        ViewInteraction postalCodeEditText = onView(
                allOf(withId(R.id.postal_code_edit_text),
                        childAtPosition(
                                childAtPosition(
                                        allOf(withId(R.id.postal_code_text_input_layout), withContentDescription("Postal code")),
                                        0),
                                0),
                        isDisplayed()));
        postalCodeEditText.perform(replaceText("lerghj"), closeSoftKeyboard());

        ViewInteraction materialButton5 = onView(
                allOf(withId(R.id.payButton), withText("Pay"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                1),
                        isDisplayed()));
        materialButton5.perform(click());

        ViewInteraction textView = onView(
                allOf(withId(R.id.textView9), withText("Thank you for ordering at Chi"),
                        withParent(withParent(withId(android.R.id.content))),
                        isDisplayed()));
        textView.check(matches(withText("Thank you for ordering at Chi")));
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
