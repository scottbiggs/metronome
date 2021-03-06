package sleepfuriously.com.metronome.widgets;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import androidx.appcompat.widget.AppCompatImageView;


import androidx.annotation.Nullable;
import sleepfuriously.com.metronome.R;

/**
 * Custom widget for sliding the accent beats.
 * Very similar to TempoWidget.
 *
 * The user can slide with their finger left or right along this
 * widget to change the number of beats per accent.
 */
public class AccentWidget extends AppCompatImageView {

    //---------------------------
    //  constants
    //---------------------------

    private static final String TAG = AccentWidget.class.getSimpleName();

    /** How many pixels (dip) to equal a beat */
    private static final float PIXELS_PER_BEAT = 36f;

    /** Minimum number of dips for this widget to register a slide */
    private static final float MOVEMENT_THRESHOLD = PIXELS_PER_BEAT;

	/** number of images in the animation */
	private static final int NUM_IMAGES = 3;


	//---------------------------
    //  data
    //---------------------------

    /** Y position of the pointer when last recorded */
    private int m_movementLastX;

    private Context m_ctx;

	/** index to the currently displayed image--just like it says! */
	private int m_currentDisplayedImageIndex;

	/** array of all the image resources */
	private Drawable[] m_displayedImages = new Drawable[NUM_IMAGES];


	//---------------------------
    //  methods
    //---------------------------

    public AccentWidget(Context context) {
        super(context);
        m_ctx = context;
    }

    public AccentWidget(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        m_ctx = context;
    }

    public AccentWidget(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        m_ctx = context;
    }

    /**
     * All the custom initializations happen here.
     */
    public void init(final AccentChanger changer) {

	    m_displayedImages[0] = m_ctx.getResources().getDrawable(R.drawable.blue_scrollwheel_horiz_1);
	    m_displayedImages[1] = m_ctx.getResources().getDrawable(R.drawable.blue_scrollwheel_horiz_2);
	    m_displayedImages[2] = m_ctx.getResources().getDrawable(R.drawable.blue_scrollwheel_horiz_3);

	    setOnTouchListener(new OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        m_movementLastX = (int) event.getX();
                        Log.d(TAG, "Down, y = " + m_movementLastX);
                        break;

                    case MotionEvent.ACTION_UP:
                        Log.d(TAG, "Up");
                        // todo: not currently used (might be nice if the widget changed color when dragging)
                        break;

                    case MotionEvent.ACTION_MOVE:
                        float x = event.getX();
                        float diff = m_movementLastX - x;
                        diff = -diff;   // going other direction according to screen coords

                        int diff_dp = pxToDp((int)diff, m_ctx);

                        if (Math.abs(diff_dp) > MOVEMENT_THRESHOLD) {
                            int beats = (int) (diff_dp / PIXELS_PER_BEAT);
                            Log.d(TAG, "move, beat change = " + beats);
                            changer.changeAccent(beats);
                            m_movementLastX = (int) x;
                        }
                        break;
                }

                return true;    // event consumed completely
            }
        });
    }


	public  void increaseSlider() {
		m_currentDisplayedImageIndex++;
		m_currentDisplayedImageIndex = m_currentDisplayedImageIndex % NUM_IMAGES;
		setImageDrawable(m_displayedImages[m_currentDisplayedImageIndex]);
	}

	public void decreaseSlider() {
		m_currentDisplayedImageIndex += (NUM_IMAGES - 1);
		m_currentDisplayedImageIndex = m_currentDisplayedImageIndex % NUM_IMAGES;
		setImageDrawable(m_displayedImages[m_currentDisplayedImageIndex]);
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
     * Creates callbacks that the AccentWidget calls when the
     * user changes the accents per beat.
     */
    public interface AccentChanger {

        /**
         * Returns the amount to change.
         * Can be positive or negative.
         */
        void changeAccent(int changeAmount);

    }

}
