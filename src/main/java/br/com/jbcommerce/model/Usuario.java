package br.com.jbcommerce.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import br.com.olimposistema.aipa.model.Model;


@Entity
public class Usuario extends Model {
	
	@NotEmpty @Size(min = 3, max = 100, message= "{usuario.nome.size}")
	private String nome;
	
	@NotEmpty @Email @Column(unique = true)
	private String email;
	
	@NotEmpty @Size(min = 8, max = 100, message= "{usuario.senha.size}")
	private String senha;
	
	@Enumerated(EnumType.STRING)
    private Perfil perfil;

	public Perfil getPerfil() {
		return perfil;
	}
	public void setPerfil(Perfil perfil) {
		this.perfil = perfil;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome.toUpperCase();
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getSenha() {
		return senha;
	}
	public void setSenha(String senha) {
		this.senha = senha;
	}
	
	
}
