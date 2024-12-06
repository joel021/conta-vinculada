alter table funcionario add column raca_cor varchar(100) null;

create table if not exists lotacao(
    id_lotacao int not null auto_increment,
    descricao varchar(255),
    primary key (id_lotacao)
);

alter table contrato_terceirizado add column id_lotacao int null;

alter table contrato_terceirizado add foreign key (id_lotacao) references lotacao(id_lotacao);

create table if not exists afastamento (
    id_afastamento int not null auto_increment,
    id_contrato_substituido int null,
    id_contrato_substituto int null,
    data_inicio datetime null,
    data_fim date null,
    primary key (id_afastamento),
    foreign key(id_contrato_substituido) references contrato_terceirizado(id),
    foreign key(id_contrato_substituto) references contrato_terceirizado(id)
);

