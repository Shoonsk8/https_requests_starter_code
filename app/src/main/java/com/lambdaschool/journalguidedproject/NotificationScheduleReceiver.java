package com.lambdaschool.journalguidedproject;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.RemoteInput;
import android.util.Log;

// S02M04-9 create an intent to receive our broadcasts
public class NotificationScheduleReceiver extends BroadcastReceiver {
    Context context;

    private static final String TAG = "NotificationScheduleRec";

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;

        Log.i(JournalListActivity.TAG, "received broadcast");

        displayNotification();
    }

    // S02M04-9b move notification generator to this class
    void displayNotification() {
        final long start = System.nanoTime();
        Thread notificationThread = new Thread(new Runnable() {
            @Override
            public void run() {

                // S02M04-1 get a handle to the notification manager
                NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

                // S02M04-2 build a channel for newer versions of android
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    CharSequence name        = "Journal Reminder";
                    String       description = "This channel will remind the user to make a journal entry";
                    int          importance  = NotificationManager.IMPORTANCE_LOW;

                    NotificationChannel channel = new NotificationChannel(JournalListActivity.channelId, name, importance);
                    channel.setDescription(description);

                    notificationManager.createNotificationChannel(channel);
                }

                // S02M04-5 create an intent for the user to interact with
                Intent launchListIntent = new Intent(context, JournalListActivity.class);
                PendingIntent pendingLaunchListIntent = PendingIntent.getActivity(
                        context,
                        JournalListActivity.LIST_INTENT_REQUEST_CODE,
                        launchListIntent,
                        PendingIntent.FLAG_ONE_SHOT);

                // S02M04-6 add support objects for allowing remote input
                RemoteInput remoteInput = new RemoteInput.Builder(JournalListActivity.NEW_ENTRY_ACTION_KEY)
                        .setLabel("Enter your Entry Text")
                        .build();

                // S02M04-6b
                // S02M04-9c change the remote input intent to broadcast to our other receiver
                Intent inputIntent = new Intent(context, NotificationResponseReceiver.class);
                PendingIntent pendingInputIntent = PendingIntent.getBroadcast(
                        context,
                        JournalListActivity.LIST_INTENT_RESPONSE_REQUEST_CODE,
                        inputIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT);

                // S02M04-6c
                NotificationCompat.Action inputAction = new NotificationCompat.Action.Builder(
                        android.R.drawable.ic_menu_edit, "Entry", pendingInputIntent)
                        .addRemoteInput(remoteInput)
                        .setAllowGeneratedReplies(true)
                        .build();

                // S02M04-4 build a simple notification
                NotificationCompat.Builder builder = new NotificationCompat.Builder(context, JournalListActivity.channelId)
                        .setPriority(NotificationManager.IMPORTANCE_LOW)
                        .setContentTitle("Journal Reminder")
                        .setAutoCancel(true)
                        .setContentIntent(pendingLaunchListIntent) // S02M04-5b
                        .setContentText("Remember to write a journal entry for today.")
                        .addAction(inputAction) // S02M04-6d add action to notification
                        .setSmallIcon(R.drawable.ic_collections_bookmark_black_24dp);

                notificationManager.notify(JournalListActivity.REMINDER_NOTIFICATION_ID, builder.build());
                Log.i("StopwatchNotifThreadCom", "Elapsed: " + (System.nanoTime() - start));
            }
        });
        notificationThread.start();
        Log.i("Stopwatch" + TAG.substring(0, 12), "Elapsed: " + (System.nanoTime() - start));
    }
}
