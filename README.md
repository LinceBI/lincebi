# Personalización de Pentaho BI Server

El siguiente documento presenta un manual de instalación y uso de la personalización de LinceBI
de Pentaho BI Server.

Es un desarrollo web moderno con ECMAScript 2018+, Vue.js, Bootstrap y SASS que reemplaza las
vistas de Pentaho BI Server `/Login` y `/Home`.

Se ha realizado de la forma más independiente posible a Pentaho BI Server, con el fin de facilitar
las actualizaciones a futuras versiones. Toda la comunicación con el servidor se realiza
exclusivamente mediante su API REST.

## Soporte para navegadores

 * Las dos últimas versiones de Google Chrome.
 * Las dos últimas versiones de Mozilla Firefox.
 * Las dos últimas versiones de Microsoft Edge.
 * Las dos últimas versiones de Safari.
 * Las dos últimas versiones de iOS.
 * Las dos últimas versiones de Opera.
 * Mozilla Firefox Extended Support Release (ESR).
 * **Ninguna versión de Internet Explorer.**

## Instalación en clientes

Actualmente la personalización tiene el logo y los colores de LinceBI, sin embargo, esto puede
cambiarse fácilmente con variables en tiempo de compilación (no es posible cambiarlo en el paquete
generado).

## Compilación

### Requisitos

 * GNU/Linux.
 * [Node.js](https://nodejs.org/en/download/package-manager/).
 * [Docker](https://docs.docker.com/install/).
 * [Docker Compose](https://docs.docker.com/compose/install/).
 * Los siguientes paquetes: `git` `jq` `make` `maven` `tmux`.

### Pasos

 1. Descargar y descomprimir un Pentaho BI Server 9.0 original en el directorio
    `./biserver/overlay/` (sin sobreescribir los archivos existentes).

 2. Descargar las dependencias.
    ```sh
     npm install
    ```

 3. Compilar el proyecto.
    ```sh
     make build
    ```

 4. El resultado de la compilación podrá encontrarse en el directorio `./dist/`.

### Workflow de desarrollo

 1. Iniciar Pentaho BI Server y el proxy inverso.
    ```sh
     make start-biserver
    ```

 2. En otra terminal, iniciar los dev server de Webpack.
    ```sh
     make start-devserver
    ```

 3. Cuando ambos servicios estén en marcha, acceder a `http://localhost:2015/`
    (los dev servers se encargarán de recompilar el proyecto en caliente con cada cambio).

### Estructura del proyecto

```
 .
 ├── biserver/               # Proyecto con la configuración y tema de Pentaho BI
 |   |                         Server.
 │   ├── overlay/            # Durante el desarrollo este directorio contiene una
 |   |                         instalación completa de Pentaho BI Server, pero todos
 |   |                         los archivos están ignorados excepto los que sean
 |   |                         necesarios (ver .gitignore).
 │   └── Makefile
 ├── docker/                 # Contenedor de Docker que mediante un proxy inverso
 |   |                         sustituye las vistas "/Login" y "/Home" por el dev
 |   |                         server de Webpack.
 │   ├── Caddyfile
 │   └── docker-compose.yml
 ├── packages/               # Paquetes de Node.js.
 │   ├── common/             # Código compartido por el resto de paquetes.
 │   │   ├── src/
 │   │   └── package.json
 │   ├── home/               # Proyecto de Vue.js que reemplaza la vista "/Home".
 │   │   ├── src/
 │   │   └── package.json
 │   └── login/              # Proyecto de Vue.js que reemplaza la vista "/Login".
 │       ├── src/
 │       └── package.json
 ├── lerna.json              # Configuración de Lerna (ver https://lerna.js.org).
 ├── Makefile                # Makefile principal que contiene tareas para iniciar
 |                             Pentaho BI Server, iniciar los dev server de Webpack
 |                             de cada paquete y compilar el proyecto.
 └── package.json
```

## Instalación

Descomprimir y reemplazar el contenido del archivo `.tgz` sobre una instalación de Pentaho BI
Server.

De ya existir los directorios `./tomcat/webapps/pentaho/Login/` y
`./tomcat/webapps/pentaho/Home/`, deben ser borrados previamente.

**Nota:** esta personalización es dependiente de los plugins
 [STSearch](https://gitlab.stratebi.com/stratebi/lincebi/stsearch),
 [file-metadata](https://gitlab.stratebi.com/stratebi/lincebi/file-metadata) y
 [global-user-settings](https://gitlab.stratebi.com/stratebi/lincebi/global-user-settings).

### Estructura del paquete compilado

```
 .
 ├── pentaho-solutions/
 │   └── system/
 │       ├── common-ui/
 │       │   ├── resources/
 │       │   │   └── themes/
 │       │   │       └── lincebi/     # Tema de Pentaho BI Server heredado del tema
 |       |   |                          Ruby.
 │       │   └── themes.xml           # El único cambio en ese archivo es la
 |       |                              definición del tema LinceBI.
 │       └── pentaho.xml              # El único cambio en ese archivo es la
 |                                      definición del tema por defecto.
 └── tomcat/
     └── webapps/
         └── pentaho/
             ├── Home/                # Vista "/Home" que reemplaza al servlet
             |                          "Home" de "web.xml".
             ├── Login/               # Vista "/Login" que reemplaza al servlet
             |                          "Login" de "web.xml".
             ├── mantle/
             │   ├── themes/
             │   │   └── lincebi/     # Tema de Pentaho BI Server heredado del tema
             |   |                      Ruby.
             │   ├── MantleBridge.jsp # Reescritura de Mantle.jsp que es utilizada a
             |   |                      modo de puente para que la personalización
             |   |                      pueda cargar las perspectivas originales de
             |   |                      Pentaho BI Server.
             │   └── themes.xml       # El único cambio en ese archivo es la
             |                          definición del tema LinceBI.
             └── WEB-INF/
                 └── web.xml          # Los cambios en ese archivo son la
                                        desactivación de los servlets "Login" y
                                        "Home" y la definición de nuevos servlets.
```
