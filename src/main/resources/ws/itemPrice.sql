select case when I18ITM.NME is null then ITM.NME else I18ITM.NME end as ITMNME,
TXCT.IID as TXCTIID, TXCT.NME as TXCTNME, TXCT.AGRT as TXCTAGRT,
ITM.IID as ITMIID, ITMPRI.PRICT as PRICT, ITMPRI.PRI, ITMPRI.UNST
from :TITMPRI as ITMPRI 
join :TITM as ITM on ITMPRI.ITM=ITM.IID
left join TXCT on TXCT.IID=ITM.TXCT
left join (select NME, HASNM from :TI18ITM where HASNM=:ITMID and LNG=':LNG') as I18ITM on I18ITM.HASNM=ITM.IID
where ITM=:ITMID;
