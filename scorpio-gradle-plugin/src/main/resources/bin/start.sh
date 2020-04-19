#!/usr/bin/env bash
# start script for scorpio application

set -e

# set locale
export LANG=en_US.UTF-8
export LC_CTYPE=en_US.UTF-8
export LC_ALL=en_US.UTF-8

# get app script dir
export APP_BIN="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

# get app home
export APP_HOME="$(dirname $APP_BIN)"; [ -d "$APP_HOME" ] || { echo "ERROR, failed to set scorpio app home."; exit 1;}


function get_pid {
	local ppid=""
	if [ -f $APP_BIN/sys.pid ]; then
		ppid=$(cat ${APP_BIN}/sys.pid)
	else
		if [[ $OS =~ Msys ]]; then
			# 在Msys上存在可能无法kill识别出的进程的BUG
			ppid=`jps -v | grep -w "\-Dmumble.app.name=$APP_NAME" | awk -F ' ' {'print $1'}`
		elif [[ $OS =~ Darwin ]]; then
			# 已知问题：'grep java' 可能无法精确识别java进程
			ppid=$(/bin/ps -o user,pid,command | grep "java" | grep -w "\-Dmumble.server.home=$APP_HOME/" | grep -v "mumble.server.command=stop " | grep -Ev "^root" |awk -F ' ' {'print $2'})
		else
			#在Linux服务器上要求尽可能精确识别进程
			ppid=$(ps -C java -o user,pid,command --cols 99999 | grep -w "\-Dmumble.server.home=$APP_HOME/" | grep -v "mumble.server.command=stop " | grep -Ev "^root" |awk -F ' ' {'print $2'})
		fi
	fi
	echo "$ppid";
}

