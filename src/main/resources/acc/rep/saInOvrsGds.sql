select SAINGDLN.IID as IID, SAINGDLN.TXFC as TXFC, ITM14.IID as ITM14IID, SAINGDLN.TDSC as TDSC, SAINGDLN.TOT as TOT,
SAINGDLN.SUBT as SUBT, SAINGDLN.PRI as PRI, SAINGDLN.TOTX as TOTX, SAINGDLN.QUAN as QUAN, SAINGDLN.TOFC as TOFC,
UOM118.IID as UOM118IID,
case when I18ITM.NME is null then ITM14.NME else I18ITM.NME  end as ITM14NME,
case when I18UOM.NME is null then UOM118.NME else I18UOM.NME  end as UOM118NME
from SAINGDLN
left join ITM as ITM14 on SAINGDLN.ITM=ITM14.IID
left join UOM as UOM118 on SAINGDLN.UOM=UOM118.IID
left join (select NME, HASNM from I18ITM where LNG=':LNG') as I18ITM on ITM14.IID=I18ITM.HASNM
left join (select NME, HASNM from I18UOM where LNG=':LNG') as I18UOM on UOM118.IID=I18UOM.HASNM
where SAINGDLN.RVID is null and SAINGDLN.OWNR=:OWNR;
