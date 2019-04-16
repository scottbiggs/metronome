package sleepfuriously.com.metronome;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import sleepfuriously.com.metronome.model.DefaultSettings;
import sleepfuriously.com.metronome.widgets.AccentWidget;
import sleepfuriously.com.metronome.widgets.TempoWidget;

import android.animation.Animator;
import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Process;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


/**
 * My attempt at making a Metronome program.  There's a long
 * list of wished-for features on the github readme.
 */
public class MainActivity extends AppCompatActivity
        implements TempoWidget.TempoChanger, AccentWidget.AccentChanger {

    //----------------------
    //  constants
    //----------------------

    private static final String TAG = MainActivity.class.getSimpleName();

    public static final int
            MIN_TEMPO = 1,
            MAX_TEMPO = 1000,
            MIN_ACCENT = 1,
            MAX_ACCENT = 40;

    private static final int RECORDING_ANIM_DURATION = 300;

    /** Milliseconds max for a recording */
    private static final int MAX_RECORDING_TIME = 5000;

    //----------------------
    //  widgets
    //----------------------

    /** Displays current beats per minutes */
    private TextView m_bbp_tv;

    /** Displays how many beats per grouping to accent */
    private TextView m_accent_tv;

    /** Slider for changing tempo */
    private TempoWidget m_tempo_widget;

    /** todo: slider or changing the accent */
    private AccentWidget m_accent_widget;

    /** The layout that flashes */
    private ConstraintLayout m_flash_cl;

    //----------------------
    //  data
    //----------------------

    /** the current tempo */
    private int m_tempo;

    /** current accent */
    private int m_accent;

    /** Recording will happen until this flag turns false */
    private boolean m_keepRecording = false;

    /** colors for animating while recording */
    private int m_colorOrig, m_colorStart, m_colorEnd;

    /** Used for animating colors while recording sounds */
    private ValueAnimator m_colorAnimator;


    //----------------------
    //  methods
    //----------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // init widgets
        m_bbp_tv = findViewById(R.id.tempo_tv);
        m_accent_tv = findViewById(R.id.accent_tv);
        m_tempo_widget = findViewById(R.id.tempo_widget);
        m_accent_widget = findViewById(R.id.accent_widget);
        Button record_butt = findViewById(R.id.record_butt);
        Button play_butt = findViewById(R.id.play_butt);
        m_flash_cl = findViewById(R.id.flash_v);


        // Any special setups here

        // Setup animation so that users will know that they are recording
        m_colorStart = getResources().getColor(R.color.recording_anim_color_start);
        m_colorEnd = getResources().getColor(R.color.recording_anim_color_end);

        m_colorOrig = getResources().getColor(R.color.colorPrimaryDark);
        Drawable background = m_flash_cl.getBackground();
        if (background instanceof ColorDrawable) {
            m_colorOrig = ((ColorDrawable) background).getColor();
        }

        m_colorAnimator = ValueAnimator.ofObject(new ArgbEvaluator(),
                m_colorStart, m_colorEnd);
        m_colorAnimator.setDuration(RECORDING_ANIM_DURATION);
        m_colorAnimator.setRepeatMode(ValueAnimator.REVERSE);
        m_colorAnimator.setRepeatCount(ValueAnimator.INFINITE);
        m_colorAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                m_flash_cl.setBackgroundColor((int)animation.getAnimatedValue());
            }
        });

        m_tempo_widget.init(this);
        m_accent_widget.init(this);

        record_butt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (m_keepRecording) {
                    // When true, then we're in the middle of a recording.
                    // This button will stop it.
                    m_keepRecording = false;
                    doRecordingUI(false);
                }
                else {
                    // Start recording.
                    m_keepRecording = true;
                    recordNewClick();
                }
            }
        });

        play_butt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // todo: play the sound we recorded
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Fill UI with data
        setUIData();
    }

    /**
     * Sets up all the data in the UI.
     *
     * preconditions:
     *      All UI variables are initialized.
     */
    @SuppressLint("SetTextI18n")    // Removes unnecessary warning on int-to-string conversion
    private void setUIData() {
        DefaultSettings settings = DefaultSettings.getInstance(this);

        m_tempo = settings.getTempo();
        m_bbp_tv.setText(Integer.toString(m_tempo));

        m_accent = settings.getAccent();
        m_accent_tv.setText(Integer.toString(m_accent));
    }


    /**
     * Does all the manipulations to modify the tempo
     *
     * @param changeAmount  Amount to increase/decrease the tempo
     */
    @SuppressLint("SetTextI18n")    // Removes unnecessary warning on int-to-string conversion
    @Override
    public void changeTempo(int changeAmount) {
        m_tempo += changeAmount;

        // Limit tempo to max and min
        if (m_tempo < MIN_TEMPO) {
            m_tempo = MIN_TEMPO;
        }
        if (m_tempo > MAX_TEMPO) {
            m_tempo = MAX_TEMPO;
        }

        m_bbp_tv.setText(Integer.toString(m_tempo));

        if ((m_tempo > MIN_TEMPO) && (m_tempo < MAX_TEMPO)) {
	        if (changeAmount > 0) {
		        m_tempo_widget.increaseSlider();
	        }
	        else {
		        m_tempo_widget.decreaseSlider();
	        }
        }
    }

    /**
     * Does all the manipulations to change the accents
     *
     * @param changeAmount  Amount to increase/decrease the beats per accent
     */
    @SuppressLint("SetTextI18n")    // Removes unnecessary warning on int-to-string conversion
    @Override
    public void changeAccent(int changeAmount) {
        m_accent += changeAmount;

        // limit
        if (m_accent < MIN_ACCENT) {
            m_accent = MIN_ACCENT;
        }
        if (m_accent > MAX_ACCENT) {
            m_accent = MAX_ACCENT;
        }

        m_accent_tv.setText(Integer.toString(m_accent));

        if ((m_accent > MIN_ACCENT) && (m_accent < MAX_ACCENT)) {
	        if (changeAmount > 0) {
		        m_accent_widget.increaseSlider();
	        }
	        else {
		        m_accent_widget.decreaseSlider();
	        }
        }

    }


    /**
     * User has indicated that they want to record a new click.
     * This makes it all happen.
     */
    private void recordNewClick() {

        // setup a buffer for recording
        final int SAMPLE_RATE = 44100;

        doRecordingUI(true);

        // Record in a new thread
        new Thread(new Runnable() {
            @Override
            public void run() {
                android.os.Process.setThreadPriority(Process.THREAD_PRIORITY_AUDIO);

                int bufferSize = AudioRecord.getMinBufferSize(SAMPLE_RATE,
                        AudioFormat.CHANNEL_IN_MONO,
                        AudioFormat.ENCODING_PCM_16BIT);

                // not surea about this...
                if ((bufferSize == AudioRecord.ERROR) ||
                    (bufferSize == AudioRecord.ERROR_BAD_VALUE)) {
                    bufferSize = SAMPLE_RATE * 2;
                }

                short[] audioBuffer = new short[bufferSize / 2];

                AudioRecord record = new AudioRecord(MediaRecorder.AudioSource.DEFAULT,
                        SAMPLE_RATE,
                        AudioFormat.CHANNEL_IN_MONO,
                        AudioFormat.ENCODING_PCM_16BIT,
                        bufferSize);

                if (record.getState() != AudioRecord.STATE_INITIALIZED) {
                    Log.e(TAG, "Audio Record can't init!");
                    m_keepRecording = false;
                    return;
                }

                // yay!
                record.startRecording();
                Log.v(TAG, "recording started");

                long shortsRead = 0;
                while (m_keepRecording) {
                    int numberOfShort = record.read(audioBuffer, 0, audioBuffer.length);
                    shortsRead += numberOfShort;

                    // todo: do something w audioBuffer
                }

                record.stop();
                record.release();
                Log.v(TAG, "recording stopped. Samples read: " + shortsRead);
            }
        }).start();

    }

    /**
     * starts or turns off a display that says we're recording
     *
     * NOTE:    Should run in the UI thread.
     *
     * @param show  When TRUE, start the recording display.
     *              FALSE means to turn it off.
     */
    private void doRecordingUI(boolean show) {
        if (show) {
            m_colorAnimator.start();
        }
        else {
            m_flash_cl.clearAnimation();
            m_colorAnimator.cancel();   // don't think this is the right thread...
            m_flash_cl.setBackgroundColor(m_colorOrig);
        }
    }

}
