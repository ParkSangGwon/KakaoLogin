/**
 * Copyright 2014 Daum Kakao Corp.
 * <p>
 * Redistribution and modification in source or binary forms are not permitted without specific prior written permission. 
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package gun0912.com.kakaologin;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.kakao.auth.ApprovalType;
import com.kakao.auth.AuthType;
import com.kakao.auth.IApplicationConfig;
import com.kakao.auth.ISessionCallback;
import com.kakao.auth.ISessionConfig;
import com.kakao.auth.KakaoAdapter;
import com.kakao.auth.Session;
import com.kakao.util.exception.KakaoException;


/**
 * 샘플에서 사용하게 될 로그인 페이지
 * 세션을 오픈한 후 action을 override해서 사용한다.
 *
 * @author Ted Park
 */
public class LoginKakaoActivity extends AppCompatActivity {
public static final String INTENT_EXTRA_ACCESS_TOKEN="access_token";
    public static final String TAG = "gun0912";
    public static AuthType auth_type = AuthType.KAKAO_TALK;
    private SessionCallback callback;

    static  Activity currentActivity;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        TedApplication.currentActivity = this;
        super.onCreate(savedInstanceState);

        currentActivity = this;






        callback = new SessionCallback();
        Session.getCurrentSession().addCallback(callback);
        Session.getCurrentSession().checkAndImplicitOpen();
        Session.getCurrentSession().open(auth_type, this);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
            return;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Session.getCurrentSession().removeCallback(callback);
    }

    protected void successLogin() {


        String accessToken = Session.getCurrentSession().getAccessToken();
        Log.d(TAG, "accessToken: " + accessToken);
        Intent intent = new Intent();
        intent.putExtra(INTENT_EXTRA_ACCESS_TOKEN, accessToken);


        setResult(RESULT_OK, intent);
        finish();
        overridePendingTransition(0, 0);


    }

    public static class KakaoSDKAdapter extends KakaoAdapter {
        /**
         * Session Config에 대해서는 default값들이 존재한다.
         * 필요한 상황에서만 override해서 사용하면 됨.
         * @return Session의 설정값.
         */
        @Override
        public ISessionConfig getSessionConfig() {
            return new ISessionConfig() {
                @Override
                public AuthType[] getAuthTypes() {
                    return new AuthType[]{auth_type};
                }

                @Override
                public boolean isUsingWebviewTimer() {
                    return false;
                }

                @Override
                public ApprovalType getApprovalType() {
                    return ApprovalType.INDIVIDUAL;
                }

                @Override
                public boolean isSaveFormData() {
                    return true;
                }
            };
        }

        @Override
        public IApplicationConfig getApplicationConfig() {
            return new IApplicationConfig() {
                @Override
                public Activity getTopActivity() {
                    return TedApplication.currentActivity;
                }

                @Override
                public Context getApplicationContext() {
                    return TedApplication.currentActivity.getApplicationContext();
                }
            };
        }
    }

    class SessionCallback implements ISessionCallback {

        @Override
        public void onSessionOpened() {

            Log.d(TAG, "onSessionOpened()");

            successLogin();
        }

        @Override
        public void onSessionOpenFailed(KakaoException exception) {
            Log.e(TAG, "onSessionOpenFailed()");
            if (exception != null) {
                Log.e(TAG, "exception: " + exception.getMessage());
            }

            // 프로그레스바를 보이고 있었다면 중지하고 세션 오픈을 못했으니 다시 로그인 버튼 노출.
            setResult(RESULT_CANCELED);
            finish();
            overridePendingTransition(0, 0);
        }
    }


}
