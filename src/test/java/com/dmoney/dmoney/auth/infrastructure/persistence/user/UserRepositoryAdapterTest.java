package com.dmoney.dmoney.auth.infrastructure.persistence.user;

import com.dmoney.dmoney.auth.domain.user.model.*;
import com.dmoney.dmoney.shared.domain.models.UserId;
import jakarta.validation.constraints.Email;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
public class UserRepositoryAdapterTest {

    @Autowired
    private UserJpaRepository jpaRepository;

    private UserRepositoryAdapter adapter;

    @BeforeEach
    void setUp(){
        adapter = new UserRepositoryAdapter(jpaRepository);
    }

    @Nested
    class RepositorySaveTest{
        @Test
        void should_save_an_user_and_return_the_domain_user(){
            User toSave = User.register(
                    UserEmail.from("email@test.com"),
                    PasswordHash.from("password"),
                    UserName.from("Name"),
                    AuthProvider.LOCAL
            );

            User saved = adapter.save(toSave);

            boolean persisted = jpaRepository.existsByEmail("email@test.com");

            assertThat(persisted).isTrue();
            assertThat(saved.userId()).isEqualTo(toSave.userId());
            assertThat(saved.email()).isEqualTo(toSave.email());
            assertThat(saved.password()).isEqualTo(toSave.password());
            assertThat(saved.userName()).isEqualTo(toSave.userName());
            assertThat(saved.authProvider()).isEqualTo(toSave.authProvider());
        }
    }

    @Nested
    class FindByIdUserRepositoryTest{
        @Test
        void should_find_user_by_its_id(){
            User toSave = User.register(
                    UserEmail.from("email@test.com"),
                    PasswordHash.from("password"),
                    UserName.from("Name"),
                    AuthProvider.LOCAL
            );

            UserId toFind = adapter.save(toSave).userId();

            Optional<User> found = adapter.findById(toFind);

            assertThat(found).isPresent();
            assertThat(found.get().userId()).isEqualTo(toSave.userId());
            assertThat(found.get().email()).isEqualTo(toSave.email());
            assertThat(found.get().password()).isEqualTo(toSave.password());
            assertThat(found.get().userName()).isEqualTo(toSave.userName());
            assertThat(found.get().authProvider()).isEqualTo(toSave.authProvider());
        }

        @Test
        void should_return_empty_optional_when_is_not_found(){
            UserId unexisting = UserId.newId();

            Optional<User> found = adapter.findById(unexisting);

            assertThat(found).isEmpty();
        }
    }

    @Nested
    class FindByEmailUserRepositoryTest{
        @Test
        void should_find_user_by_its_email(){
            User toSave = User.register(
                    UserEmail.from("email@test.com"),
                    PasswordHash.from("password"),
                    UserName.from("Name"),
                    AuthProvider.LOCAL
            );

            UserEmail toFind = adapter.save(toSave).email();

            Optional<User> found = adapter.findByEmail(toFind);

            assertThat(found).isPresent();
            assertThat(found.get().userId()).isEqualTo(toSave.userId());
            assertThat(found.get().email()).isEqualTo(toSave.email());
            assertThat(found.get().password()).isEqualTo(toSave.password());
            assertThat(found.get().userName()).isEqualTo(toSave.userName());
            assertThat(found.get().authProvider()).isEqualTo(toSave.authProvider());
        }

        @Test
        void should_return_empty_optional_when_is_not_found(){
            UserEmail unexisting = UserEmail.from("unexisting@test.com");

            Optional<User> found = adapter.findByEmail(unexisting);

            assertThat(found).isEmpty();
        }
    }

    @Nested
    class ExistsByEmailUserRepositoryTest{
        @Test
        void should_return_true_if_user_exists_by_its_email(){
            User toSave = User.register(
                    UserEmail.from("email@test.com"),
                    PasswordHash.from("password"),
                    UserName.from("Name"),
                    AuthProvider.LOCAL
            );

            UserEmail toFind = adapter.save(toSave).email();

            boolean found = adapter.existsByEmail(toFind);

            assertThat(found).isTrue();
        }

        @Test
        void should_return_false_if_user_does_not_exists_by_its_email(){
            UserEmail unexisting = UserEmail.from("unexisting@test.com");

            boolean found = adapter.existsByEmail(unexisting);

            assertThat(found).isFalse();
        }
    }


}
