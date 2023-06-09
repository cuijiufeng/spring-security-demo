package io.inferiority.demo.springsecurity;

import io.inferiority.demo.springsecurity.utils.LicenseUtil;
import org.apache.commons.io.IOUtils;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.asn1.DERSequence;
import org.junit.jupiter.api.Test;
import org.springframework.util.ResourceUtils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.text.ParseException;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;

/**
 * @author cuijiufeng
 * @Class LicenseTest
 * @Date 2023/6/9 10:21
 */
public class LicenseTest {
    private static final String PRIV_STR = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQDQmeO+RbWb9WOdOgLKlFUknc" +
            "HX2XZTbyyRYv37vdcCZ5yqxlob8NrORA9gX0LIAnZT8FdlvMVZKMniwnYQ/TEf+u8aWaphqiMmLVAEAYVwGBFGt3n/VCgZRETc5P5hrdM" +
            "0gUDeiOT5rX9VYaytqnLMhwwv5u3sAASDNGq1SeuSRqDJleE3rmJw64DLJcB77kkPpCeVR3OSDReu6lCregzomKoTka9aVwp0hbysD5EN" +
            "YKKDHKSMYvb3RdOmEfOtH5RU70A9vk0JmbLLpGf9cPJxKJo3Y+QOk9Ewezj7fnGOZF2Mhd2afwhkQcDVukvMWxcdM3La5b7csKO0c11RI" +
            "WJ5AgMBAAECggEBAMFQO8wS0fCMAhqsrfWKbTUXtZIRByTe/FBnirQHBABIgNmKI+uM01kfnZhT75FnJr03F+dCfyjXMfChAcVQvnvCkl" +
            "67BbiocRZqG6L6vexZgXklgztphBeWOrLyzAM9kWMw/Sq9dmUjscMwV1RBh02fQV4LBiYVBhmJM/J5ZIQSn4u9/toG9nEzkwE9G4XD6+2" +
            "hv3b3E3VezHZJ2p/o02JPsUeYK8YDFT6lha0zPJqbfxL0nTcok3ovkgf0NzaX2jszZmYWRDtwEUlhBoVFxazAquqLFm9a7zuP+XExLQn4" +
            "/E/eU96aWZeSCgL1zlJxbWzk6t9MasXipuN/rqLQnWECgYEA6cZMLEolmNBgASdl8EhZ7mZZWH9RMQaRh/LsOr2+L54IkkfHcZ3IvcxAC" +
            "mCKjLGqXVgfV4sGjFb/ZyTFfKRIcr3dlUFIf8JAG501ye0QVeQrvApcsseRBvZRXRqs3Yngj1DR45ctTEtya6f14IqSCj/NXfFM3jgPMd" +
            "dD+zWj2S0CgYEA5G7o6sWABKnEejuCYzqe/o4FmcujlUkRhMU/X61UU9/IB3HIdOUEaOQmFPNccXq+PxrOBgP5qZRiU7BFT63OeEx66Yp" +
            "aw83tsHdEiu9A/ohkUPkqW/L/hmVLuALdm2xGEeyVN0V2M/dglBbUVpXGyqCba6qz0fTWiPH9CZi5Zf0CgYAMuUVGF9cKBTVkeq96r4Vt" +
            "i1aLkYpqv6/GFXu0CsXCM7jFtXBuh6b4ZZiKuAxyce0S3Yv/KMr3cni+NWsmSI7E5/6adA4tHfZ9SkY2Mlf0RNH4jxvSgzgqh9eP2LbV6" +
            "/4z7rMtzvmhMG4O2/pu+TvPoP9dgJetdKGnFc9Waen8FQKBgFmImyRByt6JB+h84Ksx89WEJu/T4EcH/6TRhvhiQHYrdQRt9YrTpLnUCp" +
            "UVmbskx+KGCBBViIqODUgOA+yqDA4cs+RLhS3PzgTnimcGRaw+G0udjZvCj6LGmzoBbYiYlJI773RddmoB4MnKdOmHBQawkppNqvN1Co1" +
            "cg7g6MxilAoGAXgGG0jPJ5iBsjxtO8jri/5MtHrd1Ldr2NOaBmHNl4Xw05YHuvks5tD1VWlyRCxh1ZV66TwE+ty+eXAPlk7W/gkpVeRS3" +
            "YE2WBsZRhZ9JCox19g5IZsXWNDsQmO0lKk30XiaNZhuSCb6na+pCHUrm0Rs3eg8AtbZ583YIrYjk4lU=";

    /*
    * 生成密钥对
    * openssl genrsa -out rsa.pem 2048
    * pkcs1转pkcs8且pem转der
    * openssl pkcs8 -topk8 -in rsa.pem -out rsa.der -inform PEM -outform DER -nocrypt
    * 生成证书
    * openssl req -new -key rsa.pem -out cert.csr
    * openssl x509 -req -in cert.csr -out cert.crt -signkey rsa.pem -days 36500
    * */
    @Test
    public void testGenerateLicense() throws IOException, GeneralSecurityException {
        String name = "test";
        String ip = "192.168.1.45";
        String mac = "D8:F3:BC:99:90:19";
        Calendar calendar = Calendar.getInstance();
        calendar.set(2023, 6, 30);
        Date validityPeriod = calendar.getTime();
        String outputPath = "E:\\Desktop\\" + name + ".license";

        LicenseUtil.ToBeSigned toBeSigned = new LicenseUtil.ToBeSigned(name, ip, mac, validityPeriod);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey privateKey = keyFactory.generatePrivate(new PKCS8EncodedKeySpec(Base64.getDecoder().decode(PRIV_STR)));
        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initSign(privateKey);
        signature.update(toBeSigned.getEncoded());
        ASN1EncodableVector vector = new ASN1EncodableVector();
        vector.add(toBeSigned);
        vector.add(new DEROctetString(signature.sign()));
        try (FileOutputStream fos = new FileOutputStream(outputPath)) {
            fos.write(new DERSequence(vector).getEncoded());
        }
    }

    @Test
    public void testVerifyLicense() throws GeneralSecurityException, IOException, ParseException {
        URL url = ResourceUtils.getURL("file:E:\\Desktop\\test.license");
        LicenseUtil.parseLicense(IOUtils.toByteArray(url));
    }

    @Test
    public void testFileToBase64() throws IOException {
        URL url = ResourceUtils.getURL("file:E:\\Desktop\\cert.crt");
        System.out.println(Base64.getEncoder().encodeToString(IOUtils.toByteArray(url)));
    }
}
