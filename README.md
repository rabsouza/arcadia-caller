# Arcadia Caller GAE

Essa aplicação faz parte do Backend da aplicação **Arcadia Caller**.
Foi implementada seguindo o padrão de aplicações **Spring MVC** e **RestFul**.


## Funcionalidade

* Disponível através da api **"api/v1/"**
* Testa a APP: [uri = "ping", method = GET]
* Status da APP: [uri = "health", method = GET, produces = "application/json"]
* Login na APP: [uri = "login/{username}", method = GET]
* Cria novo usuário na APP: [uri = "login/", method = POST, produces = "application/json", consumers = "application/json"]


## Frameworks

* maven
* appengine
* springframework
* aspectj
* objectify
* hibernate-validate
* jackson
* junit
* mockito
* hamcrest
* guava
* logback
* lombok
* javax.el

### Versão

1.0.0

### Versão

Validar a app: [Arcadia-Caller.health()]

## Como usar

Fazer o clone do reposítório git e baixar as dependências
```sh
$ git clone [git-repo-url] arcadia-caller
$ cd arcadia-caller
$ mvn clean install
```
Executar o GAE local
```sh
$ mvn appengine:devserver
```
Fazer upload da app para GAE
```sh
$ mvn appengine:update
```

## Contatos

- **Nome:** Rafael Alessandro Batista de Souza
- **E-mail:** rabsouza@gmail.com
- **Celular:** (31) 98801-2741

## License

GNU GENERAL PUBLIC LICENSE




**Free Source Code, Hell Yeah! :+1:**

[//]: # (These are reference links used in the body of this note and get stripped out when the markdown processor does its job. There is no need to format nicely because it shouldn't be seen. Thanks SO - http://stackoverflow.com/questions/4823468/store-comments-in-markdown-syntax)

[Arcadia-Caller.health()]: <https://arcadia-caller.appspot.com/api/v1/health>
