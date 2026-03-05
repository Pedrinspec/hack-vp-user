package com.fiap.authservice.infra.persistence;

import com.fiap.authservice.domain.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    private String name;

    //TODO
    // Método de mapeamento (Mapper) para converter do Domínio para Infra
    public static UserEntity fromDomain(User user) {
        return new UserEntity(user.getId(), user.getEmail(), user.getPassword(), user.getName());
    }

    //TODO
    // Método para converter de Infra para Domínio
    public User toDomain() {
        return new User(this.id, this.name, this.email, this.password);
    }

}
