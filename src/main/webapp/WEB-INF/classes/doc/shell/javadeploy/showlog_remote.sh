#!/bin/bash

# 查看日志
#
# 参数：
# $1 进程UUID
# $2 项目部署路径
# $3 远程ip地址

ssh root@$3 "
tail -f -n 500 $2/$1/nohup.out
"