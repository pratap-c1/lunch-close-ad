package ad.launcher.and.closer;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class MyService extends Service {
  private static final String TAG = MyService.class.getCanonicalName();

  public static final String ACTION_LAUNCH_AD_APP = "ACTION_LAUNCH_AD_APP";
  public static final String ACTION_CLOSE_AD_APP = "ACTION_CLOSE_AD_APP";
  public static final String ACTION_STOP_SERVICE = "ACTION_STOP_SERVICE";
  private static final String NOTIFICATION_CHANNEL_ID = MyService.class.getCanonicalName();

  public MyService() {
  }

  @Nullable @Override public IBinder onBind(Intent intent) {
    return null;
  }

  @Override public void onCreate() {
    super.onCreate();
    startForegroundService();
  }

  @Override public int onStartCommand(Intent intent, int flags, int startId) {
    if (intent != null) {
      String action = intent.getAction();
      if (action != null) {
        switch (action) {
          case ACTION_LAUNCH_AD_APP:
            IntentLuncher.startAdActivity(this);
            Log.d(TAG, "Foreground service is started.");
            break;
          case ACTION_CLOSE_AD_APP:
            IntentLuncher.closeAdActivity(this);
            Log.d(TAG, "Foreground service is stopped.");
            break;
          case ACTION_STOP_SERVICE:
            stopSelf();
            break;
        }
      }
    }
    return super.onStartCommand(intent, flags, startId);
  }

  private void startForegroundService() {
    Log.d(TAG, "Start foreground service.");
    // Create notification default intent.
    Intent intent = new Intent();
    PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

    // Create notification builder.
    NotificationCompat.Builder builder;
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      String channelName = "My Background Service";
      NotificationChannel nc = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName,
          NotificationManager.IMPORTANCE_NONE);
      NotificationManager manager =
          (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
      assert manager != null;
      manager.createNotificationChannel(nc);
      builder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
    } else {
      // If earlier version channel ID is not used
      // https://developer.android.com/reference/android/support/v4/app/NotificationCompat.Builder.html#NotificationCompat.Builder(android.content.Context)
      builder = new NotificationCompat.Builder(this);
    }
    // Make notification show big text.
    NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle();
    bigTextStyle.setBigContentTitle("Ad launcher/closer service");
    bigTextStyle.bigText("Starts and Ad launcher app and also closes it");
    // Set big text style.
    builder.setStyle(bigTextStyle);

    builder.setWhen(System.currentTimeMillis());
    builder.setSmallIcon(R.mipmap.ic_launcher);
    Bitmap largeIconBitmap =
        BitmapFactory.decodeResource(getResources(), android.R.drawable.stat_sys_speakerphone);
    builder.setLargeIcon(largeIconBitmap);
    // Make the notification max priority.
    builder.setPriority(Notification.PRIORITY_MAX);
    // Make head-up notification.
    builder.setFullScreenIntent(pendingIntent, true);

    // Ad launch button intent in notification.
    builder.addAction(
        getNotificationAction(ACTION_LAUNCH_AD_APP, android.R.drawable.ic_media_play, "Launch Ad"));

    // Ad close button intent in notification.
    builder.addAction(
        getNotificationAction(ACTION_CLOSE_AD_APP, android.R.drawable.ic_media_pause, "Close Ad"));

    // stop service intent in notification.
    builder.addAction(
        getNotificationAction(ACTION_STOP_SERVICE, android.R.drawable.ic_delete, "Stop Service"));

    // Build the notification.
    Notification notification = builder.build();

    // Start foreground service.
    startForeground(1, notification);
  }

  private NotificationCompat.Action getNotificationAction(String action, int drawble,
      String title) {
    Intent launchIntent = new Intent(this, MyService.class);
    launchIntent.setAction(action);
    PendingIntent pendingLaunchIntent = PendingIntent.getService(this, 0, launchIntent, 0);
    return new NotificationCompat.Action(drawble, title, pendingLaunchIntent);
  }
}
