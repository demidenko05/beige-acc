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
insert into ACNT (TYP,BLTY,IID,NMBR,NME,VER,USED,SATY) values (0,0,'RETRECV','1110','Returns receivable',1462867931627,1,1002);
insert into ACNT (TYP,BLTY,IID,NMBR,NME,VER,USED,SATY) values (0,0,'INVENTORY','1200','Inventory',1462867931627,1,1004);
insert into ACNT (TYP,BLTY,IID,NMBR,NME,VER,USED,SATY) values (0,0,'INVDRCST','1205','Inventory direct cost (temporary)',1462867931627,1,1000);
insert into ACNT (TYP,BLTY,IID,NMBR,NME,VER,USED,SATY) values (0,0,'SATAXRECV','1310','Sales taxes receivable',1462867931627,1,1008);
insert into ACNT (TYP,BLTY,IID,NMBR,NME,VER,USED,SATY) values (0,0,'SATAXRECVRT','1315','Sales taxes receivable from returns',1462867931627,1,1008);
insert into ACNT (TYP,BLTY,IID,NMBR,NME,VER,USED,SATY) values (0,0,'PREPTO','1400','Prepayments to',1462867931627,1,1002);
insert into ACNT (TYP,BLTY,IID,NMBR,NME,VER,USED,SATY) values (1,1,'PAYABLE','2050','Accounts payable',1462867931627,1,1002);
insert into ACNT (TYP,BLTY,IID,NMBR,NME,VER,USED,SATY) values (1,1,'RETPAY','2055','Returns payable',1462867931627,1,1002);
insert into ACNT (TYP,BLTY,IID,NMBR,NME,VER,USED,SATY) values (1,1,'SATAXPAY','2310','Sales taxes payable',1462867931627,1,1008);
insert into ACNT (TYP,BLTY,IID,NMBR,NME,VER,USED,SATY) values (1,1,'SATAXPAYRT','2315','Sales taxes payable from returns',1462867931627,1,1008);
insert into ACNT (TYP,BLTY,IID,NMBR,NME,VER,USED,SATY) values (1,1,'PREPFR','2400','Prepayments from',1462867931627,1,1002);
insert into ACNT (TYP,BLTY,IID,NMBR,NME,VER,USED) values (2,1,'SCAPITAL','3010','Started capital',1462867931627,1);
insert into ACNT (TYP,BLTY,IID,NMBR,NME,VER,USED) values (2,1,'REARNINGS','3200','Retained earnings',1462867931627,1);
insert into ACNT (TYP,BLTY,IID,NMBR,NME,VER,USED) values (2,0,'RLOSSES','3300','Retained losses',1462867931627,1);
insert into ACNT (TYP,BLTY,IID,NMBR,NME,VER,USED,SATY) values (3,1,'GSALES','4010','Goods sales revenue',1462867931627,1,1004);
insert into ACNT (TYP,BLTY,IID,NMBR,NME,VER,USED,SATY) values (3,1,'SSALES','4030','Services sales revenue',1462867931627,1,1006);
insert into ACNT (TYP,BLTY,IID,NMBR,NME,VER,USED,SATY) values (4,0,'GRETURNS','5010','Goods sales returns',1462867931627,1,1004);
insert into ACNT (TYP,BLTY,IID,NMBR,NME,VER,USED,SATY) values (4,0,'COGS','5110','Cost of goods sold',1462867931627,1,1004);
insert into ACNT (TYP,BLTY,IID,NMBR,NME,VER,USED,SATY) values (4,0,'COGL','5150','Cost of goods loss/stolen/broken',1462867931627,1,1004);
insert into ACNT (TYP,BLTY,IID,NMBR,NME,VER,USED,SATY) values (4,0,'COSU','5155','Cost of supplies used',1462867931627,1,1004);
insert into ACNT (TYP,BLTY,IID,NMBR,NME,VER,USED,SATY) values (4,0,'EXPENSES','5250','Expenses',1462867931627,1,1000);
insert into CURR (IID,NME,SGN,VER) values (840,'USD','$',1462867931627);
insert into CURR (IID,NME,SGN,VER) values (978,'EUR','€',1462867931627);
insert into CURR (IID,NME,SGN,VER) values (643,'RUB','₽',1462867931627);
insert into UOM (IID,NME,VER) values (1,'each',1462867931627);
insert into UOM (IID,NME,VER) values (2,'box',1462867931627);
insert into UOM (IID,NME,VER) values (3,'dozen',1462867931627);
insert into UOM (IID,NME,VER) values (4,'gramme',1462867931627);
insert into UOM (IID,NME,VER) values (5,'pound',1462867931627);
insert into UOM (IID,NME,VER) values (6,'kilogram',1462867931627);
insert into UOM (IID,NME,VER) values (7,'cubic centimeters',1462867931627);
insert into UOM (IID,NME,VER) values (8,'liter',1462867931627);
insert into UOM (IID,NME,VER) values (9,'cubic inches',1462867931627);
insert into UOM (IID,NME,VER) values (10,'cubic feet',1462867931627);
insert into UOM (IID,NME,VER) values (11,'minute',1462867931627);
insert into UOM (IID,NME,VER) values (12,'hour',1462867931627);
insert into ACSTG (ORG,IID,VER,MNTH,CSDP,PRDP,RPDP,QUDP,TXDP,RNDM,BLPR,STRM,TTFF,TTFB,PGSZ,PGOR,MRLF,MRRI,MRTO,MRBO,FNSZ,CURR,COGS) values ('Bob''s Pizza',1,1462867931627,1462867931627,4,2,2,2,3,4,3,4,'DejaVuSerif','DejaVuSerif-Bold',2,0,20,10,10,10,3.5,840,0);
insert into ENRSRC (IID,SRTY,QUFL,VER,USED,SRIDNM,DSCR) values (1,2,'PrepFrCrAcCashDb',1462867931627,1,'PREPFR.IID','Debit Account Cash, Credit Prepayments from per customer.');
insert into ENRSRC (IID,SRTY,QUFL,VER,USED,SRIDNM,DSCR) values (2,3,'PrepToDbAcCashCr',1462867931627,1,'PREPTO.IID','Debit Prepayments to per vendor, Credit Account Cash.');
insert into ENRSRC (IID,SRTY,QUFL,VER,USED,SRIDNM,DSCR) values (3,4,'PuInPaybDbPrepToCr',1462867931627,1,'PURINV.IID','Debit Payable per vendor, Credit Prepayments to per vendor for min(prepayment,purchase) amount.');
insert into ENRSRC (IID,SRTY,QUFL,VER,USED,SRIDNM,DSCR) values (4,4,'PuInSaTxDbPaybCr',1462867931627,0,'PURINV.IID','Debit Sales tax from purchase per tax, Credit Payable per vendor for tax amount.');
insert into ENRSRC (IID,SRTY,QUFL,VER,USED,SRIDNM,DSCR) values (5,4,'PuInInvCtDbPaybCr',1462867931627,1,'PURINV.IID','Debit Inventory per item category, Credit Payable per vendor for subtotal amount.');
insert into ENRSRC (IID,SRTY,QUFL,VER,USED,SRIDNM,DSCR) values (6,4,'PuInExpDbPaybCr',1462867931627,1,'PURINV.IID','Debit Expenses, Credit Payable per vendor for subtotal amount.');
insert into ENRSRC (IID,SRTY,QUFL,VER,USED,SRIDNM,DSCR) values (7,5,'PayToPaybDbCashCr',1462867931627,1,'PAYMTO.IID','Debit Payable per vendor, Credit Cash for payment amount.');
insert into ENRSRC (IID,SRTY,QUFL,VER,USED,SRIDNM,DSCR) values (8,6,'SaInRecvDbGSalItcCr',1462867931627,1,'SALINV.IID','Debit Receivable per customer, Credit Goods sales revenue for subtotal amount.');
insert into ENRSRC (IID,SRTY,QUFL,VER,USED,SRIDNM,DSCR) values (9,6,'SaInRecvDbSSalItcCr',1462867931627,1,'SALINV.IID','Debit Receivable per customer, Credit Services sales revenue for subtotal amount.');
insert into ENRSRC (IID,SRTY,QUFL,VER,USED,SRIDNM,DSCR) values (10,6,'SaInPrepFrDbRecvCr',1462867931627,1,'SALINV.IID','Debit Prepayments from per customer, Credit Receivable per customer for min(prepayment,sales) amount.');
insert into ENRSRC (IID,SRTY,QUFL,VER,USED,SRIDNM,DSCR) values (11,6,'SaInRecvDbSaTxCr',1462867931627,1,'SALINV.IID','Debit Receivable per customer, Credit Sales taxes payable per tax for tax amount.');
insert into ENRSRC (IID,SRTY,QUFL,VER,USED,SRIDNM,DSCR) values (12,6,'SaInCogsDbInvCrItc',1462867931627,1,'SALINV.IID','Debit COGS per good category, Credit Inventory per good category for total amount.');
insert into ENRSRC (IID,SRTY,QUFL,VER,USED,SRIDNM,DSCR) values (13,7,'PayFrCashDbRecvCr',1462867931627,1,'PAYMFR.IID','Debit Cash, Credit Receivable per customer for total amount.');
insert into ENRSRC (IID,SRTY,QUFL,VER,USED,SRIDNM,DSCR) values (14,8,'PuRtRecvDbInvCtCr',1462867931627,1,'PURRET.IID','Debit Returns Receivable per vendor, Credit Inventory per good category for subtotal amount.');
insert into ENRSRC (IID,SRTY,QUFL,VER,USED,SRIDNM,DSCR) values (15,8,'PuRtRecvDbSaTxCr',1462867931627,1,'PURRET.IID','Debit Returns Receivable per vendor, Credit Sales Tax Payable from returns per Tax for tax amount.');
insert into ENRSRC (IID,SRTY,QUFL,VER,USED,SRIDNM,DSCR) values (16,9,'SaRtItCtDbPaybCr',1462867931627,1,'SALRET.IID','Debit Goods sales returns per good category, Credit Returns Payable per Customer for subtotal amount.');
insert into ENRSRC (IID,SRTY,QUFL,VER,USED,SRIDNM,DSCR) values (17,9,'SaRtSaTaxDbPaybCr',1462867931627,1,'SALRET.IID','Debit Sales tax receivable from returns per Tax, Credit Returns Payable per Customer for tax amount.');
insert into ENRSRC (IID,SRTY,QUFL,VER,USED,SRIDNM,DSCR) values (18,9,'SaRtInvCtDbCogsCr',1462867931627,1,'SALRET.IID','Debit Inventory per good category, Credit COGS per good category for (quantity*COGS price) amount.');
insert into ENRSRC (IID,SRTY,QUFL,VER,USED,SRIDNM,DSCR) values (19,11,'ItUbCogsDbInvCrItc',1462867931627,1,'ITMULB.IID','Debit COGS per good category, Credit Inventory per good category for COGS total amount.');
insert into ENRSRC (IID,SRTY,QUFL,VER,USED,SRIDNM,DSCR) values (20,12,'ItAdInvCtDb',1462867931627,1,'ITMADD.IID','Debit Inventory per good category for total amount.');
insert into ENRSRC (IID,SRTY,QUFL,VER,USED,SRIDNM,DSCR) values (21,13,'MnPrInvIctDbExpCr',1462867931627,1,'MNFPRC.IID','Debit Inventory per good category, Credit Expenses for additional total amount.');
insert into ENRSRC (IID,SRTY,QUFL,VER,USED,SRIDNM,DSCR) values (22,13,'MnPrInvCtDbCr',1462867931627,1,'MNFPRC.IID','Debit/Credit Inventory per good category for used material total amount.');
insert into ENRSRC (IID,SRTY,QUFL,VER,USED,SRIDNM,DSCR) values (23,14,'MnfInvCtDbCr',1462867931627,1,'MNFCT.IID','Debit/Credit Inventory per good category for total amount.');
insert into DRIENRSR (IID,SRTY,QUFL,VER,USED,SRSTY,DSCR) values (1,2000,'puGdLn',1462867931627,1,0,'Purchase invoice good line for FIFO/LIFO');
insert into DRIENRSR (IID,SRTY,QUFL,VER,USED,SRSTY,DSCR) values (2,2003,'saRtLn',1462867931627,1,0,'Sales return line for FIFO/LIFO');
insert into DRIENRSR (IID,SRTY,QUFL,VER,USED,SRSTY,DSCR) values (3,2006,'itAdLn',1462867931627,1,0,'Add item line for FIFO/LIFO');
