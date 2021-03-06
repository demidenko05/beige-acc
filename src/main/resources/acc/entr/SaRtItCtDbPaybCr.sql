select 9 as SRTY, SALRET.DAT, 'GRETURNS' as ACDB, ITMCT.NME as SADNM, ITMCT.IID as SADID, 1004 as SADTY, SUM(SARTLN.SUBT) as DEBT,
'RETPAY' as ACCR, DBCR.NME as SACNM, DBCR.IID as SACID, 1002 as SACTY, SUM(SARTLN.SUBT) as CRED
from SARTLN
join SAINGDLN on SAINGDLN.IID=SARTLN.INVL
join ITM on ITM.IID=SAINGDLN.ITM
join ITMCT on ITMCT.IID=ITM.CAT
join SALRET on SALRET.IID=SARTLN.OWNR
join SALINV on SALINV.IID=SALRET.INV
join DBCR on DBCR.IID=SALINV.DBCR
where SARTLN.RVID is null :WHEAD
group by SRTY, SALRET.DAT, ACDB, SADNM, SADID, SADTY, ACCR, SACNM, SACID, SACTY
