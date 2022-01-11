#main.py

from Repository import Repository
import atexit

def start_program():
    print("started")
    repo = Repository()
    repo.create_tables()
    repo.insert_all()
    atexit.register(repo.close)

start_program()