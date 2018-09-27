package com.thedot.mystoryinenglishn.Splash;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.thedot.mystoryinenglishn.Index.MainActivity;
import com.thedot.mystoryinenglishn.R;
import com.thedot.mystoryinenglishn.Setting.Preferences;

public class SplashActivity extends AppCompatActivity {
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(Preferences.getPersonalInfo(getApplicationContext())) {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    moveActivity();
                }
            }, 3000);
        }else{
            setContentView(R.layout.activity_spash);
            TextView info_click = findViewById(R.id.splash_pinfo_click);
            info_click.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.the-dot.co.kr/portfolio/our-story-privacy-policy/"));
                    startActivity(intent);
                }
            });
            Button personal_info = findViewById(R.id.splay_per_button);
            personal_info.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Preferences.setPersonalInfo(getApplicationContext(),true);
                    moveActivity();
                }
            });
        }
    }

    public void moveActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
