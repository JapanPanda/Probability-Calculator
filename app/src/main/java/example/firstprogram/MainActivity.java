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
        /*MobileAds.initialize(getApplicationContext(),
                "ca-app-pub-1651355529246335~3923360073");
        mAdView = (AdView) this.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);*/
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
