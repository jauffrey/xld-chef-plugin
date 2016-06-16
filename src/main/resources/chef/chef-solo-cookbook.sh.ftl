#!/bin/sh
#
# THIS CODE AND INFORMATION ARE PROVIDED "AS IS" WITHOUT WARRANTY OF ANY KIND, EITHER EXPRESSED OR
# IMPLIED, INCLUDING BUT NOT LIMITED TO THE IMPLIED WARRANTIES OF MERCHANTABILITY AND/OR FITNESS
# FOR A PARTICULAR PURPOSE. THIS CODE AND INFORMATION ARE NOT SUPPORTED BY XEBIALABS.
#

echo Executing chef-solo with recipe "${deployed.recipe}" with file ${deployed.file.name}
tar zxf ${deployed.file.path}
echo "cookbook_path ['$PWD']" > solo.rb
${deployed.container.chefPath}/chef-solo -c solo.rb  -o "${deployed.recipe}"
rc=$?
if [ $rc -ne 0 ]; then
 echo "Failed to execute chef recipe." >&2
 exit $rc
fi
