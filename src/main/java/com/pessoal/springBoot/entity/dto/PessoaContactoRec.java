package com.pessoal.springBoot.entity.dto;

public record PessoaContactoRec(
    Long pessoaId,
    String nome,
    Long contactoId,
    String tipo,
    String valor
) {}

