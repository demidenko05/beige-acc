select 14 as SRTY, MNFCT.DAT, 'INVENTORY' as ACDB, ITMCT.NME as SADNM, ITMCT.IID as SADID, 1004 as SADTY, SUM(MNFCT.TOT) as DEBT,
'INVENTORY' as ACCR, ITMCTU.NME as SACNM, ITMCTU.IID as SACID, 1004 as SACTY, SUM(MNFCT.TOT) as CRED
from MNFCT
join ITM on ITM.IID=MNFCT.ITM
join ITMCT on ITMCT.IID=ITM.CAT
join MNFPRC on MNFPRC.IID=MNFCT.MNP
join ITM as ITMU on ITMU.IID=MNFPRC.ITM
join ITMCT as ITMCTU on ITMCTU.IID=ITMU.CAT
where MNFCT.RVID is null and MNFCT.MDENR=0 :WHEAD
group by SRTY, MNFCT.DAT, ACDB, SADNM, SADID, SADTY, ACCR, SACNM, SACID, SACTY
