package ad.launcher.and.closer;

import ad.launcher.and.closer.databinding.ActivityAdLauncherAndCloserBinding;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public class AdLauncherAndCloserActivity extends AppCompatActivity {
  private static final String TAG = AdLauncherAndCloserActivity.class.getCanonicalName();

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    ActivityAdLauncherAndCloserBinding binding =
        DataBindingUtil.setContentView(this, R.layout.activity_ad_launcher_and_closer);
    binding.setActivity(this);
  }

  public void startAd() {
    Log.d(TAG, "startAd");
    IntentLuncher.startAdActivity(this);
  }

  public void closeAd() {
    Log.d(TAG, "closeAd");
    IntentLuncher.closeAdActivity(this);
  }

  public void startMyService() {
    Log.d(TAG, "startService");
    IntentLuncher.startMyService(this);
  }
}
