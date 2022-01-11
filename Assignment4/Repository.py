import sqlite3
from DAO import DAO
from DTO.Hat import Hat
from DTO.Supplier import Supplier
from DTO.Order import Order



class Repository():
    def __init__(self):
        self._conn = sqlite3.connect("PizzaHat.db")
        self.hats = DAO(Hat,self._conn)
        self.suppliers = DAO(Supplier,self._conn)
        self.orders = DAO(Order,self._conn)



    def close(self):
        self._conn.commit()
        self._conn.close()


    def create_tables(self):
        self._conn.executescript(
        '''
        CREATE TABLE hats (
            id          INT     PRIMARY KEY,
            topping     TEXT    NOT NULL,
            supplier    INT    
            quantity    INT     NOT NULL,

            FOREIGN KEY(supplier)     REFERENCES suppliers(id)

        );
        
        CREATE TABLE suppliers (
            id      INT     PRIMARY KEY,
            name    TEXT    NOT NULL
        );

        CREATE TABLE orders (
            id          INT     PRIMARY KEY,
            location    TEXT    NOT NULL,
            hat         INT,     

            FOREIGN KEY(hat)     REFERENCES hats(id)
        );
        
        
        '''
        )


