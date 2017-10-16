echo BentCigarFunction >>BentCigar.txt
FOR /l %%A IN (1,1,20) DO (
    echo %%A>>BentCigar.txt
    FOR /l %%B IN (1,1,20) DO (
        java -jar testrun.jar -submission=player02Particle -evaluation=BentCigarFunction -seed=%%A >>BentCigar.txt
    )
)

REM echo SchaffersEvaluation >>Schaffers.txt
REM FOR /l %%A IN (1,1,20) DO (
REM     SET seed=%%A
REM     echo %seed% >>Schaffers.txt
REM     FOR /l %%B IN (1,1,20) DO (
REM         java -jar testrun.jar -submission=player02Tournament -evaluation=SchaffersEvaluation -seed=%%A >>Schaffers.txt
REM     )
REM )
