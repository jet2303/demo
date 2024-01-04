package com.daily.demo.entity.user;

import org.hibernate.annotations.DynamicUpdate;

import com.daily.demo.dto.request.UserRequest;
import com.daily.demo.entity.auth.Provider;
import com.daily.demo.entity.daily.BaseEntity;
import com.daily.demo.entity.daily.enumData.Useyn;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Entity
@Getter
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Table(name = "Users")
@DynamicUpdate
public class Users extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String imageUrl;

    private Boolean emailVerified;

    private String email;

    @JsonIgnore
    private String password;

    // @NotNull
    @Enumerated(EnumType.STRING)
    private Provider provider;

    @Enumerated(EnumType.STRING)
    private Role role;

    // providerId 용도 확인
    private String providerId;

    @Enumerated
    private Useyn useyn;

    public void updateName(String name) {
        this.name = name;
    }

    public void updateImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void update(UserRequest userRequest) {
        this.name = userRequest.getName();
        this.imageUrl = userRequest.getImageUrl();
        this.emailVerified = userRequest.getEmailVerified();
        this.email = userRequest.getEmail();
        // this.password = userRequest.getPassword()
        this.provider = userRequest.getProvider();
        this.providerId = userRequest.getProviderId();
        // this.role = userRequest.getRole();
        this.useyn = userRequest.getUseyn();
    }

    public void disableUser() {
        this.useyn = Useyn.N;
    }

    @Override
    public String toString() {
        return "Users [id=" + id + ", name=" + name + ", imageUrl=" + imageUrl + ", emailVerified=" + emailVerified
                + ", email=" + email + ", password=" + password + ", provider=" + provider + ", role=" + role
                + ", providerId=" + providerId + ", useyn=" + useyn

                + "]";
    }

}
