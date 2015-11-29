#!/bin/sh

echo Executing chef-solo with recipe "${deployed.recipe}" with file ${deployed.file.name}
tar zxf ${deployed.file.path}
echo "cookbook_path ['$PWD']" > solo.rb
${deployed.container.chefPath}/chef-solo -c solo.rb  -o "${deployed.recipe}"
rc=$?
if [ $rc -ne 0 ]; then
 echo "Failed to execute chef recipe." >&2
 exit $rc
fi