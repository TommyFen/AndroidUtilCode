package com.blankj.utilcode.tommy;

import android.os.SystemClock;
import android.view.View;

import java.util.concurrent.TimeUnit;

/**
 * @author : Tommy
 *
 * View's click event
 */
public final class ViewClickUtil {

    private long mSkipDuration;
    private TimeUnit mTimeUnit;


    private ViewClickUtil(Builder builder) {
        this.mSkipDuration = builder.mSkipDuration;
        this.mTimeUnit = builder.mTimeUnit;
    }

    public void viewClicks(View.OnClickListener onClickListener, View view) {
        ViewClickImpl mViewClick = new ViewClickImpl(view, onClickListener);
        mViewClick.throttle(mSkipDuration, mTimeUnit);
    }

    public void viewClicks(View.OnClickListener onClickListener, View... views) {
        for (View view : views) {
            viewClicks(onClickListener, view);
        }
    }

    public static class Builder {

        private long mSkipDuration;
        private TimeUnit mTimeUnit;

        public Builder setSkipDuration(long mSkipDuration) {
            this.mSkipDuration = mSkipDuration;
            return this;
        }

        public Builder setmTimeUnit(TimeUnit mTimeUnit) {
            this.mTimeUnit = mTimeUnit;
            return this;
        }

        public ViewClickUtil build() {
            return new ViewClickUtil(this);
        }
    }


    public class ViewClickImpl implements ViewClick {

        private View mView;
        private View.OnClickListener mOnClickListener;
        private long mOldTime;
        private long mDelayMillisecond = 1000L;

        public ViewClickImpl(View view, View.OnClickListener mOnClickListener) {
            this.mView = view;
            this.mOnClickListener = mOnClickListener;
            mView.setOnClickListener(this);
        }


        @Override
        public void throttle(long skipDuration, TimeUnit timeUnit) {
            if (skipDuration < 0) {
                skipDuration = 0;
            }
            mDelayMillisecond = timeUnit.toMillis(skipDuration);
        }

        @Override
        public void onClick(View v) {
            long nowTime = SystemClock.elapsedRealtime();
            long intervalTime = nowTime - mOldTime;
            if (mOldTime == 0 || intervalTime >= mDelayMillisecond) {
                mOldTime = nowTime;
                mOnClickListener.onClick(v);
            }
        }
    }

    public interface ViewClick extends View.OnClickListener {
        void throttle(long skipDuration, TimeUnit timeUnit);
    }

}
