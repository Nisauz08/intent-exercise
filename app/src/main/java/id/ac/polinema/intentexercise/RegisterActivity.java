package id.ac.polinema.intentexercise;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;

public class RegisterActivity extends AppCompatActivity {
    private static final String TAG = RegisterActivity.class.getCanonicalName();
    private static final int GALLERY_REQUEST_CODE = 1;

    public static final String FULLNAME_KEY ="fullname";
    public static final String EMAIL_KEY ="email";
    public static final String PASSWORD_KEY ="password";
    public static final String CONFIRM_KEY ="confirm";
    public static final String HOMEPAGE_KEY ="homepage";
    public static final String ABOUT_KEY ="about";

    public static final String AVATARIMAGE_KEY ="image";

    private EditText fullnameInput;
    private EditText emailInput;
    private EditText passwordInput;
    private EditText confirmInput;
    private EditText homepageInput;
    private EditText aboutInput;
    private ImageView avatarImage;

    private Uri image = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        avatarImage = findViewById(R.id.image_profile);

        fullnameInput = findViewById(R.id.text_fullname);
        emailInput = findViewById(R.id.text_email);
        passwordInput = findViewById(R.id.text_password);
        confirmInput = findViewById(R.id.text_confirm_password);
        homepageInput = findViewById(R.id.text_homepage);
        aboutInput = findViewById(R.id.text_about);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_CANCELED){
            return;
        }

        if (requestCode == GALLERY_REQUEST_CODE){
            if (data !=  null){
                try {
                    image = data.getData();
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), image);
                    avatarImage.setImageBitmap(bitmap);
                } catch (IOException e){
                    Toast.makeText(this, "Can't load image", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, e.getMessage());
                }
            }
        }
    }

    public void changeAvatar(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, GALLERY_REQUEST_CODE);
    }

    public void handleOk(View view) {
        String fullname = fullnameInput.getText().toString();
        String email = emailInput.getText().toString();
        String password = passwordInput.getText().toString();
        String confirm = confirmInput.getText().toString();
        String homepage = homepageInput.getText().toString();
        String about = aboutInput.getText().toString();

        if (fullnameInput.length()==0){
            fullnameInput.setError("Ayo isi nama dahulu");
        } else if (emailInput.length()==0) {
            emailInput.setError("Ayo isi email dulu");
        } else if (passwordInput.length()==0){
            passwordInput.setError("Ayo isi password dulu");
        } else if (confirmInput.length()==0){
            confirmInput.setError("Konfirmasi password dahulu");
        } else if (!password.equals(confirm)) {
            confirmInput.setError("Password tidak sama");
        } else if (homepage.length()==0){
            homepageInput.setError("Isi dahulu");
        } else if (about.length()==0){
            aboutInput.setError("Isi dahulu");
        } else {
            Intent i = new Intent(this, ProfileActivity.class);
            i.putExtra(FULLNAME_KEY, fullname);
            i.putExtra(EMAIL_KEY, email);
            i.putExtra(HOMEPAGE_KEY, homepage);
            i.putExtra(ABOUT_KEY, about);

            if (image !=null){
                i.putExtra(AVATARIMAGE_KEY, image.toString());
                try{
                    startActivity(i);
                } catch (Exception ex){
                    i.putExtra(AVATARIMAGE_KEY, "");
                    Toast.makeText(this, "Your image to big", Toast.LENGTH_SHORT).show();
                    startActivity(i);
                }
            } else {
                Toast.makeText(this,"Please insert your image", Toast.LENGTH_SHORT).show();
                changeAvatar(view);
            }
        }
    }
}
