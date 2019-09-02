select :TORLN.IID as IID, GOOD, OWNR, PRI, TOT, TOTX, UOM15.NME as UOM15NME, UOM15.IID as UOM15IID,
case when AVQUAN is null or QUAN>AVQUAN then 0 else QUAN end as QUAN
from :TORLN
left join UOM as UOM15 on :TORLN.UOM=UOM15.IID
left join (select ITM, sum(QUAN) as AVQUAN from :TITPL group by ITM) as ITPL on ITPL.ITM=GOOD
where OWNR in (:ORIDS)
