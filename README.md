# scrbr

1. docker build  --rm=true  -t vvass/tomcat .
2. docker run --name dev -p 8080:8080 -d  -it  vvass/tomcat
  a. before you do this make sure jar file is in the same place as your docker file
