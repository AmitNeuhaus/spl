#main.py

from Repository import Repository
import atexit
import sys

def start_program(args):
    print(args)
    config_file, orders_file, output_file, db_name = args
    repo = Repository(db_name)
    repo.create_tables()
    repo.insert_all(config_file)
    repo.output_orders(orders_file,output_file)
    atexit.register(repo.close)

if __name__ == "__main__":
   start_program(sys.argv[1:])