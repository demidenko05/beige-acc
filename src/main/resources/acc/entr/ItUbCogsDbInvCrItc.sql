select 11 as SRTY, DAT, ACC as ACDB, ITMCT.NME as SADNM, ITMCT.IID as SADID, 1004 as SADTY, SUM(COGSENR.TOT) as DEBT,
'INVENTORY' as ACCR, ITMCT.NME as SACNM, ITMCT.IID as SACID, 1004 as SACTY, SUM(COGSENR.TOT) as CRED
from ITUBLN
join (select DRID, ITM, TOT from COGSENR where RVID is null and DRTY=2005) as COGSENR on COGSENR.DRID=ITUBLN.IID
join ITMULB on ITMULB.IID=ITUBLN.OWNR
join ITM on ITM.IID=COGSENR.ITM
join ITMCT on ITMCT.IID=ITM.CAT
where ITUBLN.RVID is null :WHEAD
group by SRTY, DAT, ACDB, SADNM, SADID, SADTY, ACCR, SACNM, SACID, SACTY
