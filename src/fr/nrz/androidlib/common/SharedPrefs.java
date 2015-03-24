package fr.nrz.androidlib.common;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SharedPrefs {

	protected final SharedPreferences _sPrefs;
	protected final Context _context;

	public SharedPrefs(final Context context, final int prefFile) {
		_context = context;

		_sPrefs = _context.getSharedPreferences(_context.getString(prefFile), Context.MODE_PRIVATE);
	}

	public void putBoolean(final String prefKey, final Boolean boolValue) {
		final Editor edit = _sPrefs.edit();
		edit.putBoolean(prefKey, boolValue);
		edit.commit();
	}
}
