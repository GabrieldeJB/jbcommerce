package br.com.jbcommerce.interceptors;

import javax.inject.Inject;
import javax.servlet.http.HttpSession;

import br.com.caelum.vraptor.InterceptionException;
import br.com.caelum.vraptor.Intercepts;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.controller.ControllerMethod;
import br.com.caelum.vraptor.core.InterceptorStack;
import br.com.caelum.vraptor.interceptor.Interceptor;

import br.com.jbcommerce.controller.LoginController;
import br.com.jbcommerce.model.Perfil;
import br.com.jbcommerce.model.Usuario;

@Intercepts
public class SomenteAdminInterceptor implements Interceptor {

    private final Result result;
    private final HttpSession session;
    
    
    @Deprecated // necessário para o CDI
    public SomenteAdminInterceptor() {
        this.result = null;
        this.session = null;
    }
    
    @Inject
    public SomenteAdminInterceptor(Result result, HttpSession session) {
        this.result = result;
        this.session = session;
    }

    @Override
    public boolean accepts(ControllerMethod method) {
        return method.containsAnnotation(SomenteAdmin.class);
    }

    @Override
    public void intercept(InterceptorStack stack, ControllerMethod method, Object resourceInstance)
            throws InterceptionException {

        Usuario usuarioLogado = (Usuario) session.getAttribute("usuarioLogado");

        if (usuarioLogado == null || !usuarioLogado.getPerfil().equals(Perfil.ADMIN)) {
            result.redirectTo(LoginController.class).login();
            return;
        }

        stack.next(method, resourceInstance);
    }
}
