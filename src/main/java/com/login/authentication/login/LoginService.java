package com.login.authentication.login;

import com.login.authentication.appuser.AppUser;
import com.login.authentication.appuser.AppUserRepository;
import com.login.authentication.appuser.AppUserService;
import com.login.authentication.email.EmailSender;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class LoginService {

    private final AppUserService appUserService;
    private final EmailSender emailSender;
    private final AppUserRepository appUserRepository;
    private OtpVerification otpVerification;

    /*@Value("$(spring.datasource.url)")
    private String databaseUrl;
    @Value("$(spring.datasource.username)")
    private String databaseUsername;
    @Value("$(spring.datasource.password)")
    private String databasePassword;*/


    public boolean loginUser(String emailId) {
        otpVerification = new OtpVerification();
        String otp = otpVerification.getOtpNumber();
        String emailContent = "WELCOME, Your OTP is : " + otp;
        AppUser user = appUserRepository.findByEmail(emailId);
        if (user == null)
            user = new AppUser(emailId, otp);
        else
            user.setOtp(otp);
        appUserService.signInUser(user);
        emailSender.send(emailId, emailContent);
        return true;
    }

    public boolean verifyOtp(String generatedOtp, String emailId) {
        AppUser appUser = appUserService.getUser(emailId);
        return generatedOtp.equals(appUser.getOtp());
    }

}
