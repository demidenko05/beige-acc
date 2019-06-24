select TXCT as TXCTID, TAX as TAXID, TAX.NME as TAXNME, RATE, sum(SUBT) as SUBT, sum(SUFC) as SUFC
from
( select TXCT, SUBT, SUFC from :TGDLN
  where TXCT is not null and RVID is null and OWNR=:INVID
union all
  select TXCT, SUBT, SUFC from :TSRVLN
  where TXCT is not null and OWNR=:INVID
) as ALLNS
join TXCT on TXCT.IID=ALLNS.TXCT
join TXCTLN on TXCTLN.OWNR=TXCT.IID
join TAX on TXCTLN.TAX=TAX.IID
group by TXCTID, TAXID, TAXNME, RATE
order by TAXID;
