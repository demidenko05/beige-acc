select ACC as ACID, NMBR as ACNB, NME as ACNM, SANM, SAID, ALRCS.SATY as SATY,
case when BLTY = 0 then DEBT - CRED else 0 end as DEBT,
case when BLTY = 1 then CRED - DEBT else 0 end as CRED
from (
  select ACC, SANM, SAID, SATY, sum(DEBT) as DEBT, sum(CRED) as CRED
  from (
    select ACC, SANM, SAID, BLNC.SATY as SATY,
    case when BLTY = 0 then BLN else 0 end as DEBT,
    case when BLTY = 1 then BLN else 0 end as CRED
    from BLNC
    join ACNT on BLNC.ACC=ACNT.IID 
    where DAT = :DT1
      union all
    select ACDB as ACC, SADNM as SANM, SADID as SAID, SADTY as SATY, DEBT, 0.00 as CRED
    from ENTR 
    where RVID is null and DAT>=:DT1 and DAT<=:DT2
      union all
    select ACCR as ACC, SACNM as SANM, SACID as SAID, SACTY as SATY, 0 as DEBT, CRED
    from ENTR 
    where RVID is null and DAT>=:DT1 and DAT<=:DT2
  ) as UNRCS
  group by ACC, SANM, SAID, SATY
) as ALRCS
join ACNT on ALRCS.ACC = ACNT.IID
order by NMBR, SANM;
