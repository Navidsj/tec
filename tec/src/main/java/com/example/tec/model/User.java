package com.example.tec.model;



import jakarta.persistence.*;
import lombok.Data;
import net.bytebuddy.asm.MemberSubstitution;
import org.checkerframework.checker.index.qual.Positive;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "users")
@Data
public class User implements UserDetails {

    @Id
    @SequenceGenerator(name = "UserIdSeqGenerator", allocationSize = 1, sequenceName = "UserIdSeq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "UserIdSeqGenerator")
    private Long id;

    @Column(nullable = false,length = 20)
    String name;

    @Column(nullable = false,unique = true,length = 30)
    String email;

    @Column(nullable = false)
    String password;

    @Column(nullable = false)
    String phoneNumber;

    @Column
    Boolean admin = false;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getUsername() {
        return name;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}

