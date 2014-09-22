create SEQUENCE QUOTATION_TEMPLATE_SEQ;


create TEST_METHOD_QT_ITEM_SEQ;

create TABLE TEST_METHOD_QT_ITEM (
        id number(19,0) not null,
		TESTMETHOD_INDEX number(19,0) not null,
		fee FLOAT(126),
		remark varchar2(500),
		quantity number(4),
		QTEMPLATE_ID number(19,0),
		TEST_METHOD_ID number(19,0),
		primary key(id),
		CONSTRAINT fk_testMethodQT_QT FOREIGN KEY (QTEMPLATE_ID) REFERENCES QUOTATION_TEMPLATE(ID),
		CONSTRAINT fk_testMethodQT_TestMethod FOREIGN KEY (TESTMETHOD_ID) REFERENCES TEST_METHOD(TEST_METHOD_ID),
);
