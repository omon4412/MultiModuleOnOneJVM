# Middle service

## Описание проекта
Java сервис. Принимает запросы от telegram-бота, выполняет валидацию и бизнес логику, маршрутизирует запросы в "Банк".

## Схема взаимодействия компонентов
<img src="img/scheme.png" alt="Схема взаимодействия компонентов">

```plantuml
@startuml

actor User as user
participant "Telegram-bot" as frontend
participant "middle" as middle #red
participant "backend" as backend

user -> frontend : Взаимодействие с ботом
activate frontend

frontend -> middle : HTTP запрос
activate middle
    middle -> middle : Валидация
alt Данные валидны
    middle -> backend : HTTP запрос

    activate backend
    backend -> backend : Обработка запроса 
    backend --> middle : Результат
    deactivate backend
    
    middle --> frontend : Результат
else Данные невалидны
    middle --> frontend : Ошибка
    deactivate middle
end
    frontend --> user: Ответ
deactivate frontend
deactivate user
@enduml
```

## Автор
>**Ветров Сергей**
>- [GitHub](https://github.com/omon4412)
>- [Email](mailto:vetrov241201@yandex.ru)