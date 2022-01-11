class DAO:


    def __init__(self,dto_type, conn):
        self.conn = conn
        self.dto_type = dto_type
        self.table_name = f'{dto_type.__name__.lower()}s'


    def insert(self,dto_instance):
        ins_dict = vars(dto_instance)
        columns_names = ','.join(ins_dict.keys())
        params = ins_dict.values()
        qmarks = ",".join()



    def update(self):
        pass

    def delete(self):
        pass

    def find_first(self):
        pass

    def find_all(self):
        pass



