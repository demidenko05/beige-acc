select 2 as SRTY, DAT, ACC as ACDB, SANM as SADNM, SAID as SADID, TOT as DEBT,
'PREPFR' as ACCR, DBCR.NME as SACNM, DBCR.IID as SACID, TOT as CRED
from PREPFR
join DBCR on DBCR.IID=PREPFR.DBCR
where RVID is null and MDENR=0 :WHEAD
