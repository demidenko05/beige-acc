select sum(SUBT) as SUBT, sum(TOT) as TOT, sum(SUFC) as SUFC, sum(TOFC) as TOFC, sum(TOTX) as TOTX, sum(TXFC) as TXFC
from
( select SUBT, TOT, SUFC, TOFC, 0 as TOTX, 0 as TXFC from :TGDLN
  where RVID is null and OWNR=:OWNR
union all
  select 0 as SUBT, 0 as TOT, 0 as SUFC, 0 as TOFC, TOT as TOTX, TOFC as TXFC from :TTAXLN 
  where OWNR=:OWNR
) as ALLNS;
