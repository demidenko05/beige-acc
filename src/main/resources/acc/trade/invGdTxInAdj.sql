select TXCT as TXCTID, TXCTLN.TAX as TAXID, RATE, sum(SUBT) as SUBT, sum(SUFC) as SUFC, min(TOTX) as TOTX, min(TXFC) as TXFC, sum(TOT) as TOT, sum(TOFC) as TOFC
from
( select TXCT, SUBT, SUFC, TOT, TOFC from :TGDLN
  where TXCT is not null and RVID is null and OWNR=:INVID
) as ALLNS
join TXCT on TXCT.IID=ALLNS.TXCT
join TXCTLN on TXCTLN.OWNR=TXCT.IID
join (select TAX, TOT as TOTX, TOFC as TXFC from :TTAXLN where OWNR=:INVID) as INVTLNS on TXCTLN.TAX=INVTLNS.TAX
group by TXCTID, TAXID, RATE
order by TAXID;
