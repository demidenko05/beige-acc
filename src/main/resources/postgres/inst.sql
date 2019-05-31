insert into LNG (IID,NME,VER) values ('ru','Русский',1462867931627);
insert into LNG (IID,NME,VER) values ('en','English',1462867931627);
insert into CNTR (IID,NME,VER) values ('US','USA',1462867931627);
insert into CNTR (IID,NME,VER) values ('RU','РФ',1462867931627);
insert into DCSP (IID,NME,VER) values (',','Comma',1462867931627);
insert into DCSP (IID,NME,VER) values ('.','Dot',1462867931627);
insert into DCGRSP (IID,NME,VER) values (',','Comma',1462867931627);
insert into DCGRSP (IID,NME,VER) values ('SPACE','Space',1462867931627);
insert into USPRF (DCGRSP,LNG,CNTR,DCSP,DEF,VER,DGINGR) values (',','en','US','.',1,1462867931627,3);
insert into USPRF (DCGRSP,LNG,CNTR,DCSP,DEF,VER,DGINGR) values ('SPACE','ru','RU',',',0,1462867931627,3);
insert into ACNT (TYP,BLTY,IID,NMBR,NME,VER,USED,SATY) values (0,0,'BANK','1030','Cash in bank',1462867931627,1,1003);
insert into ACNT (TYP,BLTY,IID,NMBR,NME,VER,USED,SATY) values (0,0,'RECEIVABLE','1105','Accounts receivable',1462867931627,1,1002);
insert into ACNT (TYP,BLTY,IID,NMBR,NME,VER,USED,SATY) values (0,0,'INVENTORY','1200','Inventory',1462867931627,1,1004);
insert into ACNT (TYP,BLTY,IID,NMBR,NME,VER,USED,SATY) values (1,1,'PAYABLE','2050','Accounts payable',1462867931627,1,1002);
insert into ACNT (TYP,BLTY,IID,NMBR,NME,VER,USED) values (2,1,'SCAPITAL','3010','Started capital',1462867931627,1);
insert into ACNT (TYP,BLTY,IID,NMBR,NME,VER,USED) values (2,1,'REARNINGS','3200','Retained earnings',1462867931627,1);
insert into ACNT (TYP,BLTY,IID,NMBR,NME,VER,USED) values (2,0,'RLOSSES','3300','Retained losses',1462867931627,1);
insert into ACNT (TYP,BLTY,IID,NMBR,NME,VER,USED,SATY) values (3,1,'GSALES','4010','Goods sales revenue',1462867931627,1,1004);
insert into ACNT (TYP,BLTY,IID,NMBR,NME,VER,USED,SATY) values (3,1,'SSALES','4030','Services sales revenue',1462867931627,1,1006);
insert into ACNT (TYP,BLTY,IID,NMBR,NME,VER,USED,SATY) values (3,0,'GRETURNS','5010','Goods sales returns',1462867931627,1,1004);
insert into ACNT (TYP,BLTY,IID,NMBR,NME,VER,USED,SATY) values (3,0,'COGS','5110','Cost of goods sold',1462867931627,1,1004);
insert into ACNT (TYP,BLTY,IID,NMBR,NME,VER,USED,SATY) values (3,0,'COGL','5150','Cost of goods loss/stolen/broken',1462867931627,1,1004);
insert into ACNT (TYP,BLTY,IID,NMBR,NME,VER,USED,SATY) values (3,0,'EXPENSES','5250','Expenses',1462867931627,1,1000);
insert into ACSTG (ORG,IID,VER,MNTH,CSDP,PRDP,RPDP,QUDP,TXDP,RNDM,BLPR,STRM,TTFF,TTFB,PGSZ,PGOR,MRLF,MRRI,MRTO,MRBO,FNSZ) values ('Bob''s Pizza',1,1462867931627,1462867931627,4,2,2,2,3,4,3,4,'DejaVuSerif','DejaVuSerif-Bold',2,0,20,10,10,10,3.5);