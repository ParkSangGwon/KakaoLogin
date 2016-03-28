package gun0912.com.kakaologin;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.kakao.auth.KakaoSDK;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.UnLinkResponseCallback;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "gun0912";
    public static final int REQUEST_CODE_KAKAO_LOGIN = 1000;


    @Bind(R.id.tv_access_token)
    TextView tv_access_token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        TedApplication.currentActivity = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        if(KakaoSDK.getAdapter() == null) {
            KakaoSDK.init(new LoginKakaoActivity.KakaoSDKAdapter());
        }

    }


    @OnClick(R.id.btn_login)
    public void startKakaoLogin() {


        Intent intent = new Intent(this, LoginKakaoActivity.class);
        startActivityForResult(intent, REQUEST_CODE_KAKAO_LOGIN);

    }

    @OnClick(R.id.btn_logout)
    public void unLinkKakao() {

        //  연동해제
        UserManagement.requestUnlink(new UnLinkResponseCallback() {
            @Override
            public void onFailure(ErrorResult errorResult) {
                Log.e(TAG, errorResult.toString());
                toast("연동해제 오류\n" + errorResult.toString());
            }

            @Override
            public void onSessionClosed(ErrorResult errorResult) {
                Log.e(TAG, errorResult.toString());
                toast("연동해제 오류\n" + errorResult.toString());
            }

            @Override
            public void onNotSignedUp() {
                Log.i(TAG, "onNotSignedUp()");
            }

            @Override
            public void onSuccess(Long userId) {
                Log.d(TAG, "onSuccess()");

                toast("연동해제 완료");
            }
        });


    }

    public void toast(String msg) {
        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {

            case REQUEST_CODE_KAKAO_LOGIN:

                if (data != null && resultCode == RESULT_OK) {

                    String accessToken = data.getStringExtra(LoginKakaoActivity.INTENT_EXTRA_ACCESS_TOKEN);

                    tv_access_token.setText(accessToken);


                } else {

                    Toast.makeText(this, "토큰 가져오기 에러", Toast.LENGTH_SHORT).show();
                }


                break;


            default:
                super.onActivityResult(requestCode, resultCode, data);
        }

    }
}
