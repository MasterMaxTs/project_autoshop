# Job4j_autoshop
#### Сервис "Cайт объявлений по продаже поддержанных машин"
[![Build Status](https://app.travis-ci.com/MasterMaxTs/project_autoshop.svg?branch=master)](https://app.travis-ci.com/MasterMaxTs/project_autoshop)
[![codecov](https://codecov.io/gh/MasterMaxTs/project_autoshop/branch/master/graph/badge.svg?token=Z2LCYQ24W4)](https://codecov.io/gh/MasterMaxTs/project_autoshop)<br>


![](https://img.shields.io/badge/java-11-4AB197)&nbsp;&nbsp;&nbsp;<br>
![](https://img.shields.io/badge/maven-3.6.3-4AB197)&nbsp;&nbsp;&nbsp;<br>
![](https://img.shields.io/badge/maven--checkstyle--plugin-3.1.1-4AB197)&nbsp;&nbsp;&nbsp;
![](https://img.shields.io/badge/maven--javadoc--plugin-3.2.0-4AB197)&nbsp;&nbsp;&nbsp;
![](https://img.shields.io/badge/liquibase--maven--plugin-3.6.2-4AB197)&nbsp;&nbsp;&nbsp;
![](https://img.shields.io/badge/jacoco--maven--plugin-0.8.6-4AB197)&nbsp;&nbsp;&nbsp;<br>
![](https://img.shields.io/badge/spring--boot--starter--web-2.7.3-4AB197)&nbsp;&nbsp;&nbsp;
![](https://img.shields.io/badge/spring--boot--starter--thymeleaf-2.7.3-4AB197)&nbsp;&nbsp;&nbsp;
![](https://img.shields.io/badge/hibenate--core-5.6.11-4AB197)&nbsp;&nbsp;&nbsp;
![](https://img.shields.io/badge/lombok-1.18.24-4AB197)&nbsp;&nbsp;&nbsp;<br>
![](https://img.shields.io/badge/Style:_bootstrap-4.4.1-4AB197)&nbsp;&nbsp;&nbsp;
![](https://img.shields.io/badge/Style:_html-5-4AB197)&nbsp;&nbsp;&nbsp;
![](https://img.shields.io/badge/Style:_css-3-4AB197)&nbsp;&nbsp;&nbsp;
![](https://img.shields.io/badge/DBMS:_PostgreSQL-14.0-4AB197)&nbsp;&nbsp;&nbsp;<br>
![](https://img.shields.io/badge/Test:_junit-4.13.2-4AB197)&nbsp;&nbsp;&nbsp;
![](https://img.shields.io/badge/Test:_hamcrest--all-1.3-4AB197)&nbsp;&nbsp;&nbsp;
![](https://img.shields.io/badge/Test:_mockito--core-4.9.0-4AB197)&nbsp;&nbsp;&nbsp;
![](https://img.shields.io/badge/Test:_h2database-1.4.200-4AB197)&nbsp;&nbsp;&nbsp;<br><br>
![](https://img.shields.io/badge/Package:-.war-4AB197)&nbsp;&nbsp;&nbsp;



### Это проект по созданию сайта с объявлениями по продаже машин, доступного в браузере.
#### Данный проект позволит _незарегистрированным_ пользователям: 
1. Просматривать все объявления, размещенные на сайте
    > Вкладка навигационного меню: _Все объявления_

2. Просматривать объявления, попавшие в архив (имеющие статус проданных)
    > Вкладка навигационного меню: _Архив_

3. Производить поиск объявлений, применив один из трёх фильтров
    > * Фильтр объявлений по заданным параметрам автомобиля
    > * Фильтр объявлений по заданной марке автомобиля и диапазону цены (между минимальной и максимальной)
    > * Фильтр объявлений, опубликованных за прошедшие сутки

4. Просматривать содержимое объявлений  
    >без возможности просмотра карточки контактов продавца,
истории владения данным автомобилем и динамики изменения цены в объявлении

#### Данный проект позволит _зарегистрированным_ пользователям:
1. Пользоваться возможностями сайта для незарегистрированных пользователей  


2. Публиковать свои объявления
    > Вкладка навигационного меню: _Создать объявление_

3. Просматривать лично опубликованные объявления
    > Вкладка навигационного меню: _Мои объявления_

4. Редактировать свои объявления
    > * Изменить содержимое: кнопка _Редактировать_
    > * Снять с публикации: кнопка _В архив_
    > * Удалить: кнопка _Удалить_

5. Просматривать содержимое объявлений
    > с возможностью просмотра карточки контактов продавца,
истории владения данным автомобилем и динамики изменения цены в объявлении


6. Добавлять понравившиеся объявление в избранное


7. Просматривать все объявления, добавленные в избранное
    > Вкладка навигационного меню: _Избранное_

8. Удалять объявление из избранного


9. Редактировать регистрационные данные профиля
    > Вкладка навигационного меню: _Профиль_

10. Оставлять заявку администраторам сайта на удаление личного профиля
    > Вкладка навигационного меню: _Профиль_


#### Администратору сайта доступны
1. Просмотр и удаление зарегистрированных пользователей
    > Навигационного меню: иконка _Заявки на удаление_

2. Просмотр и удаление архивных объявлений
    > Вкладка навигационного меню: _Архив_

---
### Стек технологий

- Java 11
- Maven 3.6.3
- Liquibase-maven-plugin v.3.6.2
- Spring-boot-starter-web v.2.7.3.
- Spring-boot-starter-thymeleaf v.2.7.3.
- Bootstrap v.4.4.1.
- Hibernate-core v.5.6.11.
- Lombok v.1.18.24.
- СУБД: PostgreSQL v.14.0.
<br><br>
- Тестирование:
  - JUnit v.4.13.2
  - Hamcrest v.1.3
  - Mockito-core v.4.9.0
  - БД: H2database v.1.4.200

---
### Требования к окружению
- Java 11
- Maven v.3.6.3
- PostgreSQL v.14.0

---
### Запуск проекта
1. Установить СУБД PostgreSQL


2. Создать базу данных с именем cars:<br>
```create database cars;```


3. Скачать файлы проекта с github по ссылке и разархивировать в выбранную директорию:<br>
   [https://github.com/MasterMaxTs/project_autoshop/archive](https://github.com/MasterMaxTs/project_autoshop/archive/refs/heads/master.zip)


4. Перейти в директорию проекта, открыть командную строку и выполнить команды:
- Для <ins>первого</ins> запуска приложения выполнить последовательно команды:
    - ```mvn package -Pproduction -Dmaven.test.skip=true```
    - ```java -jar target/cars-1.0.war```
    - внизу окна командной строки скопировать в буфер обмена url:
      <br>http://localhost:8080/index


- Для <ins>последующего</ins> запуска приложения выполнять команду:
    - ```java -jar target/cars-1.0.war```
    - внизу окна командной строки скопировать в буфер обмена url:
      <br>http://localhost:8080/index

      
      
5. Вставить из буфера обмена url в адресную строку браузера:<br>
   [http://localhost:8080/index](http://localhost:8080/index)

   
   
6. В базу данных пользователей сайта добавлена одна учётная запись пользователя в роли Администратор.

    > администратору сайта необходимо выполнить вход в систему со следующими учётными данными и сменить пароль
    > * логин: _admin_ 
    > * пароль: _adm123_
---
### Взаимодействие с приложением

1. Вид главной страницы приложения для пользователя сайта в роли User  

![img.png](img/index.JPG)

2. Вид главной страницы приложения для пользователя сайта в роли Admin  

![img.png](img/post-all-admin.JPG)

3. Вид главной страницы приложения с примененной фильтрацией объявлений по параметрам авто  

![img.png](img/post-filter-by-parameters.JPG)

4. Вид главной страницы приложения с примененной фильтрацией объявлений по марке авто и диапазону цены  

![img.png](img/post-filter-by-brand-and-price.JPG)

5. Вид главной страницы приложения с примененной фильтрацией объявлений, опубликованных за сутки  

![img.png](img/post-filter-by-last-day.JPG)

6. Вид страницы объявления для незарегистрированного пользователя  

![img.png](img/post-id-not-registered.JPG)

7. Вид страницы объявления для зарегистрированного пользователя  

![img.png](img/post-id-registered.JPG)

8. Вид страницы создания нового объявления   

![img.png](img/post-create.JPG)

9. Вид страницы редактирования объявления  

![img.png](img/post-update.JPG)

10. Вид страницы собственного объявления  

![img.png](img/post-id-personal.JPG)

11. Вид страницы объявления, добавленного в Избранное  

![img.png](img/post-id-favorite.JPG)

12. Вид страницы c избранными объявления  

![img.png](img/post-all-subscription.JPG)

13. Вид страницы с архивными объявлениями  

![img.png](img/post-all-archive.JPG)

14. Вид страницы с архивными объявлениями для пользователя сайта в роли Администратор (login = admin)  

![img.png](img/post-all-archive-in-role-admin.JPG)

15. Вид страницы аутентификации пользователя  

![img.png](img/user-authentication.JPG)

16. Вид страницы регистрации нового пользователя  

![img.png](img/user-registration.JPG)

17. Вид страницы редактирования профиля пользователя  

![img.png](img/user-id-update.JPG)

18. Вид страницы подтверждения отправки запроса на удаление Профиля пользователя  

![img.png](img/user-id-deletion-request-success.JPG)

19. Вид страницы со всеми заявками на удаление Профилей пользователей (admin)  

![img.png](img/admin-user-all-deletion-request.JPG)

20. Вид страницы со всеми зарегистрированными пользователями на сайте (admin)  

![img.png](img/admin-user-all.JPG)

---
### Контакты
* Email: java.dev-maxim.tsurkanov@yandex.ru
* Skype: https://join.skype.com/invite/ODADx0IJ3BBu
* VK: https://m.vk.com/id349328153
* Telegram: matsurkanov
