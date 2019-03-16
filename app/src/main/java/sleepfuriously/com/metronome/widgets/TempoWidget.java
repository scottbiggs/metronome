package sleepfuriously.com.metronome.widgets;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

/**
 * Custom widget for sliding the tempo.
 *
 * The user can slide with their finger up or down along this
 * widget to changeTempo or decreaseTempo the tempo.
 */
public class TempoWidget extends View {

    //---------------------------
    //  constants
    //---------------------------

    private static final String TAG = TempoWidget.class.getSimpleName();

    /** How many pixels (dip) to equal a beat */
    private static final float PIXELS_PER_BEAT = 4f;

    /** Minimum number of dips for this widget to register a slide */
    private static final float MOVEMENT_THRESHOLD = PIXELS_PER_BEAT;


    //---------------------------
    //  data
    //---------------------------

    /** Y position of the pointer when last recorded */
    private int m_movementLastY;

    private Context m_ctx;

    //---------------------------
    //  methods
    //---------------------------

    public TempoWidget(Context context) {
        super(context);
        m_ctx = context;
    }

    public TempoWidget(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        m_ctx = context;
    }

    public TempoWidget(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        m_ctx = context;
    }

    /**
     * All the custom initializations happen here.
     */
    public void init(final TempoChanger changer) {

        setOnTouchListener(new OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        m_movementLastY = (int) event.getY();
                        Log.d(TAG, "Down, y = " + m_movementLastY);
                        break;

                    case MotionEvent.ACTION_UP:
                        Log.d(TAG, "Up");
                        // todo: not currently used (might be nice if the widget changed color when dragging)
                        break;

                    case MotionEvent.ACTION_MOVE:
                        float y = event.getY();
                        float diff = m_movementLastY - y;
                        int diff_dp = pxToDp((int)diff, m_ctx);

                        if (Math.abs(diff_dp) > MOVEMENT_THRESHOLD) {
                            int beats = (int) (diff_dp / PIXELS_PER_BEAT);
                            Log.d(TAG, "move, beat change = " + beats);
                            changer.changeTempo(beats);
                            m_movementLastY = (int) y;
                        }
                        break;
                }

                return true;    // event consumed completely
            }
        });
    }

    /**
     * Converts given pixels to density-independent pixels.
     */
    @SuppressWarnings("AccessStaticViaInstance")
    public int pxToDp(int px, Context ctx) {
        return (int) (px / ctx.getResources().getSystem().getDisplayMetrics().density);
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    //  interfaces
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Creates callbacks that the TempoWidget calls when the
     * tempo changes.
     */
    public interface TempoChanger {

        /**
         * Returns the amount to change the tempo.
         * Can be positive or negative.
         */
        void changeTempo(int changeAmount);

    }

}
