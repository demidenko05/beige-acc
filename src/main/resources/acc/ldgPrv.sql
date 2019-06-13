select ACNT.BLTY, ACNT.NME as ACC, SUBACC,
case when ACNT.BLTY=0 then ALREC.DEBT-ALREC.CRED else 0 end as DEBT,
case when ACNT.BLTY=1 then ALREC.CRED-ALREC.DEBT else 0 end as CRED
from
( select ACC, SUBACC, sum(DEBT) as DEBT, sum(CRED) as CRED
  from 
  ( select ACC as ACC, SANM as SUBACC,
    case when BLTY=0 then BLN else 0 end as DEBT,
    case when BLTY=1 then BLN else 0 end as CRED
    from BLNC
    join ACNT on BLNC.ACC=ACNT.IID 
    where ACC=':ACCID' :SUBACC and DAT=:DTBLN
  union all
    select ACDB as ACC, SADNM as SUBACC, DEBT, 0.00 as CRED
    from ENTR
    where ENTR.ACDB=':ACCID' :SADNM and DAT>=:DTBLN and DAT<=:DT1
  union all
    select ACCR as ACC, SACNM as SUBACC, 0 as DEBT, CRED
    from ENTR
    where ENTR.ACCR=':ACCID' :SACNM and DAT>=:DTBLN and DAT<=:DT1
  ) as UNPRV
  group by ACC, SUBACC
) as ALREC
join ACNT on ALREC.ACC=ACNT.IID
order by SUBACC;
