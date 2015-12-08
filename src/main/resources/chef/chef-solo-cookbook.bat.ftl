@echo off
setlocal
echo Executing chef-solo with recipe "${deployed.recipe}" with file ${deployed.file.name}
<#if deployed.container.tarCommand?has_content >
    echo Unpacking the archive ${deployed.file.name} using provided tar command
    ${deployed.container.tarCommand} ${deployed.file.path}
<#else>
    echo Unpacking the archive ${deployed.file.name} using default tar command
    ${deployed.container.chefPath}\tar.exe -xf ${deployed.file.path}
</#if>
echo cookbook_path ['%CD%'] > solo.rb
${deployed.container.chefPath}\chef-solo.bat -c solo.rb  -o "${deployed.recipe}"
set RES=%ERRORLEVEL%
if not %RES% == 0 (
  exit %RES%
)
endlocal