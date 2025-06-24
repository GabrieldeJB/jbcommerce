package br.com.jbcommerce.dao;

import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;

import br.com.jbcommerce.model.Categoria;
import br.com.jbcommerce.model.Produto;
import br.com.olimposistema.aipa.dao.DAO;


@RequestScoped
public class ProdutoDAO extends DAO<Produto>{

	@Deprecated public ProdutoDAO() {super(null,null);}
	
	@Inject
	public ProdutoDAO(EntityManager em) {
		super(em, Produto.class);
	}
	
	public List<Produto> buscaTodosOsProdutosOrdenadosPeloNome(){
		//
		  CriteriaBuilder cb = em.getCriteriaBuilder();
		  CriteriaQuery<Produto> q = cb.createQuery(Produto.class);
		  
		  Root<Produto> r = q.from(Produto.class);
		  q.select(r)
		  .orderBy(cb.asc(r.get("nome")));
		  
		  TypedQuery<Produto> query = em.createQuery(q);
		  List<Produto> produtos = query.getResultList();
		  
		  return produtos;
		
	}
	
	public List<Produto> buscaPorCategoriaEspecifica(){
		
		  CriteriaBuilder cb = em.getCriteriaBuilder();
		  CriteriaQuery<Produto> q = cb.createQuery(Produto.class);
		  
		  Root<Produto> r = q.from(Produto.class);
		  	Join<Produto, Categoria> jc = r.join("categoria",JoinType.INNER);
		 
		  q.select(r)
		  .where(
				  cb.and(
						  cb.like(cb.lower(r.get("nome")), "%galaxy%")),
				  cb.equal(jc.get("id"), 5)
				  
				);
		  
		  TypedQuery<Produto> query = em.createQuery(q);
		  List<Produto> produtos = query.getResultList();
		  return produtos;
	}
	
	public Long totalProdutos() {
		 CriteriaBuilder cb = em.getCriteriaBuilder();
		 CriteriaQuery<Long> q = cb.createQuery(Long.class);
		 
		 
		 Root<Produto> r = q.from(Produto.class);
		 q.multiselect(cb.count(r));
		 
		 
		 
		 TypedQuery<Long> query = em.createQuery(q);
		 Long total = query.getSingleResult();
		 return total;
		
		
	}

}
