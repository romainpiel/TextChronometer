package com.romainpiel.textchronometer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * MPme
 * User: romainpiel
 * Date: 22/07/2013
 * Time: 08:00
 */
public class TextChronometer extends TextView {

    private boolean isAttached;
    private long time;
    private DateRenderer dateRenderer;
    private boolean hasSeconds;
    private Handler handler;

    private final BroadcastReceiver mIntentReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            onTimeChanged();
        }
    };

    private Runnable ticker;

    public TextChronometer(Context context) {
        this(context, null, 0);
    }

    public TextChronometer(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TextChronometer(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    public void setTime(long timeInMillis) {
        this.time = timeInMillis;
        onTimeChanged();
    }

    private void init(Context context) {
        dateRenderer = new DateRenderer(context);
        hasSeconds = true;
        handler = new Handler();
    }

    private void setupTicker() {
        if (hasSeconds && ticker == null) {
            ticker = new Runnable() {
                public void run() {
                    onTimeChanged();

                    long now = SystemClock.uptimeMillis();
                    long next = now + (1000 - now % 1000);

                    handler.postAtTime(ticker, next);
                }
            };
            ticker.run();
        } else if (!hasSeconds && ticker != null) {
            handler.removeCallbacks(ticker);
            ticker = null;
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        if (!isAttached) {
            isAttached = true;

            registerReceiver();

            onTimeChanged();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();

        if (isAttached) {
            unregisterReceiver();

            getHandler().removeCallbacks(ticker);

            isAttached = false;
        }
    }

    private void registerReceiver() {
        final IntentFilter filter = new IntentFilter();

        filter.addAction(Intent.ACTION_TIME_TICK);
        filter.addAction(Intent.ACTION_TIME_CHANGED);
        filter.addAction(Intent.ACTION_TIMEZONE_CHANGED);

        getContext().registerReceiver(mIntentReceiver, filter, null, getHandler());
    }

    private void unregisterReceiver() {
        getContext().unregisterReceiver(mIntentReceiver);
    }

    private void onTimeChanged() {
        DateRenderer.TimeAgo timeAgo = dateRenderer.timeAgo(time);
        hasSeconds = timeAgo.unit.type.equals(DateRenderer.SECONDS);
        setupTicker();
        setText(timeAgo.formattedDate);
    }
}
