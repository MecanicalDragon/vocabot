#!/bin/bash

git commit -am "trigger new build"
git push heroku master

echo '
Confirm job is done.
'

while [ true ] ; do
read -t 3 -n 1
if [ $? = 0 ] ; then
exit ;
fi
done
