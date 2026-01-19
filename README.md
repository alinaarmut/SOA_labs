СОА 2
Запуск 1-ого сервиса:
1. Положить .war в папку webapps
2. Запустить из папки bin ./catalina.sh start


Запуск 2-ого сервиса:
1. Положить .war в папку в deployments
2. Запустить из папки bin ./standalone.sh

Запуск фронт:
npm start

СОА 3

## Сервисы

- `config-server` — Spring Cloud Config Server 
- `eureka-server` — сервер сервис-реестра (service discovery) для service-grammy
- `service-grammy` — сервис номинаций и жанров 
- `first-service` — сервис музыкальных групп (backend) и ejb

EJB-модуль запускается на **Apache TomEE** 
Используется как отдельный сервис и взаимодействует с backend
- `api-gateway` — API-шлюз для роутинга запросов к микросервисам
- `music-bands-frontend` — фронтенд для работы с музыкальными группами и номинациями

Запуск фронт:

npm start



