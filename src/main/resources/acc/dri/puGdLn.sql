select 2000 as SRTY, DAT, PUINGDLN.IID as IID, PURINV.IID as OWID, ITLF,
case when TOLF=0.0 then PUINGDLN.SUBT else TOLF end as TOLF
from PUINGDLN
join PURINV on PURINV.IID=PUINGDLN.OWNR
where PUINGDLN.RVID is null and MDENR=1 and ITLF>0 and PUINGDLN.DBOR=:DBOR and ITM=:ITM and UOM=:UOM
