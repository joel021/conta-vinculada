
alter table contrato add column cnpj_secao varchar(14) default null;

alter table unidade add column cnpj_secao varchar(14) default null;

alter table usuario
    add column email varchar(255) default null,
    add column is_enabled boolean default false;

create table if not exists secao_judiciaria(
    cnpj_secao varchar(14) not null,
    nome varchar(255) not null,
    primary key (cnpj_secao)
);

create table if not exists inc_grupo_a_contrato(
    id int not null auto_increment,
    id_contrato int not null,
    data date not null,
    inc_grupo_a double not null,
    primary key(id),
    foreign key(id_contrato) references contrato(id_contrato)
);

create table if not exists usuario_papeis(
    usuario_id_usuario int not null,
    papeis varchar(255) not null default 'ROLE_GUEST',
	foreign key(usuario_id_usuario) references usuario(id_usuario),
    primary key (usuario_id_usuario, papeis)
);

create table if not exists contrato_terceirizado(
    id int not null auto_increment,
    id_funcionario int not null,
    id_contrato int not null,
    cargo varchar(255),
    remuneracao float,
    carga_horaria int,
    data_inicio date,
    primary key (id),
    foreign key(id_funcionario) references funcionario(id_funcionario),
    foreign key(id_contrato) references contrato(id_contrato)
);

create table if not exists liberacao(
    id_liberacao int not null auto_increment,
    id_contrato_terceirizado int not null,
    data_liberacao date,
    tipo varchar(15),
    primary key(id_liberacao),
    foreign key(id_contrato_terceirizado) references contrato_terceirizado(id)
);
