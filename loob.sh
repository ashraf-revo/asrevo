#!/usr/bin/env bash
echo ""> l.log
for i in {1..20}
do
#   curl http://104.198.195.171/actuator/info >>l.log
   curl http://gateway-streama.1d35.starter-us-east-1.openshiftapps.com/clone/message >>l.log
   echo "" >>l.log
done