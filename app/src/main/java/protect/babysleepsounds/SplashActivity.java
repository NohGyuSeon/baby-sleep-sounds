package protect.babysleepsounds;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.romainpiel.shimmer.Shimmer;
import com.romainpiel.shimmer.ShimmerTextView;

public class SplashActivity extends AppCompatActivity {
    Shimmer shimmer;
    ShimmerTextView shimmer_app;
    ShimmerTextView shimmer_author;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);	//theme로 지정했다면 삭제한다.

        shimmer_app = findViewById(R.id.shimmer_app);
        shimmer_author = findViewById(R.id.shimmer_author);

        shimmer = new Shimmer();
        shimmer.start(shimmer_app);
        shimmer.start(shimmer_author);

        moveMain(2);	//1초 후 main activity 로 넘어감
    }

    private void moveMain(int sec) {
        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                //new Intent(현재 context, 이동할 activity)
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);

                startActivity(intent);	//intent 에 명시된 액티비티로 이동

                finish();	//현재 액티비티 종료
            }
        }, 1000 * sec); // sec초 정도 딜레이를 준 후 시작
    }
}