package com.base.location;

import android.support.annotation.FloatRange;
import android.support.annotation.IntRange;

public class TrackerSettings {
    public static final TrackerSettings DEFAULT = new TrackerSettings();
    public static final long DEFAULT_MIN_TIME_BETWEEN_UPDATES = 300000L;
    public static final float DEFAULT_MIN_METERS_BETWEEN_UPDATES = 100.0F;
    public static final int DEFAULT_TIMEOUT = 60000;
    private long mTimeBetweenUpdates = -1L;
    private float mMetersBetweenUpdates = -1.0F;
    private int mTimeout = -1;
    private boolean mUseGPS = true;
    private boolean mUseNetwork = true;
    private boolean mUsePassive = true;

    public TrackerSettings() {
    }

    public TrackerSettings setTimeBetweenUpdates(@FloatRange(
    from = 1.0D
) long timeBetweenUpdates) {
        if(timeBetweenUpdates > 0L) {
            this.mTimeBetweenUpdates = timeBetweenUpdates;
        }

        return this;
    }

    public long getTimeBetweenUpdates() {
        return this.mTimeBetweenUpdates <= 0L?300000L:this.mTimeBetweenUpdates;
    }

    public TrackerSettings setMetersBetweenUpdates(@FloatRange(
    from = 1.0D
) float metersBetweenUpdates) {
        if(metersBetweenUpdates > 0.0F) {
            this.mMetersBetweenUpdates = metersBetweenUpdates;
        }

        return this;
    }

    public float getMetersBetweenUpdates() {
        return this.mMetersBetweenUpdates <= 0.0F?100.0F:this.mMetersBetweenUpdates;
    }

    public TrackerSettings setTimeout(@IntRange(
    from = 1L
) int timeout) {
        if(timeout > 0) {
            this.mTimeout = timeout;
        }

        return this;
    }

    public int getTimeout() {
        return this.mTimeout <= -1?'\uea60':this.mTimeout;
    }

    public TrackerSettings setUseGPS(boolean useGPS) {
        this.mUseGPS = useGPS;
        return this;
    }

    public boolean shouldUseGPS() {
        return this.mUseGPS;
    }

    public TrackerSettings setUseNetwork(boolean useNetwork) {
        this.mUseNetwork = useNetwork;
        return this;
    }

    public boolean shouldUseNetwork() {
        return this.mUseNetwork;
    }

    public TrackerSettings setUsePassive(boolean usePassive) {
        this.mUsePassive = usePassive;
        return this;
    }

    public boolean shouldUsePassive() {
        return this.mUsePassive;
    }
}
