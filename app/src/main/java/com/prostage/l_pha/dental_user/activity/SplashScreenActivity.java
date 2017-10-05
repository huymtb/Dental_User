package com.prostage.l_pha.dental_user.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.prostage.l_pha.dental_user.R;
import com.prostage.l_pha.dental_user.common.Constants;
import com.prostage.l_pha.dental_user.utils.SharedHelper;

public class SplashScreenActivity extends Activity {

	Thread splashTread;
	TextView txtAppName;
	ImageView imgLogo;
	SharedHelper sharedHelper;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash_screen_layout);

		sharedHelper = new SharedHelper(this);

		txtAppName = (TextView) findViewById(R.id.txtAppName);
		imgLogo = (ImageView) findViewById(R.id.imgLogo);

		Typeface custom_font = Typeface.createFromAsset(getAssets(), "Fabfelt.otf");
		txtAppName.setTypeface(custom_font);

		startAnimations();
	}

	private void startAnimations() {
		Animation anim = AnimationUtils.loadAnimation(this, R.anim.translate);
		anim.reset();

		imgLogo.clearAnimation();
		imgLogo.startAnimation(anim);

		anim = AnimationUtils.loadAnimation(this, R.anim.alpha);
		anim.reset();

		txtAppName.clearAnimation();
		txtAppName.startAnimation(anim);

		splashTread = new Thread() {
			@Override
			public void run() {
				try {
					int waited = 0;
					// Splash screen pause time
					while (waited < 3500) {
						sleep(100);
						waited += 100;
					}
					String authToken = sharedHelper.getString(Constants.TOKEN);
					Intent intent = null;
					if (authToken != null) {
						intent = new Intent(SplashScreenActivity.this, MainActivity.class);
					} else {
						intent = new Intent(SplashScreenActivity.this, LoginActivity.class);
					}
					intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
					startActivity(intent);
					SplashScreenActivity.this.finish();
				} catch (InterruptedException e) {
					// do nothing
				} finally {
					SplashScreenActivity.this.finish();
				}

			}
		};
		splashTread.start();
	}
}
