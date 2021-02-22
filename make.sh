#!/bin/bash
#set environment variables: GPG_USER_ID, GPG_KEY_NAME, JAR_KEY_ALIAS, JAR_PASS_PATH, GPG_PASS_PATH
jarpassw=`gpg --no-verbose -d -r "$GPG_USER_ID" $JAR_PASS_PATH 2>/dev/null`
gpgpassw=`gpg --no-verbose -d -r "$GPG_USER_ID" $GPG_PASS_PATH 2>/dev/null`
mvn clean install -Prelease -Dsignpass=$jarpassw -Dsignalias=$JAR_KEY_ALIAS -Dgpgpass=$gpgpassw -Dgpgkeyname=$GPG_KEY_NAME

