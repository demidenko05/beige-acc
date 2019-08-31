select :TORLN.IID as IID, :TORLN.NME as NME, GOOD, OWNR, PRI, TOT, TOTX, UOM15.NME as UOM15NME, UOM15.IID as UOM15IID,
case when AVQUAN is null or QUANT>AVQUAN then 0 else QUANT end as QUANT
from :TORLN
left join UOM as UOM15 on :TORLN.UOM=UOM15.IID
left join (select ITM, QUAN as AVQUAN from :TITPL where PIPL=:PLACE) as ITPL on ITPL.ITM=GOOD
where OWNR in (:ORIDS);
