FROM openjdk:17

WORKDIR /app

COPY build/libs/control.deudas-0.0.1-SNAPSHOT.jar /app/control.deudas.jar

ENV db_jdb_url jdbc:postgresql://192.168.4.70:5432/finanzas
ENV db_user postgres
ENV db_pass Stonesour25!

ENTRYPOINT ["java", "-jar", "control.deudas.jar"]
