/*
 * Copyright (c) 2019-2022
 * Mateusz Hermanowicz - All rights reserved.
 * My Pantry
 * https://www.mypantry.eu
 *
 * Licensed under the Apache License, Version 2.0 (the "License")
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hermanowicz.pantry;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.assertEquals;

import android.content.Context;
import android.content.Intent;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.navigation.testing.TestNavHostController;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.contrib.DrawerActions;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.hermanowicz.pantry.ui.activity.MainActivity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import dagger.hilt.android.testing.HiltAndroidRule;
import dagger.hilt.android.testing.HiltAndroidTest;

@HiltAndroidTest
@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    public HiltAndroidRule hiltRule = new HiltAndroidRule(this);
    @Rule
    public ActivityTestRule<MainActivity> activityRule =
            new ActivityTestRule<>(MainActivity.class, false, false);
    private Context context;

    @Before
    public void init() {
        hiltRule.inject();
        context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        activityRule.launchActivity(new Intent());
        setupNavController();
    }

    private void setupNavController() {
        TestNavHostController navController = new TestNavHostController(
                ApplicationProvider.getApplicationContext());

        InstrumentationRegistry.getInstrumentation().runOnMainSync(() -> {
            navController.setGraph(R.navigation.navigation);

            Fragment myPantryFragment = getForegroundFragment();
            Navigation.setViewNavController(myPantryFragment.requireView(), navController);
        });
    }

    private Fragment getForegroundFragment() {
        MainActivity activity = activityRule.getActivity();
        Fragment navHostFragment = activity.getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_content_main);
        return navHostFragment == null ? null : navHostFragment.getChildFragmentManager().getFragments().get(0);
    }

    @Test
    public void happy_scenario_check_is_right_context() {
        assertEquals("com.hermanowicz.pantry", context.getPackageName());
    }

    //navigation
    @Test
    public void happy_scenario_should_show_fragment_my_pantry() {
        onView(withId(R.id.drawer_layout)).check(matches(isDisplayed()));
        onView(withId(R.id.recyclerview_products)).check(matches(isDisplayed()));
        onView(withId(R.id.newProductFab)).check(matches(isDisplayed()));
    }

    @Test
    public void happy_scenario_should_go_to_new_product_and_show_fragment() {
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
        onView(withText(R.string.title_new_product)).perform(ViewActions.click());

        onView(withId(R.id.button_addNewProduct)).check(matches(isDisplayed()));
        onView(withId(R.id.button_clearFields)).check(matches(isDisplayed()));
    }

    @Test
    public void happy_scenario_should_go_to_scan_product_and_show_fragment() {
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
        onView(withText(R.string.title_scan_product)).perform(ViewActions.click());

        onView(withId(R.id.button_scanProductBarcode)).check(matches(isDisplayed()));
        onView(withId(R.id.button_scanProductQRCode)).check(matches(isDisplayed()));
        onView(withId(R.id.button_enterBarcodeManually)).check(matches(isDisplayed()));
    }

    @Test
    public void happy_scenario_should_go_to_own_categories_and_show_fragment() {
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
        onView(withText(R.string.title_own_categories)).perform(ViewActions.click());

        onView(withId(R.id.recyclerview_categories)).check(matches(isDisplayed()));
        onView(withId(R.id.newCategoryFab)).check(matches(isDisplayed()));
    }

    @Test
    public void happy_scenario_should_go_to_new_category_and_show_fragment() {
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
        onView(withText(R.string.title_own_categories)).perform(ViewActions.click());
        onView(withId(R.id.newCategoryFab)).perform(ViewActions.click());

        onView(withId(R.id.categoryNameInput)).check(matches(isDisplayed()));
        onView(withId(R.id.categoryDescriptionInput)).check(matches(isDisplayed()));
        onView(withId(R.id.button_addNewCategory)).check(matches(isDisplayed()));
    }

    @Test
    public void happy_scenario_should_go_to_storage_locations_and_show_fragment() {
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
        onView(withText(R.string.title_storage_locations)).perform(ViewActions.click());

        onView(withId(R.id.recyclerview_storage_locations)).check(matches(isDisplayed()));
        onView(withId(R.id.newStorageLocationFab)).check(matches(isDisplayed()));
    }

    @Test
    public void happy_scenario_should_go_to_new_storage_location_and_show_fragment() {
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
        onView(withText(R.string.title_storage_locations)).perform(ViewActions.click());
        onView(withId(R.id.newStorageLocationFab)).perform(ViewActions.click());

        onView(withId(R.id.storageLocationNameInput)).check(matches(isDisplayed()));
        onView(withId(R.id.storageLocationDescriptionInput)).check(matches(isDisplayed()));
        onView(withId(R.id.button_addStorageLocation)).check(matches(isDisplayed()));
    }

    @Test
    public void happy_scenario_should_go_to_settings_and_show_fragment() {
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
        onView(withText(R.string.title_app_settings)).perform(ViewActions.click());

        onView(withText(context.getString(R.string.settings_general_settings))).check(matches(isDisplayed()));
        onView(withText(context.getString(R.string.settings_go_premium))).check(matches(isDisplayed()));
        onView(withText(context.getString(R.string.settings_database_mode))).check(matches(isDisplayed()));
    }

    @Test
    public void happy_scenario_should_go_to_about_author_and_show_fragment() {
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
        onView(withText(R.string.title_about_author)).perform(ViewActions.click());

        onView(withId(R.id.linkedIn)).check(matches(isDisplayed()));
        onView(withId(R.id.facebook)).check(matches(isDisplayed()));
        onView(withId(R.id.contactWithAuthor)).check(matches(isDisplayed()));
        onView(withId(R.id.myPantryWebsite)).check(matches(isDisplayed()));
    }
}