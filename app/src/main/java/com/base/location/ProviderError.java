package com.base.location;

public class ProviderError extends Throwable {
    String provider;

    public ProviderError(String provider, String detailMessage) {
        super(detailMessage);
        this.provider = provider;
    }

    public String getProvider() {
        return this.provider;
    }

    public String toString() {
        return super.toString() + " | ProviderError{" + "provider=\'" + this.provider + '\'' + '}';
    }
}