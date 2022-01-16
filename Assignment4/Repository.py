import sqlite3
from DAO import DAO
from DTO.Hat import Hat
from DTO.Supplier import Supplier
from DTO.Order import Order



class Repository():
    def __init__(self, db_name):
        self._conn = sqlite3.connect(db_name)
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
            line = line.rstrip("\n")
            if line:
                splited_line= line.split(',')
                orders.append({ "location":splited_line[0], "topping":splited_line[1]})
        return orders


    def get_hats_and_suppliers(self, file_path):
        file = open(file_path)
        hats = []
        suppliers = []
        lines = file.readlines()
        first_line = lines[0].rstrip("\n")
        first_line = first_line.split(',')
        hats_number = int(first_line[0])
        for i,line in enumerate(lines[1:]):
            line = line.rstrip("\n")
            if line:
                splited_line = line.split(',')
                if i<hats_number:
                    hats.append({'id':int(splited_line[0]), "topping":splited_line[1], "supplier":int(splited_line[2]), "quantity":int(splited_line[3])})
                else:
                    suppliers.append({"id":int(splited_line[0]), "name":splited_line[1]})

        return hats, suppliers


    def insert_all(self,config_file):
        hats, suppliers = self.get_hats_and_suppliers(config_file)
        for hat in hats:
            hat_obj = Hat(hat["id"], hat["topping"],hat["supplier"], hat["quantity"])
            self.hats.insert(hat_obj)
        for supplier in suppliers:
            supplier_obj = Supplier(supplier["id"], supplier["name"])
            self.suppliers.insert(supplier_obj)


    def output_orders(self,orders_file,output_file):
        orders = self.get_orders(orders_file)
        with open(output_file, 'w') as the_file:
            for idx,order in enumerate(orders):
                topping = order["topping"]
                location = order["location"]
                hat = self.hats.find_first(topping=topping,order_by="supplier").fetchone()
                first_supplier = self.suppliers.find_first(id=hat[2]).fetchone()
                self.orders.insert(Order(idx+1,location,hat[0]))
                if hat[3] == 1:
                    self.hats.delete(id=hat[0])
                else:
                    self.hats.update({"quantity" : hat[3]-1}, {"id":hat[0]})
                # add to file:
                line=f"{topping},{first_supplier[1]},{location}\n"
                the_file.write(line)

