package sleepfuriously.com.metronome;

import androidx.appcompat.app.AppCompatActivity;
import sleepfuriously.com.metronome.model.DefaultSettings;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.TextView;


/**
 * My attempt at making a Metronome program.  There's a long
 * list of wished-for features on the github readme.
 */
public class MainActivity extends AppCompatActivity {

    //----------------------
    //  constants
    //----------------------

    //----------------------
    //  widgets
    //----------------------

    /** Displays current beats per minutes */
    TextView m_bbp_tv;

    /** Displays how many beats per grouping to accent */
    TextView m_accent_tv;


    //----------------------
    //  data
    //----------------------

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
        m_bbp_tv.setText(Integer.toString(settings.getTempo()));
        m_accent_tv.setText(Integer.toString(settings.getAccent()));
    }
}
