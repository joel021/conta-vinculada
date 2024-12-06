use conta_vinculada;

insert into pessoa(id_pessoa, nome) values (111212121, "Pessoa Juridica Para Teste");

insert into pessoa_juridica(id_pessoa, cnpj) values (111212121, "76767786673432345");

insert into objeto_contrato(id_objeto_contrato, descricao) values (1, "Objecto Contrato de teste");

insert into contrato(unidade, id_objeto_contrato, id_pessoa, data_assinatura, fim_vigencia, inicio_vigencia,
 numero, tipo, cnpj_secao) values ("JFBA", 1, 111212121, "2019-01-01", "2026-01-01", "2019-01-01",
"17080061", "CONTRATO", "05442957000101");

insert into contrato(unidade, id_objeto_contrato, id_pessoa, data_assinatura, fim_vigencia, inicio_vigencia,
 numero, tipo, cnpj_secao) values ("JFBA", 1, 111212121, "2019-01-01", "2026-01-01", "2019-01-01",
"17370260", "CONTRATO", "05442957000101");

insert into contrato(unidade, id_objeto_contrato, id_pessoa, data_assinatura, fim_vigencia, inicio_vigencia,
 numero, tipo, cnpj_secao) values ("JFBA", 1, 111212121, "2019-01-01", "2026-01-01", "2019-01-01",
"18039468", "CONTRATO", "05442957000101");

insert into contrato(unidade, id_objeto_contrato, id_pessoa, data_assinatura, fim_vigencia, inicio_vigencia,
 numero, tipo, cnpj_secao) values ("JFBA", 1, 111212121, "2019-01-01", "2026-01-01", "2019-01-01",
"18359456", "CONTRATO", "05442957000101");

insert into contrato(unidade, id_objeto_contrato, id_pessoa, data_assinatura, fim_vigencia, inicio_vigencia,
 numero, tipo, cnpj_secao) values ("JFBA", 1, 111212121, "2019-01-01", "2026-01-01", "2019-01-01",
"18970305", "CONTRATO", "05442957000101");

