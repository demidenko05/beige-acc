select TXCT as TXCTID, TAX as TAXID, TAX.NME as TAXNME, RATE, sum(SUBT) as SUBT, sum(SUFC) as SUFC, sum(TOT) as TOT, sum(TOFC) as TOFC
from
( select TXCT, SUBT, SUFC, TOT, TOFC from :TGDLN
  where TXCT is not null and RVID is null and OWNR=:INVID
) as ALLNS
join TXCT on TXCT.IID=ALLNS.TXCT
join TXCTLN on TXCTLN.OWNR=TXCT.IID
join TAX on TXCTLN.TAX=TAX.IID
group by TXCTID, TAXID, TAXNME, RATE;
