select TAX as TAXID, TAX.NME as TAXNME, sum(TOT) as TOTX, sum(TOFC) as TXFC
from
( select TAX, TOT, TOFC from :TGDTXLN
  where RVID is null and INVID=:INVID
union all
  select TAX, TOT, TOFC from :TSRVTXLN
  where INVID=:INVID
) as ALLNS
join TAX on ALLNS.TAX=TAX.IID
group by TAX, TAXNME;
