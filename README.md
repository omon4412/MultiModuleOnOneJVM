## Запуск двух модулей в одной JVM

Для реализации этого нужно:
 - Создать пустой Gradle проект
 - Добавить в этот проект наши модули
 - Прописать в settings.gradle путь к модулям
```groovy
include(':modules:vetrov-middle-service')
include(':modules:vetrov-telegram-bot')
```
 - Прописать в build.gradle путь к модулям
```groovy
implementation project(':modules:vetrov-middle-service')
implementation project(':modules:vetrov-telegram-bot')
```
 - Создать application-{название-модуля}.yaml для каждого модуля
 - Прописать необходимые переменные в каждый application-{название-модуля}.yaml
```yaml
# Пример для моего случая
application:
  middleService:
    url: ${MIDDLE_SERVICE_URL:http://localhost:8080}
  services:
    type: inMemory
  backendService:
    url: ${BACKEND_SERVICE_URL:http://localhost:8081}
bot:
  name: MiniBankTelegramBot
  token: ${BOT_TOKEN}
```
 - В main функции создать контексты наших модулей
```java
public class MultiModulesInOneJVMApplication {
    public static void main(String[] args) {
        new SpringApplicationBuilder()
                .sources(MiddleServiceApplication.class)
                .profiles("middle")
                .run(args);
        new SpringApplicationBuilder()
                .sources(TelegramBotApplication.class)
                .profiles("telegrambot")
                .run(args);
    }
}
```
 - Запустить проект c помощью gradle:
```gradle
./gradlew bootRun --args='--bot.token={token}'
```
Это решение наверное странное, но зато можно запускать модули на одной JVM и на разных портах