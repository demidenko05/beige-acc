select IID, TYP, ITALL.ITID as ITID, IMG, PRI, PRIPR, QUAN, DETMT,
case when I18SPELI.NME is null then ITALL.NME else I18SPELI.NME end as NME,
case when I18SPELI.VAL is null then ITALL.SPECS else I18SPELI.VAL end as SPECS
from (
  select IID, TYP, ITID, NME, IMG, SPECS, PRI, PRIPR, QUAN, DETMT
  from ITLIST
  where QUAN>0 and TYP=:ITTYP :WHEREADD
) as ITALL
join (
  select distinct ITM as ITINCAT from :TITCAT where CATL:FLTCAT
) as ITINCATALL on ITINCATALL.ITINCAT=ITALL.ITID
left join (
  select NME, VAL, ITID from I18SPELI  where TYP=:ITTYP and LNG=':LNG'
) as I18SPELI on I18SPELI.ITID=ITALL.ITID
