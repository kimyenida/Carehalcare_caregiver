package carehalcare.carehalcare.Feature_write;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.FrameLayout;
import android.view.LayoutInflater;
import android.widget.RadioButton;
import android.widget.TextView;
import android.content.Intent;
import android.graphics.Bitmap;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import carehalcare.carehalcare.Feature_write.Active.ActiveFragment;
import carehalcare.carehalcare.Feature_write.Active.Active_API;
import carehalcare.carehalcare.Feature_write.Bowel.BowelFragment;
import carehalcare.carehalcare.Feature_write.Bowel.Bowel_API;
import carehalcare.carehalcare.Feature_write.Bowel.Bowel_adapter;
import carehalcare.carehalcare.Feature_write.Bowel.Bowel_text;
import carehalcare.carehalcare.Feature_write.Clean.CleanFragment;
import carehalcare.carehalcare.Feature_write.Clean.Clean_API;
import carehalcare.carehalcare.Feature_write.Clean.Clean_ResponseDTO;
import carehalcare.carehalcare.Feature_write.Clean.Clean_adapter;
import carehalcare.carehalcare.Feature_write.Clean.Clean_text;
import carehalcare.carehalcare.Feature_write.Meal.MealFragment;
import carehalcare.carehalcare.Feature_write.Meal.Meal_API;
import carehalcare.carehalcare.Feature_write.Meal.Meal_ResponseDTO;
import carehalcare.carehalcare.Feature_write.Meal.Meal_adapter;
import carehalcare.carehalcare.Feature_write.Meal.Meal_form;
import carehalcare.carehalcare.Feature_write.Meal.Meal_text;
import carehalcare.carehalcare.Feature_write.Medicine.MedicineFragment;
import carehalcare.carehalcare.Feature_write.Medicine.Medicine_API;
import carehalcare.carehalcare.Feature_write.Medicine.Medicine_adapter;
import carehalcare.carehalcare.Feature_write.Medicine.Medicine_text;
import carehalcare.carehalcare.Feature_write.Active.Active_adapter;
import carehalcare.carehalcare.Feature_write.Active.Active_text;
import carehalcare.carehalcare.Feature_write.Sleep.SleepFragment;
import carehalcare.carehalcare.Feature_write.Sleep.Sleep_API;
import carehalcare.carehalcare.Feature_write.Sleep.Sleep_adapter;
import carehalcare.carehalcare.Feature_write.Sleep.Sleep_text;
import carehalcare.carehalcare.Feature_write.Walk.WalkFragment;
import carehalcare.carehalcare.Feature_write.Walk.Walk_API;
import carehalcare.carehalcare.Feature_write.Walk.Walk_ResponseDTO;
import carehalcare.carehalcare.Feature_write.Walk.Walk_adapter;
import carehalcare.carehalcare.Feature_write.Walk.Walk_form;
import carehalcare.carehalcare.Feature_write.Walk.Walk_text;
import carehalcare.carehalcare.Feature_write.Wash.WashFragment;
import carehalcare.carehalcare.Feature_write.Wash.Wash_API;
import carehalcare.carehalcare.Feature_write.Wash.Wash_ResponseDTO;
import carehalcare.carehalcare.Feature_write.Wash.Wash_adapter;
import carehalcare.carehalcare.Feature_write.Wash.Wash_text;
import carehalcare.carehalcare.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class EightMenuActivity extends AppCompatActivity implements Button.OnClickListener {
    Long ids;  //TODO ids는 삭제할 id값
    LoadingDialog loadingDialog;
    private FrameLayout container;
    private static final int REQUEST_CODE = 1099;
    private static final int MY_PERMISSION_CAMERA = 1111;
    private static final int MY_PERMISSION_CAMERA2 = 1112;
    String mCurrentPhotoPath;
    Uri imageUri;
    private ArrayList<Active_text> activeArrayList;
    private ArrayList<Clean_text> cleanArrayList;
    private ArrayList<Wash_text> washArrayList;
    private ArrayList<Sleep_text> sleepArrayList;
    private ArrayList<Bowel_text> bowelArrayList;
    private ArrayList<Medicine_text> medicineArrayList;
    private Medicine_adapter medicineAdapter;
    private Active_adapter activeAdapter;
    private Clean_adapter cleanAdapter;
    private Bowel_adapter bowelAdapter;
    private Wash_adapter washAdapter;
    private Sleep_adapter sleepAdapter;
    private ArrayList<Meal_text> mealArrayList;
    private Meal_adapter mealAdapter;
    private Walk_adapter walkAdapter;
    private ArrayList<Walk_text> walkArrayList;
    Gson gson = new GsonBuilder()
            .setLenient()
            .create();
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(Meal_API.URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.eightmenu_main);
        container = (FrameLayout) findViewById(R.id.container_menu);
        if (Build.VERSION.SDK_INT < 23){}
        else {
            requestUserPermission();
        }
        loadingDialog = new LoadingDialog(this);
        loadingDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        loadingDialog.setCancelable(false);
    }

    // 각 버튼에 맞는 함수들
    public void onMeal(View view) {
        deleteview();
        MealFragment mealFragment = new MealFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container_menu, mealFragment);
        transaction.commit();

    }

    public void onWalk(View view){
        deleteview();
        WalkFragment walkFragment = new WalkFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container_menu, walkFragment);
        transaction.commit();

    }

    public void onMedicine(View view) {
        deleteview();
        MedicineFragment medicineFragment = new MedicineFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container_menu, medicineFragment);
        transaction.commit();
    }


    public void onActive(View view) {
        deleteview();
        ActiveFragment activeFragment = new ActiveFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container_menu, activeFragment);
        transaction.commit();
    }

    public void onSleep(View view) {
        deleteview();
        SleepFragment sleepFragment = new SleepFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container_menu, sleepFragment);
        transaction.commit();

    }
    public void onToilet(View view) {
        deleteview();
        BowelFragment bowelFragment = new BowelFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container_menu, bowelFragment);
        transaction.commit();

    }
    public void onWash(View view) {
        deleteview();
        WashFragment washFragment = new WashFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container_menu, washFragment);
        transaction.commit();

    }
    public void onClean(View view) {
        deleteview();
        CleanFragment cleanFragment = new CleanFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container_menu, cleanFragment);
        transaction.commit();
    }

    public void deleteview(){
        container.removeAllViews();
    }


    ActivityResultLauncher<Intent> activityResultPicture = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    try {
                        if (result.getResultCode() == RESULT_OK){
                            Log.i("REQUEST_TAKE_PHOTO", "OK");
                            Log.i("카메라 Uri주소", imageUri.getPath());
                        }
                    } catch (Exception e) {
                        Log.e("REQUEST_TAKE_PHOTO", e.toString());
                    }
                }
            }
    );
    ActivityResultLauncher<Intent> walkResultDetail = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() ==2888){
                        Uri uris = result.getData().getParcelableExtra("uris");

                        Walk_text dict = new Walk_text(uris, Long.valueOf(1), "uri");

                        walkArrayList.add(0, dict); //첫번째 줄에 삽입됨
                        //mArrayList.add(dict); //마지막 줄에 삽입됨

                        // 어댑터에서 RecyclerView에 반영하도록 합니다.
                        walkAdapter.notifyItemInserted(0);
                        walkAdapter.notifyDataSetChanged();
                    }
                }
            });
    ActivityResultLauncher<Intent> activityResultDetail = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() ==2888){
                        Uri uris = result.getData().getParcelableExtra("uris");
                        String tsa = result.getData().getStringExtra("edit");
                        Log.e("왜 안돼는건데ㄹㄹㄹㄹㄹㄹㄹ",""+uris.getPath());

                        String mealTodayResult;
                        mealTodayResult = tsa;
                        Date today_date = Calendar.getInstance().getTime();
                        SimpleDateFormat format = new SimpleDateFormat("yyyy년 M월 dd일 : HH시 MM분", Locale.getDefault());
                        String seeText = format.format(today_date);

                        Meal_text dict = new Meal_text(uris, mealTodayResult, Long.valueOf(1), "uri",seeText,"uriuri");

                        mealArrayList.add(0, dict); //첫번째 줄에 삽입됨
                        //mArrayList.add(dict); //마지막 줄에 삽입됨

                        // 어댑터에서 RecyclerView에 반영하도록 합니다.
                        mealAdapter.notifyItemInserted(0);
                        mealAdapter.notifyDataSetChanged();
                    }
                }
            });

    private void captureCamera(int using){
        String state = Environment.getExternalStorageState();
        // 외장 메모리 검사
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                File photoFile = null;
                try {
                    photoFile = createImageFile();
                } catch (IOException ex) {
                    Log.e("captureCamera Error", ex.toString());
                }
                if (photoFile != null) {
                    // getUriForFile의 두 번째 인자는 Manifest provier의 authorites와 일치해야 함

                    Uri providerURI = FileProvider.getUriForFile(this, getPackageName(), photoFile);
                    imageUri = providerURI;

                    // 인텐트에 전달할 때는 FileProvier의 Return값인 content://로만!!, providerURI의 값에 카메라 데이터를 넣어 보냄
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, providerURI);

                    if (using == 100) {
                        Log.e("dfdjfdkfjdskfjksdjflsjfk", "whwhwhwhwhwhwhwhw");
                        Intent mealintent = new Intent(this, Meal_form.class);
                        mealintent.putExtra("uri", providerURI);
                        Log.e("whwhwhwhwhwhwhwh", "whwhwhwhwhwhwhwhw");
                        activityResultDetail.launch(mealintent);
                    }

                    else if (using==200){
                        Intent walkintent = new Intent(this, Walk_form.class);
                        walkintent.putExtra("uri", providerURI);
                        walkResultDetail.launch(walkintent);
                    }
                    activityResultPicture.launch(takePictureIntent);                }
            }
        } else {
            Toast.makeText(this, "저장공간이 접근 불가능한 기기입니다", Toast.LENGTH_SHORT).show();
            return;
        }
    }

    public File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + ".jpg";
        File imageFile = null;
        File storageDir = new File(Environment.getExternalStorageDirectory() + "/Pictures", "yeeun");
        if (!storageDir.exists()) {
            Log.i("mCurrentPhotoPath1", storageDir.toString());
            storageDir.mkdirs();
        }

        imageFile = new File(storageDir, imageFileName);
        mCurrentPhotoPath = imageFile.getAbsolutePath();
        Log.e("현재 절대경로는 : ",""+mCurrentPhotoPath);
        return imageFile;
    }

    private void requestUserPermission(){
        try {
            if(Build.VERSION.SDK_INT > Build.VERSION_CODES.P){
                if (ActivityCompat.checkSelfPermission(EightMenuActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED
                        || ActivityCompat.checkSelfPermission(EightMenuActivity.this, Manifest.permission.CAMERA)!=PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(EightMenuActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.CAMERA}
                            ,MY_PERMISSION_CAMERA);
                } else {
                }
            } else{
                if (ActivityCompat.checkSelfPermission(EightMenuActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED
                        || ActivityCompat.checkSelfPermission(EightMenuActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED
                        || ActivityCompat.checkSelfPermission(EightMenuActivity.this, Manifest.permission.CAMERA)!=PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(EightMenuActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.CAMERA},MY_PERMISSION_CAMERA2);
                } else {
                }
            }
        } catch (Exception e){
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode != -1){
            for (int i = 0; i < grantResults.length; i++) {
                // grantResults[] : 허용된 권한은 0, 거부한 권한은 -1
                if (grantResults[i] < 0) {
                    Log.e("거부된 권한",""+permissions[i]+"       "+i+"      "+grantResults[i]);
                    // Toast.makeText(MainActivity.this, "해당 권한을 활성화 하셔야 합니다.", Toast.LENGTH_SHORT).show();
                }}
            switch (requestCode) {
                case MY_PERMISSION_CAMERA:
                    if (grantResults.length > 0 &&
                            grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    } else{
                        new AlertDialog.Builder(this)
                                .setTitle("알림")
                                .setMessage("저장소 권한이 거부되었습니다. 사용을 원하시면 설정에서 해당 권한을 직접 허용하셔야 합니다.")
                                .setNeutralButton("설정", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                        intent.setData(Uri.parse("package:" + getPackageName()));
                                        startActivity(intent);
                                    }
                                })
                                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        finish();
                                    }
                                })
                                .setCancelable(false)
                                .create()
                                .show();
                    }
                    break;
                case MY_PERMISSION_CAMERA2:
                    if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                            && grantResults[1] == PackageManager.PERMISSION_GRANTED){
                    }else {
                        checkPermission();
                    } break;
                default:
                    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            }
        }
    }
    @TargetApi(Build.VERSION_CODES.M)
    private void checkPermission(){
        boolean writeExternalStorageRationale = ActivityCompat.shouldShowRequestPermissionRationale(EightMenuActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int hasWriteExternalStoragePermission = ContextCompat.checkSelfPermission(EightMenuActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (hasWriteExternalStoragePermission == PackageManager.PERMISSION_DENIED &&
                writeExternalStorageRationale){
            Toast.makeText(EightMenuActivity.this, "앱을 실행하려면 퍼미션을 허가하셔야 합니다.", Toast.LENGTH_SHORT).show();
        } else if(hasWriteExternalStoragePermission == PackageManager.PERMISSION_DENIED
                && !writeExternalStorageRationale){
            new AlertDialog.Builder(this)
                    .setTitle("알림")
                    .setMessage("저장소 권한이 거부되었습니다. 사용을 원하시면 설정에서 해당 권한을 직접 허용하셔야 합니다.")
                    .setNeutralButton("설정", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            intent.setData(Uri.parse("package:" + getPackageName()));
                            startActivity(intent);
                        }
                    })
                    .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            finish();
                        }
                    })
                    .setCancelable(false)
                    .create()
                    .show();
        } else if(hasWriteExternalStoragePermission == PackageManager.PERMISSION_GRANTED){}
    }


    public void getmeallsit(){
        Meal_API meal_service = retrofit.create(Meal_API.class);
        loadingDialog.show();
        meal_service.getDatameal("userId1","puserId1").enqueue(new Callback<List<Meal_ResponseDTO>>() {
            @Override
            public void onResponse(Call<List<Meal_ResponseDTO>> call, Response<List<Meal_ResponseDTO>> response) {
                if (response.body() != null) {
                    List<Meal_ResponseDTO> datas = response.body();
                    String encodedString;
                    byte[] encodeByte;
                    Bitmap mealbitmap;
                    String filepath_;
                    String times;
                    for (int i = 0; i < datas.size(); i++) {

                        encodedString = response.body().get(i).getImages().get(0).getEncodedString();

                        encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
                        mealbitmap = BitmapFactory.decodeByteArray( encodeByte, 0, encodeByte.length ) ;

                        filepath_ = response.body().get(i).getImages().get(0).getFilePath();
                        times = response.body().get(i).getCreatedDateTime();
                        Meal_text dict_0 = new Meal_text(filepath_,
                                response.body().get(i).getContent(),response.body().get(i).getId(),times,"pth");
//                        Meal_text dict_0 = new Meal_text(mealbitmap,
//                                response.body().get(i).getContent(),response.body().get(i).getId());

                        mealArrayList.add( dict_0);
                        mealAdapter.notifyItemInserted(0);
                        Log.e("음식리스트 출력", "********1*************1*********!");
                    }
                    Log.e("getDatameal end", "======================================");

                    loadingDialog.dismiss();
                }}
            @Override
            public void onFailure(Call<List<Meal_ResponseDTO>> call, Throwable t) {
                Log.e("통신에러","+"+t.toString());
                Toast.makeText(getApplicationContext(), "통신에러", Toast.LENGTH_SHORT).show();
                loadingDialog.dismiss();
            }
        });

    }
    public void getwalklist(){
        Walk_API walk_service = retrofit.create(Walk_API.class);
        loadingDialog.show();
        walk_service.getDataWalk("userId1","puserId1").enqueue(new Callback<List<Walk_ResponseDTO>>() {
            @Override
            public void onResponse(Call<List<Walk_ResponseDTO>> call, Response<List<Walk_ResponseDTO>> response) {
                if (response.body() != null) {
                    List<Walk_ResponseDTO> datas = response.body();
                    String encodedString;
                    byte[] encodeByte;
                    Bitmap mealbitmap;
                    for (int i = 0; i < datas.size(); i++) {
                        encodedString = response.body().get(i).getImages().get(0).getEncodedString();
                        encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
                        mealbitmap = BitmapFactory.decodeByteArray( encodeByte, 0, encodeByte.length ) ;

                        Walk_text dict_0 = new Walk_text(mealbitmap,
                                response.body().get(i).getId());

                        walkArrayList.add( dict_0);
                        walkAdapter.notifyItemInserted(0);
                        Log.e("산책리스트 출력", "********1*************1*********!");
                    }
                    Log.e("getDatawalk end", "======================================");

                    loadingDialog.dismiss();
                }}
            @Override
            public void onFailure(Call<List<Walk_ResponseDTO>> call, Throwable t) {
                Log.e("통신에러","+"+t.toString());
                Toast.makeText(getApplicationContext(), "통신에러", Toast.LENGTH_SHORT).show();
                loadingDialog.dismiss();
            }
        });
    }

    @Override
    public void onClick(View view) {
    }

}
