#!/bin/bash

# 查看进程是否正在运行
#
# 参数：
# $1 UUID
# $2 远程地址

ssh root@$2 "ps -ef | grep $1"