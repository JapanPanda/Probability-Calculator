package example.firstprogram;

import android.content.Intent;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

public class MainActivity extends AppCompatActivity {
    private AdView mAdView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void startCalculate(View view) {
        Intent intent = new Intent(this, NewGame.class);
        startActivity(intent);
    }

    public void startRealTimeCalculate(View view) {
        Intent intent = new Intent(this, InAGame.class);
        startActivity(intent);
    }
}
