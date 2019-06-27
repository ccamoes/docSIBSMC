@echo off
cls
Title PRT Server CETELEM SPP
:LOOP
java -cp PrtSrv30-sa.jar sibs.deswin.server.ServerGroup PrtServer.cfg
GOTO LOOP

