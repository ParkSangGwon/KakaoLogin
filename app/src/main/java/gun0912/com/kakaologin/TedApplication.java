package gun0912.com.kakaologin;

import android.app.Activity;
import android.app.Application;

import com.kakao.auth.KakaoSDK;

/**
 * Created by TedPark on 16. 2. 2..
 */
public class TedApplication extends Application {

   public static Activity currentActivity;

   @Override
   public void onCreate() {
      super.onCreate();

   }
}
