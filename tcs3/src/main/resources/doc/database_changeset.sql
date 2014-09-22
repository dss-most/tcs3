create TABLE app_info(
	PROPERTY varchar2(100) not null,
	VALUE varchar2(100),
	primary key(PROPERTY)
);

insert into app_info VALUES ('DATABASE_VERSION', '1');

create SEQUENCE QUOTATION_TEMPLATE_SEQ;

create sequence TEST_METHOD_QT_ITEM_SEQ;

create TABLE TEST_METHOD_QT_ITEM (
        id number(19,0) not null,
		TESTMETHOD_INDEX number(19,0),
    name varchar2(500),
		fee FLOAT(126),
		remark varchar2(500),
		quantity number(4),
		QTEMPLATE_ID number(19,0),
		TEST_METHOD_ID number(19,0),
		primary key(id),
		CONSTRAINT fk_testMethodQT_QT FOREIGN KEY (QTEMPLATE_ID) REFERENCES QUOTATION_TEMPLATE(ID),
		CONSTRAINT fk_testMethodQT_TestMethod FOREIGN KEY (TEST_METHOD_ID) REFERENCES TEST_METHOD(TEST_METHOD_ID)
);

