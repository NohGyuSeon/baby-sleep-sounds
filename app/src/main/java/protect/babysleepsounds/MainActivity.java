package protect.babysleepsounds;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.CountDownTimer;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.common.collect.ImmutableMap;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import es.dmoral.toasty.Toasty;
import nl.bravobit.ffmpeg.ExecuteBinaryResponseHandler;
import nl.bravobit.ffmpeg.FFmpeg;
import nl.bravobit.ffmpeg.exceptions.FFmpegCommandAlreadyRunningException;
/**
 * @author NohGyuSeon
 * @Date 2023.01.06
 * @version 11.0, OSSP
 */
public class MainActivity extends AppCompatActivity
{
    private final static String TAG = "BabySleepSounds";

    private static final String ORIGINAL_MP3_FILE = "original.mp3";
    private static final String PROCESSED_RAW_FILE = "processed.raw";

    private Map<String, Integer> _soundMap;
    private Map<String, Integer> _timeMap;

    private boolean _playing = false;
    private Timer _timer;

    private FFmpeg _ffmpeg;
    private ProgressDialog _encodingProgress;

    // 타이머 구현 변수 선언부
    int hour, minute, second;

    TextView hourTV, minuteTV, secondTV;
    TextView msgView;

    LinearLayout timeCountLV;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        Preferences.get(this).applyTheme();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        hourTV = (TextView)findViewById(R.id.hourTV);
        minuteTV = (TextView)findViewById(R.id.minuteTV);
        secondTV = (TextView)findViewById(R.id.secondTV);

        msgView = (TextView)findViewById(R.id.msgView);
        timeCountLV = (LinearLayout)findViewById(R.id.timeCountLV);

        // These sound files by convention are:
        // - take a ~10 second clip
        // - Apply a 2 second fade-in and fade-out
        // - Cut the first 3 seconds, and place it over the last three seconds
        //   which should create a seamless track appropriate for looping
        // - Save as a mp3 file, 128kbps, stereo
        _soundMap = ImmutableMap.<String, Integer>builder()
            .put(getResources().getString(R.string.campfire), R.raw.campfire)
            .put(getResources().getString(R.string.dryer), R.raw.dryer)
            .put(getResources().getString(R.string.fan), R.raw.fan)
            .put(getResources().getString(R.string.ocean), R.raw.ocean)
            .put(getResources().getString(R.string.rain), R.raw.rain)
            .put(getResources().getString(R.string.refrigerator), R.raw.refrigerator)
            .put(getResources().getString(R.string.shhhh), R.raw.shhhh)
            .put(getResources().getString(R.string.shower), R.raw.shower)
            .put(getResources().getString(R.string.stream), R.raw.stream)
            .put(getResources().getString(R.string.vacuum), R.raw.vacuum)
            .put(getResources().getString(R.string.water), R.raw.water)
            .put(getResources().getString(R.string.waterfall), R.raw.waterfall)
            .put(getResources().getString(R.string.waves), R.raw.waves)
            .put(getResources().getString(R.string.whiteNoise), R.raw.white_noise)
            .build();

        _timeMap = ImmutableMap.<String, Integer>builder()
                .put(getResources().getString(R.string.disabled), 0)
                .put(getResources().getString(R.string.time_1minute), 1000*60*1)
                .put(getResources().getString(R.string.time_5minute), 1000*60*5)
                .put(getResources().getString(R.string.time_10minute), 1000*60*10)
                .put(getResources().getString(R.string.time_30minute), 1000*60*30)
                .put(getResources().getString(R.string.time_1hour), 1000*60*60*1)
                .put(getResources().getString(R.string.time_2hour), 1000*60*60*2)
                .put(getResources().getString(R.string.time_4hour), 1000*60*60*4)
                .put(getResources().getString(R.string.time_8hour), 1000*60*60*8)
                .build();

        final Spinner soundSpinner = findViewById(R.id.soundSpinner);

        List<String> names = new ArrayList<>(_soundMap.keySet());

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, names);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        soundSpinner.setAdapter(dataAdapter);

        // Add NohGyuSeon
        // Timer imagebutton click animation
        ImageButton imageButton = findViewById(R.id.imageButton);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YoYo.with(Techniques.FadeIn)
                        .duration(700)
                        .repeat(1)
                        .playOn(findViewById(R.id.timeCountLV));
            }
        });

        // Add NohGyuSeon
        // Video imagebutton click event
        ImageButton imageButton2 = findViewById(R.id.imageButton2);
        imageButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), VideoActivity.class));
                Toasty.info(getApplication(), R.string.playVideo, Toast.LENGTH_SHORT, true).show();
            }
        });

        // Add NohGyuSeon
        // Show gif image
        soundSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            ImageView gif_img = (ImageView) findViewById(R.id.imageView);
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {

                if (position == 0) {
                    Glide.with(view).load(R.raw.gif_campfire).into(gif_img);
                } else if (position == 1) {
                    Glide.with(view).load(R.raw.gif_dryer).into(gif_img);
                } else if (position == 2) {
                    Glide.with(view).load(R.raw.gif_fan).into(gif_img);
                } else if (position == 3) {
                    Glide.with(view).load(R.raw.gif_ocean).into(gif_img);
                } else if (position == 4) {
                    Glide.with(view).load(R.raw.gif_rain).into(gif_img);
                } else if (position == 5) {
                    Glide.with(view).load(R.raw.gif_refrigerator).into(gif_img);
                } else if (position == 6) {
                    Glide.with(view).load(R.raw.gif_shhh).into(gif_img);
                } else if (position == 7) {
                    Glide.with(view).load(R.raw.gif_shower).into(gif_img);
                } else if (position == 8) {
                    Glide.with(view).load(R.raw.gif_stream).into(gif_img);
                } else if (position == 9) {
                    Glide.with(view).load(R.raw.gif_vacuum).into(gif_img);
                } else if (position == 10) {
                    Glide.with(view).load(R.raw.gif_water).into(gif_img);
                } else if (position == 11) {
                    Glide.with(view).load(R.raw.gif_waterfall).into(gif_img);
                } else if (position == 12) {
                    Glide.with(view).load(R.raw.gif_waves).into(gif_img);
                } else if (position == 13) {
                    Glide.with(view).load(R.raw.gif_whitenoise).into(gif_img);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Glide.with(adapterView).load(R.raw.gif_campfire).into(gif_img);
            }
        });

        // Modify NohGyuSeon
        // Set textview value to selected time
        final Spinner sleepTimeoutSpinner = findViewById(R.id.sleepTimerSpinner);
        List<String> times = new ArrayList<>(_timeMap.keySet());
        sleepTimeoutSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                if(_playing)
                {
                    updatePlayTimeout();
                    Toasty.success(getApplication(), R.string.sleepTimerUpdated, Toast.LENGTH_SHORT, true).show();
                }

                if (position == 1) {
                    timeSet("000100");
                } else if (position == 2) {
                    timeSet("000500");
                } else if (position == 3) {
                    timeSet("001000");
                } else if (position == 4) {
                    timeSet("003000");
                } else if (position == 5) {
                    timeSet("010000");
                } else if (position == 6) {
                    timeSet("020000");
                } else if (position == 7) {
                    timeSet("040000");
                } else if (position == 8) {
                    timeSet("080000");
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {
                // noop
            }
        });

        ArrayAdapter<String> timesAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, times);
        timesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sleepTimeoutSpinner.setAdapter(timesAdapter);

        setVolumeControlStream(AudioManager.STREAM_MUSIC);

        final Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(_playing == false)
                {
                    startPlayback();
                }
                else
                {
                    stopPlayback();
                }
            }
        });

        _ffmpeg = FFmpeg.getInstance(this);


        if(_ffmpeg.isSupported())
        {
            button.setEnabled(true);
        }
        else
        {
            Log.d(TAG, "ffmpeg not supported");
            reportPlaybackUnsupported();
        }

    }

    // Use the Toasty library instead of these
/*    private void showToast(String str) {
        LayoutInflater inflater = getLayoutInflater();

        View layout = inflater.inflate(R.layout.toastborder, (ViewGroup) findViewById(R.id.toast_layout));

        TextView Toasttext = layout.findViewById(R.id.text);

        Toast toast = new Toast(this);
        Toasttext.setText(str);
        toast.setGravity(Gravity.CENTER, 0, -300);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
    }*/

    // 선언 및 초기화
    private void timeSet(String str) {

        String getHour = str.substring(0, 2);
        String getMin = str.substring(2, 4);
        String getSecond = str.substring(4, 6);

        hourTV.setText(getHour);
        minuteTV.setText(getMin);
        secondTV.setText(getSecond);

        hour = Integer.parseInt(getHour);
        minute = Integer.parseInt(getMin);
        second = Integer.parseInt(getSecond);
    }

    /**
     * Report to the user that playback is not supported on this device
     */
    private void reportPlaybackUnsupported()
    {
        Toasty.warning(getApplication(), R.string.playbackNotSupported, Toast.LENGTH_SHORT, true).show();

    }

    private void startPlayback()
    {
        final Spinner soundSpinner = findViewById(R.id.soundSpinner);
        String selectedSound = (String)soundSpinner.getSelectedItem();
        int id = _soundMap.get(selectedSound);

        setVolumeControlStream(AudioManager.STREAM_MUSIC);

        try
        {
            File originalFile = new File(getFilesDir(), ORIGINAL_MP3_FILE);

            Log.i(TAG, "Writing file out prior to WAV conversion");
            writeToFile(id, originalFile);

            final File processed = new File(getFilesDir(), PROCESSED_RAW_FILE);
            if(processed.exists())
            {
                boolean result = processed.delete();
                if(result == false)
                {
                    throw new IOException("Unable to delete previous file, cannot prepare new file");
                }
            }

            Log.i(TAG, "Converting file to WAV");

            LinkedList<String> arguments = new LinkedList<>();
            arguments.add("-i");
            arguments.add(originalFile.getAbsolutePath());
            if(Preferences.get(this).isLowPassFilterEnabled())
            {
                int frequencyValue = Preferences.get(this).getLowPassFilterFrequency();

                Log.i(TAG, "Will perform lowpass filter to " + frequencyValue + " Hz");
                arguments.add("-af");
                arguments.add("lowpass=frequency=" + frequencyValue);
            }
            arguments.add("-f");
            arguments.add("s16le");
            arguments.add("-acodec");
            arguments.add("pcm_s16le");
            arguments.add(processed.getAbsolutePath());

            _encodingProgress = new ProgressDialog(this);
            _encodingProgress.setMessage(getString(R.string.preparing));
            _encodingProgress.show();

            Log.i(TAG, "Launching ffmpeg");
            String[] cmd = arguments.toArray(new String[arguments.size()]);
            _ffmpeg.execute(cmd, new ExecuteBinaryResponseHandler()
            {
                public void onStart()
                {
                    Log.d(TAG, "ffmpeg execute onStart()");
                }

                public void onSuccess(String message)
                {
                    Log.d(TAG, "ffmpeg execute onSuccess(): " + message);

                    Intent startIntent = new Intent(MainActivity.this, AudioService.class);
                    startIntent.putExtra(AudioService.AUDIO_FILENAME_ARG, processed.getAbsolutePath());
                    startService(startIntent);

                    updateToPlaying();
                }

                public void onProgress(String message)
                {
                    Log.d(TAG, "ffmpeg execute onProgress(): " + message);
                }

                public void onFailure(String message)
                {
                    Log.d(TAG, "ffmpeg execute onFailure(): " + message);
                    reportPlaybackFailure();
                }

                public void onFinish()
                {
                    Log.d(TAG, "ffmpeg execute onFinish()");
                }
            });
        }
        catch(IOException|FFmpegCommandAlreadyRunningException e)
        {
            Log.i(TAG, "Failed to start playback", e);
            reportPlaybackFailure();
        }
    }

    /**
     * Write a resource to a file
     * @param resource resource to write
     * @param output destination of the resource
     * @throws IOException if a write failure occurs
     */
    private void writeToFile(int resource, File output) throws IOException
    {
        InputStream rawStream = getResources().openRawResource(resource);
        FileOutputStream outStream = null;

        byte[] buff = new byte[1024];
        int read;

        try
        {
            outStream = new FileOutputStream(output);

            while ((read = rawStream.read(buff)) > 0)
            {
                outStream.write(buff, 0, read);
            }
        }
        finally
        {
            try
            {
                rawStream.close();
            }
            catch(IOException e)
            {
                // If it fails, there is nothing to do
            }

            if(outStream != null)
            {
                outStream.close();
            }
        }
    }

    /**
     * Report to the user that playback has failed, and hide the progress dialog
     */
    private void reportPlaybackFailure()
    {
        if(_encodingProgress != null)
        {
            _encodingProgress.dismiss();
            _encodingProgress = null;
        }

        Toasty.warning(getApplication(), R.string.playbackFailure, Toast.LENGTH_SHORT, true).show();
    }

    /**
     * Update the timeout for playback to stop
     */
    private void updatePlayTimeout() {
        // Cancel the running timer
        if (_timer != null) {
            _timer.cancel();
            _timer.purge();
        }

        final Spinner sleepTimeoutSpinner = findViewById(R.id.sleepTimerSpinner);
        String selectedTimeout = (String) sleepTimeoutSpinner.getSelectedItem();
        int timeoutMs = _timeMap.get(selectedTimeout);
        if (timeoutMs > 0) {
            _timer = new Timer();
            TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {
                    // 반복실행할 구문
                    msgView.setText("");

                    // 0초 이상이면
                    if (second != 0) {
                        //1초씩 감소
                        second--;

                        // 0분 이상이면
                    } else if (minute != 0) {
                        // 1분 = 60초
                        second = 60;
                        second--;
                        minute--;

                        // 0시간 이상이면
                    } else if (hour != 0) {
                        // 1시간 = 60분
                        second = 60;
                        minute = 60;
                        second--;
                        minute--;
                        hour--;
                    }

                    // 시, 분, 초가 10이하(한자리수) 라면
                    // 숫자 앞에 0을 붙인다 ( 8 -> 08 )
                    if (second <= 9) {
                        secondTV.setText("0" + second);
                    } else {
                        secondTV.setText(Integer.toString(second));
                    }

                    if (minute <= 9) {
                        minuteTV.setText("0" + minute);
                    } else {
                        minuteTV.setText(Integer.toString(minute));
                    }

                    if (hour <= 9) {
                        hourTV.setText("0" + hour);
                    } else {
                        hourTV.setText(Integer.toString(hour));
                    }

                    // 시분초가 모두 0이 될 시 타이머를 종료하고, 메시지를 보여준다.
                    if (hour == 0 && minute == 0 && second == 0) {
                        msgView.setText("타이머가 종료되었습니다.");
                        stopPlayback();
                    }
                }
            };

            // 타이머를 실행
            _timer.schedule(timerTask, 0, 1000); // Timer 실행
        }
    }

    /**
     * Update the UI to reflect it is playing
     */
    private void updateToPlaying()
    {
        _playing = true;

        runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                updatePlayTimeout();

                final Button button = findViewById(R.id.button);
                button.setText(R.string.stop);

                setControlsEnabled(false);

                if(_encodingProgress != null)
                {
                    _encodingProgress.hide();
                    _encodingProgress = null;
                }
            }
        });
    }

    private void stopPlayback()
    {
        Intent stopIntent = new Intent(MainActivity.this, AudioService.class);
        startService(stopIntent);

        _playing = false;

        if(_timer != null)
        {
            _timer.cancel();
            _timer.purge();
            _timer = null;
        }

        runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                final Button button = findViewById(R.id.button);
                button.setText(R.string.play);

                setControlsEnabled(true);
            }
        });
    }

    private void setControlsEnabled(boolean enabled)
    {
        for(int resId : new int[]{R.id.soundSpinner})
        {
            final View view = findViewById(resId);
            view.setEnabled(enabled);
        }
    }

    @Override
    protected void onDestroy()
    {
        if(_playing)
        {
            stopPlayback();
        }

        for(String toDelete : new String[]{ORIGINAL_MP3_FILE, PROCESSED_RAW_FILE})
        {
            File file = new File(getFilesDir(), toDelete);
            boolean result = file.delete();
            if(result == false)
            {
                Log.w(TAG, "Failed to delete file on exit: " + file.getAbsolutePath());
            }
        }

        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();

        if(id == R.id.action_settings)
        {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }
        else if (id == R.id.action_about)
        {
            displayAboutDialog();
            return true;
        }
        else if (id == R.id.action_view)
        {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void displayAboutDialog()
    {
        final Map<String, String> USED_LIBRARIES = ImmutableMap.of
        (
            "FFmpeg", "https://ffmpeg.org/",
            "FFmpeg-Android", "https://github.com/writingminds/ffmpeg-android"
        );

        final Map<String, String> SOUND_RESOURCES = ImmutableMap.of
        (
            "Canton Becker", "http://whitenoise.cantonbecker.com/",
            "The MC2 Method", "http://mc2method.org/white-noise/",
            "Campfire-1.mp3 Copyright SoundJay.com Used with Permission", "https://www.soundjay.com/nature/campfire-1.mp3"
        );

        final Map<String, String> IMAGE_RESOURCES = ImmutableMap.of
        (
            "'Music' by Aleks from the Noun Project", "https://thenounproject.com/term/music/886761/"
        );

        StringBuilder libs = new StringBuilder().append("<ul>");
        for (Map.Entry<String, String> entry : USED_LIBRARIES.entrySet())
        {
            libs.append("<li><a href=\"").append(entry.getValue()).append("\">").append(entry.getKey()).append("</a></li>");
        }
        libs.append("</ul>");

        StringBuilder soundResources = new StringBuilder().append("<ul>");
        for (Map.Entry<String, String> entry : SOUND_RESOURCES.entrySet())
        {
            soundResources.append("<li><a href=\"").append(entry.getValue()).append("\">").append(entry.getKey()).append("</a></li>");
        }
        soundResources.append("</ul>");

        StringBuilder imageResources = new StringBuilder().append("<ul>");
        for (Map.Entry<String, String> entry : IMAGE_RESOURCES.entrySet())
        {
            imageResources.append("<li><a href=\"").append(entry.getValue()).append("\">").append(entry.getKey()).append("</a></li>");
        }
        imageResources.append("</ul>");

        String appName = getString(R.string.app_name);
        int year = Calendar.getInstance().get(Calendar.YEAR);

        String version = "?";
        try
        {
            PackageInfo pi = getPackageManager().getPackageInfo(getPackageName(), 0);
            version = pi.versionName;
        }
        catch (PackageManager.NameNotFoundException e)
        {
            Log.w(TAG, "Package name not found", e);
        }

        WebView wv = new WebView(this);
        String html =
            "<meta http-equiv=\"content-type\" content=\"text/html; charset=utf-8\" />" +
            "<img src=\"file:///android_res/mipmap/ic_launcher.png\" alt=\"" + appName + "\"/>" +
            "<h1>" +
            String.format(getString(R.string.about_title_fmt),
                    "<a href=\"" + getString(R.string.app_webpage_url)) + "\">" +
            appName +
            "</a>" +
            "</h1><p>" +
            appName +
            " " +
            String.format(getString(R.string.debug_version_fmt), version) +
            "</p><p>" +
            String.format(getString(R.string.app_revision_fmt),
                    "<a href=\"" + getString(R.string.app_revision_url) + "\">" +
                            getString(R.string.app_revision_url) +
                            "</a>") +
            "</p><hr/><p>" +
            String.format(getString(R.string.app_copyright_fmt), year) +
            "</p><hr/><p>" +
            getString(R.string.app_license) +
            "</p><hr/><p>" +
            String.format(getString(R.string.sound_resources), appName, soundResources.toString()) +
            "</p><hr/><p>" +
            String.format(getString(R.string.image_resources), appName, imageResources.toString()) +
            "</p><hr/><p>" +
            String.format(getString(R.string.app_libraries), appName, libs.toString());

        wv.loadDataWithBaseURL("file:///android_res/drawable/", html, "text/html", "utf-8", null);
        new AlertDialog.Builder(this)
            .setView(wv)
            .setCancelable(true)
            .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener()
            {
                public void onClick(DialogInterface dialog, int which)
                {
                    dialog.dismiss();
                }
            })
            .show();
    }
}
