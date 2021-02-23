#!/bin/bash
# Deploy maven artefact in current directory into Maven central repository 
# using maven-release-plugin goals
#set environment variables: GPG_USER_ID, GPG_KEY_NAME, JAR_KEY_ALIAS, JAR_PASS_PATH, GPG_PASS_PATH, SSH_KEY_PATH, OSSRH_PASS_PATH

read -p "Really deploy to maven cetral repository  (yes/no)? "

if ( [ "$REPLY" == "yes" ] ) then
  ssh-add $SSH_KEY_PATH
  ssh-add -l
  jarpassw=`gpg --no-verbose -d -r "$GPG_USER_ID" $JAR_PASS_PATH 2>/dev/null`
  ossrhpassw=`gpg --no-verbose -d -r "$GPG_USER_ID" $OSSRH_PASS_PATH 2>/dev/null`
  gpgpassw=`gpg --no-verbose -d -r "$GPG_USER_ID" $GPG_PASS_PATH 2>/dev/null`
  mvn -Darguments="-Prelease -Dandroid.release=true -Dossrhpass=$ossrhpassw -Dsignpass=$jarpassw -Dsignalias=$JAR_KEY_ALIAS -Dgpgpass=$gpgpassw -Dgpgkeyname=$GPG_KEY_NAME" release:clean release:prepare release:perform -B -e | tee maven-central-deploy.log
  ssh-add -D
else
  echo 'Exit without deploy'
fi

