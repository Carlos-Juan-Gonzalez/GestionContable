drop database if exists gestorContableDB;
create database gestorContableDB;
use gestorContableDB;
create table if not exists permisos(id int primary key auto_increment,permiso varchar(10));
create table if not exists diarios(id int primary key auto_increment,nombre varchar(155));
create table if not exists usuarios(id int primary key auto_increment,permiso_id int not null,diario_id int not null,usuario varchar(155) not null,contraseña varchar(155) not null,constraint fk_usuario_permiso foreign key (permiso_id) references permisos(id) on update cascade on delete no action,constraint fk_usuario_diario foreign key (diario_id) references diarios(id) on update cascade on delete no action);
create table if not exists asientos(id int primary key auto_increment,diario_id int,numero int,fecha varchar(155),descripcion varchar(255),constraint fk_asiento_diario foreign key (diario_id) references diarios(id) on update cascade on delete cascade);
create table if not exists cuentas(id int primary key auto_increment,diario_id int,codigo int,nombre varchar(155),constraint fk_cuenta_diario foreign key (diario_id) references diarios(id) on update cascade on delete cascade);
create table if not exists anotaciones(id int primary key auto_increment,cuenta_id int,asiento_id int,orden int,debe int,haber int,constraint fk_anotacion_cuenta foreign key (cuenta_id) references cuentas(id) on update cascade on delete cascade,constraint fk_anotacion_asiento foreign key (asiento_id) references asientos(id) on update cascade on delete cascade);
insert into permisos (permiso) values ('admin');
insert into permisos (permiso) values ('usuario');
insert into permisos (permiso) values ('invitado');
insert into diarios (nombre) values ('prueba');
insert into usuarios (permiso_id,diario_id,usuario,contraseña) values (1,1,'admin','');
insert into usuarios (permiso_id,diario_id,usuario,contraseña) values (3,1,'invitado','111')