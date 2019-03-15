package sleepfuriously.com.metronome.widgets;

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
 * widget to increase or decrease the tempo.
 */
public class TempoWidget extends View {

    //---------------------------
    //  constants
    //---------------------------

    private static final String TAG = TempoWidget.class.getSimpleName();

    //---------------------------
    //  data
    //---------------------------

    //---------------------------
    //  methods
    //---------------------------

    public TempoWidget(Context context) {
        super(context);
        init();
    }

    public TempoWidget(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TempoWidget(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    /**
     * All the custom initializations happen here.
     */
    private void init() {
        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        Log.d(TAG, "Down");
                        break;

                    case MotionEvent.ACTION_UP:
                        Log.d(TAG, "Up");
                        break;

                    case MotionEvent.ACTION_MOVE:
                        Log.d(TAG, "move");
                        break;
                }

                return true;    // event consumed completely
            }
        });
    }



}
