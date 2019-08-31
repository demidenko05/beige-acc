select :TORLN.IID as IID, :TORLN.NME as NME, SRV, OWNR, PRI, TOT, TOTX, UOM17.NME as UOM17NME, UOM17.IID as UOM17IID, DT1, DT2,
case when AVQUAN is null or QUAN>AVQUAN then 0 else QUAN end as QUAN
from :TORLN
left join UOM as UOM17 on :TORLN.UOM=UOM17.IID
left join (select ITM, QUAN as AVQUAN from :TITPL where PIPL=:PLACE) as ITPL on ITPL.ITM=SRV
where OWNR in (:ORIDS);
