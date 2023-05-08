drop database if exists taskemon;
create database taskemon;
use taskemon;

create table pokemon (
	pokemon_id int primary key auto_increment,
	pokemon_name varchar(255) not null,
	img_url varchar(2048) not null
);

create table user (
	user_id int primary key auto_increment,
	username varchar(255) not null unique,
	password_hash varchar(128) not null unique,
	email varchar(320) not null unique,
    enabled boolean not null
);

create table auth (
	auth_id int primary key auto_increment,
	name varchar(25) not null
);

create table checklist (
	checklist_id int primary key auto_increment,
	total_completion varchar(255) not null,
	checklist_name varchar(255) not null,
	pokemon_id int not null,
	user_id int not null,
    constraint checklist_fk_pokemon_id 
		foreign key (pokemon_id) 
		references pokemon(pokemon_id),
	constraint checklist_fk_user_id
		foreign key (user_id) 
		references user (user_id)
);

create table task (
	task_id int primary key auto_increment,
	task_name varchar(255) not null,
	due_date date null,
	status varchar(255) not null,
	checklist_id int not null,
	constraint task_fk_checklist_id
		foreign key (checklist_id) 
        references checklist(checklist_id)
);


create table user_auth (
	user_id int not null,
	auth_id int not null,
	constraint user_auth_fk_user_id
		foreign key (user_id) 
		references user(user_id),
	constraint user_auth_fk_auth_id
		foreign key (auth_id) 
		references auth(auth_id)
);


create table pokemon_user (
	pokemon_id int not null,
	user_id int not null,
    constraint pokemon_user_fk_pokemon_id
		foreign key (pokemon_id) 
        references pokemon(pokemon_id),
	constraint pokemon_user_fk_user_id
		foreign key (user_id) 
        references user(user_id)
);















