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
            supplier    INT    ,
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

    def insert_all(self):

        hat = Hat(1,"mushroom",2,25)
        supplier = Supplier(1,"tom")
        order = Order(1,"lehavim", 1)

        self.suppliers.insert(supplier)
        self.hats.insert(hat)
        self.orders.insert(order)
        print("inserted all")

    def get_orders(self):
        orders = [{"id":1}]
        with open('output.txt', 'a') as the_file:
            for idx,order in enumerate(orders):
                topping = order["topping"]
                location = order["location"]
                hat = self.hats.find_first(topping=topping)[0]
                supplier = self.suppliers.find_first(hat=hat[0])
                self.orders.insert(idx,location,hat[0])
                if hat[3] == 1:
                    self.hats.delete(id=hat[0])
                else:
                    self.hats.update({"quantity" : hat[3]-1}, {"id":hat[0]})
                # add to file:
                line=f"{topping},{supplier[1]},{location}\n"
                the_file.write(line)
                


