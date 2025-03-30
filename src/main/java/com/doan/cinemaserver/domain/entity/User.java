package com.doan.cinemaserver.domain.entity;

import com.doan.cinemaserver.domain.entity.common.DateAuditing;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User extends DateAuditing {
    @Id
    @UuidGenerator
    @Column(name = "user_id", columnDefinition = "CHAR(32)")
    private String id;

    private String password;

    private String email;

    @JsonIgnore
    @Column(name = "refresh_token")
    @Lob
    private String refreshToken;

    @ManyToOne
    @JoinColumn(name = "role_id", foreignKey = @ForeignKey(name = "FK_USER_ROLE"), referencedColumnName = "role_id")
    private Role role;

}
