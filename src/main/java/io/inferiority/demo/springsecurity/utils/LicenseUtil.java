package io.inferiority.demo.springsecurity.utils;

import io.inferiority.demo.springsecurity.exception.ErrorEnum;
import io.inferiority.demo.springsecurity.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1UTCTime;
import org.bouncycastle.asn1.ASN1UTF8String;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DERUTCTime;
import org.bouncycastle.asn1.DERUTF8String;
import oshi.SystemInfo;
import oshi.hardware.NetworkIF;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.Signature;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author cuijiufeng
 * @Class LicenseDo
 * @Date 2023/6/9 9:11
 */
@Slf4j
public class LicenseUtil {
    private static final String CERT_STR = "LS0tLS1CRUdJTiBDRVJUSUZJQ0FURS0tLS0tDQpNSUlERXpDQ0Fmc0NGRE1YK0taVEpPekNJVkl6" +
            "TkVRV1VtWU1HVlBrTUEwR0NTcUdTSWIzRFFFQkN3VUFNRVV4DQpDekFKQmdOVkJBWVRBa0ZWTVJNd0VRWURWUVFJREFwVGIyMWxMVk4wWVh" +
            "SbE1TRXdId1lEVlFRS0RCaEpiblJsDQpjbTVsZENCWGFXUm5hWFJ6SUZCMGVTQk1kR1F3SUJjTk1qTXdOakE1TURNME9UQXpXaGdQTWpFeU" +
            "16QTFNVFl3DQpNelE1TUROYU1FVXhDekFKQmdOVkJBWVRBa0ZWTVJNd0VRWURWUVFJREFwVGIyMWxMVk4wWVhSbE1TRXdId1lEDQpWUVFLR" +
            "EJoSmJuUmxjbTVsZENCWGFXUm5hWFJ6SUZCMGVTQk1kR1F3Z2dFaU1BMEdDU3FHU0liM0RRRUJBUVVBDQpBNElCRHdBd2dnRUtBb0lCQVFE" +
            "UW1lTytSYldiOVdPZE9nTEtsRlVrbmNIWDJYWlRieXlSWXYzN3ZkY0NaNXlxDQp4bG9iOE5yT1JBOWdYMExJQW5aVDhGZGx2TVZaS01uaXd" +
            "uWVEvVEVmK3U4YVdhcGhxaU1tTFZBRUFZVndHQkZHDQp0M24vVkNnWlJFVGM1UDVocmRNMGdVRGVpT1Q1clg5VllheXRxbkxNaHd3djV1M3" +
            "NBQVNETkdxMVNldVNScURKDQpsZUUzcm1KdzY0RExKY0I3N2trUHBDZVZSM09TRFJldTZsQ3JlZ3pvbUtvVGthOWFWd3AwaGJ5c0Q1RU5ZS" +
            "0tEDQpIS1NNWXZiM1JkT21FZk90SDVSVTcwQTl2azBKbWJMTHBHZjljUEp4S0pvM1krUU9rOUV3ZXpqN2ZuR09aRjJNDQpoZDJhZndoa1Fj" +
            "RFZ1a3ZNV3hjZE0zTGE1Yjdjc0tPMGMxMVJJV0o1QWdNQkFBRXdEUVlKS29aSWh2Y05BUUVMDQpCUUFEZ2dFQkFDNTg5cFZFTmpONDFJNmZ" +
            "sNFk1RFNaT2xwRlc4dWZLN2NKMEU2ZzFOS1hXZlVlWmdJYWFoZGFCDQpHV1l5ZzkrSzhUWENZZG1xdTNjNmhwdXBLcm1qVFZURjYreTBFMl" +
            "U1TUw4bGlnekh1bWlnREJWcVYwYWlJWHZFDQpnbmFxS3JldFJjMEwvdU56T3F0M0h4ZFpkaWZ1WkE0WWFBUFJyaEwvMmVGVmthNUlqem51Y" +
            "TVla1V5Y0k3MkhqDQpvSmx0MDVrZ2wwUk9vS0lhby9ycW1nR2Nzb1R3Sm9iV2IwRldsbFdBMWUzbHoraThIWU5keUk0MWkxajRvWFQrDQpZ" +
            "bGVMWW9janpubGNXTHdOR3lJWU90bXZ4UWFuSUZGcVJ0WFZDeG5vSlpiMUNaaVlweStmZnNwVVdIMDBjemQxDQoyVkh4a29VTEI5VzBhbDM" +
            "ya1FUY3NCcmNzSm1pSHVzPQ0KLS0tLS1FTkQgQ0VSVElGSUNBVEUtLS0tLQ0K";
    private static final List<NetworkIF> NETWORK_IFS;

    static {
        NETWORK_IFS = new SystemInfo().getHardware().getNetworkIFs()
                .stream()
                .filter(e -> ArrayUtils.isNotEmpty(e.getIPv4addr()))
                .collect(Collectors.toList());
        NETWORK_IFS.forEach(e -> log.info("ip-mac: {}-{}", e.getIPv4addr(), e.getMacaddr()));
    }

    public static ToBeSigned parseLicense(byte[] bytes) throws GeneralSecurityException, IOException, ParseException {
        ASN1Sequence sequence = ASN1Sequence.getInstance(new ASN1InputStream(bytes).readObject());
        Enumeration enumeration = sequence.getObjects();
        ToBeSigned toBeSigned = ToBeSigned.getInstance(enumeration.nextElement());
        ASN1OctetString signatureValue = ((ASN1OctetString) enumeration.nextElement());
        ByteArrayInputStream certBytes = new ByteArrayInputStream(Base64.getDecoder().decode(CERT_STR));
        Certificate certificate = CertificateFactory.getInstance("X.509").generateCertificate(certBytes);
        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initVerify(certificate);
        signature.update(toBeSigned.getEncoded());
        if (!signature.verify(signatureValue.getOctets())) {
            throw new ServiceException(ErrorEnum.ILLEGAL_LICENSE);
        }
        if (toBeSigned.getValidityPeriod().compareTo(new Date()) < 0) {
            log.warn("license expires: {}", new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(toBeSigned.getValidityPeriod()));
            throw new ServiceException(ErrorEnum.ILLEGAL_LICENSE);
        }
        if (NETWORK_IFS.stream().noneMatch(e -> ArrayUtils.contains(e.getIPv4addr(), toBeSigned.getIP()) && toBeSigned.getMAC().equalsIgnoreCase(e.getMacaddr()))) {
            log.warn("license ip-mac illegal: {}-{}", toBeSigned.getIP(), toBeSigned.getMAC());
            throw new ServiceException(ErrorEnum.ILLEGAL_LICENSE);
        }
        return toBeSigned;
    }

    public static class ToBeSigned extends DERSequence {
        public ToBeSigned(String name, String ip, String mac, Date validityPeriod) {
            super(new ASN1Encodable[]{new DERUTF8String(name), new DERUTF8String(ip), new DERUTF8String(mac), new DERUTCTime(validityPeriod)});
        }

        ToBeSigned(ASN1Encodable[] elements) {
            super(elements);
        }

        public static ToBeSigned getInstance(Object obj) {
            if (obj instanceof ASN1Sequence) {
                return new ToBeSigned(((ASN1Sequence) obj).toArray());
            }
            throw new IllegalArgumentException("unknown object in getInstance: " + obj.getClass().getName());
        }

        public String getName() {
            return ((ASN1UTF8String) getObjectAt(0)).getString();
        }

        public String getIP() {
            return ((ASN1UTF8String) getObjectAt(1)).getString();
        }

        public String getMAC() {
            return ((ASN1UTF8String) getObjectAt(2)).getString();
        }

        public Date getValidityPeriod() throws ParseException {
            return ((ASN1UTCTime) getObjectAt(3)).getDate();
        }
    }
}
