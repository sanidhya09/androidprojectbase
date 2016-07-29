package xicom.com.baselibrary.retrofit2;

import okhttp3.Headers;

/**
 * Created by sanidhya on 29/7/16.
 */
public class RetrofitConfigModel {

    public String baseUrl;
    public int readOutTime;
    public int connectOutTime;
    public Headers.Builder headers;
    public boolean loggingEnabled;

    public RetrofitConfigModel(String baseUrl, int readOutTime, int connectOutTime, Headers.Builder headers, boolean loggingEnabled) {
        this.baseUrl = baseUrl;
        this.readOutTime = readOutTime;
        this.connectOutTime = connectOutTime;
        this.headers = headers;
        this.loggingEnabled = loggingEnabled;
    }

    public static class Builder {
        private String baseUrl;
        private int readOutTime;
        private int connectOutTime;
        private Headers.Builder headers;
        public boolean loggingEnabled;

        public Builder setBaseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
            return this;
        }

        public Builder setReadOutTime(int readOutTime) {
            this.readOutTime = readOutTime;
            return this;
        }

        public Builder setConnectOutTime(int connectOutTime) {
            this.connectOutTime = connectOutTime;
            return this;
        }

        public Builder setHeaders(Headers.Builder headers) {
            this.headers = headers;
            return this;
        }

        public Builder setLoggingEnabled(boolean loggingEnabled) {
            this.loggingEnabled = loggingEnabled;
            return this;
        }

        public RetrofitConfigModel build() {
            return new RetrofitConfigModel(baseUrl, readOutTime, connectOutTime, headers, loggingEnabled);
        }
    }
}
