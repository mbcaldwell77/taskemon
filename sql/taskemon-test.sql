drop database if exists taskemon_test;
create database taskemon_test;
use taskemon_test;

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
    enabled bit not null default(1)
);

create table auth (
	auth_id int primary key auto_increment,
	name varchar(25) not null
);

create table checklist (
	checklist_id int primary key auto_increment,
	total_completion int not null,
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
	status boolean not null,
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

delimiter //
create procedure set_known_good_state()
begin

    delete from pokemon_user;
    alter table pokemon_user auto_increment = 1;
    delete from user_auth;
    alter table user_auth auto_increment = 1;
    delete from task;
    alter table task auto_increment = 1;
    delete from checklist;
    alter table checklist auto_increment = 1;
    delete from auth;
    alter table auth auto_increment = 1;
	delete from user;
    alter table user auto_increment = 1;
    delete from pokemon;
    alter table pokemon auto_increment = 1;
    
    
insert into pokemon(pokemon_id, pokemon_name, img_url) values
(1, 'MissingNo', 'https://i.postimg.cc/VkJdySwx/Missing-No.gif'),
(2, 'Egg', 'https://i.postimg.cc/vZ1H2cwc/Egg.gif'),
(3, 'Cracked Egg', 'https://i.postimg.cc/nh5MnqG4/Cracked-Egg.gif'),
(4, 'Bulbasaur', 'https://i.postimg.cc/NGXDtMsr/Bulbasaur.gif'),
(5, 'Ivysaur', 'https://i.postimg.cc/8zcr1KRn/Ivysaur.gif'),
(6, 'Venusaur', 'https://static.pokemonpets.com/images/monsters-images-300-300/3-Venusaur.webp'),
(7, 'Squirtle', 'https://i.postimg.cc/BQVnCqFM/Squirtle.gif'),
(8, 'Wartortle', 'https://assets.pokemon.com/assets/cms2/img/pokedex/full/008.png'),
(9, 'Blastoise', 'https://assets.pokemon.com/assets/cms2/img/pokedex/full/009.png'),
(10, 'Charmander', 'https://i.postimg.cc/gkdVPHtf/Charmander.gif'),
(11, 'Charmeleon', 'https://assets.pokemon.com/assets/cms2/img/pokedex/full/005.png'),
(12, 'Charizard', 'https://assets.pokemon.com/assets/cms2/img/pokedex/full/006.png');

insert into user(username, password_hash, email, enabled) values 
('user', '$2a$10$5kG2SmZYKJs.VkcZ8GhWCe7oUaQyn1iKVTlg4h11o0Uq4PvQmt4Sm', 'user@dtest.com', 1),
('admin', '$2a$10$RMUKb8ZJFrCzvyFAyGoDGugvCmWLVTWnZoa3jW4cGkv.jG403Vl3W', 'admin@test.com', 1),
('bob@jones.com', '$2a$10$DTePBFdf2/Nd6K5GyFuXFev5pDr5/hC82NatIvsXALwg2N.Pm7Ti.', 'bob@jones.com', 1);


insert into auth(name) values
('USER'),
('ADMIN');

insert into checklist(checklist_id, checklist_name, total_completion, pokemon_id, user_id) values							
(1, 'chores', 50, 10, 3),
(2, 'errands', 75, 7, 3),
(3, 'goals', 0, 4, 3),
(4, 'health', 25, 10, 1),
(5, 'work', 100, 7, 1),
(6, 'test', 100, 4, 1);
-- (5, 'work', '100%', 3, 1);

insert into task(task_id, task_name, due_date, status, checklist_id) values
-- chores 50%
(1, 'dishes', '2024-01-01', 0, 1),
(2, 'laundry', '2024-01-01', 1, 1),
(3, 'homework', '2024-01-01', 1, 1),
(4, 'clean', null, 0, 1),

-- errands 75%
(5, 'grocery store', '2024-01-01', 1, 2),
(6, 'pharmacy', '2024-01-01', 1, 2),
(7, 'bank', null, 1, 2),
(8, 'walmart', '2024-01-01', 0, 2),

-- goals 0%
(9, 'Meet Tommy Wiseau', '2024-01-01', 0, 3),
(10, 'Eat a snowcone under the Eiffel Tower', null, 0, 3),
(11, 'Consume a bible', '2024-01-01', 0, 3),
(12, 'Create new show, billionaires gone wild, elon musk, bezos, and zuckerburg', null, 0, 3),

-- health 25%
(13, 'eat healthy', '2024-01-01', 1, 4),
(14, 'workout', '2024-01-01', 0, 4),
(15, 'do a marathon', '2024-01-01', 0, 4),
(16, 'cook at home', '2024-01-01', 0, 4),

-- work 100%
(17, 'tumble out of bed', null, 1, 5),
(18, 'stumble to the kitchen', null, 1, 5),
(19, 'pour myself a cup of ambition', null, 1, 5),
(20, 'yawn and stretch and try to come to life...', null, 1, 5);

insert into user_auth(user_id, auth_id) values
(1, 1),
(2, 2),
(3, 1);

insert into pokemon_user(pokemon_id, user_id) values
(2, 1),
(3, 1),
(4, 1);

end //
-- 4. Change the statement terminator back to the original.
delimiter ;

select * from user; 
-- left join user_auth on user.user_id = user_auth.user_id
-- inner join auth on user_auth.auth_id = auth.auth_id;




























