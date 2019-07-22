select 13 as SRTY, DAT, 'INVENTORY' as ACDB, ITMCT.NME as SADNM, ITMCT.IID as SADID, 1004 as SADTY, SUM(MNPACS.TOT) as DEBT,
MNPACS.ACC as ACCR, MNPACS.SANM as SACNM, MNPACS.SAID as SACID, MNPACS.SATY as SACTY, SUM(MNPACS.TOT) as CRED
from MNPACS
join MNFPRC on MNFPRC.IID=MNPACS.OWNR
join ITM on ITM.IID=MNFPRC.ITM
join ITMCT on ITMCT.IID=ITM.CAT
where MNFPRC.RVID is null and MNFPRC.MDENR=0 :WHEAD
group by SRTY, DAT, ACDB, SADNM, SADID, SADTY, ACCR, SACNM, SACID, SACTY