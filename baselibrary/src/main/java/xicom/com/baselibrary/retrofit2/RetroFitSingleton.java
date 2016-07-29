package xicom.com.baselibrary.retrofit2;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by sanidhya on 20/7/16.
 */
public enum RetroFitSingleton {
    INSTANCE;

    private Retrofit retrofit;
    private RetrofitConfigModel retrofitConfigModel;
    public static final String TAG = RetroFitSingleton.class.getName();

    /**
     * Description : Mandatory Configuration Settings for Retrofit to work
     *
     * @param retrofitConfigModel : set important configuration here like base url, timeouts, etc
     *                            <p>
     *                            Sample implementation :
     *                            <p>
     *                            Headers.Builder builder = new Headers.Builder();
     *                            builder.add("OS", "ANDROID");
     *                            <p>
     *                            RetrofitConfigModel retrofitConfigModel = new RetrofitConfigModel.Builder()
     *                            .setBaseUrl("https://pubs.usgs.gov/")
     *                            .setConnectOutTime(60)
     *                            .setReadOutTime(45)
     *                            .setLoggingEnabled(true)
     *                            .setHeaders(builder)
     *                            .build();
     */
    public void setRetrofitConfig(RetrofitConfigModel retrofitConfigModel) {
        this.retrofitConfigModel = retrofitConfigModel;
    }

    public Retrofit getRetrofit() {
        return (retrofit == null) ? setRetrofit() : retrofit;
    }

    public Retrofit setRetrofit() {

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.NONE);
        if (retrofitConfigModel.loggingEnabled)
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        Interceptor interceptorHeader = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {

                Request original = chain.request();
                Request.Builder requestBuilder = original.newBuilder()
                        .headers(retrofitConfigModel.headers.build())
                        .method(original.method(), original.body());

                Request request = requestBuilder.build();
                return chain.proceed(request);
            }
        };

        OkHttpClient client = new OkHttpClient.Builder()
                .readTimeout(retrofitConfigModel.readOutTime, TimeUnit.SECONDS)
                .connectTimeout(retrofitConfigModel.connectOutTime, TimeUnit.SECONDS)
                .addInterceptor(interceptor)
                .addInterceptor(interceptorHeader)
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl(retrofitConfigModel.baseUrl)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit;
    }

    /**
     * Description : Downloads the large file to External Storage
     *
     * @param url           : Full url of file to download
     * @param fileName      : Full url of file to download
     * @param fileExtension : File Extension to be saved. Eg : PDF, JPG, etc
     */
    public void downloadLargeFile(final String url, final String fileName, final String fileExtension, final Context context) {
        final FileDownloadService downloadService =
                getRetrofit().create(FileDownloadService.class);

        new AsyncTask<Void, Long, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                Call<ResponseBody> call = downloadService.downloadFileWithDynamicUrlAsync(url);

                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            Log.d(TAG, "server contacted and has file");
                            boolean writtenToDisk = writeResponseBodyToDisk(response.body(), fileName, fileExtension);
                            Log.d(TAG, "file download was a success? " + writtenToDisk);
                        } else {
                            Log.d(TAG, "server contact failed");
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.d(TAG, "Failed :: " + t.getLocalizedMessage());
                    }

                });
                return null;
            }
        }.execute();

    }

    private boolean writeResponseBodyToDisk(ResponseBody body, String fileName, String fileExtension) {
        try {
            // todo change the file location/name according to your needs
            File file = new File(Environment.getExternalStorageDirectory() + File.separator + fileName + "." + fileExtension);

            InputStream inputStream = null;
            OutputStream outputStream = null;

            try {
                byte[] fileReader = new byte[4096];

                long fileSize = body.contentLength();
                long fileSizeDownloaded = 0;

                inputStream = body.byteStream();
                outputStream = new FileOutputStream(file);

                while (true) {
                    int read = inputStream.read(fileReader);

                    if (read == -1) {
                        break;
                    }

                    outputStream.write(fileReader, 0, read);
                    fileSizeDownloaded += read;
                    Log.d(TAG, "file download: " + fileSizeDownloaded + " of " + fileSize);
                }

                outputStream.flush();

                return true;
            } catch (IOException e) {
                return false;
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }

                if (outputStream != null) {
                    outputStream.close();
                }
            }
        } catch (IOException e) {
            return false;
        }
    }

    public Map<String, RequestBody> uploadImagesRequestGenerator(ArrayList<File> filesArray, String fileExtension, String keyParam) {

        Map<String, RequestBody> files = new HashMap<>();
        for (int i = 0; i < filesArray.size(); i++) {
            String key = keyParam + "[" + String.valueOf(i) + "]";
            files.put("" + key + "\"; filename=\"" + key + "." + fileExtension, getRequestFile(filesArray.get(i)));
        }
        return files;
    }

    public RequestBody getRequestFile(File file) {
        if (file != null) {
            return RequestBody.create(MediaType.parse("image/jpg"), file);
        } else {
            return null;
        }
    }

    public RequestBody getRequestString(String text) {
        if (text == null) {
            text = "";
        }
        return RequestBody.create(
                MediaType.parse("text/plain"), text);
    }

}
