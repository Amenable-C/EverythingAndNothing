select student_name, school_level from students;
select DISTINCT school_name from students;
select school_name from students limit 5;
select DISTINCT school_name from students limit 1, 2;
select * from students order by school_grade;
select * from students order by school_grade desc;
select * from students order by school_grade, school_level;
select * from students where school_level='고등학교';
select * from students where school_grade=2;
select * from students where school_level='고등학교' and school_grade=2;
select student_name from students where school_level='고등학교' and school_grade=2;
select * from students where school_name='도화' or school_name='활빈';
ALTER TABLE students ADD student_gender varchar(1);
alter table students drop student_gender;
ALTER TABLE students ADD student_gender varchar(1);
alter table students modify column student_gender varchar(2);
alter table students change column id std_num int auto_increment;
alter table students change column std_num st_num int auto_increment;
select * from students;
update students set student_gender='남' where std_num=1;
update students set student_gender='남';
update students set student_gender='여' where st_num=6 or st_num=9 or st_num=11 or st_num=14 or st_num=15;
delete from students where st_num=15;

alter table students add student_age int;
alter table students add is_enrolled tinyint;
alter table students add enrolled_date date;
select * from students;

delete from students;
insert into students (student_name, school_name, school_level, school_grade, student_gender, student_age, is_enrolled, enrolled_date)
values ('홍길동', '활빈', '고등학교', 2, '남자', 16, 1, 20180201),
('허균', '활빈', '고등학교', 2, '남자', 32, 1, 20180201),
('연산군', '조선', '고등학교', 3, '남자', 36, 0, 20170528),
('세종', '조선', '고등학교', 3, '남자', 42, 1, 20170601),
('홍상직', '조선', '고등학교', 2, '남자', 40, 0, 20180504),
('성춘향', '남원', '중학교', 3, '여자', 15, 1, 20190216),
('이몽룡', '남원', '고등학교', 1, '남자', 17, 1, 20180415),
('방자', '남원', '중학교', 2, '남자', 18, 1, 20181121),
('향단', '남원', '중학교', 2, '여자', 16, 1, 20190303),
('변사또', '남원', '고등학교', 3, '남자', 38, 1, 20170119),
('심청', '도화', '중학교', 2, '여자', 15, 1, 20190201),
('심학규', '도화', '고등학교', 3, '남자', 33, 1, 20180407),
('용왕', '도화', '고등학교', 3, '남자', 299, 0, 20170804),
('곽씨', '도화', '고등학교', 2, '여자', 31, 1, 20180529),
('뺑덕', '도화', '고등학교', 1, '여자', 33, 1, 20180912),
('흥부', '남원', '고등학교', 2, '남자', 35, 1, 20170611),
('놀부', '남원', '고등학교', 3, '남자', 36, 1, 20170611);
select student_name from students where student_name like '%부';
select student_name from students where student_name not like '%부';

select school_name from students where school_name not like '도화' and school_name not like '남원';
select * from students where student_age >= 31;
select * from students;