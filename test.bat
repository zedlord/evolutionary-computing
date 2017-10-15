echo BentCigarFunction >>output.txt
FOR /l %%A IN (1,1,3) DO (
    java -jar testrun.jar -submission=player02Particle -evaluation=BentCigarFunction -seed=%%A >>output.txt
)
echo SchaffersEvaluation >>output.txt
FOR /l %%A IN (1,1,3) DO (
    java -jar testrun.jar -submission=player02Particle -evaluation=SchaffersEvaluation -seed=%%A >>output.txt
)