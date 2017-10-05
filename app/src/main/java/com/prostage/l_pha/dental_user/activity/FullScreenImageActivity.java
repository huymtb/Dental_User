package com.prostage.l_pha.dental_user.activity;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.prostage.l_pha.dental_user.R;
import com.prostage.l_pha.dental_user.utils.TouchImageView;

public class FullScreenImageActivity extends AppCompatActivity {

    private TouchImageView touchImageView;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_image);

        addControls();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setValues();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        System.gc();
        finish();
    }

    private void addControls() {
        touchImageView = (TouchImageView) findViewById(R.id.imageViewFullScreen);
        progressDialog = new ProgressDialog(this);
    }

    private void setValues() {
        String nameUser, urlPhotoClick;
        nameUser = getIntent().getStringExtra("idUser");
        urlPhotoClick = getIntent().getStringExtra("urlPhotoClick");
        Log.i("TAG","imagem recebida " + urlPhotoClick);

        Glide.with(this)
                .load( urlPhotoClick)
                .asBitmap()
                .error(getResources().getDrawable(R.drawable.placeholder))
                .override(1024,1024)
                .fitCenter()
                .into(new SimpleTarget<Bitmap>() {

            @Override
            public void onLoadStarted(Drawable placeholder) {
                progressDialog.setMessage("Loading image...");
                progressDialog.show();
                progressDialog.setCancelable(false);
            }

            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                progressDialog.dismiss();
                touchImageView.setImageBitmap(resource);
            }

            @Override
            public void onLoadFailed(Exception e, Drawable errorDrawable) {
                Toast.makeText(FullScreenImageActivity.this,"Error, please try again",Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }
}
