drop database if exists gestorContableDB;
create database gestorContableDB;
use gestorContableDB;
create table if not exists permisos(id int primary key auto_increment,permiso varchar(10));
create table if not exists diarios(id int primary key auto_increment,nombre varchar(155));
create table if not exists usuarios(id int primary key auto_increment,permiso_id int not null,diario_id int not null,usuario varchar(155) not null,contraseña varchar(155) not null,constraint fk_usuario_permiso foreign key (permiso_id) references permisos(id) on update cascade on delete no action,constraint fk_usuario_diario foreign key (diario_id) references diarios(id) on update cascade on delete no action);
insert into permisos (permiso) values ('superAdmin');
insert into permisos (permiso) values ('admin');
insert into permisos (permiso) values ('usuario');
insert into permisos (permiso) values ('invitado');
insert into diarios (nombre) values ('pruebaController');
insert into usuarios (permiso_id,diario_id,usuario,contraseña) values (1,1,'superAdmin','superAdministrador')