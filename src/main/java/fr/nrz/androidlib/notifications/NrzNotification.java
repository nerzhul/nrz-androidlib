package fr.nrz.androidlib.notifications;

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

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.Notification.Builder;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

public class NrzNotification {
	public NrzNotification(Context ct, int iconId) {
		_iconId = iconId;
		_ct = ct;
	}

	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	public boolean createNotify(int nType, String nTitle, String nText) {
		if (_ct == null) {
			return false;
		}
		NotificationManager notificationManager = (NotificationManager)_ct.getSystemService(Context.NOTIFICATION_SERVICE);

		Builder mBuilder = new Notification.Builder(_ct)
		.setContentText(nText)
		.setContentTitle(nTitle)
		.setSmallIcon(_iconId);

		notificationManager.notify(nType, mBuilder.build());
		return true;
	}

	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	public void cancelNotify(int nType) {
		NotificationManager notificationManager = (NotificationManager)_ct.getSystemService(Context.NOTIFICATION_SERVICE);
		notificationManager.cancel(nType);
	}

	private final Context _ct;
	private final int _iconId;
}
