package com.dmoney.dmoney.auth.infrastructure.restapi;

import com.dmoney.dmoney.auth.application.user.login.LoginCommand;
import com.dmoney.dmoney.auth.application.user.login.LoginUserUseCase;
import com.dmoney.dmoney.auth.application.user.register.RegisterUserUseCase;
import com.dmoney.dmoney.auth.application.user.register.UserRegistrationCommand;
import com.dmoney.dmoney.auth.domain.user.UserId;
import com.dmoney.dmoney.auth.infrastructure.restapi.dto.LoginRequest;
import com.dmoney.dmoney.auth.infrastructure.restapi.dto.LoginResponse;
import com.dmoney.dmoney.auth.infrastructure.restapi.dto.RegisterRequest;
import com.dmoney.dmoney.auth.infrastructure.restapi.dto.RegisterResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final RegisterUserUseCase registerUserUseCase;
    private final LoginUserUseCase loginUserUseCase;

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(
            @RequestBody RegisterRequest request
    ){
        UserRegistrationCommand command = new UserRegistrationCommand(
                request.email(),
                request.password(),
                request.name()
        );

        UserId id = registerUserUseCase.execute(command);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new RegisterResponse(id.toString()));
    }

    @PostMapping("/login")
    public LoginResponse login(
            @RequestBody LoginRequest request
    ){
        String token = loginUserUseCase.execute(
                new LoginCommand(request.email(), request.password())
        );

        return new LoginResponse(token);
    }
}
