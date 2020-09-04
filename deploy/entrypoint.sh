#!/bin/sh

java -jar app.jar \
          --spring.redis.port=$REDIS_PORT \
          --spring.redis.host=$REDIS_HOST \
          --judge-authorization.user-id=$JUDGE_HOST_USER_ID \
          --judge-authorization.user-secret=$JUDGE_HOST_USER_SECRET \
          --judge-authorization.secret-key=$JUDGE_HOST_SECRET_KEY