//package com.springSecurity.stepsForSecurity.service.impl;
//
//import com.springSecurity.stepsForSecurity.service.EmailService;
//import org.springframework.beans.factory.annotation.Autowired;
//
//import java.util.HashMap;
//import java.util.Map;
//
//public class OtpServiceImpl {
//
//    private final Map<String, String> otpStore = new HashMap<>();
//    private final Map<String, Long> otpExpiry = new HashMap<>();
//    private static final long OTP_EXPIRATION_TIME = 5 * 60 * 1000; // 5 minutes
//
//    @Autowired
//    private EmailService emailService;
//
//    public void generateAndSendOtp(String username) {
//        String otp = String.valueOf((int) (Math.random() * 9000) + 1000); // 4-digit OTP
//        otpStore.put(username, otp);
//        otpExpiry.put(username, System.currentTimeMillis() + OTP_EXPIRATION_TIME);
//
//        // Send OTP to the user's email
//        emailService.sendOtpEmail(username, otp);
//    }
//
//    public boolean validateOtp(String username, String otp) {
//        String storedOtp = otpStore.get(username);
//        Long expiryTime = otpExpiry.get(username);
//
//        if (storedOtp != null && storedOtp.equals(otp) && System.currentTimeMillis() < expiryTime) {
//            // Remove OTP after successful validation
//            otpStore.remove(username);
//            otpExpiry.remove(username);
//            return true;
//        }
//        return false;
//    }
//}
