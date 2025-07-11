package br.com.jbcommerce.dao;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import br.com.jbcommerce.model.Usuario;
import br.com.olimposistema.aipa.dao.DAO;


@RequestScoped
public class UsuarioDAO extends DAO<Usuario>{

	@Deprecated public UsuarioDAO() {super(null,null);}
	
	@Inject
	public UsuarioDAO(EntityManager em) {
		super(em, Usuario.class);
	}

	public Usuario existeUsuarioCom(String email, String senha) {
		String jpql = "Select u from Usuario as u where u.email = :pEmail and u.senha = :pSenha";
		Query query = em.createQuery(jpql);
		
		query.setParameter("pEmail", email);
		query.setParameter("pSenha", senha);
		try {
		Usuario usuario = (Usuario) query.getSingleResult();
		return usuario;
		}catch (NoResultException e) {
			return null;
		}
	}
	
	
	public Usuario buscaPorEmail(String email) {
        String jpql = "SELECT u FROM Usuario u WHERE u.email = :pEmail";
        try {
            return em.createQuery(jpql, Usuario.class)
                     .setParameter("pEmail", email)
                     .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    } 


	
}
