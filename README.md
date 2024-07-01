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

Теперь можно запускать эти модули и в разных и в одной JVM

# Проблема
 - Если делать так
```java
public class MultiModulesInOneJVMApplication {
    public static void main(String[] args) {
        SpringApplication.run(new Class[]{MiddleServiceApplication.class, TelegramBotApplication.class}, args);
    }
}
```
 - то возникает проблема:
```text
Caused by: org.springframework.context.annotation.ConflictingBeanDefinitionException: Annotation-specified bean name 'accountServiceImpl' for bean class [ru.omon4412.minibank.telegrambot.service.AccountServiceImpl] conflicts with existing, non-compatible bean definition of same name and class [ru.omon4412.minibank.middle.service.AccountServiceImpl]
```
 - У меня есть в двух модулях одиннаковые названия классов которые естественно конфликтуют.
 - Я пробовал разные способы: и через переопределение BeanFactoryPostProcessor, и через ручное создание бинов @Bean и через ApplicationListener<ContextRefreshedEvent> и через BeanDefinitionRegistry.
 - Но проблема в том, что я не могу достать до этих конфликтующих бинов, потому что при создании контекста их нет, либо есть бины самих модулей, и приложение падает с ошибкой.
 - Есть решение, это в аннотации @Service прописать название бина @Service("middleAccountServiceImpl) и @Service("telegramAccountServiceImpl)
 - Но тогда это нарушает условие задание на запрет редактирования кода модулей.