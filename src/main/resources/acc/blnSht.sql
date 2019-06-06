select ACC as ACID, NMBR, TYP, NME,
case when BLTY=0 then ALRCS.DEBT-ALRCS.CRED else 0 end as DEBT,
case when BLTY=1 then ALRCS.CRED-ALRCS.DEBT else 0 end as CRED
from
  (select ACC, NMBR, TYP, BLTY, NME, sum(DEBT) as DEBT, sum(CRED) as CRED
    from
      ( select ACC, NMBR, TYP, BLTY, NME, 
        case when ACNT.BLTY=0 then BLN else 0 end as DEBT,
        case when ACNT.BLTY=1 then BLN else 0 end as CRED
        from  BLNC
        join ACNT on BLNC.ACC=ACNT.IID 
        where TYP<3 and DAT=:DT1
        union all
        select ACDB as ACC, NMBR, TYP, BLTY, ACNT.NME as NME, sum(DEBT) as DEBT, 0.00 as CRED
        from  ENTR 
        join ACNT on ENTR.ACDB=ACNT.IID 
        where TYP<3 and DAT>=:DT1 and DAT<=:DT2
        group by ACC, NMBR, TYP, BLTY, ACNT.NME
        union all
        select ACCR as ACC, NMBR, TYP, BLTY, ACNT.NME as NME, 0 as DEBT, sum(CRED) as CRED
        from  ENTR 
        join ACNT on ENTR.ACCR=ACNT.IID 
        where TYP<3 and DAT>=:DT1 and DAT<=:DT2
        group by ACC, TYP, BLTY, ACNT.NME
      ) as UNRECS
    group by ACC, NMBR, TYP, BLTY, NME
  ) as ALRCS
order by NMBR;
