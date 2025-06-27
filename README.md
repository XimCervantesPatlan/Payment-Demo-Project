
# Payment Demo Project

Este proyecto simula una orquestación de pagos usando Temporal.io, Spring Boot y una arquitectura distribuida basada en microservicios.

### Arquitectura

La arquitectura contempla los siguientes componentes:

- **temporal-orchestrator:** A través de un endpoint recibe la solicitud de pago e inicia el Temporal.io Workflow que coordinará las actividades que deben realizar los microservicios para llevar a cabo la transacción.
- **eureka-server:** Servidor de Service Discovery de Spring Cloud Eureka que permitirá el reconocimento entre los diferentes servicios.
- **ms-validator-charger:** Microservicio de Spring Boot que expone dos endpoints para realizar los POST requests correspondientes a la validación de cuenta y el cargo a tarjeta.
- **ms-persistence:** Servicio de Spring Boot que guarda los detalles del pago en una colección de MongoDB.

### Instalaciones y requisitos previos

- Java 17+.
- Maven. (ver apéndice).
- Podman con Podman Compose (ver apéndice).
- Temporal.io (ver apéndice).






## Ejecución en Entorno Local
Para la ejecución en Local es necesario tener abierta una terminal, el IDE de tu preferencia y una herramienta que te permita hacer las peticiones REST.
### Ejecución del Folio-Demo Compose
En la terminal, clona el projecto:

```bash
  git clone https://github.com/XimCervantesPatlan/Payment-Demo-Project.git
```
Sitúate en el directorio del proyecto e inicia Podman Machine:

```bash
  podman machine start
```

> [!TIP]
> Si obtienes errores en este paso o es tu primera vez trabajando con Podman puedes consultar el apéndice.

En esta misma terminal ejecuta el siguiente comando:

```bash
  podman compose --file docker-compose.yml up --detach
```

Verifica que puedas acceder a Mongo Express abriendo en el navegador la URL http://localhost:8081/.

### Ejecución del proyecto

Abre el proyecto en tu IDE y ejecuta los programas en el orden mostrado a continuación:

1. eureka-server
2. ms-persistence
3. ms-validator-charger
4. temporal-orchestrator

En el navegador accede a la interfaz web de Temporal a través de la URL http://localhost:8233/.

Para registrar un pago indica en la herramienta para hacer las peticiones REST que el POST será para el endpoint:

```bash
  http://localhost:8080/api/v1/orchestrator/process-payment
```
Y como Body puedes utilizar el siguiente ejemplo:
```bash
  {
  "clientNumber": "145565",
  "chargeAmount": 340
  }
```
Una vez enviada la petición podrá visualizarse el Workflow creado y los logs en la terminal indicando los pasos que se siguieron en el proceso. 

Para validar que se cumplan las políticas de reintento, intervalos y timeouts se cumplan se sugiere intentar bajar alguno de los microservicios ms-persistence o ms-validator-charger.
## Apéndice

Para revisar más información sobre las instalaciones y pasos intermedios se sugiere consultar las siguientes fuentes:

- [Instalación de Maven](https://maven.apache.org/install.html)
- [Instalación de Podman y creación de Podman Machine](https://podman.io/docs/installation)
- [Ejecución de Podman Compose](https://podman--desktop-io.translate.goog/docs/compose/running-compose?_x_tr_sl=en&_x_tr_tl=es&_x_tr_hl=es&_x_tr_pto=tc)
- [Instalación de Temporal.io](https://temporal.io/setup/install-temporal-cli)

