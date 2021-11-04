package org.sid.registrationwithemailverification.registration;

import lombok.AllArgsConstructor;
import org.sid.registrationwithemailverification.appuser.AppUser;
import org.sid.registrationwithemailverification.appuser.AppUserRole;
import org.sid.registrationwithemailverification.appuser.AppUserService;
import org.sid.registrationwithemailverification.registration.token.ConfirmationToken;
import org.sid.registrationwithemailverification.registration.token.ConfirmationTokenService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class RegistrationService {

    private final AppUserService appUserService;
    private final EmailValidator emailValidator;
    private final ConfirmationTokenService confirmationTokenService;

    public String register(RegistrationRequest request) {
        boolean isValidEmail = emailValidator.test(request.getEmail());
        if(!isValidEmail){
            throw new IllegalStateException("email not valid");
        }
        return appUserService.signUpUser(
                new AppUser(
                        request.getFirstName(),
                        request.getLastName(),
                        request.getEmail(),
                        request.getPassword(),
                        AppUserRole.USER
                )
        );
    }

    @Transactional
    public String confirmToken(String token){
        ConfirmationToken confirmationToken = confirmationTokenService
                .getToken(token)
                .orElseThrow(() -> new IllegalStateException("token not found"));

        if(confirmationToken.getConfirmedAt() != null){
            throw new IllegalStateException("token already confirmed");
        }

        LocalDateTime expiresAt = confirmationToken.getExpiresAt();
        if (expiresAt.isBefore(LocalDateTime.now())){
            throw new IllegalStateException("token expired");
        }

        confirmationToken.setConfirmedAt(LocalDateTime.now());
        appUserService.enableAppUser(confirmationToken.getAppUser().getEmail());

        return "confirmed";

    }
}
