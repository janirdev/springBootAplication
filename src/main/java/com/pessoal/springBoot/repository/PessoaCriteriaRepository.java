package com.pessoal.springBoot.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import org.springframework.stereotype.Repository;

import com.pessoal.springBoot.entity.Contacto;
import com.pessoal.springBoot.entity.Pessoa;
import com.pessoal.springBoot.entity.dto.PessoaContactoRec;

import java.util.ArrayList;
import java.util.List;


@Repository
public class PessoaCriteriaRepository {

    @PersistenceContext
    private EntityManager em;

    /**
     * Lista pessoas com seus contactos, com filtro por nome e paginação
     *
     * @param nome  filtro opcional pelo nome da pessoa
     * @param page  número da página (0-based)
     * @param size  tamanho da página
     * @return lista paginada de PessoaContactoRec
     */
    public List<PessoaContactoRec> listar(String nome, int page, int size) {

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<PessoaContactoRec> cq =
                cb.createQuery(PessoaContactoRec.class);

        Root<Pessoa> pessoa = cq.from(Pessoa.class);
        Join<Pessoa, Contacto> contacto =
                pessoa.join("contactos", JoinType.LEFT);

        cq.select(cb.construct(
                PessoaContactoRec.class,
                pessoa.get("id"),
                pessoa.get("nome"),
                contacto.get("id"),
                contacto.get("tipo"),
                contacto.get("valor")
        ));

        List<Predicate> filtros = new ArrayList<>();

        if (nome != null && !nome.isBlank()) {
            filtros.add(cb.like(cb.lower(pessoa.get("nome")), "%" + nome.toLowerCase() + "%"));
        }

        if (!filtros.isEmpty()) {
            cq.where(cb.and(filtros.toArray(new Predicate[0])));
        }

        // Ordenação opcional (por nome, por exemplo)
        cq.orderBy(cb.asc(pessoa.get("nome")));

        // Cria query
        var query = em.createQuery(cq);

        // Paginação
        query.setFirstResult(page * size); // índice do primeiro resultado
        query.setMaxResults(size);         // quantidade de resultados

        return query.getResultList();
    }
}


