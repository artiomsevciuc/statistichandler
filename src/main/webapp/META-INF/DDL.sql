--------------------------------------------------------
--  DDL for Table CUSTOMER
--------------------------------------------------------

  CREATE TABLE "YOUR_SCHEMA_NAME"."CUSTOMER" 
   (	"ID" NUMBER, 
	"NAME" VARCHAR2(255 BYTE), 
	"ACTIVE" NUMBER(1,0) DEFAULT 1
   ) SEGMENT CREATION IMMEDIATE 
  PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 
 NOCOMPRESS LOGGING
  STORAGE(INITIAL 81920 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "USERS" ;
--------------------------------------------------------
--  DDL for Index CUSTOMER_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "YOUR_SCHEMA_NAME"."CUSTOMER_PK" ON "YOUR_SCHEMA_NAME"."CUSTOMER" ("ID") 
  PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 81920 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "USERS" ;
--------------------------------------------------------
--  DDL for Trigger CUSTOMER_TRG
--------------------------------------------------------

  CREATE OR REPLACE NONEDITIONABLE TRIGGER "YOUR_SCHEMA_NAME"."CUSTOMER_TRG" 
BEFORE INSERT ON CUSTOMER 
FOR EACH ROW 
BEGIN
  <<COLUMN_SEQUENCES>>
  BEGIN
    IF INSERTING AND :NEW.ID IS NULL THEN
      SELECT CUSTOMER_SEQ1.NEXTVAL INTO :NEW.ID FROM SYS.DUAL;
    END IF;
  END COLUMN_SEQUENCES;
END;
/
ALTER TRIGGER "YOUR_SCHEMA_NAME"."CUSTOMER_TRG" ENABLE;
--------------------------------------------------------
--  Constraints for Table CUSTOMER
--------------------------------------------------------

  ALTER TABLE "YOUR_SCHEMA_NAME"."CUSTOMER" MODIFY ("ID" NOT NULL ENABLE);
  ALTER TABLE "YOUR_SCHEMA_NAME"."CUSTOMER" MODIFY ("NAME" NOT NULL ENABLE);
  ALTER TABLE "YOUR_SCHEMA_NAME"."CUSTOMER" ADD CONSTRAINT "CUSTOMER_PK" PRIMARY KEY ("ID")
  USING INDEX PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 81920 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "USERS"  ENABLE;

  --------------------------------------------------------
--  DDL for Table HOURLY_STATS
--------------------------------------------------------

  CREATE TABLE "YOUR_SCHEMA_NAME"."HOURLY_STATS" 
   (	"ID" NUMBER, 
	"CUSTOMER_ID" NUMBER, 
	"TIME" NUMBER, 
	"REQUEST_COUNT" NUMBER DEFAULT 0, 
	"INVALID_COUNT" NUMBER DEFAULT 0
   ) SEGMENT CREATION IMMEDIATE 
  PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 
 NOCOMPRESS LOGGING
  STORAGE(INITIAL 81920 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "USERS" ;
--------------------------------------------------------
--  DDL for Index HOURLY_STATS_INDEX
--------------------------------------------------------

  CREATE UNIQUE INDEX "YOUR_SCHEMA_NAME"."HOURLY_STATS_INDEX" ON "YOUR_SCHEMA_NAME"."HOURLY_STATS" ("ID") 
  PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 81920 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "USERS" ;
--------------------------------------------------------
--  DDL for Index HOURLY_STATS_UK1
--------------------------------------------------------

  CREATE UNIQUE INDEX "YOUR_SCHEMA_NAME"."HOURLY_STATS_UK1" ON "YOUR_SCHEMA_NAME"."HOURLY_STATS" ("CUSTOMER_ID", "TIME") 
  PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 81920 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "USERS" ;
--------------------------------------------------------
--  DDL for Trigger HOURLY_STATS_TRG
--------------------------------------------------------

  CREATE OR REPLACE NONEDITIONABLE TRIGGER "YOUR_SCHEMA_NAME"."HOURLY_STATS_TRG" 
BEFORE INSERT ON HOURLY_STATS 
FOR EACH ROW 
BEGIN
  <<COLUMN_SEQUENCES>>
  BEGIN
    IF INSERTING AND :NEW.ID IS NULL THEN
      SELECT HOURLY_STATS_SEQ.NEXTVAL INTO :NEW.ID FROM SYS.DUAL;
    END IF;
  END COLUMN_SEQUENCES;
END;
/
ALTER TRIGGER "YOUR_SCHEMA_NAME"."HOURLY_STATS_TRG" ENABLE;
--------------------------------------------------------
--  Constraints for Table HOURLY_STATS
--------------------------------------------------------

  ALTER TABLE "YOUR_SCHEMA_NAME"."HOURLY_STATS" MODIFY ("CUSTOMER_ID" NOT NULL ENABLE);
  ALTER TABLE "YOUR_SCHEMA_NAME"."HOURLY_STATS" MODIFY ("TIME" NOT NULL ENABLE);
  ALTER TABLE "YOUR_SCHEMA_NAME"."HOURLY_STATS" ADD CONSTRAINT "HOURLY_STATS_UK1" UNIQUE ("CUSTOMER_ID", "TIME")
  USING INDEX PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 81920 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "USERS"  ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table HOURLY_STATS
--------------------------------------------------------

  ALTER TABLE "YOUR_SCHEMA_NAME"."HOURLY_STATS" ADD CONSTRAINT "HOURLY_STATS_FK1" FOREIGN KEY ("CUSTOMER_ID")
	  REFERENCES "YOUR_SCHEMA_NAME"."CUSTOMER" ("ID") ON DELETE CASCADE ENABLE;
--------------------------------------------------------
--  DDL for Table IP_BLACKLIST
--------------------------------------------------------

  CREATE TABLE "YOUR_SCHEMA_NAME"."IP_BLACKLIST" 
   (	"IP" NUMBER(11,0)
   ) SEGMENT CREATION IMMEDIATE 
  PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 
 NOCOMPRESS LOGGING
  STORAGE(INITIAL 81920 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "USERS" ;
--------------------------------------------------------
--  DDL for Index IP_BLACKLIST_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "YOUR_SCHEMA_NAME"."IP_BLACKLIST_PK" ON "YOUR_SCHEMA_NAME"."IP_BLACKLIST" ("IP") 
  PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 81920 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "USERS" ;
--------------------------------------------------------
--  Constraints for Table IP_BLACKLIST
--------------------------------------------------------

  ALTER TABLE "YOUR_SCHEMA_NAME"."IP_BLACKLIST" MODIFY ("IP" NOT NULL ENABLE);
  ALTER TABLE "YOUR_SCHEMA_NAME"."IP_BLACKLIST" ADD CONSTRAINT "IP_BLACKLIST_PK" PRIMARY KEY ("IP")
  USING INDEX PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 81920 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "USERS"  ENABLE;

  --------------------------------------------------------
--  DDL for Table UA_BLACKLIST
--------------------------------------------------------

  CREATE TABLE "YOUR_SCHEMA_NAME"."UA_BLACKLIST" 
   (	"UA" VARCHAR2(255 BYTE)
   ) SEGMENT CREATION IMMEDIATE 
  PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 
 NOCOMPRESS LOGGING
  STORAGE(INITIAL 81920 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "USERS" ;
--------------------------------------------------------
--  DDL for Index UA_BLACKLIST_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "YOUR_SCHEMA_NAME"."UA_BLACKLIST_PK" ON "YOUR_SCHEMA_NAME"."UA_BLACKLIST" ("UA") 
  PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 81920 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "USERS" ;
--------------------------------------------------------
--  Constraints for Table UA_BLACKLIST
--------------------------------------------------------

  ALTER TABLE "YOUR_SCHEMA_NAME"."UA_BLACKLIST" MODIFY ("UA" NOT NULL ENABLE);
  ALTER TABLE "YOUR_SCHEMA_NAME"."UA_BLACKLIST" ADD CONSTRAINT "UA_BLACKLIST_PK" PRIMARY KEY ("UA")
  USING INDEX PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 81920 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "USERS"  ENABLE;
