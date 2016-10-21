DIR=$(dirname $0)

if [ "$1" = "debug" ]; then
    DEBUG="-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=9999"
    shift
fi

echo "Starting sbt..."

JAVA_OPTS=$DEBUG
JAVA_OPTS="$JAVA_OPTS -Dconfig.file=./etc/application.conf"
JAVA_OPTS="$JAVA_OPTS -DallowOrigin=true"
JAVA_OPTS="$JAVA_OPTS -Djava.net.preferIPv4Stack=true"
JAVA_OPTS="$JAVA_OPTS -Xms512M -Xmx2048M -Xss1M -XX:+CMSClassUnloadingEnabled -XX:MaxPermSize=512M -XX:+PrintCommandLineFlags"
JAVA_OPTS="$JAVA_OPTS -Dsbt.repository.config=${DIR}/sbtrepositories -Dsbt.override.build.repos=true"

java ${JAVA_OPTS} -jar ${DIR}/sbt-launch.jar "$@"
