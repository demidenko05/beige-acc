select SAINSRLN.IID as IID, SAINSRLN.TXFC as TXFC, ITM14.IID as ITM14IID, SAINSRLN.TDSC as TDSC, SAINSRLN.TOT as TOT,
SAINSRLN.SUBT as SUBT, SAINSRLN.PRI as PRI, SAINSRLN.TOTX as TOTX, SAINSRLN.QUAN as QUAN, SAINSRLN.TOFC as TOFC,
UOM118.IID as UOM118IID,
case when I18ITM.NME is null then ITM14.NME else I18ITM.NME  end as ITM14NME,
case when I18UOM.NME is null then UOM118.NME else I18UOM.NME  end as UOM118NME
from SAINSRLN
left join SRV as ITM14 on SAINSRLN.ITM=ITM14.IID
left join UOM as UOM118 on SAINSRLN.UOM=UOM118.IID
left join (select NME, HASNM from I18SRV where LNG=':LNG') as I18ITM on ITM14.IID=I18ITM.HASNM
left join (select NME, HASNM from I18UOM where LNG=':LNG') as I18UOM on UOM118.IID=I18UOM.HASNM
where SAINSRLN.RVID is null and SAINSRLN.OWNR=:OWNR;
