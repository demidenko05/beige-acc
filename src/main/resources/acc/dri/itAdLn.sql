select 2006 as SRTY, DAT, ITADLN.IID as IID, ITMADD.IID as OWID, ITLF, TOLF
from ITADLN
join ITMADD on ITMADD.IID=ITADLN.OWNR
where ITADLN.RVID is null and MDENR=1 and ITLF>0 and ITADLN.DBOR=:DBOR and ITM=:ITM and UOM=:UOM
