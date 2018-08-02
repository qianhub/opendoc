
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;

public class TestClient {

    public final static String PrivateKeyBase64 = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAOAikmMxqPRhsZkuRtqCD6oZ9jfO\n" +
            "5UqOPya5J8Ivse6AwA20oOP/OITGHw1KugDZsjqLQNHWfqyWuKjpsFaqcoUNnCOV0OhsM6s1dY3F\n" +
            "tvdwidwOR70I9sN3JapqKZyEoyK1WBULwNbLfwQMTa7nFB9rPxNx52SWWTtQmRZtyIcxAgMBAAEC\n" +
            "gYEAimOuQUL7UBE2CB+zrd0acOvgw+qiVptn0LAIJXUvhtTGQHAj20LNkeWGbL2UBUxlKJKsriOj\n" +
            "SUsyr1DDCW/qjqa0D2v0pr2/ZAVFVq9gYdYKBhabNDbwMettt2R3jXAHKs2oy4I2cd9wYW6Ue30V\n" +
            "PBqXy/Q80RXA3wcLSfs+E3ECQQD3z8R0h+lMhMTtaUdV9VA6a9WHXQpLx6kBoPkobd+OJ6ASbwyY\n" +
            "Arq4a9IV/ADyEhKNYeAQwjFKfYQuiXuNAU+NAkEA54qGas3uEkoNDVYPlOesf2XCpx934hIqw8Qn\n" +
            "emMZSZU8F1as/ZIw3S2lmoxMzfAEyFRWSEVpow/5KA5ydHgLNQJAO1+GPD1MAk9VN2Sf+NJbtIOd\n" +
            "l18NVnax4XgF+k/I3jBUQ9ZjeBA/WGxM24OTXmxCEam/m4RLdwN3pga+mVwVCQJBAJyUCghqEGgf\n" +
            "2am+HDVnYjiY+USJPEoOXQscOFJEd9JR8FwcCkpENXUtLENSQ5I0kRdkKSEgh6p039pdwrrOf00C\n" +
            "QGVoickJeJzMyJ+QJQyWH/PO0OpP57Ye4TUiloESf+pYSbZAHUgJ7pRFk/++5HkD9oDTGjkUsaK9\n" +
            "gXCfk2tfIok=";

    public final static String AppID = "507435914698";
    public final static String URL = "https://open.sanyitest.com/gateway";

    public static PrivateKey loadKey() throws NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] bytes = Base64.decodeBase64(PrivateKeyBase64);
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(bytes);
        return KeyFactory.getInstance("RSA").generatePrivate(spec);
    }

    public static String sign(PrivateKey key, byte[] bytes) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        Signature signature = Signature.getInstance("SHA1WithRSA");
        signature.initSign(key);
        signature.update(bytes);
        byte[] sign = signature.sign();
        return Hex.encodeHexString(sign);
    }

    public static void call() throws IOException, InvalidKeySpecException, NoSuchAlgorithmException, SignatureException, InvalidKeyException {
        final String body = "{\"version\":\"1.0\",\"action\":\"qianhub.test\",\"body\":{\"name\":\"Jack\"}}";
        PrivateKey key = loadKey();
        byte[] bytes = body.getBytes("utf-8");
        final String sign = sign(key, bytes);
        HttpPost request = new HttpPost(URL);
        request.setHeader("X-QianHub-App", AppID);
        request.setHeader("X-QianHub-Sign", sign);
        StringEntity s = new StringEntity(body);
        s.setContentEncoding("UTF-8");
        s.setContentType("application/json");
        request.setEntity(s);
        CloseableHttpClient closeableHttpClient = HttpClients.createDefault();
        CloseableHttpResponse response = closeableHttpClient.execute(request);
        String result = EntityUtils.toString(response.getEntity());
        System.out.println("Out: " + result);
        closeableHttpClient.close();
    }

    public static void main(String[] args) throws Exception {
        call();
    }
}
