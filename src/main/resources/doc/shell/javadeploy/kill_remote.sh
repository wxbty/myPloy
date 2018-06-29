#!/bin/bash
# kill进程
#
# 参数：
# $1 remote_ip
# $2 UUID


ssh root@$1 "ps -aux|grep $2|grep -v grep|cut -c 9-15|xargs kill -9"
pkill -f $1
echo "服务已停止"