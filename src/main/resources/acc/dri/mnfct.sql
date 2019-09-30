select 14 as SRTY, DAT, IID, null as OWID, WRHP, ITLF, TOLF
from MNFCT
where MNFCT.RVID is null and MDENR=1 and ITLF>0 and DBOR=:DBOR and ITM=:ITM and UOM=:UOM
