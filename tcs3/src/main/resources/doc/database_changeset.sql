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
);

create SEQUENCE QUOTATION_TCS3_SEQ;
create TABLE QUOTATION_TCS3 (
        id number(19,0),
		MAIN_ORG_ID number(19,0),
		MAIN_GROUP_ID number(19,0),
		NAME varchar2(255),
		ABBR varchar2(255),
		REMARK varchar2(255),
		SAMPLE_NOTE	 varchar2(255),
		SAMPLE_PREP varchar2(255),
		COMPANY_ID number(19,0),
		ADDRESS_ID number(19,0),
		CUSTOMER_ID number(19,0),
		quotationDate date,
		estimatedDay number(5),
		quotationNo varchar2(255),
		cancelFlag varchar2(4),
		CREATED_BY_PERSON_ID number(19,0),
		primary key (ID),
		CONSTRAINT fk_QUOTATION_COMPANY FOREIGN KEY (COMPANY_ID) REFERENCES COMPANIES(COMPANY_ID),
		CONSTRAINT fk_QUOTATION_CUSTOMER FOREIGN KEY (CUSTOMER_ID) REFERENCES CUSTOMER_TCS3(ID),
		CONSTRAINT fk_QUOTATION_ADDRESS FOREIGN KEY (ADDRESS_ID) REFERENCES ADDRESS(ADDRESS_ID),
		CONSTRAINT fk_QUOTATION_MAINORG FOREIGN KEY (MAIN_ORG_ID) REFERENCES V_ORGANIZATION(ORG_ID),
		CONSTRAINT fk_QUOTATION_MAINGRP FOREIGN KEY (MAIN_GROUP_ID) REFERENCES V_ORGANIZATION(ORG_ID),
		CONSTRAINT fk_QUOTATION_CREATEDBY FOREIGN KEY (CREATED_BY_PERSON_ID) REFERENCES ORGANIZATION_PERSONS(PERSON_ID)
);

create sequence TEST_METHOD_Q_ITEM_SEQ;

create TABLE TEST_METHOD_Q_ITEM (
        id number(19,0),
        rowNo number(4,0),
		TESTMETHOD_INDEX number(19,0),
	    name varchar2(500),
		fee FLOAT(126),
		remark varchar2(500),
		quantity number(4),
		QUOTATION_ID number(19,0),
		TEST_METHOD_ID number(19,0),
		primary key(id),
		CONSTRAINT fk_testMethodQ_Q FOREIGN KEY (QUOTATION_ID) REFERENCES QUOTATION_TCS3(ID),
		CONSTRAINT fk_testMethodQ_TestMethod FOREIGN KEY (TEST_METHOD_ID) REFERENCES TEST_METHOD(TEST_METHOD_ID)
		
);

create sequence QUOTATION_NO_SEQ_SEQ;
create TABLE Qutation_NO_seq(
  id number(19,0),
  MAIN_ORG_ID number(19,0),
  Year number(6,0),
  CURRENT_NO number(6,0),
  primary key (ID),
  CONSTRAINT fk_QUOTATIONNO_SEQ_MAINORG FOREIGN KEY (MAIN_ORG_ID) REFERENCES V_ORGANIZATION(ORG_ID)
);


alter table QUOTATION_TEMPLATE add (EXAMPLE_ID number(19,0));
alter table QUOTATION_TEMPLATE 
  add constraint FK_QTEMPLATE_SAMPLE 
        foreign key (EXAMPLE_ID) 
        references EXAMPLE;

alter table QUOTATION_TCS3 add (EXAMPLE_ID number(19,0));
alter table QUOTATION_TCS3 
  add constraint FK_QTCS3_SAMPLE 
        foreign key (EXAMPLE_ID) 
        references EXAMPLE;
        
        
alter table QUOTATION_TCS3 add ( 
  SAMPLE_NUM number(19,0),
  TRANSLATE_ITEM number(19,0),
  TRANSLATE_FEE number(19,0),
  COA_ITEM number(19,0),
  COA_FEE number(19,0),
  COPY_ITEM number(19,0),
  copy_FEE number(19,0),
   ETC_FEE number(19,0),
   ETC varchar2(255)
);


alter table QUOTATION_TCS3 add (
	STANDARDREF varchar(255)
);

alter table QUOTATION_TEMPLATE add (
	STANDARDREF varchar(255)
);

alter table TEST_METHOD_QT_ITEM add (
	is_Sub_Item  NUMBER(1)
);

alter table TEST_METHOD_Q_ITEM add (
	is_Sub_Item  NUMBER(1)
);

alter table QUOTATION_TCS3 add ( 
	SERVICE_NO varchar2(100)
);


create table PROMOTION (
	id number(19,0),
	
	
	start_date date,
	end_date date,
	percent_discount NUMBER(4),
	description varchar2(1024),
	remark varchar2(1024),
	
	primary key (id)
);

create sequence PROMOTION_SEQ;

create sequence PROMOTION_DISCOUNT_SEQ;

create table PROMOTION_DISCOUNT (
	id number(19,0),
	
	
	quotation_id number(19,0),
	promotion_id number(19,0),
	
	
	discount NUMBER(10),
	
	PROMOTION_INDEX number(4),
	
	primary key (id),
	CONSTRAINT fk_PromotionDicount_Quotation FOREIGN KEY (QUOTATION_ID) REFERENCES QUOTATION_TCS3(ID),
	CONSTRAINT fk_PromotionDicount_Promotion FOREIGN KEY (PROMOTION_ID) REFERENCES PROMOTION(ID)

	
);