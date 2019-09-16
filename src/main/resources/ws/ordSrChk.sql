select :TORLN.IID as IID, :TORLN.NME as NME, SRV, OWNR, PRI, TOT, TOTX, UOM114.NME as UOM114NME, UOM114.IID as UOM114IID, DT1, DT2,
case when AVQUAN is null or QUAN>AVQUAN then 0 else QUAN end as QUAN
from :TORLN
left join UOM as UOM114 on :TORLN.UOM=UOM114.IID
left join (select ITM, sum(QUAN) as AVQUAN from :TITPL group by ITM) as ITPL on ITPL.ITM=SRV
where OWNR in (:ORIDS);
