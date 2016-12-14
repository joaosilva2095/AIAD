@echo off
SET IP=127.0.0.1

ECHO Starting test at %IP%

FOR /L %%i IN (1,1,10000) DO (
	ECHO Test # %%i
	ECHO %IP%
	start "BOARD" java -cp pows_main.jar aiad.feup.Main %IP% 1099 board 30
	timeout 5

	ECHO Starting HIGH
	FOR /L %%j IN (1,1,5) DO (
		start "HIGH_%%j" java -cp pows_main.jar aiad.feup.Main %IP% 1099 player HIGH_%%j high %IP% 110%%j
		timeout 1
	)

	ECHO Starting LOW
	FOR /L %%j IN (1,1,5) DO (
		start "LOW_%%j" java -cp pows_main.jar aiad.feup.Main %IP% 1099 player LOW_%%j high %IP% 120%%j
		timeout 1
	)

	ECHO Starting RANDOM
	FOR /L %%j IN (1,1,5) DO (
		start "RANDOM_%%j" java -cp pows_main.jar aiad.feup.Main %IP% 1099 player RANDOM_%%j high %IP% 130%%j
		timeout 1
	)

	timeout 180
)