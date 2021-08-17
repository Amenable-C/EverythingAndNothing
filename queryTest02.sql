update students set student_gender="남자";
select * from students;
update students set student_gender="여자" where sid in(23,26,28,31,32);
select * from students;
select * from students where (school_name='남원' or school_name='조선') and student_age not between 10 and 19;

create table tests
(
sid int auto_increment,
student_name  varchar (20),
is_taken tinyint (1),
test_date date,
korean int unsigned,
english int unsigned,
math int unsigned,
history int unsigned,
primary key (sid)
);

select * from tests;
insert into tests (student_name, is_taken, test_date, korean, english,math, history)
values ('홍길동', 1, 20191211, 65, 83, 43, 100),
('허균', 0, 20191211, 0, 0, 0, 0),
('연산군', 0, 20191211, 0, 0, 0, 0),
('세종', 1, 20191211, 100, 67, 88, 100),
('홍상직', 1, 20191211, 67, 67, 60, 78),
('성춘향', 1, 20191211, 96, 100, 94, 90),
('이몽룡', 1, 20191211, 100, 96, 98, 100),
('방자', 1, 20191211, 80, 50, 40, 60),
('향단', 1, 20191211, 78, 77, 64, 40),
('변사또', 1, 20191211, 82, 88, 78, 100),
('심천', 1, 20191211, 93, 82, 84, 98),
('심학규', 0, 20191211, 0, 0, 0, 0),
('용왕', 1, 20191211, 100, 100, 100, 100),
('곽씨', 1, 20191211, 100, 88, 98, 100),
('뺑덕', 1, 20191211, 45, 33, 42, 40),
('흥부', 1, 20191211, 83, 79, 100, 90),
('놀부', 1, 20191211, 98, 100, 88, 94),
('현진건', 1, 20191211, 100, 96, 72, 100),
('염상섭', 1, 20191211, 100, 100, 71, 96),
('신채호', 1, 20191211, 100, 98, 100, 100);

select korean+10, english from tests;
select korean, english, korean+english from tests;
select student_name as Name, korean, english, korean+english as total from tests;
select student_name from tests where CHAR_LENGTH(student_name) <= 2;
select CONCAT(student_name,'학생의 수학성적은', math,'입니다.') from tests where sid=7;
select concat('국어평균 : ',avg(korean),'영어평균 : ',avg(english), '수학평균 : ',avg(math)) from tests;
select format(avg(korean),2) as korean_average from tests;
select count(student_name) from tests;
select * from tests;

select adddate(CURRENT_DATE(), interval 100 day);
select subdate(CURRENT_DATE(), interval 100 day);
select curdate(), date_format(curdate(), "%b, %D, %Y");

select student_name, math from tests
where math >= (select avg(math) from tests)
order by math desc;

desc students;
desc tests;

select * from students;
select * from tests;

select students.student_name, students.school_name, tests.math from students, tests where students.student_name=tests.student_name;
select students.student_name, students.school_name, tests.math from students, tests;
select students.student_name, students.school_name, tests.math from students, tests where (students.student_name=tests.student_name) and (tests.is_taken=1);
select students.student_name, students.school_name, tests.math from students join tests on students.student_name=tests.student_name and tests.is_taken=1;
create table tuitions
(
sid int auto_increment,
student_name varchar (20),
is_paid tinyint (1),
due_year int,
due_month int,
due_day int,
due_amount int,
memo varchar (100),
primary key (sid)
);
desc tuitions;

insert into tuitions (student_name, is_paid, due_year, due_month, due_day,due_amount, memo)
values ('홍길동', 1, 2019, 11, 1, 300000, ''),
('허균', 1, 2019, 11, 2, 300000, ''),
('연산군', 1, 2019, 11, 3, 150000, '교직원 할인'),
('세종', 1, 2019, 11, 3, 150000, '교직원 할인'),
('흥상직', 0, 2019, 11, 5, 300000, ''),
('성춘향', 0, 2019, 11, 11, 300000, ''),
('이몽룡', 1, 2019, 11, 15, 300000,''),
('변사또', 1, 2019, 11, 18, 300000, '상습체납자'),
('심청', 0, 2019, 11, 21, 150000, '사회보호대상자'),
('심학규', 1, 2019, 11, 21, 150000, '사회보호대상자'),
('놀부', 1, 2019, 11, 30, 300000,""),
('신채호', 1, 2019, 11, 30, 150000, '국가유공자');

select * from tuitions;
select students.student_name, tests.math, tuitions.memo 
from students inner join tests 
on students.student_name=tests.student_name and tests.is_taken=1
inner join tuitions
on tuitions.student_name=tests.student_name;
select * from tests;
select math, count(sid) from tests
GROUP BY math
order by math desc;
select school_name, count(sid) from students
GROUP BY school_name
having count(sid)>=5;