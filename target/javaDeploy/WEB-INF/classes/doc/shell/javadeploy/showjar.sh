#!/bin/bash

# 查看日志
#
# 参数：
# $1 项目部署路径

cd $1
ls *.jar| grep -v original
