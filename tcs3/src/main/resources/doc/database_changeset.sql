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

alter table ADDRESS add(
	line1 varchar2(500),
	line2 varchar2(500)
);

-- should initially set to 580000
create sequence COMPANY_TCS3_SEQ start with 580000;

create sequence ADDRESS_TCS3_SEQ start with 580000;

create TABLE COMPANY_ADDRESSES (
	COMPANY_ID number(19,0),
	ADDRESS_ID number(19,0),
	primary key(COMPANY_ID, ADDRESS_ID),
	CONSTRAINT fk_COMPANY FOREIGN KEY (COMPANY_ID) REFERENCES COMPANIES(COMPANY_ID),
	CONSTRAINT fk_ADDRESS FOREIGN KEY (ADDRESS_ID) REFERENCES ADDRESS(ADDRESS_ID)
);

create sequence CUSTOMER_TCS3_SEQ start with 580000;
create TABLE CUSTOMER_TCS3 (
	ID number(19,0),
	SEX varchar2(10),
	TITLE varchar2(10),
	FIRSTNAME varchar2(100),
	LASTNAME varchar2(100),
	MIDDLENAME varchar2(100),
	EMAIL varchar2(100),
	MOBILEPHONE varchar2(50),
	OFFICEPHONE varchar2(50),
	FAX varchar2(50),
	COMPANY_ID number(19,0),
	primary key (ID),
	CONSTRAINT fk_CUSTOMER_COMPANY FOREIGN KEY (COMPANY_ID) REFERENCES COMPANIES(COMPANY_ID)
	
)





