package com.moviebasket.android.client.join;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.moviebasket.android.client.R;
import com.moviebasket.android.client.global.ApplicationController;
import com.moviebasket.android.client.intro.IntroActivity;
import com.moviebasket.android.client.network.MBService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class JoinActivity extends AppCompatActivity {
    private static final String SUCCESS = "create";
    private static final String OVERLAP = "repetition";  //중복확인

    ImageView signupBtn;
    EditText username;
    EditText email;
    EditText password;
    EditText confirm;

    ImageView UsernameX;
    ImageView E_mailX;
    ImageView pwX;
    ImageView CPX;

    private MBService mbService;
    private boolean isJoinSuccess;
    private boolean isOverlap;
    private String member_name;
    private String member_email;
    private String member_pwd;
    private String member_pwd_con;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        mbService = ApplicationController.getInstance().getMbService();

        signupBtn = (ImageView) findViewById(R.id.signupBtn);
        username = (EditText) findViewById(R.id.username);
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);  //6자이상 ~ 15자미만 6~14
        confirm = (EditText) findViewById(R.id.confirm);

        UsernameX = (ImageView) findViewById(R.id.UsernameX);
        E_mailX = (ImageView) findViewById(R.id.E_mailX);
        pwX = (ImageView) findViewById(R.id.pwX);
        CPX = (ImageView) findViewById(R.id.CPX);

        signupBtn.setOnClickListener(clickListener);
        UsernameX.setOnClickListener(clickListener);
        E_mailX.setOnClickListener(clickListener);
        pwX.setOnClickListener(clickListener);
        CPX.setOnClickListener(clickListener);

        confirm.setImeOptions(EditorInfo.IME_ACTION_DONE); // 키보드 확인 버튼 클릭시
        confirm.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE)
                    signupBtn.performClick(); // signupBtn 이란 Button 누르는 동작 실행
                return false;
            }
        });

    }


    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                //join on(signupBtn) 버튼 눌렀을 때
                 case R.id.signupBtn:
                    //join을 위한 networking
                    member_name = username.getText().toString().trim();
                    member_email = email.getText().toString().trim();
                    member_pwd = password.getText().toString().trim();
                    member_pwd_con = confirm.getText().toString().trim();

                     //비밀번호 길이 제한
                     boolean isPwdNotValid = ((member_pwd.length() >= 6) && (member_pwd.length() < 15)) ? false : true;
                     if (isPwdNotValid) {
                         Toast.makeText(JoinActivity.this, "비밀번호는 최소 6자 이상, 최대 15자 미만입니다.", Toast.LENGTH_SHORT).show();
                         break;
                     }

                     //정규식으로 email형식 잡기기
                     String emailPattern = "^[_A-Za-z0-9-]+@[A-Za-z0-9]+\\.[A-Za-z]{2,4}$";
                    if (!member_email.matches(emailPattern)) {
                        Toast.makeText(JoinActivity.this, "e-mail과 다른 형식입니다.다시 입력해주세요", Toast.LENGTH_SHORT).show();
                        break;
                    }

                    //비밀번호란과 비밀번호 확인란이 다를경우 잡아주는 부분
                    if (!(member_pwd.equals(member_pwd_con))) {
                        Toast.makeText(JoinActivity.this, "비밀번호와 확인이 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                        break;
                    }

                    Log.i("Join infomation", " , name : " + member_name + " , email : " + member_email + " , pwd : " + member_pwd + " , pwd_con : " + member_pwd_con);

                    Call<JoinResult> getJoinResult = mbService.getJoinResult(member_name, member_email, member_pwd);
                    getJoinResult.enqueue(new Callback<JoinResult>() {
                        @Override
                        public void onResponse(Call<JoinResult> call, Response<JoinResult> response) {
                            if (response.isSuccessful()) {// 응답코드 200
                                Log.i("JoinTest", "요청메시지:" + call.toString() + " 응답메시지:" + response.toString());
                                JoinResult JoinResult = response.body();
                                isJoinSuccess = JoinResult.result.message.equals(SUCCESS) ? true : false;
                                isOverlap = JoinResult.result.message.equals(OVERLAP) ? true : false;
                                Log.i("JoinTest", "회원가입 결과 : " + JoinResult.result.message);
                            }
                            if (isJoinSuccess) {
                                //회원가입이 성공했을 때. 튜토리얼 화면으로 이동.
                                Intent introIntent = new Intent(JoinActivity.this, IntroActivity.class);
                                startActivity(introIntent);
                                setResult(RESULT_OK);
                                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                                finish();
                            } else {
                                //edittext가 공백일때 경고하기
                                if ((member_name.equals("")) || (member_email.equals("")) || (member_pwd.equals("")) || (member_pwd_con.equals(""))) {
                                    Toast.makeText(JoinActivity.this, "모든 정보를 입력해주세요", Toast.LENGTH_SHORT).show();
                                } else if(isOverlap) {
                                    Toast.makeText(JoinActivity.this, "이미 가입한 회원입니다.", Toast.LENGTH_SHORT).show();
                                } else{
                                    Toast.makeText(JoinActivity.this, "내부통신오류", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                        @Override
                        public void onFailure(Call<JoinResult> call, Throwable t) {
                            Toast.makeText(JoinActivity.this, "서비스에 오류가 있습니다.", Toast.LENGTH_SHORT).show();
                            Log.i("LoginTest", "요청메시지:" + call.toString());
                        }
                    });
                    break;

                case R.id.UsernameX:
                    username.setText("");
                    break;
                case R.id.E_mailX:
                    email.setText("");
                    break;
                case R.id.pwX:
                    password.setText("");
                    break;
                case R.id.CPX:
                    confirm.setText("");
                    break;

            }

            username.setText("");
            email.setText("");
            password.setText("");
            confirm.setText("");

            username.requestFocus();
        }
    };
}
