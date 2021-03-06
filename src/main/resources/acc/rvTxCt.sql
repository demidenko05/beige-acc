select IT.IID as ITIID, IT.NME as ITMNE, TCO.IID as TCOIID, TCO.NME as TCONME, TCO.AGRT as TCOAGRT, TCDIID, TCDNME, TCDAGRT, STRM from :TITM as IT
left join TXCT  as TCO on IT.TXCT=TCO.IID
left join (select OWNR, TXCT.IID as TCDIID, TXCT.NME as TCDNME, AGRT as TCDAGRT, STRM from :TTDL as ITDL
  join TXDST on TXDST.IID=ITDL.TXDS
  left join TXCT on TXCT.IID=ITDL.TXCT where OWNR=:ITM and TXDS=:TXDS) as TCD on TCD.OWNR=IT.IID
where IT.IID=:ITM;
