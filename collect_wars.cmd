rmdir ..\asba-backend-wars
mkdir ..\asba-backend-wars
for /R .\ %%f in (*.war) do copy "%%f" ..\asba-backend-wars\