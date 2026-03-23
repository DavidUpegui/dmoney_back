package com.dmoney.dmoney.auth.infrastructure.persistence.user;

import com.dmoney.dmoney.auth.domain.user.model.*;
import com.dmoney.dmoney.shared.domain.models.UserId;

public class UserPersistenceMapper {

    public static UserEntity toEntity(User domain){
        return new UserEntity(
                domain.userId().value(),
                domain.email().toString(),
                domain.password().toString(),
                domain.userName().toString(),
                domain.authProvider().toString()
        );
    }

    public static User toDomain(UserEntity entity){
        return User.rehydrate(
                UserId.fromUUID(entity.getId()),
                UserEmail.from(entity.getEmail()),
                PasswordHash.from(entity.getPasswordHash()),
                UserName.from(entity.getName()),
                AuthProvider.valueOf(entity.getProvider())
        );
    }
}
