import sqlite3
db = sqlite3.connect('db.sqlite3')
cursor = db.cursor()
# CREATE #
##########
# cursor = db.cursor()
# cursor.execute('''
#     CREATE TABLE IF NOT EXISTS users(
#         id INTEGER PRIMARY KEY,
#         name TEXT,
#         phone TEXT,
#         email TEXT,
#         password TEXT
#     )
# ''')


# users = [('a','1', 'a@b.com', 'a1'),
#          ('b','2', 'b@b.com', 'b1'),
#          ('c','3', 'c@b.com', 'c1'),
#          ('c','3', 'c@b.com', 'c1')]
# cursor.executemany(''' INSERT INTO users(name, phone, email, password) VALUES(?,?,?,?)''', users)

# cursor.execute('''SELECT name, email, phone FROM users''')
# for row in cursor:
#     print('{0} : {1}, {2}'.format(row[0], row[1], row[2]))

# cursor.execute('''SELECT name, email, phone FROM users''')
# for row in cursor:
#     print('{0} : {1}, {2}'.format(row[0], row[1], row[2]))

# newphone = '3113093164'
# userid = 1
# cursor.execute('''UPDATE users SET phone = ? WHERE id = ? ''', (newphone, userid))

delete_userid = 2
cursor.execute('''DELETE FROM users WHERE id = ? ''', (delete_userid,))
db.commit() #변경되는 명령어일때 commit하기

cursor.execute('''SELECT name, email, phone FROM users''')
for row in cursor:
    print('{0} : {1}, {2}'.format(row[0], row[1], row[2]))


