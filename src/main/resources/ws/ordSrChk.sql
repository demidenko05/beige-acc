select :TORLN.IID as IID, :TORLN.NME as NME, SRV, OWNR, PRI, TOT, TOTX, UOM.NME as UOMNME, UOM.IID as UOMIID, DT1, DT2,
case when AVQUAN is null or QUAN>AVQUAN then 0 else QUAN end as QUAN
from :TORLN
left join UOM on :TORLN.UOM=UOM.IID
left join (select ITM, sum(QUAN) as AVQUAN from :TITPL group by ITM) as ITPL on ITPL.ITM=SRV
where OWNR in (:ORIDS);
