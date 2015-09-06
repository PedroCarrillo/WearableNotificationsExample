package com.example.android.basicnotifications;

import android.app.Activity;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.view.View;

/**
 * The entry point to the BasicNotification sample.
 */
public class MainActivity extends Activity {
    /**
     * A numeric value that identifies the notification that we'll be sending.
     * This value needs to be unique within this app, but it doesn't need to be
     * unique system-wide.
     */
    public static final int NOTIFICATION_ID = 1;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sample_layout);

    }

    /**
     * Send a sample notification using the NotificationCompat API.
     */
    public void sendNotification(View view) {

        // BEGIN_INCLUDE(build_action)
        /** Create an intent that will be fired when the user clicks the notification.
         * The intent needs to be packaged into a {@link android.app.PendingIntent} so that the
         * notification service can fire it on our behalf.
         */
        Intent intent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://developer.android.com/reference/android/app/Notification.html"));
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        // END_INCLUDE(build_action)

        // BEGIN_INCLUDE (build_notification)
        /**
         * Use NotificationCompat.Builder to set up our notification.
         */
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);

        /** Set the icon that will appear in the notification bar. This icon also appears
         * in the lower right hand corner of the notification itself.
         *
         * Important note: although you can use any drawable as the small icon, Android
         * design guidelines state that the icon should be simple and monochrome. Full-color
         * bitmaps or busy images don't render well on smaller screens and can end up
         * confusing the user.
         */
        builder.setSmallIcon(R.drawable.ic_stat_notification);

        // Set the intent that will fire when the user taps the notification.
        builder.setContentIntent(pendingIntent);

        // Set the notification to auto-cancel. This means that the notification will disappear
        // after the user taps it, rather than remaining until it's explicitly dismissed.
        builder.setAutoCancel(true);

        /**
         *Build the notification's appearance.
         * Set the large icon, which appears on the left of the notification. In this
         * sample we'll set the large icon to be the same as our app icon. The app icon is a
         * reasonable default if you don't have anything more compelling to use as an icon.
         */
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher));

        /**
         * Set the text of the notification. This sample sets the three most commononly used
         * text areas:
         * 1. The content title, which appears in large type at the top of the notification
         * 2. The content text, which appears in smaller text below the title
         * 3. The subtext, which appears under the text on newer devices. Devices running
         *    versions of Android prior to 4.2 will ignore this field, so don't use it for
         *    anything vital!
         */
        builder.setContentTitle("BasicNotifications Sample");
        builder.setContentText("Time to learn about notifications!");
        builder.setSubText("Tap to view documentation about notifications.");

        // END_INCLUDE (build_notification)
        buildNotification(builder);
        // END_INCLUDE(send_notification)
    }

    public void sendNotificationWithAction(View view) {
        Intent mapIntent = new Intent(Intent.ACTION_VIEW);
        Uri geoUri = Uri.parse("geo:0,0?q=" + Uri.encode("Lima"));
        mapIntent.setData(geoUri);
        PendingIntent mapPendingIntent = PendingIntent.getActivity(this, 0, mapIntent, 0);

        PendingIntent viewPendingIntent = PendingIntent.getActivity(this,0,new Intent(getApplicationContext(), MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this);

        //Building an action for only wearable. Important: If you set WearableExtender to a notification, it will separate the actions from default notification
        //from the wearables completely.

        NotificationCompat.Action action = new NotificationCompat.Action.Builder(R.drawable.ic_launcher, "action wear", viewPendingIntent).build();

        notificationBuilder
                .setSmallIcon(R.drawable.ic_stat_notification)
                .setContentTitle("Notification title")
                .setContentText("Content text")
                .setContentIntent(viewPendingIntent)
        .addAction(R.drawable.ic_launcher, "map", mapPendingIntent)
        .extend(new NotificationCompat.WearableExtender().addAction(action));
        buildNotification(notificationBuilder);
    }

    public void sendNotificationWithPages(View view) {
        PendingIntent viewPendingIntent = PendingIntent.getActivity(this,0,new Intent(getApplicationContext(), MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this);
        notificationBuilder
            .setSmallIcon(R.drawable.ic_stat_notification)
            .setContentTitle("Page 1")
            .setContentText("Short message")
            .setContentIntent(viewPendingIntent);
        NotificationCompat.BigTextStyle secondPageStyle = new NotificationCompat.BigTextStyle();
        secondPageStyle
            .setBigContentTitle("Page 2")
            .bigText("A lot of text..");

        Notification secondNotification = new NotificationCompat.Builder(this)
                .setStyle(secondPageStyle).build();

        buildNotification(notificationBuilder.extend(new NotificationCompat.WearableExtender().addPage(secondNotification)));

    }

    public static final String GROUP_KEY_EMAILS = "_group_keys";

    public void sendNotificationWithStack(View view) {

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this);
        notificationBuilder.setContentTitle("New mail from Pedro")
            .setContentText("Pedro Carrillo")
            .setSmallIcon(android.R.drawable.ic_dialog_email)
            .setGroup(GROUP_KEY_EMAILS);
        buildNotification(notificationBuilder);


        NotificationCompat.Builder notificationBuilder2 = new NotificationCompat.Builder(this);
        notificationBuilder2.setContentTitle("New mail from Pedro2")
                .setContentText("Pedro Carrillo")
                .setSmallIcon(android.R.drawable.ic_dialog_email)
                .setGroup(GROUP_KEY_EMAILS);
        buildNotification(200, notificationBuilder2);

        NotificationCompat.Builder summaryNotificationBuilder = new NotificationCompat.Builder(this);
        summaryNotificationBuilder
                .setContentTitle("2 new messages")
                .setSmallIcon(android.R.drawable.ic_dialog_email)
                .setStyle(new NotificationCompat.InboxStyle()
                        .addLine("Linio - Oferta")
                        .addLine("Cuponatic - Oferta")
                        .setBigContentTitle("2 new messages")
                        .setSummaryText("jhondoe@gmail.com"))
                .setGroup(GROUP_KEY_EMAILS)
                .setGroupSummary(true);
        buildNotification(123, summaryNotificationBuilder);
    }

    private void buildNotification(NotificationCompat.Builder builder) {
        buildNotification(NOTIFICATION_ID, builder);
    }

    private void buildNotification(int notificationId, NotificationCompat.Builder builder) {
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(notificationId, builder.build());
    }

}
