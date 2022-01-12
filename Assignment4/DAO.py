import sqlite3
class DAO:

    def __init__(self,dto_type, conn):
        self.conn = conn
        self.dto_type = dto_type
        self.table_name = dto_type.__name__.lower()+ "s"


    def insert(self,dto_instance):
        ins_dict = vars(dto_instance)
        columns_names = ','.join(ins_dict.keys())
        params = list(ins_dict.values())
        qmarks = ",".join(['?']*len(ins_dict))

        stmt = f'''INSERT INTO {self.table_name} ({columns_names}) VALUES ({qmarks})'''
        self.conn.execute(stmt, params)




    def update(self,new_values, conditions):
        column_names = list(new_values.keys())
        new_params = list(new_values.values())
        cond_cols = list(conditions.keys())
        cond_values = list(conditions.values())

        stmt = f'''UPDATE {self.table_name} SET {','.join([col + '= ?' for col in column_names])} WHERE {' AND '.join([col + '= ?' for col in cond_cols])}'''
        self.conn.execute(stmt,new_params+cond_values)




    def delete(self,**kwargs):
        column_name = list(kwargs.keys())
        params = list(kwargs.values())

        stmt = f'''DELETE FROM {self.table_name} WHERE {' AND '.join([col+ '=?' for col in column_name])}'''
        self.conn.execute(stmt,params)



    def find_first(self, order_by=False, join=False, **kwargs):
        join = f"JOIN {join}" if join else ""
        order_by = f"ORDER BY {order_by} ASC" if order_by else ""

        column_names = list(kwargs.keys())
        params = list(kwargs.values())

        stmt = f'''SELECT * FROM {self.table_name} {join} WHERE {' AND '.join([col + '=?' for col in column_names])} {order_by} LIMIT 1 '''
        c = self.conn.cursor()
        c.execute(stmt, params)
        # TODO: add orm to return
        return c

    def find_all(self, join=False, **kwargs):
        if not join:
            join_on = ""
        else:
            join_on = f"JOIN {join}"
        column_names = list(kwargs.keys())
        params = list(kwargs.values())

        stmt = f'''SELECT * FROM {self.table_name} WHERE {' AND '.join([col + '=?' for col in column_names])} {join_on}'''
        c = self.conn.cursor()
        c.execute(stmt, params)
        return c




if __name__ == "__main__":
    conn = sqlite3.connect("test.db")
