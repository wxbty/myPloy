#!/bin/bash

# 查看日志
#
# 参数：
# $1 项目部署路径
# $2 远程ip地址

echo begin_showjar;
ssh root@$2 "
cd $1
ls *.jar| grep -v original
"