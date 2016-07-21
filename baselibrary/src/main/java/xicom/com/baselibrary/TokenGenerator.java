package xicom.com.baselibrary;

import android.util.Base64;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class TokenGenerator {
    static String sha, buf;

    public static String getToken(String secretKey, long timestamp) throws UnsupportedEncodingException {
        buf = secretKey + timestamp;
        // //System.out.println("buf => " + buf);
        try {
            MessageDigest md = MessageDigest.getInstance("SHA1");
            md.reset();
            md.update((buf).getBytes("iso-8859-1"), 0, buf.length());
            byte[] result = md.digest();

            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < result.length; i++) {
                sb.append(Integer.toString((result[i] & 0xff) + 0x100, 16)
                        .substring(1));
            }
            sha = sb.toString();
            // //System.out.println("sha => " + sha);
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // //System.out.println("s => " + s);
        return toBase64fromString(sha);
    }

    public static String toBase64fromString(String text) {
        byte bytes[] = text.getBytes();
        return Base64.encodeToString(bytes, Base64.NO_WRAP);
    }

    public static String getSha1(String secretKey, long timestamp) throws NoSuchAlgorithmException {
        String string = secretKey + timestamp;
        MessageDigest mDigest = MessageDigest.getInstance("SHA1");
        byte[] result = mDigest.digest(string.getBytes());
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < result.length; i++) {
            sb.append(Integer.toString((result[i] & 0xff) + 0x100, 16).substring(1));
        }

        return sb.toString();
    }

}