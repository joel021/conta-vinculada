/* inc_grupo_a_contrato */
alter table inc_grupo_a_contrato
    add column id_criado_por int null,
    add column criado_em datetime not null default NOW(),
    add column id_deletado_por int null,
    add column deletado_em datetime null;

alter table inc_grupo_a_contrato
    add foreign key (id_criado_por) references usuario(id_usuario),
    add foreign key (id_deletado_por) references usuario(id_usuario);

/* liberacao */
alter table liberacao
    add column id_criado_por int null,
    add column criado_em datetime not null default NOW(),
    add column id_deletado_por int null,
    add column deletado_em datetime null;

alter table liberacao
    add foreign key (id_criado_por) references usuario(id_usuario),
    add foreign key (id_deletado_por) references usuario(id_usuario);

/* funcionario */
alter table funcionario
    add column id_criado_por int null,
    add column criado_em datetime not null default NOW(),
    add column id_deletado_por int null,
    add column deletado_em datetime null;

alter table funcionario
    add foreign key (id_criado_por) references usuario(id_usuario),
    add foreign key (id_deletado_por) references usuario(id_usuario);

/* contrato_terceirizado */
alter table contrato_terceirizado
    add column id_criado_por int null,
    add column criado_em datetime not null default NOW(),
    add column id_deletado_por int null,
    add column deletado_em datetime null;

alter table contrato_terceirizado
    add foreign key (id_criado_por) references usuario(id_usuario),
    add foreign key (id_deletado_por) references usuario(id_usuario);

