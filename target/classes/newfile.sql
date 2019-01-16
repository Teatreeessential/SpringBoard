
/* Drop Tables */

DROP TABLE tbl_friend CASCADE CONSTRAINTS;




/* Create Tables */

CREATE TABLE tbl_friend
(
	userid1 varchar2(50),
	userid2 varchar2(50),
	CONSTRAINT tbl_friend_pk primary key (userid1, userid2),
	constraint tbl_friend_fk foreign key(userid1) references tbl_member(userid) on delete cascade
);


alter table tbl_friend add (accept number(1))

create sequence chat_room_seq
start with 1
increment by 1
maxvalue 100000
minvalue 1
nocache;


drop sequence chat_room_seq;

create table tbl_test(
	num number(1,5)
);


select chat_room_seq.nextval from dual;

select chat_room_seq.currval from dual;


CREATE TABLE chatroom
(
	chat_room_num number(5),
	userid varchar2(50),
	message varchar2(1000),
	message_date date
);

