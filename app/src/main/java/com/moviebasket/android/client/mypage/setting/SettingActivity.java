package com.moviebasket.android.client.mypage.setting;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.content.IntentCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.moviebasket.android.client.R;
import com.moviebasket.android.client.global.ApplicationController;
import com.moviebasket.android.client.network.MBService;
import com.moviebasket.android.client.splash.SplashActivity;

import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SettingActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_FOR_IMAGE = 1001;
    private boolean isDefaulUserImage;

    private MBService mbService;
    private boolean isResponseSuccess;
    private static final String FAILURE = "session error";
    private String token;
    CircleImageView userimage;
    TextView username;
    TextView useremail;
    TextView version;
    ImageView backBtnIcon;
    ImageView camera;
    RelativeLayout btn_withdraw;
    private ProgressDialog mProgressDialog;

    //컨텍스트메뉴 테스트용

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        mbService = ApplicationController.getInstance().getMbService();

        userimage = (CircleImageView) findViewById(R.id.userimage1);
        camera = (ImageView)findViewById(R.id.camera);
        username = (TextView) findViewById(R.id.username);
        useremail = (TextView) findViewById(R.id.useremail);
        backBtnIcon = (ImageView) findViewById(R.id.backBtnIcon);
        backBtnIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        btn_withdraw = (RelativeLayout)findViewById(R.id.btn_withdraw);
        version = (TextView)findViewById(R.id.version);

        token = ApplicationController.getInstance().getPreferences();
        version.setText("V."+ApplicationController.getInstance().getVersion());

        mProgressDialog = new ProgressDialog(SettingActivity.this);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setMessage("처리중..");
        mProgressDialog.setIndeterminate(true);

        //탈퇴하기 버튼 클릭시 탈퇴하겠냐고 확인
        btn_withdraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SettingActivity.this);
                builder.setMessage("정말로 무비바스켓을 떠나실건가요?");
                builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //탈퇴 요청하기.
                        Call<MemberWithdrawResult> verifyMemberWithdraw = mbService.verifyMemberWithdraw(token);
                        verifyMemberWithdraw.enqueue(new Callback<MemberWithdrawResult>() {
                            @Override
                            public void onResponse(Call<MemberWithdrawResult> call, Response<MemberWithdrawResult> response) {
                                if (response.isSuccessful()) {
                                    String message = response.body().result.message;
                                    if (message.equals("withdraw success")) {
                                        ApplicationController.getInstance().savePreferences("");
                                        Intent withdrawIntent = new Intent(SettingActivity.this, SplashActivity.class);
                                        //withdrawIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        withdrawIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | IntentCompat.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(withdrawIntent);
                                        finish();
                                    } else {
                                        Toast.makeText(SettingActivity.this, "회원탈퇴 실패", Toast.LENGTH_SHORT).show();
                                    }

                                }

                            }

                            @Override
                            public void onFailure(Call<MemberWithdrawResult> call, Throwable t) {
                                Toast.makeText(SettingActivity.this, "서버와 연결을 확인해주세요", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
                builder.show();

            }
        });

        //유저의 개인정보 가져오기.
        requestProfileImage();

        //유저의 프로필 사진 만들기. (요청하기는 OnActivityResult에서 처리함.)
        userimage.setOnClickListener(clickListener);
        camera.setOnClickListener(clickListener);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.hold, R.anim.slide_out_right);
    }

    //컨텍스트 메뉴 생성
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderIcon(R.drawable.bar_logo);
        menu.setHeaderTitle("프로필");
        menu.add(0,1,100,"앨범에서 사진 선택");
        menu.add(0,2,100,"기본 이미지로 변경");
    }

    //컨텍스트 메뉴 아이템 판별
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            //앨범에서 사진 선택한 경우
            case 1 :
                Toast.makeText(this, "앨범에서 사진 선택", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                // Gallery 호출
                //intent.setType("Camera/*");
                //intent.setAction(Intent.ACTION_GET_CONTENT);
                // 잘라내기 셋팅
                intent.putExtra("crop", "true");
                intent.putExtra("aspectX", 10);
                intent.putExtra("aspectY", 10);
                intent.putExtra("outputX", 300);
                intent.putExtra("outputY", 300);
                Log.i("NetConfirm", "NewFragment 바스켓리스트 가져옴.");
                try {
                    intent.putExtra("return-data", true);
                    startActivityForResult(Intent.createChooser(intent,
                            "갤러리 어플리케이션을 선택하세요"), REQUEST_CODE_FOR_IMAGE);
                } catch (ActivityNotFoundException e) {
                    // Do nothing for now
                }

                return true;

            //기본이미지로 바꾸면서 딜리트 요청 해야함.
            case 2 :
                if(isDefaulUserImage){
                    return true;
                }

                String changedToken = ApplicationController.getInstance().getPreferences();
                //SharedPreferences pref = ApplicationController.getInstance().getSharedPreferences(SecurityDataSet.STR_NAME, MODE_PRIVATE);
                //final String token = pref.getString(SecurityDataSet.TK_KEY, "");
                Log.i("profileImg", "프로필삭제전 Token: "+changedToken);
                Call<UpdateProfileImageResult> deleteProfile = mbService.deleteProfileImage(changedToken);
                deleteProfile.enqueue(new Callback<UpdateProfileImageResult>() {
                    @Override
                    public void onResponse(Call<UpdateProfileImageResult> call, Response<UpdateProfileImageResult> response) {
                        UpdateProfileImageResult result = response.body();
                        //Log.i("profileImg", "내용 : "+ result.result.message);
                        //String message = result.result.message;
                        if ( result != null ) {
                            String member_token  = result.result.member_token;
                            String message = result.result.message;
                            if ( message.equals("delete profile success") ) {
                                ApplicationController.getInstance().savePreferences(member_token);
                                Log.i("profileImg", "프로필삭제후 Token: "+ApplicationController.getInstance().getPreferences());
                                Toast.makeText(SettingActivity.this, "기본 이미지로 변경", Toast.LENGTH_SHORT).show();
                                userimage.setImageResource(R.drawable.mypage_myimage);
                            } else {
                                Toast.makeText(SettingActivity.this, "프로필변경 실패1번", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<UpdateProfileImageResult> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), "네트워크를 확인해주세요.", Toast.LENGTH_SHORT).show();
                    }
                });
/*
                Toast.makeText(SettingActivity.this, "기본 이미지로 변경", Toast.LENGTH_SHORT).show();
                userimage.setImageResource(R.drawable.mypage_myimage);*/


                /**
                 *  TODO: 딜리트 요청하는 부분
                 */

                return true;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_FOR_IMAGE) {
            //이미지를 성공적으로 가져왔을 경우
            if (resultCode == Activity.RESULT_OK) {
                if (data == null)
                    return;
                Bundle extras2 = data.getExtras();
                Bitmap selectedBitmapImage = null;
                if (extras2 != null) {
                    selectedBitmapImage = extras2.getParcelable("data");
                    userimage.setImageBitmap(selectedBitmapImage);
                }

                //아 근데.. 사진 비율찌그러짐.. ㅡㅡ


                //이미지 리사이징 후 서버에 upload 요청
                MultipartBody.Part body;

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                if (selectedBitmapImage == null) {
                    Toast.makeText(this, "비트맵 파일을 생성할 수 없습니다", Toast.LENGTH_SHORT).show();
                    return;
                }
                selectedBitmapImage.compress(Bitmap.CompressFormat.JPEG, 100, baos); // 압축 옵션( JPEG, PNG ) , 품질 설정 ( 0 - 100까지의 int형 ),

                RequestBody photoBody = RequestBody.create(MediaType.parse("image/jpg"), baos.toByteArray());

                body = MultipartBody.Part.createFormData("profile_file", ".png", photoBody);

                requestImageUpload(body);

                //혹시 모르니까 한번더 이미지 바꿔주는 곳
                userimage.setImageBitmap(selectedBitmapImage);

            }
        }
    }

    private void requestProfileImage(){
        String changedToken = ApplicationController.getInstance().getPreferences();
        Call<SettingResult> getSettingResult = mbService.getSettingResult(changedToken);
        getSettingResult.enqueue(new Callback<SettingResult>() {
            @Override
            public void onResponse(Call<SettingResult> call, Response<SettingResult> response) {
                SettingResult settingResult = response.body();
                if (response.isSuccessful()) {// 응답코드 200
                    Log.i("recommendMovie Test", "요청메시지:" + call.toString() + " 응답메시지:" + response.toString());
                    isResponseSuccess = settingResult.result.message == null ? true : false;
                    Log.i("recommendMovie Test", "응답 결과 : " + isResponseSuccess);
                }
                if (isResponseSuccess) {
                    username.setText(String.valueOf(settingResult.result.member_name));
                    useremail.setText(String.valueOf(settingResult.result.member_email));
                    Log.i("NetConfirm", " 유저 사진 url ㅎㅎㅎㅎ: " + settingResult.result.member_image);
                    if (!(settingResult.result.member_image == null || settingResult.result.member_image.equals(""))) {
                        Glide.with(SettingActivity.this).load(String.valueOf(settingResult.result.member_image)).into(userimage);
                    } else {
                        userimage.setImageResource(R.drawable.mypage_myimage);
                        isDefaulUserImage=true;
                    }
                }
            }

            @Override
            public void onFailure(Call<SettingResult> call, Throwable t) {
                Toast.makeText(SettingActivity.this, "서비스에 오류가 있습니다.", Toast.LENGTH_SHORT).show();
                Log.i("recommendMovie Test", "요청메시지:" + call.toString());
            }
        });

    }

    private void requestImageUpload(MultipartBody.Part body){
        String changedToken = ApplicationController.getInstance().getPreferences();
        Call<UpdateProfileImageResult> updateProfileImageResult = mbService.updateProfileImage(changedToken, body);
        Log.i("NetConfirm", " 서버에 이미지 요청...");
        updateProfileImageResult.enqueue(new Callback<UpdateProfileImageResult>() {
            @Override
            public void onResponse(Call<UpdateProfileImageResult> call, Response<UpdateProfileImageResult> response) {
                if (response.isSuccessful()) {
                    UpdateProfileImageResult result = response.body();
                    if (!(result.result.message == null || result.result.message.equals(""))) {
                        Toast.makeText(SettingActivity.this, "사진 업로드 성공", Toast.LENGTH_SHORT).show();
                        ApplicationController.getInstance().savePreferences(result.result.member_token);
                        Log.i("NetConfirm", "사진 업로드 후 token : " + result.result.member_token);
                        Log.i("NetConfirm", "토큰받아오고 개인정보 가져옴.");
                    } else {
                        Toast.makeText(SettingActivity.this, "사진 업로드 실패", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(SettingActivity.this, "사진 업로드 실패", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UpdateProfileImageResult> call, Throwable t) {
                Log.i("NetConfirm", " 서버에 이미지 요청.../fail");
                Toast.makeText(SettingActivity.this, "서버와 연결을 확인하세요", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //컨텍스트 메뉴 숏클릭.
            registerForContextMenu(v);
            openContextMenu(v);
            unregisterForContextMenu(v);
        }
    };

}
