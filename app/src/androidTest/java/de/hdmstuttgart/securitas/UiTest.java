package de.hdmstuttgart.securitas;


import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.matcher.BoundedMatcher;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.filters.LargeTest;
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;


import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.action.ViewActions.swipeLeft;
import static androidx.test.espresso.action.ViewActions.swipeRight;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.core.internal.deps.guava.base.Preconditions.checkNotNull;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;


@LargeTest
@RunWith(AndroidJUnit4ClassRunner.class)
public class UiTest {


    //test only works when the password list is empty, otherwise there will be no match due to different positions in the recyclerView
    // starts from the PwListActivity to skip the authentication with fingerprint
    @Rule
    public ActivityScenarioRule<PwListActivity> activityScenarioRuleListActivity
            = new ActivityScenarioRule<>(PwListActivity.class);


    //tests persistence and delete function
    @Test
    public void manualPasswordCreationTest() {

        //creates password + saves it
        onView(withId(R.id.add_password_FAB)).perform(click());
        onView(withId(R.id.title_input)).perform(replaceText("Discord"), closeSoftKeyboard());
        onView(withId(R.id.password_input)).perform(replaceText("MyS€Cr3tP4ssW0rd!"), closeSoftKeyboard());
        onView(withId(R.id.save_Btn)).perform(click());


        //checks if pw was saved and is still there after closing and opening the app again
        onView(withId(R.id.main_recyclerview)).check(matches(atPosition(0, hasDescendant(withText("Discord")))));
        activityScenarioRuleListActivity.getScenario().close();
        ActivityScenario.launch(PwListActivity.class);
        onView(withId(R.id.main_recyclerview)).check(matches(atPosition(0, hasDescendant(withText("Discord")))));


        //creates second password, deletes the first one and checks if the second password is now the first one
        onView(withId(R.id.add_password_FAB)).perform(click());
        onView(withId(R.id.title_input)).perform(replaceText("Slack"), closeSoftKeyboard());
        onView(withId(R.id.password_input)).perform(replaceText("p@ssword:)"), closeSoftKeyboard());
        onView(withId(R.id.save_Btn)).perform(click());
        //left swipe deletes the password
        onView(withId(R.id.main_recyclerview)).perform(RecyclerViewActions.actionOnItemAtPosition(0, swipeLeft()));
        onView(withId(R.id.main_recyclerview)).check(matches(atPosition(0, hasDescendant(withText("Slack")))));
        //clears list to be able to run test again
        onView(withId(R.id.main_recyclerview)).perform(RecyclerViewActions.actionOnItemAtPosition(0, swipeLeft()));
    }


    // tests information transaction between fragments
    @Test
    public void generatorPasswordCreatingTest() throws InterruptedException {

        //creates Password using the generator(proves that after fragment switch, the textViews are not deleted)
        onView(withId(R.id.add_password_FAB)).perform(click());
        onView(withId(R.id.title_input)).perform(replaceText("Pinterest"), closeSoftKeyboard());
        onView(withId(R.id.view_pager)).perform(swipeLeft());
        onView(withId(R.id.generate_btn)).perform(scrollTo());
        //wait for async threads to finish -> instrumentation.waitForIdleSync(); does not work always
        Thread.sleep(500);
        onView(withId(R.id.generate_btn)).perform(click());
        //back to the form,(title is still there + generated password was passed(otherwise you could not save the password))
        onView(withId(R.id.view_pager)).perform(swipeRight());
        Thread.sleep(500);
        onView(withId(R.id.save_Btn)).perform(click());
        onView(withId(R.id.main_recyclerview)).check(matches(atPosition(0, hasDescendant(withText("Pinterest")))));
        //clears list to be able to run test again
        onView(withId(R.id.main_recyclerview)).perform(RecyclerViewActions.actionOnItemAtPosition(0, swipeLeft()));
    }


    public static Matcher<View> atPosition(final int position, @NonNull final Matcher<View> itemMatcher) {
        checkNotNull(itemMatcher);
        return new BoundedMatcher<View, RecyclerView>(RecyclerView.class) {
            @Override
            public void describeTo(Description description) {
                description.appendText("has item at position " + position + ": ");
                itemMatcher.describeTo(description);
            }

            @Override
            protected boolean matchesSafely(final RecyclerView view) {
                RecyclerView.ViewHolder viewHolder = view.findViewHolderForAdapterPosition(position);
                if (viewHolder == null) {
                    // has no item on such position
                    return false;
                }
                return itemMatcher.matches(viewHolder.itemView);
            }
        };
    }
}


