FROM postgres:16

COPY init.sh /docker-entrypoint-initdb.d/init.sh
COPY backup.sql /docker-entrypoint-initdb.d/backup.sql

RUN chmod +x /docker-entrypoint-initdb.d/init.sh
RUN chmod +x /docker-entrypoint-initdb.d/backup.sql

EXPOSE 5432
