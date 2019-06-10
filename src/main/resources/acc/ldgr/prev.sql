select ACNT.NME, SUBACC,
case when ACNT.BLTY=0 then ALREC.DEBT-ALREC.CRED else 0 end as DEBT,
case when ACNT.BLTY=1 then ALREC.CRED-ALREC.DEBT else 0 end as CRED
from
( select ACC, SUBACC, sum(DEBT) as DEBT, sum(CRED) as CRED
  from 
  ( select ACC as ACC, SANM as SUBACC,
    case when ACNTIN.BLTY=0 then BLN
    else 0 end as DEBT,
    case when ACNTIN.BLTY=1 then BLN
    else 0 end as CRED
    from  BLNC
    join ACNT as ACNTIN on BLNC.ACC=ACNTIN.IID 
    where ACC=':ACCID' :SUBACC and DAT=:DTBLN
  union all
    select ACDB as ACC, SADNM as SUBACC, sum(DEBT) as DEBT, 0.00 as CRED
    from  ENTR 
    where ENTR.ACDB=':ACCID' :SADNM and DAT>=:DTBLN and DAT<=:DT1
    group by ACC,  SUBACC
  union all
    select ACCR as ACC, SACNM as SUBACC, 0 as DEBT, sum(CRED) as CRED
    from  ENTR 
    where ENTR.ACCR=':ACCID' :SACNM and DAT>=:DTBLN and DAT<=:DT1
    group by ACC, SUBACC
  ) as UNION_PREVIOUS
  group by ACC, SUBACC
) as ALREC
join ACNT on ALREC.ACC=ACNT.IID
order by SUBACC;
