use mydb;
create table students
(
id int auto_increment,
student_name varchar(20) not null,
school_name varchar(20) not null,
school_level varchar(10) not null,
school_grade tinyint(1),
is_admin tinyint(1),
primary key(id)
);
insert into students (student_name, school_name, school_level, school_grade, is_admin) 
values ('홍길동', '활빈', '고등학교', 2, 0),
( '허균', '활빈', '고등학교', 2, 0),
('연산군', '조선', '고등학교', 3, 0),
('세종', '조선', '고등학교', 3, 1),
('홍상직', '조선', '고등학교', 2, 0),
('성춘향', '남원', '중학교', 3, 0),
('이몽룡', '남원', '고등학교', 1, 0),
('방자', '남원', '중학교', 2, 0),
('향단', '남원', '중학교', 2, 0),
('변사또', '남원', '고등학교', 3, 0),
('심청', '도화', '중학교', 2, 0),
('심학규', '도화', '고등학교', 3, 0),
('용왕', '도화', '고등학교', 3, 0),
('곽씨', '도화', '고등학교', 2, 0),
('뺑덕', '도화', '고등학교', 1, 0),
('흥부', '남원', '고등학교', 2, 0),
('놀부', '남원', '고등학교', 3, 0);