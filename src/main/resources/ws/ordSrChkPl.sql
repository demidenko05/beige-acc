select :TORLN.IID as IID, :TORLN.NME as NME, SERVICE, OWNR, PRI, TOT, TOTX, UOM.NME as UOMNME, UOM.IID as UOMIID, DT1, DT2,
case when AVQUAN is null or QUANT>AVQUAN then 0 else QUANT end as QUANT
from :TORLN
left join UOM on :TORLN.UOM=UOM.IID
left join (select ITM, QUAN as AVQUAN from :TITPL where PICKUPPLACE=:PLACE) as ITPL on ITPL.ITM=SERVICE
where OWNR in (:ORIDS);
