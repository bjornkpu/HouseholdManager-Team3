#Viser alle prosessene startet av brukeren vår
SHOW PROCESSLIST ;

#Kopier resultatet av denne spørringa for å drepe alle connections som ikke utfører spørringer.
SELECT CONCAT('KILL ',id,';') AS run_this FROM information_schema.processlist WHERE user='g_tdat2003_t3' AND info IS NULL ORDER BY id;