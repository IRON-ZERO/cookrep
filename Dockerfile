# Tomcat 9.0을 베이스 이미지로 사용
FROM tomcat:9.0

# ROOT.war를 루트 컨텍스트로 배포
COPY target/ROOT.war /usr/local/tomcat/webapps/

# 포트 노출
EXPOSE 8080

# 컨테이너 시작 시 Tomcat 실행
CMD ["catalina.sh", "run"]