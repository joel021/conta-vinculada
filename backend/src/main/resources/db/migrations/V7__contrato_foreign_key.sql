ALTER TABLE secao_judiciaria
    MODIFY COLUMN cnpj_secao varchar(14) COLLATE latin1_swedish_ci;

alter table contrato
    add foreign key (cnpj_secao) references secao_judiciaria(cnpj_secao);

