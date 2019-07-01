select sum(TOT) as TOT, sum(TOFC) as TOFC, sum(TOTX) as TOTX, sum(TXFC) as TXFC
from
( select TOT, TOFC, 0 as TOTX, 0 as TXFC
  from :TGDLN 
  where RVID is null and OWNR=:OWNR
union all
  select TOT, TOFC, 0 as TOTX, 0 as TXFC
  from :TSRVLN 
  where OWNR=:OWNR
union all
  select 0 as TOT, 0 as TOFC, TOT as TOTX, TOFC as TXFC
  from :TTAXLN 
  where OWNR=:OWNR
) as ALLNS;
