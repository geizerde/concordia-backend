# concordia-backend

# API

## Общедоступные конечные точки

| Method | Endpoint         | Description          | Request Body        | Response Body       |
|--------|------------------|----------------------|---------------------|---------------------|
| POST   | /login           | Логин               | `{ "email": "string", "password": "string" }` | `{ "status": true, "data": { "access_token": "string" } }` |
| POST   | /register        | Регистрация         | `{ "name": "string", "email": "string", "phone": "string", "password": "string", "description": "string", "age": int, "city": { "id": int, "region": { "id": int, "country": { "id": int } } } }` | `{ "status": true, "data": { "id": int, "name": "string", "email": "string", "phone": "string", "description": "string", "age": int, "city": { "id": int, "name": "string", "region": { "id": int, "name": "string", "country": { "id": int, "name": "string" } } }, "photos": null, "tags": null, "is_active": true, "role_code": "string" } }` |

## Конечные точки для работы с локациями

| Method | Endpoint         | Description          | Request Body        | Response Body       |
|--------|------------------|----------------------|---------------------|---------------------|
| GET    | /api/locations/countries | Получить все доступные страны | - | `{ "status": true, "data": [ { "id": int, "name": "string" } ] }` |
| GET    | /api/locations/regions/{country_id} | Получить все доступные регионы страны | - | `{ "status": true, "data": [ { "id": int, "name": "string", "country": { "id": int, "name": "string" } } ] }` |
| GET    | /api/locations/cities/{region_id} | Получить все доступные города региона | - | `{ "status": true, "data": [ { "id": int, "name": "string", "region": { "id": int, "name": "string", "country": { "id": int, "name": "string" } } } ] }` |

## Конечные точки для работы с фото

| Method | Endpoint         | Description          | Request Body        | Response Body       |
|--------|------------------|----------------------|---------------------|---------------------|
| POST   | /api/photos/upload | Загрузка фото       | `file: {file}`      | `{ "status": true, "data": { "id": int, "path": "string", "isAvatar": bool, "userId": int } }` |
| POST   | /api/photos/{photo_id}/set-avatar | Установить фото как аватар | - | `{ "status": true, "data": { "id": int, "path": "string", "isAvatar": bool, "userId": int } }` |

## Конечные точки для работы с тегами

| Method | Endpoint         | Description          | Request Body        | Response Body       |
|--------|------------------|----------------------|---------------------|---------------------|
| GET    | /api/tags        | Получить все доступные теги | - | `{ "status": true, "data": [ { "id": int, "name": "string" } ] }` |
| POST   | /api/tags        | Создать тег          | `{ "name": "string" }` | `{ "status": true, "data": { "id": int, "name": "string" } }` |
| POST   | /api/tags/user   | Привязать теги к пользователю | `{ "tagIds": [int] }` | `{ "status": true, "data": { "id": int, "name": "string", "tags": [ { "id": int, "name": "string" } ] } }` |

## Конечные точки для работы с пользователем

| Method | Endpoint         | Description          | Request Body        | Response Body       |
|--------|------------------|----------------------|---------------------|---------------------|
| GET    | /api/users/me    | Получить информацию о текущем пользователе | - | `{ "status": true, "data": { "id": int, "name": "string", "email": "string", "phone": "string", "description": "string", "age": int, "city": { "id": int, "name": "string", "region": { "id": int, "name": "string", "country": { "id": int, "name": "string" } } }, "photos": [], "tags": [], "is_active": true, "role_code": "string" } }` |

## Конечные точки для работы с чатами и сообщениями

| Method | Endpoint         | Description          | Request Body        | Response Body       |
|--------|------------------|----------------------|---------------------|---------------------|
| GET    | /messages/{senderId}/{recipientId}/count | Получить количество непрочитанных сообщений | - | `{ "status": true, "data": { "count": int } }` |
| GET    | /messages/{senderId}/{recipientId} | Получить все сообщения из чата | - | `{ "status": true, "data": [ { "id": "string", "content": "string", "timestamp": "string", "status": "string", "chat_id": "string", "sender_id": int, "recipient_id": int, "sender_name": "string", "recipient_name": "string" } ] }` |
| GET    | /messages/{message_id} | Получить конкретное сообщение по ID | - | `{ "status": true, "data": { "id": "string", "content": "string", "timestamp": "string", "status": "string", "chat_id": "string", "sender_id": int, "recipient_id": int, "sender_name": "string", "recipient_name": "string" } }` |
| GET    | /chats          | Получить чаты с пользователями | - | `{ "status": true, "data": [ { "user": { "id": int, "name": "string", "email": "string", "phone": "string", "description": "string", "age": int, "city": { "id": int, "name": "string", "region": { "id": int, "name": "string", "country": { "id": int, "name": "string" } } }, "photos": [], "tags": [], "is_active": true, "role_code": "string" }, "last_message": "string" } ] }` |

## Конечные точки для матчей

| Method | Endpoint         | Description          | Request Body        | Response Body       |
|--------|------------------|----------------------|---------------------|---------------------|
| GET    | /api/matches     | Получить пользователей для матчей | `{ "count_neighbors": int, "mutation_chance": float }` | `{ "status": true, "data": [ { "receiver": { "id": int, "name": "string", "email": "string", "phone": "string", "description": "string", "age": int, "city": { "id": int, "name": "string", "region": { "id": int, "name": "string", "country": { "id": int, "name": "string" } } }, "photos": [], "tags": [], "is_active": true, "role_code": "string" }, "coverage": float } ] }` |
| GET    | /api/matches/last/{count} | Получить n последних матчей | - | `{ "status": true, "data": [ { "id": int, "name": "string", "email": "string", "phone": "string", "description": "string", "age": int, "city": { "id": int, "name": "string", "region": { "id": int, "name": "string", "country": { "id": int, "name": "string" } } }, "photos": [], "tags": [], "is_active": true, "role_code": "string" } ] }` |
| POST   | /api/matches     | Совершить действие с матчами | `{ "receiver": { "id": int }, "is_liked": bool }` | `{ "status": true, "data": { "receiver": { "id": int, "name": "string", "email": "string", "phone": "string", "description": "string", "age": int, "city": { "id": int, "name": "string", "region": { "id": int, "name": "string", "country": { "id": int, "name": "string" } } }, "photos": [], "tags": [], "is_active": true, "role_code": "string" }, "is_liked": bool } }` |


## Подключение через WebSocket

Для подключения к WebSocket серверу для работы с чатом можно использовать следующий пример кода:

```javascript
function connect() {
    stompClient = new Client({
        brokerURL: 'ws://localhost:8080/ws', // Замените на ваш WebSocket URL
        connectHeaders: {
            Authorization: `Bearer ${authToken}`
        },
        onConnect: () => {
            logMessage(`Подключен как ${userId}`);

            // Подключение к чату
            stompClient.subscribe(`/users/queue/messages`, onMessageReceived);
            
            // Подключение к сокету ошибок
            stompClient.subscribe(`/users/queue/errors`, onMessageReceived);
        },
        onStompError: (error) => {
            logMessage(`Ошибка подключения: ${error}`);
        }
    });

    stompClient.activate();
}

function onMessageReceived(payload) {
    const message = JSON.parse(payload.body);
    logMessage(`Получено сообщение: ${JSON.stringify(message, null, 2)}`);
}

document.getElementById('message-form').addEventListener('submit', (event) => {
    event.preventDefault();
    const message = document.getElementById('message').value.trim();

    try {
        const parsedMessage = JSON.parse(message);
        stompClient.publish({ destination: "/app/chat", body: JSON.stringify(parsedMessage) });
        logMessage(`Отправлено сообщение: ${JSON.stringify(parsedMessage, null, 2)}`);
    } catch (e) {
        logMessage('Ошибка отправки: некорректный JSON');
    }
});

document.getElementById('disconnect').addEventListener('click', () => {
    if (stompClient) {
        stompClient.deactivate();
        logMessage('Отключено');
    }
    chatPage.classList.add('hidden');
    userPage.classList.remove('hidden');
});

function logMessage(message) {
    const logItem = document.createElement('div');
    logItem.textContent = message;
    logs.appendChild(logItem);
    logs.scrollTop = logs.scrollHeight;
}
```

### Пример тела сообщения

```json
{
    "senderId": "1",
    "recipientId": "2",
    "senderName": "Bob",
    "recipientName": "Alice",
    "content": "Hey Bob, how's it going?",
    "timestamp": "2024-12-02T21:30:33.276+03:00"
}
```

