# Personalización de Pentaho BI Server

El siguiente repositorio contiene una personalización para Pentaho BI Server hecha con Vue.js que
reemplaza las vistas de login e inicio.

## Compilación del proyecto

### Requisitos

 * GNU/Linux.
 * [Node.js](https://nodejs.org/en/download/package-manager/).
 * [Docker](https://docs.docker.com/install/).
 * [Docker Compose](https://docs.docker.com/compose/install/).
 * Los siguientes paquetes: `git jq make maven tmux`.

### Pasos

 1. Descargar las dependencias.

 ```sh
 npm install
 ```

 2. Compilar el proyecto.

 ```sh
 make build
 ```

 3. El resultado de la compilación podrá encontrarse en el directorio `./dist/`.

## Instalación del proyecto

Descomprimir y reemplazar el contenido del archivo `.tgz` sobre una instalación de Pentaho BI Server.
