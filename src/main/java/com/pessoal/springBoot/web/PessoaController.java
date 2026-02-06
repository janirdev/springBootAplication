package com.pessoal.springBoot.web;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pessoal.springBoot.entity.dto.PessoaContactoRec;
import com.pessoal.springBoot.repository.PessoaCriteriaRepository;

@RestController
@RequestMapping("/pessoas")
public class PessoaController {

    private final PessoaCriteriaRepository repo;

    public PessoaController(PessoaCriteriaRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public List<PessoaContactoRec> listar(
            @RequestParam(required = false) String nome, 
            @RequestParam(required = false) int page,
             @RequestParam(required = false) int size) {
        return repo.listar(nome, page, size);
    }
}

