select 9 as SRTY, SALRET.DAT, 'INVENTORY' as ACDB, ITMCT.NME as SADNM, ITMCT.IID as SADID, SUM(SARTLN.QUAN*ITPRI) as DEBT,
'COGS' as ACCR, ITMCT.NME as SACNM, ITMCT.IID as SACID, SUM(SARTLN.QUAN*ITPRI) as CRED
from SARTLN
join SAINGDLN on SAINGDLN.IID=SARTLN.INVL
join (select DRID, TOT/QUAN as ITPRI from COGSENR where DRTY=2001) as COGSENR on COGSENR.DRID=SAINGDLN.IID
join ITM on ITM.IID=SAINGDLN.ITM
join ITMCT on ITMCT.IID=ITM.CAT
join SALRET on SALRET.IID=SARTLN.OWNR
where SALRET.RVID is null and SALRET.MDENR=0 :WHEAD
group by SRTY, DAT, ACDB, SADNM, SADID, ACCR, SACNM, SACID
