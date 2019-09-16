select :TORLN.IID as IID, :TORLN.NME as NME, GOOD, OWNR, PRI, TOT, TOTX, UOM112.NME as UOM112NME, UOM112.IID as UOM112IID,
case when AVQUAN is null or QUAN>AVQUAN then 0 else QUAN end as QUAN
from :TORLN
left join UOM as UOM112 on :TORLN.UOM=UOM112.IID
left join (select ITM, sum(QUAN) as AVQUAN from :TITPL group by ITM) as ITPL on ITPL.ITM=GOOD
where OWNR in (:ORIDS)
