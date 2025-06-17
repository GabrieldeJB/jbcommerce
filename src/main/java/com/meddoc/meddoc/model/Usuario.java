package com.meddoc.meddoc.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Usuario implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private String cpf;
    private String email;
    private String celular;
    private String endereco;
    private Integer numero;
    private String bairro;
    private String cidade;
    private String senha;
    private String confirmarSenha;
    private LocalDate dataNascimento;
    private String nomeparentescoEmergencia;
    private String telefoneEmergencia;
    private String parentescoEmergencia;
    private String crm;
    private String tipoSanguineo;

    @ElementCollection
    private List<String> alergias;
    @ElementCollection
    private List<String> condicoesCronicas;
    @ElementCollection
    private List<String> medicamentosRegulares;

    @Enumerated(EnumType.STRING)
    private TipoUsuario tipoUsuario;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (this.tipoUsuario == null) {
            return List.of();
        }
        return List.of(new SimpleGrantedAuthority("ROLE_" + this.tipoUsuario.name()));
    }

    @Override
    public String getPassword() {
        return this.senha;
    }

    @Override
    public String getUsername() {
        return this.email;
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
