package com.dmoney.dmoney.auth.domain.user;


public class User {
    private final UserId userId;
    private final UserEmail email;
    private PasswordHash password;
    private UserName userName;
    private AuthProvider authProvider;

    public User(
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
}
