package org.sid.registrationwithemailverification.registration;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/registration")
@AllArgsConstructor
public class RegistrationController {

    private final RegistrationService registrationService;

    @PostMapping
    public String register(@RequestBody RegistrationRequest request){
        return registrationService.register(request);
    }

    @PostMapping("/confirm")
    public String confirm(@RequestParam(name = "token") String token){
        return registrationService.confirmToken(token);
    }
}
