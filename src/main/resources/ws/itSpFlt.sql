join (
  select ITM as ITSPEC
  from :TITSPEC
  where :WHESPITFLR
  group by ITM
  having count(ITM)=:SPITFLTCO
 ) as ITSPECALL on ITINCATALL.ITINCAT=ITSPECALL.ITSPEC
