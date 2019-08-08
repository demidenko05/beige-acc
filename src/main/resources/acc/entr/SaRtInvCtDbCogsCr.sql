select 9 as SRTY, SALRET.DAT, 'INVENTORY' as ACDB, ITMCT.NME as SADNM, ITMCT.IID as SADID, 1004 as SADTY, SUM(SARTLN.QUAN*ITPRI) as DEBT,
'COGS' as ACCR, ITMCT.NME as SACNM, ITMCT.IID as SACID, 1004 as SACTY, SUM(SARTLN.QUAN*ITPRI) as CRED
from SARTLN
join SAINGDLN on SAINGDLN.IID=SARTLN.INVL
join (select DRID, TOT/QUAN as ITPRI from COGSENR where RVID is null and DRTY=2001) as COGSENR on COGSENR.DRID=SAINGDLN.IID
join ITM on ITM.IID=SAINGDLN.ITM
join ITMCT on ITMCT.IID=ITM.CAT
join SALRET on SALRET.IID=SARTLN.OWNR
where SARTLN.RVID is null :WHEAD
group by SRTY, SALRET.DAT, ACDB, SADNM, SADID, SADTY, ACCR, SACNM, SACID, SACTY
