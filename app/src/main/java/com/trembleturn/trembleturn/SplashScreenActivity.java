package com.trembleturn.trembleturn;

import android.content.Intent;
import android.support.v4.view.animation.FastOutLinearInInterpolator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageView;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;


public class SplashScreenActivity extends BaseActivity implements Animation.AnimationListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.splash_appear);
        anim.setAnimationListener(this);
        anim.setInterpolator(new FastOutLinearInInterpolator());
        anim.setRepeatCount(0);
        anim.setDuration(2000);
        AppCompatImageView splashImage = findViewById(R.id.iv_splash_icon);
        splashImage.setAnimation(anim);
        anim.start();
    }

    private void openNextActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {
        openNextActivity();
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }
}
