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
import com.pessoal.springBoot.entity.dto.PageResponse;
import com.pessoal.springBoot.entity.dto.PessoaContactoRec;

import java.util.ArrayList;
import java.util.List;


@Repository
public class PessoaCriteriaRepository {

    @PersistenceContext
    private EntityManager em;

    public PageResponse<PessoaContactoRec> listar(String nome, int page, int size) {

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
            filtros.add(cb.like(
                    cb.lower(pessoa.get("nome")),
                    "%" + nome.toLowerCase() + "%"
            ));
        }

        if (!filtros.isEmpty()) {
            cq.where(cb.and(filtros.toArray(new Predicate[0])));
        }

        cq.orderBy(cb.asc(pessoa.get("nome")));

        var query = em.createQuery(cq);
        query.setFirstResult(page * size);
        query.setMaxResults(size);

        List<PessoaContactoRec> resultados = query.getResultList();

        /* ======================
           QUERY DE CONTAGEM
        ======================= */
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<Pessoa> pessoaCount = countQuery.from(Pessoa.class);

        pessoaCount.join("contactos", JoinType.LEFT);

        countQuery.select(cb.countDistinct(pessoaCount));

        if (!filtros.isEmpty()) {
            countQuery.where(cb.and(filtros.toArray(new Predicate[0])));
        }

        Long total = em.createQuery(countQuery).getSingleResult();

        int totalPages = (int) Math.ceil((double) total / size);

        return new PageResponse<>(
                resultados,
                total,
                totalPages,
                page,
                size
        );
    }
}



