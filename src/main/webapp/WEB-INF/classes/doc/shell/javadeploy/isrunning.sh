#!/bin/bash

# 查看进程是否正在运行
#
# 参数：
# $1 UUID
# $1 远程ip

ssh root@$2
"ps -ef | grep $1"