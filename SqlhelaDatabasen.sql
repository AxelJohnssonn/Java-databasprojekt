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
ordNbr		int auto_increment,
customer varchar(100) not null,
primary key (ordNbr),
foreign key (customer) references Customers(customer)
);
create table Pallet(
palletNbr	int auto_increment,
palletDate	date,
blocked 	boolean,
dDate 		date,
ordNbr    	int auto_increment,
location 	varchar(100) not null,
cName		varchar(100) not null,
primary key (palletNbr),
foreign key (cName) references Cookie(cName),
foreign key (ordNbr) references Orders(ordNbr)
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


INSERT INTO Customers(customer, address, teleNbr) VALUES
			('Finkakor AB', 'Helsingborg', '07696969'),
			('Småbröd AB', 'Malmö', '07696969'),
			('Kaffebröd AB', 'Landskrona', '07696969'),
			('Bjudkakor AB', 'Ystad', '07696969'),
			('Kalaskakor AB', 'Trelleborg', '07696969'),
			('Partykakor AB', 'Kristianstad', '07696969'),
			('Gästkakor AB', 'Hässleholm', '07696969'),
			('Skånekakor AB', 'Perstorp', '07696969');

INSERT INTO Ingredients(ingredientName, stockAmount, buyDate, unit) VALUES
             ('Bread crumbs', 500000,'2022-05-03', 'g'),
             ('Butter', 500000,'2022-05-03', 'g'),
             ('Chocolate', 500000,'2022-05-03', 'g'),
            ('Chopped almonds',500000, '2022-05-03', 'g'),
            ('Cinnamon', 500000,'2022-05-03', 'g'),
            ('Egg whites', 500000,'2022-05-03', 'ml'),
            ('Eggs', 500000,'2022-05-03', 'g'),
            ('Fine-ground nuts', 500000,'2022-05-03', 'g'),
            ('Flour', 500000,'2022-05-03', 'g'),
            ('Ground, roasted nuts', 500000,'2022-05-03', 'g'),
            ('Icing sugar', 500000,'2022-05-03', 'g'),
            ('Marzipan', 500000,'2022-05-03', 'g'),
            ('Potato starch', 500000,'2022-05-03', 'g'),
            ('Roasted, chopped nuts', 500000,'2022-05-03', 'g'),
            ('Sodium bicarbonate', 500000,'2022-05-03', 'g'),
            ('Sugar', 500000,'2022-05-03', 'g'),
            ('Vanilla sugar', 500000,'2022-05-03', 'g'),
            ('Vanilla', 500000,'2022-05-03', 'g'),
            ('Wheat flour', 500000,'2022-05-03', 'g') 
            ;
INSERT INTO Cookie(cName) VALUES
            ('Amneris'),
            ('Berliner'),
            ('Nut cookie'),
            ('Nut ring'),
            ('Tango'),
            ('Almond delight');
INSERT INTO Contain (cName, ingredientName, rAmount) VALUES
            ('Almond delight', 'Butter', 400),('Almond delight', 'Chopped almonds', 279),('Almond delight', 'Cinnamon', 10),('Almond delight', 'Flour', 400),('Almond delight', 'Sugar', 270),
            ('Amneris', 'Butter', 250),('Amneris', 'Eggs', 250),('Amneris', 'Marzipan', 750),('Amneris', 'Potato starch', 25),('Amneris', 'Wheat flour', 25),
            ('Berliner', 'Butter', 250),('Berliner', 'Chocolate', 50),('Berliner', 'Eggs', 50),('Berliner', 'Flour', 350),('Berliner', 'Icing sugar', 100),('Berliner', 'Vanilla sugar', 5),
            ('Nut cookie', 'Bread crumbs', 125),('Nut cookie', 'Chocolate', 50),('Nut cookie', 'Egg whites', 350),('Nut cookie', 'Fine-ground nuts', 750),('Nut cookie', 'Ground, roasted nuts', 625),('Nut cookie', 'Sugar', 375),
            ('Nut ring', 'Butter', 450),('Nut ring', 'Flour', 450),('Nut ring', 'Icing sugar', 190),('Nut ring', 'Roasted, chopped nuts', 225),
            ('Tango', 'Butter', 200),('Tango', 'Flour', 300),('Tango', 'Sodium bicarbonate', 4),('Tango', 'Sugar', 250),('Tango', 'Vanilla', 2)
            ;
