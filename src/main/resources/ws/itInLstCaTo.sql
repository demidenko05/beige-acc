select IID, ITID, PRI
from (
  select IID, ITID, PRI
  from ITLIST
  where QUAN>0 and TYP=:ITTYP :WHEREADD
 ) as ITALL
join (
  select distinct ITM as ITINCAT from :TITCAT where CATL:FLTCAT
 ) as ITINCATALL on ITINCATALL.ITINCAT=ITALL.ITID
