package sleepfuriously.com.metronome;

import androidx.appcompat.app.AppCompatActivity;
import sleepfuriously.com.metronome.model.DefaultSettings;
import sleepfuriously.com.metronome.widgets.AccentWidget;
import sleepfuriously.com.metronome.widgets.TempoWidget;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
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

    //----------------------
    //  widgets
    //----------------------

    /** Displays current beats per minutes */
    TextView m_bbp_tv;

    /** Displays how many beats per grouping to accent */
    TextView m_accent_tv;

    /** Slider for changing tempo */
    TempoWidget m_tempo_widget;

    /** todo: slider or changing the accent */
    AccentWidget m_accent_widget;

    //----------------------
    //  data
    //----------------------

    /** the current tempo */
    int m_tempo;

    /** current accent */
    int m_accent;


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

        // Any special setups here
        m_tempo_widget.init(this);
        m_accent_widget.init(this);
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
    }


}
