import sqlite3
class DAO:

    def __init__(self,dto_type, conn):
        self.conn = conn
        self.dto_type = dto_type
        self.table_name = f'{dto_type.__name__.lower()}s'


    def insert(self,dto_instance):
        ins_dict = vars(dto_instance)
        columns_names = ','.join(ins_dict.keys())
        params = ins_dict.values()
        qmarks = ",".join(['?']*len(ins_dict))

        stmt = f'''INSERT INTO{self.table_name} ({columns_names}VALUES({qmarks}))'''
        self.conn.execute(stmt, params)




    def update(self,new_values, conditions):
        column_names = list(new_values.keys())
        new_params = list(new_values.values())
        cond_cols = list(conditions.keys())
        cond_values = list(conditions.values())

        stmt = f'''UPDATE {self.table_name}SET{','.join([col + '= ?' for col in column_names])} WHERE {' AND '.join([col + '= ?' for col in cond_cols])}'''
        self.conn.execute(stmt,new_params+cond_values)




    def delete(self,**kwargs):
        column_name = list(kwargs.keys())
        params = list(kwargs.values)

        stmt = f'''DELETE FROM{self.table_name}WHERE{' AND '.join([col+ '=?' for col in column_name])}'''
        self.conn.execute(stmt,prams)




    def find_first(self, **kwargs):
        column_names = list(kwargs.keys())
        params = list(kwargs.values())

        stmt = f'''SELECT * FROM{self.table_name}WHERE{' AND '.join([col + '=?' for col in column_name])}ORDER BY id ASC LIMIT 1'''
        c = self.conn.cursor()
        c.execute(stmt, params)
        # TODO: add orm to return
        return c

    def find_all(self, **kwargs):
        column_names = list(kwargs.keys())
        params = list(kwargs.values())

        stmt = f'''SELECT * FROM{self.table_name} WHERE{' AND '.join([col + '=?' for col in column_names])}'''
        c = self.conn.cursor()
        c.execute(stmt, params)




if __name__ == "__main__":
    conn = sqlite3.connect("test.db")
