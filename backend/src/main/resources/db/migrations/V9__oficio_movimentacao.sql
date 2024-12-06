create table if not exists oficio_movimentacao (
    doc_sei int not null,
    numero_oficio int not null,
    ano_oficio int not null,
    primary key (doc_sei)
);

alter table liberacao add column doc_sei int null;

alter table liberacao add foreign key (doc_sei) references oficio_movimentacao(doc_sei);
