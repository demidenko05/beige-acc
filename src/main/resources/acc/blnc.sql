select ACC as ACID, NMBR as ACNB, NME as ACNM, SANM, SAID, SATY,
case when BLTY = 0 then DEBT - CRED else 0 end as DEBT,
case when BLTY = 1 then CRED - DEBT else 0 end as CRED
from (
  select ACC, SANM, SAID, sum(DEBT) as DEBT, sum(CRED) as CRED
  from (
    select ACC, SANM, SAID,
    case when ACCIN.BLTY = 0 then BLN else 0 end as DEBT,
    case when ACCIN.BLTY = 1 then BLN else 0 end as CRED
    from BLNC
    join ACNT as ACCIN on BLNC.ACC = ACCIN.IID 
    where DAT = :DT1
      union all
    select ACDB as ACC, SADNM as SANM, SADID as SAID, sum(DEBT) as DEBT, 0.00 as CRED
    from ENTR 
    where DAT>=:DT1 and DAT<=:DT2
    group by ACC, SANM, SAID
      union all
    select ACCR as ACC, SACNM as SANM, SACID as SAID, 0 as DEBT, sum(CRED) as CRED
    from ENTR 
    where DAT>=:DT1 and DAT<=:DT2
    group by ACC, SANM, SAID
  ) as UNRCS
  group by ACC, SANM, SAID
) as ALRCS
join ACNT on ALRCS.ACC = ACNT.IID
order by NMBR, SANM;
