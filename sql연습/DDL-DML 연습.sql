-- DDL/DML 연습

drop table member;
create table member(
	no int not null auto_increment,
    email varchar(200) not null default '',
    password varchar(64) not null,
    name varchar(50) not null,
    department varchar(100),    
    primary key(no)
);
desc member;

alter table member add column juminbunho char(13) not null;

alter table member drop column juminbunho;

alter table member add column juminbunho char(13) not null after email;

alter table member change column department dept varchar(100) not null;
desc member;

alter table member add column self_intro text;
desc member;

alter table member drop juminbunho;
desc member;

-- insert
insert 
	into member
    values(null, 'kickscar@gmail.com', password('1234'),'신친규','개발팀');

insert 
	into member(no,email,name,dept,password)
    values (null,'kickscar@gmail.com', '안대혁2','개발팀2',password('1234'));
select * from member;

-- update


-- delete
delete
	from member
where no = 2;
select * from member;

-- transaction
select no, email from member;

select @@autocommit; -- 1
insert 
	into member(no,email,name,dept,password)
    values (null,'kickscar@gmail.com', '안대혁2','개발팀2',password('1234'));
select no, email from member;

-- tx begin
set autocommit = 0;
select @@autocommit; -- 0

insert 
	into member(no,email,name,dept,password)
    values (null,'kicksca3r@gmail.com', '안대혁3','개발팀3',password('1234'));
select no, email from member;

-- tx end
commit;
select no, email from member;