package com.dmoney.dmoney.auth.infrastructure.restapi;

import com.dmoney.dmoney.auth.application.user.command.UserRegistrationCommand;
import com.dmoney.dmoney.auth.application.user.usecase.LoginUserUseCase;
import com.dmoney.dmoney.auth.application.user.usecase.RegisterUserUseCase;
import com.dmoney.dmoney.auth.infrastructure.security.JwtAuthenticationFilter;
import com.dmoney.dmoney.auth.infrastructure.security.JwtTokenParser;
import com.dmoney.dmoney.shared.domain.exceptions.ResourceAlreadyExistsException;
import com.dmoney.dmoney.shared.domain.exceptions.UnauthenticatedException;
import com.dmoney.dmoney.shared.domain.models.UserId;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
public class AuthControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @MockBean
    private JwtTokenParser jwtTokenParser;

    @MockBean
    private RegisterUserUseCase registerUserUseCase;

    @MockBean
    private LoginUserUseCase loginUserUseCase;

    @Nested
    class RegisterControllerTest{
        @Test
        void should_return_user_id_and_status_201_when_register() throws Exception{
            UserId userId = UserId.newId();
            String json = """
                        {
                            "email": "email@test.com",
                            "password": "password",
                            "name": "Name"
                        }
                    """;

            when(registerUserUseCase.execute(any(UserRegistrationCommand.class)))
                    .thenReturn(userId);

            mockMvc.perform(post("/auth/register")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(json))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.userId").value(userId.toString()));

            verify(registerUserUseCase).execute(argThat(command ->
                    command.email().equals("email@test.com") &&
                            command.password().equals("password") &&
                            command.name().equals("Name")));
        }
        @Test
        void should_return_400_when_email_is_blank() throws Exception {
            mockMvc.perform(post("/auth/register")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("""
                                {
                                    "email": "",
                                    "password": "password123",
                                    "name": "John Doe"
                                }
                            """))
                    .andExpect(status().isBadRequest());
        }

        @Test
        void should_return_400_when_password_is_blank() throws Exception {
            mockMvc.perform(post("/auth/register")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("""
                                {
                                    "email": "test@email.com",
                                    "password": "",
                                    "name": "John Doe"
                                }
                            """))
                    .andExpect(status().isBadRequest());
        }

        @Test
        void should_return_400_when_name_is_blank() throws Exception {
            mockMvc.perform(post("/auth/register")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("""
                                {
                                    "email": "test@email.com",
                                    "password": "password123",
                                    "name": ""
                                }
                            """))
                    .andExpect(status().isBadRequest());
        }

        @Test
        void should_return_409_when_email_already_exists() throws Exception {
            when(registerUserUseCase.execute(any()))
                    .thenThrow(new ResourceAlreadyExistsException("Email", "value", "test@email.com"));

            mockMvc.perform(post("/auth/register")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("""
                                {
                                    "email": "test@email.com",
                                    "password": "password123",
                                    "name": "John Doe"
                                }
                            """))
                    .andExpect(status().isConflict());
        }

        @Test
        void should_return_400_when_malformed_json() throws Exception {
            mockMvc.perform(post("/auth/register")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("""
                                {
                                    "email": "test@email.com"
                                    "password": "password123"
                                }
                            """))
                    .andExpect(status().isBadRequest());
        }

        @Test
        void should_return_500_when_unexpected_error() throws Exception {
            when(registerUserUseCase.execute(any()))
                    .thenThrow(new RuntimeException("Unexpected error"));

            mockMvc.perform(post("/auth/register")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("""
                                {
                                    "email": "test@email.com",
                                    "password": "password123",
                                    "name": "John Doe"
                                }
                            """))
                    .andExpect(status().isInternalServerError());
        }
    }

    @Nested
    class LoginControllerTests {

        @Test
        void should_login_and_return_200_with_token() throws Exception {
            String token = "eyJhbGciOiJIUzI1NiJ9.token";

            when(loginUserUseCase.execute(any()))
                    .thenReturn(token);

            mockMvc.perform(post("/auth/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("""
                            {
                                "email": "test@email.com",
                                "password": "password123"
                            }
                        """))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.token").value(token));

            verify(loginUserUseCase).execute(argThat(command ->
                    command.email().equals("test@email.com") &&
                            command.password().equals("password123")
            ));
        }

        @Test
        void should_return_400_when_email_is_blank() throws Exception {
            mockMvc.perform(post("/auth/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("""
                            {
                                "email": "",
                                "password": "password123"
                            }
                        """))
                    .andExpect(status().isBadRequest());
        }

        @Test
        void should_return_400_when_password_is_blank() throws Exception {
            mockMvc.perform(post("/auth/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("""
                            {
                                "email": "test@email.com",
                                "password": ""
                            }
                        """))
                    .andExpect(status().isBadRequest());
        }

        @Test
        void should_return_401_when_invalid_credentials() throws Exception {
            when(loginUserUseCase.execute(any()))
                    .thenThrow(new UnauthenticatedException("Invalid credentials"));

            mockMvc.perform(post("/auth/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("""
                            {
                                "email": "test@email.com",
                                "password": "wrongpassword"
                            }
                        """))
                    .andExpect(status().isUnauthorized());
        }

        @Test
        void should_return_400_when_malformed_json() throws Exception {
            mockMvc.perform(post("/auth/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("""
                            {
                                "email": "test@email.com"
                                "password": "password123"
                            }
                        """))
                    .andExpect(status().isBadRequest());
        }

        @Test
        void should_return_500_when_unexpected_error() throws Exception {
            when(loginUserUseCase.execute(any()))
                    .thenThrow(new RuntimeException("Unexpected error"));

            mockMvc.perform(post("/auth/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("""
                            {
                                "email": "test@email.com",
                                "password": "password123"
                            }
                        """))
                    .andExpect(status().isInternalServerError());
        }
    }

}
