package com.gym.app.parts.findcourses.day_courses;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.gym.app.R;
import com.gym.app.activities.HomeActivity;
import com.gym.app.data.Prefs;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * @author Paul
 * @since 2018.01.19
 */

public class CoursesReceiver extends BroadcastReceiver {

    private static final String ARG_COURSE_NAME = "name";

    public static Intent createIntent(Context context, String courseName) {
        Intent intent = new Intent(context, CoursesReceiver.class);
        intent.putExtra(ARG_COURSE_NAME, courseName);
        return intent;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (!Prefs.NotificationsEnabled.getBoolean(true)) {
            return;
        }
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setAutoCancel(true)
                        .setContentText(context.getString(R.string.course_notification_title, intent.getStringExtra(ARG_COURSE_NAME)))
                        .setContentTitle(context.getString(R.string.yo));
        Intent resultIntent = HomeActivity.createMyCoursesIntent(context);
        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        context,
                        (int) (Math.random() * 1000),
                        resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);

        NotificationManager mNotifyMgr =
                (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        mNotifyMgr.notify((int) (Math.random() * 1000), mBuilder.build());
    }
}
