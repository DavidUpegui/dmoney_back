package com.dmoney.dmoney.auth.domain.user.model;


import com.dmoney.dmoney.shared.domain.models.UserId;

public class User {
    private final UserId userId;
    private final UserEmail email;
    private PasswordHash password;
    private UserName userName;
    private AuthProvider authProvider;

    private User(
            UserId id,
            UserEmail email,
            PasswordHash password,
            UserName name,
            AuthProvider provider){
        this.userId = id;
        this.email = email;
        this.password = password;
        this.userName = name;
        this.authProvider = provider;
    }

    public static User register(
                                UserEmail email,
                                PasswordHash hash,
                                UserName name,
                                AuthProvider authProvider) {
        return new User(
                UserId.newId(),
                email,
                hash,
                name,
                authProvider
        );
    }

    public static User rehydrate(
            UserId id,
            UserEmail email,
            PasswordHash hash,
            UserName name,
            AuthProvider authProvider){
        return new User(
                id,
                email,
                hash,
                name,
                authProvider
        );
    }

    public UserId userId(){
        return userId;
    }
    public UserEmail email(){
        return email;
    }
    public PasswordHash password(){
        return password;
    }
    public UserName userName(){
        return userName;
    }
    public AuthProvider authProvider(){
        return authProvider;
    }
}
