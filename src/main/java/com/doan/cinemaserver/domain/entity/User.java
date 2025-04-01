package com.doan.cinemaserver.domain.entity;

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
public class User  {
    @Id
    @UuidGenerator
    @Column(name = "user_id", columnDefinition = "NVARCHAR(64)")
    private String id;

    private String password;

    private String email;

    @JsonIgnore
    @Column(name = "refresh_token")
    @Lob
    private String refreshToken;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "role_id", foreignKey = @ForeignKey(name = "FK_USER_ROLE"), referencedColumnName = "role_id")
    private Role role;

    @OneToOne(mappedBy = "user")
    private Customer customer;
}
