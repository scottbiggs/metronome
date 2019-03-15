package sleepfuriously.com.metronome;

import androidx.appcompat.app.AppCompatActivity;
import sleepfuriously.com.metronome.model.DefaultSettings;
import sleepfuriously.com.metronome.widgets.TempoWidget;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;


/**
 * My attempt at making a Metronome program.  There's a long
 * list of wished-for features on the github readme.
 */
public class MainActivity extends AppCompatActivity {

    //----------------------
    //  constants
    //----------------------

    private static final String TAG = MainActivity.class.getSimpleName();

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


    //----------------------
    //  data
    //----------------------

    /** data for handling the drag of the tempo widget */
    int m_tempo_dragY_start, m_tempo_dragY_current;

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

        setUIData();
    }

    /**
     * Sets up all the data in the UI.
     *
     * preconditions:
     *      All UI variables are initialized.
     */
    @SuppressLint("SetTextI18n")
    private void setUIData() {
        DefaultSettings settings = DefaultSettings.getInstance(this);

        m_tempo = settings.getTempo();
        m_bbp_tv.setText(Integer.toString(m_tempo));

        m_accent = settings.getAccent();
        m_accent_tv.setText(Integer.toString(m_accent));
    }
}
