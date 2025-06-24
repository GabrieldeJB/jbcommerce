 package br.com.jbcommerce.controller;

import javax.inject.Inject;
import javax.servlet.http.HttpSession;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;
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
import br.com.jbcommerce.model.Usuario;


@Controller
@Path("login")
public class LoginController {
	
	@Inject Result result;
	@Inject Validator validator;
	@Inject UsuarioDAO usuarioDao;
	@Inject HttpSession session;
	
	
@Get("")
	public void login() {
	}

@IncludeParameters
@Post("autenticar")
public void autenticar(@NotEmpty String email,
		@NotEmpty @Size(min = 8, max = 100, message= "{usuario.senha.size}")String senha){
	
	validator.onErrorRedirectTo(this).login();
	
	Usuario usuario = usuarioDao.buscaPorEmail(email);
	
	if (usuario == null || !BCrypt.checkpw(senha, usuario.getSenha())) {
	    validator.add(new SimpleMessage("erro", "Email ou senha incorreto"));
	    validator.onErrorRedirectTo(this).login();
	}
	
	session.setAttribute("usuarioLogado", usuario);
	
	

	result.redirectTo(ProdutosController.class).produtos(null);
	
}

}
