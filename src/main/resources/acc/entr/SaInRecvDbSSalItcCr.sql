select 6 as SRTY, DAT, 'RECEIVABLE' as ACDB, DBCR.NME as SADNM, DBCR.IID as SADID, SUM(SAINSRLN.SUBT) as DEBT,
'SSALES' as ACCR, SRVCT.NME as SACNM, SRVCT.IID as SACID, SUM(SAINSRLN.SUBT) as CRED
from SAINSRLN
join SRV on SRV.IID=SAINSRLN.ITM
join SRVCT on SRVCT.IID=SRV.CAT
join SALINV on SALINV.IID=SAINSRLN.OWNR
join DBCR on DBCR.IID=SALINV.DBCR
where SALINV.RVID is null and SALINV.MDENR=0 :WHEAD
group by SRTY, DAT, ACDB, SADNM, SADID, ACCR, SACNM, SACID