package ad.launcher.and.closer;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.Log;

public class IntentLuncher {
  private static final String TAG = IntentLuncher.class.getCanonicalName();
  private static final String AD_PKG = "com.lemma.digital";
  private static final String AD_HOME_SCREEN = "com.lemma.app.Activity.HomeScreen";

  public static boolean startAdActivity(Context context) {
    Log.d(TAG, "startAdActivity");
    return getIntent(context, "LAUNCH_APP");
  }

  public static boolean closeAdActivity(Context context) {
    Log.d(TAG, "closeAdActivity");
    return getIntent(context, "CLOSE_APP");
  }

  public static void startMyService(Context context) {
    Log.d(TAG, "startMyService");
    Intent intent = new Intent(context, MyService.class);
    context.startService(intent);
  }

  private static boolean getIntent(Context context, String extra) {
    final String EXTRA_EVENT = "EXTRA_EVENT";
    Log.d(TAG, "getIntent");
    Intent intent = new Intent();
    intent.setComponent(new ComponentName(AD_PKG, AD_HOME_SCREEN));
    intent.putExtra(EXTRA_EVENT, extra);
    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    if (!(context instanceof Activity)) intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    PackageManager packageManager = context.getPackageManager();
    ComponentName cn = intent.resolveActivity(packageManager);
    Log.d(TAG, "cn = " + cn);
    if (cn != null) {
      context.startActivity(intent);
      return true;
    } else {
      return false;
    }
  }
}
