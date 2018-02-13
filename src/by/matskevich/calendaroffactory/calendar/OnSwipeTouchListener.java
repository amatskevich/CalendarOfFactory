package by.matskevich.calendaroffactory.calendar;

import android.content.Context;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public class OnSwipeTouchListener implements OnTouchListener {

	private final GestureDetector gestureDetector;

	OnSwipeTouchListener(Context ctx) {
		gestureDetector = new GestureDetector(ctx, new GestureListener());
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		v.performClick();
		return gestureDetector.onTouchEvent(event);
	}

	private final class GestureListener extends SimpleOnGestureListener {

		private static final int SWIPE_THRESHOLD = 100;
		private static final int SWIPE_VELOCITY_THRESHOLD = 100;

		@Override
		public boolean onDown(MotionEvent e) {
			return true;
		}

		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
			boolean result = false;
			try {
				float diffY = e2.getY() - e1.getY();
				float diffX = e2.getX() - e1.getX();
				if (Math.abs(diffX) > Math.abs(diffY)) {
					if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
						if (diffX > 0) {
							result = onSwipeRight();
						} else {
							result = onSwipeLeft();
						}
					}
				} else if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
					if (diffY > 0) {
						result = onSwipeBottom();
					} else {
						result = onSwipeTop();
					}
				}

			} catch (Exception exception) {
				exception.printStackTrace();
			}
			return result;
		}

		@Override
		public boolean onSingleTapUp(MotionEvent e) {
			onTouchPopupShow();
			return true;
		}

	}

	public boolean onSwipeRight() {
		return false;
	}

	public boolean onSwipeLeft() {
		return false;
	}

	private boolean onSwipeTop() {
		return false;
	}

	private boolean onSwipeBottom() {
		return false;
	}

	public void onTouchPopupShow() {
	}
}