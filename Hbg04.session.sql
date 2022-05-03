set FOREIGN_KEY_CHECKS = 0;
-- Drop the tables if they already exist.
drop table if exists Customers;
drop table if exists Orders;
drop table if exists Pallet;
drop table if exists Cookie;
drop table if exists Amount;
drop table if exists Contain;
drop table if exists Ingredients;

create table Customers(
customer	varchar(100) not null,
address		varchar(100) not null,
teleNbr		int not null,
primary key (customer)
);
create table Orders(
ordNbr		int not null,
customer varchar(100) not null,
primary key (ordNbr),
foreign key (customer) references Customers(customer)
);
create table Pallet(
palletNbr	int not null,
palletDate	date,
blocked 	boolean,
dDate 		date,
location 	varchar(100) not null,
cName		varchar(100) not null,
primary key (palletNbr),
foreign key (cName) references Cookie(cName)
);
create table Cookie(
cName 		varchar(100),
primary key (cName)
);
create table Amount(
ordNbr		int not null,
cName		varchar(100) not null,
amountOfPallets int,
primary key (ordNbr, cName),
foreign key (ordNbr) references Orders(ordNbr),
foreign key (cName) references Cookie(cName)
);
create table Contain(
cName		varchar(100) not null,
ingredientName varchar(100) not null,
rAmount		int,
primary key (cName, ingredientName),
foreign key(cName) references Cookie(cName),
foreign key (ingredientName) references Ingredients(ingredientName)
);
create table Ingredients(
ingredientName	varchar(100) not null,
stockAmount		int,
buyDate 		date,
unit			varchar(10),
primary key(ingredientName)
);
set FOREIGN_KEY_CHECKS = 1;