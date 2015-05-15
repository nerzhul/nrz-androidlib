package fr.nrz.androidlib.adapters;

import java.util.ArrayList;

import android.accounts.Account;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class AndroidAccountAdapter extends ArrayAdapter<Account> {

	private final ArrayList<Account> _accounts;
	private static int _itemLayout;
	private static int _accountFieldId;
	private final Activity _activity;
	Class<?> _newActivityClass;

	public AndroidAccountAdapter(final Activity activity, final int resource,
			final ArrayList<Account> objects, final int itemLayout, final int accountFieldId, final Class<?> newActivityClass) {
		super(activity.getBaseContext(), resource, objects);
		_accounts = objects;
		_itemLayout = itemLayout;
		_accountFieldId = accountFieldId;
		_activity = activity;
		_newActivityClass = newActivityClass;
	}

	@Override
	public View getView(final int position, final View convertView, final ViewGroup parent) {
		View v = convertView;
		if (v == null) {
			final LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = inflater.inflate(_itemLayout, null);
		}

		final Account account = _accounts.get(position);

		if (account != null) {
			final TextView label = (TextView) v.findViewById(_accountFieldId);
			if (label != null) {
				label.setText(account.name + " >");
				label.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(final View v) {
						final Intent i = new Intent(_activity, _newActivityClass);
						i.putExtra("account", account.name);
						_activity.startActivity(i);
					}
				});
			}
		}

		return v;
	}

}
