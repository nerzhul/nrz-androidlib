package fr.nrz.androidlib.activities;

/**
 * Copyright (c) 2013-2015, Loic Blot <loic.blot@unix-experience.fr>
 * All rights reserved.
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * The views and conclusions contained in the software and documentation are those
 * of the authors and should not be interpreted as representing official policies,
 * either expressed or implied, of the FreeBSD Project.
 */

import java.util.Vector;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

public class NrzSettingsActivity extends PreferenceActivity {
	private static final boolean ALWAYS_SIMPLE_PREFS = false;
	protected static Context _context;
	protected static int _prefsRessourceFile;
	protected static Vector<BindObjectPref> _boolPrefs = new Vector<BindObjectPref>();
	protected static Vector<BindObjectPref> _stringPrefs = new Vector<BindObjectPref>();

	@Override
	protected void onPostCreate(final Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		_context = getBaseContext();
		setupSimplePreferencesScreen();
	}

	/**
	 * Shows the simplified settings UI if the device configuration if the
	 * device configuration dictates that a simplified, single-pane UI should be
	 * shown.
	 */
	@SuppressWarnings("deprecation")
	private void setupSimplePreferencesScreen() {
		if (!isSimplePreferences(this)) {
			return;
		}

		// In the simplified UI, fragments are not used at all and we instead
		// use the older PreferenceActivity APIs.
		addPreferencesFromResource(_prefsRessourceFile);
		bindPreferences();
	}

	/** {@inheritDoc} */
	@Override
	public boolean onIsMultiPane() {
		return isXLargeTablet(this) && !isSimplePreferences(this);
	}

	/**
	 * Helper method to determine if the device has an extra-large screen. For
	 * example, 10" tablets are extra-large.
	 */
	private static boolean isXLargeTablet(final Context context) {
		return (context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_XLARGE;
	}

	/**
	 * Determines whether the simplified settings UI should be shown. This is
	 * true if this is forced via {@link #ALWAYS_SIMPLE_PREFS}, or the device
	 * doesn't have newer APIs like {@link PreferenceFragment}, or the device
	 * doesn't have an extra-large screen. In these cases, a single-pane
	 * "simplified" settings UI should be shown.
	 */
	protected static boolean isSimplePreferences(final Context context) {
		return ALWAYS_SIMPLE_PREFS
				|| Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB
				|| !isXLargeTablet(context);
	}

	/**
	 * Binds a preference's summary to its value. More specifically, when the
	 * preference's value is changed, its summary (line of text below the
	 * preference title) is updated to reflect the value. The summary is also
	 * immediately updated upon calling this method. The exact display format is
	 * dependent on the type of preference.
	 *
	 * @see #sBindPreferenceSummaryToValueListener
	 */
	public static void bindPreferenceBooleanToValue(final Preference preference, final Boolean defValue) {
		// Set the listener to watch for value changes.
		preference
		.setOnPreferenceChangeListener(_bindPreferenceListener);

		// Trigger the listener immediately with the preference's
		// current value.
		_bindPreferenceListener.onPreferenceChange(
				preference,
				PreferenceManager.getDefaultSharedPreferences(
						preference.getContext()).getBoolean(
								preference.getKey(),
								defValue
								)
				);
	}

	public static void bindPreferenceStringToValue(final Preference preference, final String defValue) {
		// Set the listener to watch for value changes.
		preference
		.setOnPreferenceChangeListener(_bindPreferenceListener);

		// Trigger the listener immediately with the preference's
		// current value.
		_bindPreferenceListener.onPreferenceChange(
				preference,
				PreferenceManager.getDefaultSharedPreferences(
						preference.getContext()).getString(
								preference.getKey(),
								defValue
								)
				);
	}

	/**
	 * This fragment shows data and sync preferences only. It is used when the
	 * activity is showing a two-pane settings UI.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public static class DataSyncPreferenceFragment extends PreferenceFragment {
		@Override
		public void onCreate(final Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			addPreferencesFromResource(_prefsRessourceFile);

			// Bind the summaries of EditText/List/Dialog/Ringtone preferences
			// to their values. When their values change, their summaries are
			// updated to reflect the new value, per the Android Design
			// guidelines.
			for (int i=0; i < _stringPrefs.size(); i++) {
				bindPreferenceStringToValue(findPreference(_stringPrefs.get(i).name),
						(String) _stringPrefs.get(i).value);
			}
			for (int i=0; i < _boolPrefs.size(); i++) {
				bindPreferenceBooleanToValue(findPreference(_boolPrefs.get(i).name),
						(Boolean) _boolPrefs.get(i).value);
			}
		}
	}

	private void bindPreferences() {
		// Bind the summaries of EditText/List/Dialog/Ringtone preferences to
		// their values. When their values change, their summaries are updated
		// to reflect the new value, per the Android Design guidelines.
		for (int i=0; i < _stringPrefs.size(); i++) {
			bindPreferenceStringToValue(findPreference(_stringPrefs.get(i).name),
					(String) _stringPrefs.get(i).value);
		}

		for (int i=0; i < _boolPrefs.size(); i++) {
			bindPreferenceBooleanToValue(findPreference(_boolPrefs.get(i).name),
					(Boolean) _boolPrefs.get(i).value);
		}
	}

	// The preference object, it's only a key value pair
	protected class BindObjectPref {
		public String name;
		public Object value;
		public BindObjectPref(final String prefName, final Object prefVal) {
			name = prefName;
			value = prefVal;
		}
	}

	/**
	 * A preference value change listener that updates the preference's summary
	 * to reflect its new value.
	 */
	private static Preference.OnPreferenceChangeListener _bindPreferenceListener = new Preference.OnPreferenceChangeListener() {
		@Override
		public boolean onPreferenceChange(final Preference preference, final Object value) {
			if (preference instanceof ListPreference) {
				handleListPreference(preference.getKey(), value.toString(), (ListPreference) preference);

			} else if (preference instanceof CheckBoxPreference) {
				handleCheckboxPreference(preference.getKey(), (Boolean)value);
			} else {
				// For all other preferences, set the summary to the value's
				// simple string representation.
				//preference.setSummary(boolValue);
			}
			return true;
		}
	};

	protected static void handleCheckboxPreference(final String key, final Boolean value) {}
	protected static void handleListPreference(final String key, final String value,
			final ListPreference preference) {}
}
