package com.dmoney.dmoney.auth.infrastructure.persistence.user;

import com.dmoney.dmoney.auth.domain.user.model.*;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UserPersistenceMapperTest {

    @Test
    void should_have_private_constructor() throws Exception{
        Constructor<UserPersistenceMapper> constructor =
                UserPersistenceMapper.class.getDeclaredConstructor();

        assertTrue(Modifier.isPrivate(constructor.getModifiers()));

        constructor.setAccessible(true);
        constructor.newInstance();
    }

    @Test
    void should_transform_domain_to_entity(){
        User domain = User.register(
                UserEmail.from("domain@test.com"),
                PasswordHash.from("password"),
                UserName.from("Domain"),
                AuthProvider.LOCAL
        );

        UserEntity entity = UserPersistenceMapper.toEntity(domain);

        assertThat(entity.id).isEqualTo(domain.userId().value());
        assertThat(entity.getEmail()).isEqualTo(domain.email().value());
        assertThat(entity.getPasswordHash()).isEqualTo(domain.password().value());
        assertThat(entity.getName()).isEqualTo(domain.userName().value());
        assertThat(entity.getProvider()).isEqualTo(domain.authProvider().toString());
    }

    @Test
    void should_transform_entity_to_domain(){
        UserEntity entity = new UserEntity(
                UUID.randomUUID(),
                "entity@test.com",
                "password",
                "Entity",
                "LOCAL"
        );

        User domain = UserPersistenceMapper.toDomain(entity);

        assertThat(domain.userId().value()).isEqualTo(entity.id);
        assertThat(domain.email().value()).isEqualTo(entity.getEmail());
        assertThat(domain.password().value()).isEqualTo(entity.getPasswordHash());
        assertThat(domain.userName().value()).isEqualTo(entity.getName());
        assertThat(domain.authProvider().toString()).hasToString(entity.getProvider());
    }
}
