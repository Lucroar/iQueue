package com.Lucroar.iQueue.Otp;

import org.springframework.scheduling.annotation.Scheduled;

import java.time.Instant;
import java.util.concurrent.ConcurrentHashMap;

public class OtpCleanupTask {
    private OtpStore otpStore;

    @Scheduled(fixedRate = 3600000)
    public void cleanUpExpiredOTPs(){
        ConcurrentHashMap<String, OtpStore.OtpEntry> otpMap = OtpStore.getOtpStore();
        Instant now = Instant.now();
        otpMap.entrySet().removeIf(entry -> entry.getValue().getExpirationTime().isBefore(now));
    }
}
