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
 - Прописать необходимые переменные в application.yaml
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
 - В main функции создать контекст c нашими модулями
```java
public class MultiModulesInOneJVMApplication {
    public static void main(String[] args) {
        SpringApplication.run(new Class[]{MiddleServiceApplication.class, TelegramBotApplication.class}, args);
    }
}
```
 - Запустить проект c помощью gradle:
```gradle
./gradlew bootRun --args='--bot.token={token}'
```

Теперь можно запускать эти модули и в разных и в одной JVM