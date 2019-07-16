select 7 as SRTY, SALINV.DAT, ACC as ACDB, SANM as SADNM, SAID as SADID, SATY as SADTY, PAYMFR.TOT  as DEBT,
'RECEIVABLE' as ACCR, DBCR.NME as SACNM, DBCR.IID as SACID, 1002 as SACTY, PAYMFR.TOT as CRED
from PAYMFR
join SALINV on SALINV.IID=PAYMFR.INV
join DBCR on DBCR.IID=SALINV.DBCR
where PAYMFR.RVID is null and SALINV.RVID is null and PAYMFR.MDENR=0 and SALINV.MDENR=1 :WHEAD
