select 8 as SRTY, PURRET.DAT, 'RETRECV' as ACDB, DBCR.NME as SADNM, DBCR.IID as SADID, 1002 as SADTY, PURTTXLN.TOT as DEBT,
'SATAXPAYRT' as ACCR, TAX.NME as SACNM, TAX.IID as SACID, 1008 as SACTY, PURTTXLN.TOT as CRED
from PURTTXLN
join TAX on TAX.IID=PURTTXLN.TAX
join PURRET on PURRET.IID=PURTTXLN.OWNR
join PURINV on PURINV.IID=PURRET.INV
join DBCR on DBCR.IID=PURINV.DBCR
where PURRET.RVID is null and PURRET.MDENR=0 and PURINV.MDENR=1 and round(PURINV.TOPA+PURRET.TOT,2)>=round(PURINV.TOT,2) :WHEAD
