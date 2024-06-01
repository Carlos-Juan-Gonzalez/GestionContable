use gestorContableDB;
insert into cuentas (diario_id,codigo,nombre) values (1,210,'terrenos y bienes naturales');
insert into cuentas (diario_id,codigo,nombre) values (1,211,'construcciones');
insert into cuentas (diario_id,codigo,nombre) values (1,213,'maquinaria');
insert into cuentas (diario_id,codigo,nombre) values (1,216,'mobiliario');
insert into cuentas (diario_id,codigo,nombre) values (1,300,'mercaderias');
insert into cuentas (diario_id,codigo,nombre) values (1,32,'otros aprovisionamientos');
insert into cuentas (diario_id,codigo,nombre) values (1,326,'envases');
insert into cuentas (diario_id,codigo,nombre) values (1,327,'enbalajes');
insert into cuentas (diario_id,codigo,nombre) values (1,400,'proveedores');
insert into cuentas (diario_id,codigo,nombre) values (1,401,'proveedores, efectos comerciales a pagar');
insert into cuentas (diario_id,codigo,nombre) values (1,430,'clientes');
insert into cuentas (diario_id,codigo,nombre) values (1,431,'clientes, efectos comerciales a cobrar');
insert into cuentas (diario_id,codigo,nombre) values (1,472,'HP,iva soportado');
insert into cuentas (diario_id,codigo,nombre) values (1,477,'HP,iva devengado');
insert into cuentas (diario_id,codigo,nombre) values (1,572,'bancos');
insert into cuentas (diario_id,codigo,nombre) values (1,621,'arrendamiento y canones');
insert into cuentas (diario_id,codigo,nombre) values (1,628,'suministros');
insert into cuentas (diario_id,codigo,nombre) values (1,640,'sueldos y salarios');
insert into asientos (diario_id,numero,fecha,descripcion) value (1,1,'2024-05-28','compra de mobiliario');
insert  into anotaciones (cuenta_id, asiento_id,orden,debe, haber) VALUES (4,1,1,500,null);
insert  into anotaciones (cuenta_id, asiento_id,orden, debe, haber) VALUES (13,1,2,105,null);
insert  into anotaciones (cuenta_id, asiento_id,orden, debe, haber) VALUES (15,1,3,null,605);
insert into asientos (diario_id,numero,fecha,descripcion) value (1,2,'2024-05-28','venta de mercaderias');
insert  into anotaciones (cuenta_id, asiento_id,orden, debe, haber) VALUES (11,2,1,2000,null);
insert  into anotaciones (cuenta_id, asiento_id,orden, debe, haber) VALUES (5,2,2,null,2000);
insert into asientos (diario_id,numero,fecha,descripcion) value (1,3,'2024-05-28','compra de embalajes');
insert  into anotaciones (cuenta_id, asiento_id,orden, debe, haber) VALUES (8,3,1,450,null);
insert  into anotaciones (cuenta_id, asiento_id,orden, debe, haber) VALUES (9,3,2,null,450);
insert into asientos (diario_id,numero,fecha,descripcion) value (1,4,'2024-05-28','suministros');
insert  into anotaciones (cuenta_id, asiento_id,orden, debe, haber) VALUES (17,4,1,472,null);
insert  into anotaciones (cuenta_id, asiento_id,orden, debe, haber) VALUES (15,4,2,null,472);
insert into asientos (diario_id,numero,fecha,descripcion) value (1,5,'2024-05-28','alquiler junio');
insert  into anotaciones (cuenta_id, asiento_id,orden, debe, haber) VALUES (16,5,1,1220,null);
insert  into anotaciones (cuenta_id, asiento_id,orden, debe, haber) VALUES (15,5,2,null,1220);