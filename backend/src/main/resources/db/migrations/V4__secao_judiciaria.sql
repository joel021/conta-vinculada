insert into secao_judiciaria(cnpj_secao, nome, sigla)
	select * from (select '05442957000101', 'Justiça Federal da Bahia', 'JFBA') AS tmp
	where not exists (
		select cnpj_secao from secao_judiciaria where cnpj_secao = '05442957000101'
	) LIMIT 1;

insert into unidade(nome_unidade, sigla_unidade, email_unidade, is_nucleo, cnpj_secao)
	select * from (select 'Seção de Registros Financeiros', 'SERFI', 'serfi.ba@trf1.jus.br', false, '05442957000101') AS tmp
	where not exists (
		select sigla_unidade from unidade where sigla_unidade = 'SERFI'
) LIMIT 1;


