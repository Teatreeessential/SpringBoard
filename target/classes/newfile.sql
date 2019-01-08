
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

