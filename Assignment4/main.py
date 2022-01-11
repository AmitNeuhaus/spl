#main.py

from Repository import Repository
import atexit

def start_program():
    repo = Repository()
    repo.create_tables()
    repo.insert_all()
    repo.output_orders()
    atexit.register(repo.close)

start_program()