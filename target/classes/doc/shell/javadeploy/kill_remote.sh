#!/bin/bash
# kill进程
#
# 参数：
# $1 remote_ip
# $2 UUID

ssh root@$1 "
pid = ps -aux|grep $2|grep -v grep|cut -c 9-15;
if [ -z pid ]
then
	echo 进程已死直接重启
else
    ps -aux|grep $2|grep -v grep|cut -c 9-15|xargs kill -9
fi
"
echo "服务已停止"ps -aux|grep $2|grep -v grep|cut -c 9-15|xargs kill -9