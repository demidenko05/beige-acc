select 8 as SRTY, PURRET.DAT, 'RETRECV' as ACDB, DBCR.NME as SADNM, DBCR.IID as SADID, 1002 as SADTY, SUM(PURTLN.SUBT) as DEBT,
'INVENTORY' as ACCR, ITMCT.NME as SACNM, ITMCT.IID as SACID, 1004 as SACTY, SUM(PURTLN.SUBT) as CRED
from PURTLN
join PUINGDLN on PUINGDLN.IID=PURTLN.INVL
join ITM on ITM.IID=PUINGDLN.ITM
join ITMCT on ITMCT.IID=ITM.CAT
join PURRET on PURRET.IID=PURTLN.OWNR
join PURINV on PURINV.IID=PURRET.INV
join DBCR on DBCR.IID=PURINV.DBCR
where PURTLN.RVID is null :WHEAD
group by SRTY, PURRET.DAT, ACDB, SADNM, SADID, SADTY, ACCR, SACNM, SACID, SACTY
