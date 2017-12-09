CREATE TABLE SPRING_SESSION (
	SESSION_ID CHAR(36) NOT NULL,
	CREATION_TIME BIGINT NOT NULL,
	LAST_ACCESS_TIME BIGINT NOT NULL,
	MAX_INACTIVE_INTERVAL INT NOT NULL,
	PRINCIPAL_NAME VARCHAR(100),
	CONSTRAINT SPRING_SESSION_PK PRIMARY KEY (SESSION_ID)
);

CREATE INDEX SPRING_SESSION_IX1 ON SPRING_SESSION (LAST_ACCESS_TIME);

CREATE TABLE SPRING_SESSION_ATTRIBUTES (
	SESSION_ID CHAR(36) NOT NULL,
	ATTRIBUTE_NAME VARCHAR(200) NOT NULL,
	ATTRIBUTE_BYTES LONGVARBINARY NOT NULL,
	CONSTRAINT SPRING_SESSION_ATTRIBUTES_PK PRIMARY KEY (SESSION_ID, ATTRIBUTE_NAME),
	CONSTRAINT SPRING_SESSION_ATTRIBUTES_FK FOREIGN KEY (SESSION_ID) REFERENCES SPRING_SESSION(SESSION_ID) ON DELETE CASCADE
);

CREATE INDEX SPRING_SESSION_ATTRIBUTES_IX1 ON SPRING_SESSION_ATTRIBUTES (SESSION_ID);

create table T_ROLE
(
  ID BIGINT primary key,
  ROLE_NAME varchar(100) not null,
  DESCRIPTION varchar(100) not null
)
;

create table T_USER
(
  ID BIGINT primary key,
  LOGIN_ID VARCHAR(20) not null,
  PASSWORD VARCHAR(200) not null,
  FULL_NAME VARCHAR(100) not null,
  DEPT_NAME VARCHAR(100)
)
;

create table T_USER_ROLE
(
  USER_ID BIGINT,
  ROLE_ID BIGINT,
  primary key(USER_ID, ROLE_ID)
)
;

insert into T_ROLE values(1, 'ROLE_USER', '一般ロール');
insert into T_ROLE values(2, 'ROLE_ADMIN', '管理ロール');

insert into T_USER values(1, 'user', '$2a$10$nodSZsM7Lyi34l3/YEg61uTVDRIgH.DkM/WF4AM0BKTCGINBOnFOm', '一般 太郎', '開発部');
insert into T_USER values(2, 'admin', '$2a$10$0VRu6ZC4zcuXZiS34AaPF.gDcbq25Z5lX01gnf97iBNdl4WeCbCXC', '管理 次郎', '管理部');


insert into T_USER_ROLE values(1, 1);
insert into T_USER_ROLE values(2, 2);
