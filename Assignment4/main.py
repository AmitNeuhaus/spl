import sqlite3
from sqlite3.dbapi2 import connect

connection = sqlite3.connect("PizzaHat.db")
c=connection.cursor()