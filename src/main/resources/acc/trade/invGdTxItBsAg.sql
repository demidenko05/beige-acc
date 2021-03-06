select ILID, TXCT as TXCTID, TAX as TAXID, TAX.NME as TAXNME, RATE, sum(TOTX) as TOTX, sum(TXFC) as TXFC
from
( select IID as ILID, TXCT, TOTX, TXFC from :TGDLN
  where RVID is null and TXCT is not null and OWNR=:INVID
) as ALLNS
join TXCT on TXCT.IID=TXCT
join TXCTLN on TXCTLN.OWNR=TXCT.IID
join TAX on TXCTLN.TAX=TAX.IID
group by ILID, TXCTID, TAXID, TAXNME, RATE;
