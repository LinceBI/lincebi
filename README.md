# LinceBI

## Requisitos

 * [OpenJDK](https://www.azul.com/downloads/zulu-community/).
 * [Node.js](https://nodejs.org/en/download/package-manager/).
 * [Docker](https://docs.docker.com/install/).
 * [Docker Compose](https://docs.docker.com/compose/install/).

## Compilación

```sh
./gradlew clean build
```

El resultado de la compilación podrá encontrarse en el directorio `./lincebi-bundle/build/`.

## Instalación

Descomprimir el contenido del archivo `.zip` sobre una instalación de Pentaho BI Server.

## Workflow de desarrollo

 1. Iniciar Pentaho BI Server.
    ```sh
    cd ./docker/
    docker-compose up
    ```

 2. En otra terminal, iniciar los dev server de Webpack.
    ```sh
    cd ./lincebi-frontend/
    npm i
    npm run serve
    ```

 3. Acceder a [https://localhost:8443](https://localhost:8443) cuando ambos servicios estén en
    marcha.

## Estructura del proyecto

```
 .
 │   # Subproyecto de backend.
 ├── lincebi-backend/
 │
 │   # Subproyecto de frontend.
 ├── lincebi-frontend/ 
 |   └── packages/
 |       |   # Reemplazo de la vista "/Home".
 │       ├── home/
 |       |   # Reemplazo de la vista "/Login".
 │       ├── login/
 |       |   # Código compartido por los paquetes.
 │       └── common/
 │
 │   # Subproyecto que combina frontend y backend.
 └── lincebi-bundle/
     │   # Archivos reemplazados en Pentaho BI Server.
     └── src/
         ├── pentaho-solutions/
         │   └── system/
         │       ├── common-ui/
         │       │   ├── resources/
         │       │   │   └── themes/
         │       │   │       │   # Tema de LinceBI.
         │       │   │       └── lincebi/
         │       │   │   # Definición del tema de LinceBI.
         │       │   └── themes.xml
         │       │   # Plugin del backend (incluido por Gradle).
         │       ├── lincebi/
         │       │   # Cambio del tema por defecto.
         │       └── pentaho.xml
         └── tomcat/
             └── webapps/
                 └── pentaho/
                     |   # Reemplazo de la vista "/Home" (incluido por Gradle).
                     ├── Home/
                     |   # Reemplazo de la vista "/Login" (incluido por Gradle).
                     ├── Login/
                     ├── mantle/
                     │   ├── themes/
                     │   │   │   # Tema de LinceBI.
                     │   │   └── lincebi/
                     |   |   # Reescritura de "Mantle.jsp" que es utilizada a modo
                     |   |   # de puente para que la personalización pueda cargar
                     |   |   # las perspectivas originales de Pentaho BI Server.
                     │   ├── MantleBridge.jsp
                     |   |   # Definición del tema de LinceBI.
                     │   └── themes.xml
                     └── WEB-INF/
                         │   # Cambios en los servlets.
                         └── web.xml
```

## Soporte para navegadores

 * Las dos últimas versiones de Google Chrome.
 * Las dos últimas versiones de Mozilla Firefox.
 * Las dos últimas versiones de Microsoft Edge.
 * Las dos últimas versiones de Safari.
 * Las dos últimas versiones de iOS.
 * Las dos últimas versiones de Opera.
 * Mozilla Firefox Extended Support Release (ESR).
 * **Ninguna versión de Internet Explorer.**
