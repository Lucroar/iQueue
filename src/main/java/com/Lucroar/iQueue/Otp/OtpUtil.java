package com.Lucroar.iQueue.Otp;

import java.security.SecureRandom;

public class OtpUtil {
    private static final int OTP_LENGTH = 6;
    private static final String CHARACTERS = "0123456789";

    public static String generateOtp() {
        SecureRandom random = new SecureRandom();
        StringBuilder OTP = new StringBuilder(OTP_LENGTH);
        for (int i = 0; i < OTP_LENGTH; i++) {
            OTP.append(CHARACTERS.charAt(random.nextInt(CHARACTERS.length())));
        }
        return OTP.toString();
    }
}
