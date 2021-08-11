# Тайный Санта
# rest routing
## ***/user/authorization*** - POST
### Тело — класс AuthenticationRequest
##### Возвращаемые статусы
* **OK(200)** - Пользователь успешно авторизовался. Возвращается User
* **Unauthorized(401)** - Коды ошибок: AuthenticationStatusCode(*USER_NOT_EXISTS*, *INVALID_PASSWORD*)
* **BadRequest(400)**

## ***/user/registration*** - POST
### Тело — класс RegistrationRequest
#### Возвращаемые статусы
* **Created(201)** - Пользователь успешно зарегистрировался. Возвращается User
* **NotAcceptable(406)** - Коды ошибок: RegistrationStatusCode(*EMAIL_NOT_AVAILABLE*, *PHONE_NOT_AVAILABLE*)
* **BadRequest(400)**

## ***/user/avatarUrl*** - POST
### Тело — класс SetAvatarUrlRequest
#### Возвращаемые статусы
* **OK(200)** - Аватар пользователя успешно установлен
* **NotFound(404)** - Пользователь не найден
* **BadRequest(400)**

## ***/guid/create*** - POST
### Тело — класс CreateGuidRequest
#### Возвращаемые статусы
* **Created(201)** - Возвращается созданные UUID
* **InternalServerError(500)** - Коды ошибок: GuidException(*ACCOUNT_ALREADY_LINKED*, *GUID_ALREADY_ISSUED*)
* **BadRequest(400)**

## ***/guid/connect*** - POST
### Тело — класс ConnectGuidRequest
#### Возвращаемые статусы
* **OK(200)** - Телеграмм аккаунт успешно привязан. Возвращается telegramId
* **NotFound(404)** - Пользователь не найден
* **InternalServerError(500)** - Коды ошибок: GuidException(*GUID_NOT_FOUNDED*)
* **BadRequest(400)**

## ***/session/create*** - POST
### Тело — класс CreateSessionRequest
#### Возвращаемые статусы
* **Created(201)** - Сессия успешно создана
* **NotFound(404)** - Пользователь не найден
* **BadRequest(400)**

## ***/session/join*** - POST
### Тело — класс JoinRequest
#### Возвращаемые статусы
* **OK(200)** - Пользователь успешно присоединился к сессии
* **NotFound(404)** - Пользователь не найден
* **InternalServerError(500)** - Коды ошибок: SessionException(*SESSION_NOT_FOUND*, *USER_ALREADY_IN_SESSION*, *SESSION_ALREADY_STARTED*, *SESSION_ALREADY_FINISHED*)
* **BadRequest(400)**

