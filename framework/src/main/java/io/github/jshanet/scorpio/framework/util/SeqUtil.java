package io.github.jshanet.scorpio.framework.util;

import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.time.FastDateFormat;
import org.apache.commons.text.CharacterPredicates;
import org.apache.commons.text.RandomStringGenerator;

import java.net.NetworkInterface;
import java.net.SocketException;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.Enumeration;
import java.util.zip.CRC32;

/**
 * @author seanjiang
 *
 */
@Log4j2
public class SeqUtil {

    private static final FastDateFormat FAST_DATE_FORMAT = FastDateFormat.getInstance("yyyyMMddHHmmssSSS");

    private static final RandomStringGenerator RANDOM_STRING_GENERATOR =
            new RandomStringGenerator.Builder()
            .withinRange('0', 'z')
            .filteredBy(CharacterPredicates.LETTERS, CharacterPredicates.DIGITS)
            .build();

    private static String getMacAddress() throws SocketException {
        Enumeration<NetworkInterface> ni = NetworkInterface.getNetworkInterfaces();
        while (ni.hasMoreElements()) {
            NetworkInterface netI = ni.nextElement();
            if (null == netI) {
                continue;
            }
            byte[] macBytes = netI.getHardwareAddress();
            if (netI.isUp() && !netI.isLoopback() && null != macBytes && macBytes.length == 6) {
                StringBuilder sb = new StringBuilder();
                for (int i = 0, nLen = macBytes.length; i < nLen; i++) {
                    byte b = macBytes[i];
                    sb.append(Integer.toHexString((b & 240) >> 4));
                    sb.append(Integer.toHexString(b & 15));
                    if (i < nLen - 1) {
                        sb.append("-");
                    }
                }
                return sb.toString().toUpperCase();
            }
        }
        return null;
    }

    private static long getCRC32Code(String macAddress) {
        if (null == macAddress) {
            return 0;
        }
        byte[] data = macAddress.getBytes(Charset.forName("utf-8"));
        CRC32 crc32 = new CRC32();
        for (byte b : data) {
            crc32.update(b);
        }
        return crc32.getValue();
    }

    public static String nextValue() {
        try {
            String dateStr = FAST_DATE_FORMAT.format(new Date());
            String code = String.valueOf(getCRC32Code(getMacAddress()));
            String randomStr = RANDOM_STRING_GENERATOR.generate(11);
            return dateStr + code + randomStr;
        } catch (Exception e) {
            log.error("get next value error", e);
            return null;
        }
    }

    @SneakyThrows
    public static void main(String[] args) {
        System.out.println(nextValue());
    }

}
