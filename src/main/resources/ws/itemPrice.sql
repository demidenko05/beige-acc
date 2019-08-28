select case when I18ITM.NME is null then ITM10.NME else I18ITM.NME end as ITM10NME,
TXCT21.IID as TXCT21IID, TXCT21.NME as TXCT21NME, TXCT21.AGRT as TXCT21AGRT,
ITM10.IID as ITM10IID, ITMPRI.PRICT, ITMPRI.PRI, ITMPRI.UNST
from :TITMPRI as ITMPRI 
join :TITM as ITM10 on ITMPRI.ITM=ITM10.IID
left join TXCT as TXCT21 on TXCT21.IID=ITM10.TXCT
left join (select NME, HASNM from :TI18ITM where HASNM=:ITMID and LNG=':LNG') as I18ITM on I18ITM.HASNM=ITM10.IID
where ITM=:ITMID;
