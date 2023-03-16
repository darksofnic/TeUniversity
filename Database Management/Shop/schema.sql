CREATE TABLE Products (pid int PRIMARY KEY,
                       name VARCHAR(250),
                       price int);

CREATE TABLE Customers (cid int PRIMARY KEY,
                        name VARCHAR(250),
                        budget int);

CREATE TABLE Sales (pid int,
                    cid int,
                    quantity int NOT NULL CHECK (quantity >= 0),
                    FOREIGN KEY (pid) REFERENCES Products,
                    FOREIGN KEY (cid) REFERENCES Customers);


insert into Products values (1, 'Laptop', 899.99);
insert into Products values (2, 'Iphone 13', 999.99);
insert into Products values (3, '12 Pack Soda Can', 9.99);
insert into Products values (4, 'Popcorn', 5.99);

insert into Customers values (5, 'Marco', 15500);
insert into Customers values (6, 'Polo', 1400);
insert into Customers values (7, 'Elon', 133300);
insert into Customers values (8, 'Musk', 122200);