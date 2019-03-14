package sleepfuriously.com.metronome.model;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * This class provides external access to all settings that
 * we want to stick around from one session to another.
 */
public class DefaultSettings {

    //-----------------------
    //  constants
    //-----------------------

    /** Default tempo */
    private final static int DEFAULT_TEMPO = 92;

    /** Default accent subdivisions (which is none) */
    private final static int DEFAULT_ACCENT = 1;

    /** name of the prefs file */
    private final static String PREFS_FILE_NAME = "prefs.file";

    /** keys for the preferences */
    private static final String
        START_TEMPO = "start_tempo",
        START_ACCENT = "start_accent";

    //-----------------------
    //  data
    //-----------------------

//    private Context m_ctx;

    /** Used to access this class--lazy singleton pattern */
    private static DefaultSettings m_instance;

    /** Accesses the SharedPreferences */
    private SharedPreferences m_prefs;

    //-----------------------
    //  public methods
    //-----------------------

    public static DefaultSettings getInstance(Context ctx) {

        if (m_instance == null) {
            m_instance = new DefaultSettings(ctx);
        }
        return m_instance;
    }

    /**
     * Returns the starting tempo in bpm (beats per minute).
     */
    public int getTempo() {
        return m_prefs.getInt(START_TEMPO, DEFAULT_TEMPO);
    }

    /**
     * Immediately saves the tempo (no background processes!).
     *
     * @param tempo     Beats per minute
     */
    @SuppressLint("ApplySharedPref")
    public void setTempoNow(int tempo) {
        SharedPreferences.Editor editor = m_prefs.edit();
        editor.putInt(START_TEMPO, tempo);
        editor.commit();
    }

    /**
     * Saves the tempo in permanent storage.  Works in the
     * background, so use {@link #setTempoNow(int)} if you need
     * to do this in a hurry.
     *
     * @param tempo     beats per minute
     */
    public void setTempo(int tempo) {
        SharedPreferences.Editor editor = m_prefs.edit();
        editor.putInt(START_TEMPO, tempo);
        editor.apply();
    }


    /**
     * Returns the beats per accent that is currently stored.
     */
    public int getAccent() {
        return m_prefs.getInt(START_ACCENT, DEFAULT_ACCENT);
    }

    /**
     * Sets the accent count in permanent storage.
     * Acts right NOW (no background processes).
     *
     * @param accent    The beats per accent.
     */
    public void setAccentNow(int accent) {
        SharedPreferences.Editor editor = m_prefs.edit();
        editor.putInt(START_ACCENT, accent);
        editor.commit();
    }

    /**
     * Sets the accent count in permanent storage.
     *
     * @param accent    The beats per accent.
     */
    public void setAccent(int accent) {
        SharedPreferences.Editor editor = m_prefs.edit();
        editor.putInt(START_ACCENT, accent);
        editor.apply();
    }


    //-----------------------
    //  private methods
    //-----------------------

    private DefaultSettings(Context ctx) {
        m_prefs = ctx.getSharedPreferences(PREFS_FILE_NAME, Context.MODE_PRIVATE);
    }

}
