package br.com.jbcommerce.controller;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.mindrot.jbcrypt.BCrypt;

import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.interceptor.IncludeParameters;
import br.com.caelum.vraptor.validator.SimpleMessage;
import br.com.caelum.vraptor.validator.Validator;
import br.com.jbcommerce.dao.UsuarioDAO;
import br.com.jbcommerce.model.Perfil;
import br.com.jbcommerce.model.Usuario;


@Controller
@Path("cadastrar")
public class CadastrarController {
	
	
	@Inject EntityManager em;
	@Inject Result result;
	@Inject UsuarioDAO usuarioDao; 
	@Inject Validator validator;
	@Inject HttpSession session;
	@Get("")
	public void cadastrar() {
		}
	
	
	@IncludeParameters
	@Post("salvaUsuario")
	public void salvaUsuario( @Valid Usuario usuario, String confirmaSenha) {

		boolean asSenhasSaoIguais = usuario.getSenha().equals(confirmaSenha);
		validator.ensure(asSenhasSaoIguais, new SimpleMessage("erro", "A confirmação de senha está diferente"));
		validator.onErrorRedirectTo(this).cadastrar();

		
		usuario.setSenha(BCrypt.hashpw(usuario.getSenha(), BCrypt.gensalt()));
		
		// Define o perfil do novo usuário como CLIENTE
				usuario.setPerfil(Perfil.CLIENTE);
		
		usuarioDao.insert(usuario);
		em.persist(usuario);
		
		session.setAttribute("usuarioLogado", usuario);
		result.redirectTo(ProdutosController.class).produtos(null);
		
		
		
	
	}




}
