#!/bin/bash
# kill进程
#
# 参数：
# $1 remote_ip
# $2 UUID

echo "kill_remote,remote_ip=$1,uuid=$2";
ssh root@$1 "ps -aux|grep $2|grep -v grep|cut -c 9-15|xargs kill -9"
echo "服务已停止"