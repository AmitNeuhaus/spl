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

    def get_orders(self, file_path):
        file = open(file_path)
        orders = []
        lines = file.readlines()
        for line in lines:
            if line:
                splited_line= line.split(',')
                orders.append({ "location":splited_line[0], "topping":splited_line[1]})
        return orders


    def get_hats_and_suppliers(self, file_path):
        file = open(file_path)
        hats = []
        suppliers = []
        lines = file.readlines(file)
        first_line = line[0].split(',')
        hats_number = first_line[0]
        for i,line in enumerate(lines[1:]):
            splited_line = line.split(',')
            if i<hats_number:
                hats.append({'id':splited_line[0], "topping":splited_line[1], "supplier":splited_line[2], "quantity":splited_line[3]})
            else:
                suppliers.append({"id":splited_line[0], "name":splited_line[1]})

        return hats, suppliers


    def insert_all(self):
        hats, suppliers = self.get_hats_and_suppliers("config.txt")
        for hat in hats:
            hat_obj = Hat(hat["id"], hat["topping"],hat["supplier"], hat["quantity"])
            self.hats.insert(hat_obj)
        for supplier in suppliers:
            supplier_obj = Supplier(supplier["id"], supplier["name"])
            self.suppliers.insert(supplier_obj)

    def output_orders(self):
        orders = self.get_orders("orders.txt")
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
                


