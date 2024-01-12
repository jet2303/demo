package com.daily.demo.repository.user;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.daily.demo.dto.response.UserResponse;
import com.daily.demo.entity.daily.enumData.Useyn;
import com.daily.demo.entity.user.Users;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

import static com.daily.demo.entity.user.QUsers.*;

@Repository
@RequiredArgsConstructor
public class UserCustomRepositoryImpl implements UserCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Optional<UserResponse> findEmail(String email, Useyn useyn) {
        UserResponse response = jpaQueryFactory
                .select(Projections.fields(UserResponse.class, users.id.as("id"), users.name.as("name"),
                        users.imageUrl.as("imageUrl"), users.emailVerified.as("emailVerified"), users.email.as("email"),
                        users.password.as("password"), users.provider.as("provider"), users.role.as("role"),
                        users.providerId.as("providerId"), users.useyn.as("useYn"), users.createdBy.as("createdBy"),
                        users.createdDate.as("createdDate"), users.modifiedBy.as("modifiedBy"),
                        users.modifiedDate.as("modifiedDate")))
                .from(users)
                .where(users.email.eq(email).and(users.useyn.eq(useyn)))
                .fetchOne();

        return response != null ? Optional.of(response) : Optional.empty();
    }

}
