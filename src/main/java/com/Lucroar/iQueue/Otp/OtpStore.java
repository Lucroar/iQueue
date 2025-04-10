package com.Lucroar.iQueue.Otp;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Instant;
import java.util.concurrent.ConcurrentHashMap;

public class OtpStore {
    private static final ConcurrentHashMap<String, OtpEntry> OTPStore = new ConcurrentHashMap<>();

    @AllArgsConstructor
    @Getter
    static class OtpEntry {
        private String otp;
        private Instant expirationTime;

    }
    public static void storeOtp(String email, String otp, Instant expirationTime) {
        OTPStore.put(email, new OtpEntry(otp, expirationTime));
    }

    public static String getOtp(String email) {
        OtpEntry otpEntry = OTPStore.get(email);
        if (otpEntry != null && Instant.now().isBefore(otpEntry.getExpirationTime())) {
            return otpEntry.getOtp();
        } else {
            removeOtp(email);
            return null;
        }
    }

    public static void removeOtp(String email) {
        OTPStore.remove(email);
    }

    static ConcurrentHashMap<String, OtpEntry> getOtpStore() {
        return OTPStore;
    }
}
