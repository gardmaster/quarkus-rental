# Quarkus Rental (Locadora de Veículos)

## Supersonic Rides. Subatomic Prices.

#### Projeto Educacional Ada Tech - Instrutor [Matheus Cruz](https://github.com/mcruzdev)
<br/>

<p align="center">
  <img src="https://img.shields.io/badge/Java-21-blue?style=for-the-badge" />
  <img src="https://img.shields.io/badge/Quarkus-3-red?style=for-the-badge" />
  <img src="https://img.shields.io/badge/Maven-Build-orange?style=for-the-badge" />
  <img src="https://img.shields.io/badge/H2-Database-lightgrey?style=for-the-badge" />
  <img src="https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=docker&logoColor=white" />
</p>

### 📬 [Postman Collection](https://github.com/gardmaster/quarkus-rental/blob/main/postman/quarkus-rental.postman_collection.json) 
``` A ordem das requisições elaboradas na collection representa o caminho feliz 🎯 ```

``` Vehicle API -> http port 8080 📌 ```

``` Booking API -> http port 9090 📌 ```

<br/>

### 🚩 [Tags Importantes](https://github.com/gardmaster/quarkus-rental/tags)

[v3.0.0](https://github.com/gardmaster/quarkus-rental/releases/tag/v3.0.0) -> [Projeto Final](https://github.com/mcruzdev/aluga-simples/blob/main/PROJETO_FINAL_QUARKUS_AVANCADO.md) concluído | Itens de pontuação extra não contemplados: Redis e Kafka

**Observações importantes:**  
- É necessário ter o **Docker** instalado e em execução.  
- O **Keycloak** é iniciado automaticamente junto com o serviço **Vehicle API**, e utilizado pelas duas APIs. 
- É necessário criar manualmente a **role** `employee` e um **usuário** vinculado a esta `role`, pois o *partial export* do Keycloak não exporta usuários.
- O `employee` é necessário para contemplar os itens de pontuação extra: `check-in`, `check-out` e `cancel`.
- O `user` só pode criar uma reserva e visualizar as suas. Isto é realizado através da **claim** `sub` do JWT Token.

[v2.0.1](https://github.com/gardmaster/quarkus-rental/tree/v2.0.1) -> Exercícios [1](https://github.com/mcruzdev/aluga-simples/blob/main/EXERCISE1.md) e [2](https://github.com/mcruzdev/aluga-simples/blob/main/EXERCISE2.md) concluídos | Finalização do projeto para o Módulo Quarkus Básico

[v1.1.1](https://github.com/gardmaster/quarkus-rental/tree/v1.1.1) -> [Exercício 1](https://github.com/mcruzdev/aluga-simples/blob/main/EXERCISE1.md) + testes unitários e de integração para a entidade Vehicle requeridos no [Exercício 2](https://github.com/mcruzdev/aluga-simples/blob/main/EXERCISE2.md)

[v1.0.0](https://github.com/gardmaster/quarkus-rental/tree/v1.0.0) -> Representa a resolução do [Exercício 1](https://github.com/mcruzdev/aluga-simples/blob/main/EXERCISE1.md) do Módulo Quarkus Básico
