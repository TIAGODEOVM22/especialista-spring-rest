create table cidade(
	id bigint not null auto_increment,
    nome_cidade varchar(80) not null,
	nome_estado varchar(80) not null,

	primary key (id)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;